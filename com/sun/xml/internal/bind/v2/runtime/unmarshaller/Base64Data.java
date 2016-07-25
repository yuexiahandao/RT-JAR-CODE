/*     */ package com.sun.xml.internal.bind.v2.runtime.unmarshaller;
/*     */ 
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.bind.DatatypeConverterImpl;
/*     */ import com.sun.xml.internal.bind.v2.runtime.output.Pcdata;
/*     */ import com.sun.xml.internal.bind.v2.runtime.output.UTF8XmlOutput;
/*     */ import com.sun.xml.internal.bind.v2.util.ByteArrayOutputStreamEx;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import javax.activation.DataHandler;
/*     */ import javax.activation.DataSource;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ 
/*     */ public final class Base64Data extends Pcdata
/*     */ {
/*     */   private DataHandler dataHandler;
/*     */   private byte[] data;
/*     */   private int dataLen;
/*     */ 
/*     */   @Nullable
/*     */   private String mimeType;
/*     */ 
/*     */   public void set(byte[] data, int len, @Nullable String mimeType)
/*     */   {
/*  89 */     this.data = data;
/*  90 */     this.dataLen = len;
/*  91 */     this.dataHandler = null;
/*  92 */     this.mimeType = mimeType;
/*     */   }
/*     */ 
/*     */   public void set(byte[] data, @Nullable String mimeType)
/*     */   {
/* 102 */     set(data, data.length, mimeType);
/*     */   }
/*     */ 
/*     */   public void set(DataHandler data)
/*     */   {
/* 109 */     assert (data != null);
/* 110 */     this.dataHandler = data;
/* 111 */     this.data = null;
/*     */   }
/*     */ 
/*     */   public DataHandler getDataHandler()
/*     */   {
/* 118 */     if (this.dataHandler == null) {
/* 119 */       this.dataHandler = new DataHandler(new DataSource()
/*     */       {
/*     */         public String getContentType() {
/* 122 */           return Base64Data.this.getMimeType();
/*     */         }
/*     */ 
/*     */         public InputStream getInputStream() {
/* 126 */           return new ByteArrayInputStream(Base64Data.this.data, 0, Base64Data.this.dataLen);
/*     */         }
/*     */ 
/*     */         public String getName() {
/* 130 */           return null;
/*     */         }
/*     */ 
/*     */         public OutputStream getOutputStream() {
/* 134 */           throw new UnsupportedOperationException();
/*     */         }
/*     */       });
/*     */     }
/*     */ 
/* 139 */     return this.dataHandler;
/*     */   }
/*     */ 
/*     */   public byte[] getExact()
/*     */   {
/* 146 */     get();
/* 147 */     if (this.dataLen != this.data.length) {
/* 148 */       byte[] buf = new byte[this.dataLen];
/* 149 */       System.arraycopy(this.data, 0, buf, 0, this.dataLen);
/* 150 */       this.data = buf;
/*     */     }
/* 152 */     return this.data;
/*     */   }
/*     */ 
/*     */   public InputStream getInputStream()
/*     */     throws IOException
/*     */   {
/* 159 */     if (this.dataHandler != null) {
/* 160 */       return this.dataHandler.getInputStream();
/*     */     }
/* 162 */     return new ByteArrayInputStream(this.data, 0, this.dataLen);
/*     */   }
/*     */ 
/*     */   public boolean hasData()
/*     */   {
/* 171 */     return this.data != null;
/*     */   }
/*     */ 
/*     */   public byte[] get()
/*     */   {
/* 178 */     if (this.data == null) {
/*     */       try {
/* 180 */         ByteArrayOutputStreamEx baos = new ByteArrayOutputStreamEx(1024);
/* 181 */         InputStream is = this.dataHandler.getDataSource().getInputStream();
/* 182 */         baos.readFrom(is);
/* 183 */         is.close();
/* 184 */         this.data = baos.getBuffer();
/* 185 */         this.dataLen = baos.size();
/*     */       }
/*     */       catch (IOException e) {
/* 188 */         this.dataLen = 0;
/*     */       }
/*     */     }
/* 191 */     return this.data;
/*     */   }
/*     */ 
/*     */   public int getDataLen() {
/* 195 */     return this.dataLen;
/*     */   }
/*     */ 
/*     */   public String getMimeType() {
/* 199 */     if (this.mimeType == null) {
/* 200 */       return "application/octet-stream";
/*     */     }
/* 202 */     return this.mimeType;
/*     */   }
/*     */ 
/*     */   public int length()
/*     */   {
/* 212 */     get();
/* 213 */     return (this.dataLen + 2) / 3 * 4;
/*     */   }
/*     */ 
/*     */   public char charAt(int index)
/*     */   {
/* 225 */     int offset = index % 4;
/* 226 */     int base = index / 4 * 3;
/*     */     byte b1;
/* 230 */     switch (offset) {
/*     */     case 0:
/* 232 */       return DatatypeConverterImpl.encode(this.data[base] >> 2);
/*     */     case 1:
/*     */       byte b1;
/* 234 */       if (base + 1 < this.dataLen)
/* 235 */         b1 = this.data[(base + 1)];
/*     */       else {
/* 237 */         b1 = 0;
/*     */       }
/* 239 */       return DatatypeConverterImpl.encode((this.data[base] & 0x3) << 4 | b1 >> 4 & 0xF);
/*     */     case 2:
/* 243 */       if (base + 1 < this.dataLen) {
/* 244 */         b1 = this.data[(base + 1)];
/*     */         byte b2;
/*     */         byte b2;
/* 245 */         if (base + 2 < this.dataLen)
/* 246 */           b2 = this.data[(base + 2)];
/*     */         else {
/* 248 */           b2 = 0;
/*     */         }
/*     */ 
/* 251 */         return DatatypeConverterImpl.encode((b1 & 0xF) << 2 | b2 >> 6 & 0x3);
/*     */       }
/*     */ 
/* 255 */       return '=';
/*     */     case 3:
/* 258 */       if (base + 2 < this.dataLen) {
/* 259 */         return DatatypeConverterImpl.encode(this.data[(base + 2)] & 0x3F);
/*     */       }
/* 261 */       return '=';
/*     */     }
/*     */ 
/* 265 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */   public CharSequence subSequence(int start, int end)
/*     */   {
/* 274 */     StringBuilder buf = new StringBuilder();
/* 275 */     get();
/* 276 */     for (int i = start; i < end; i++) {
/* 277 */       buf.append(charAt(i));
/*     */     }
/* 279 */     return buf;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 286 */     get();
/* 287 */     return DatatypeConverterImpl._printBase64Binary(this.data, 0, this.dataLen);
/*     */   }
/*     */ 
/*     */   public void writeTo(char[] buf, int start)
/*     */   {
/* 292 */     get();
/* 293 */     DatatypeConverterImpl._printBase64Binary(this.data, 0, this.dataLen, buf, start);
/*     */   }
/*     */ 
/*     */   public void writeTo(UTF8XmlOutput output) throws IOException
/*     */   {
/* 298 */     get();
/* 299 */     output.text(this.data, this.dataLen);
/*     */   }
/*     */ 
/*     */   public void writeTo(XMLStreamWriter output) throws IOException, XMLStreamException {
/* 303 */     get();
/* 304 */     DatatypeConverterImpl._printBase64Binary(this.data, 0, this.dataLen, output);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.unmarshaller.Base64Data
 * JD-Core Version:    0.6.2
 */