/*     */ package com.sun.org.apache.xml.internal.serializer;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Writer;
/*     */ 
/*     */ final class SerializerTraceWriter extends Writer
/*     */   implements WriterChain
/*     */ {
/*     */   private final Writer m_writer;
/*     */   private final SerializerTrace m_tracer;
/*     */   private int buf_length;
/*     */   private byte[] buf;
/*     */   private int count;
/*     */ 
/*     */   private void setBufferSize(int size)
/*     */   {
/*  83 */     this.buf = new byte[size + 3];
/*  84 */     this.buf_length = size;
/*  85 */     this.count = 0;
/*     */   }
/*     */ 
/*     */   public SerializerTraceWriter(Writer out, SerializerTrace tracer)
/*     */   {
/* 101 */     this.m_writer = out;
/* 102 */     this.m_tracer = tracer;
/* 103 */     setBufferSize(1024);
/*     */   }
/*     */ 
/*     */   private void flushBuffer()
/*     */     throws IOException
/*     */   {
/* 118 */     if (this.count > 0)
/*     */     {
/* 120 */       char[] chars = new char[this.count];
/* 121 */       for (int i = 0; i < this.count; i++) {
/* 122 */         chars[i] = ((char)this.buf[i]);
/*     */       }
/* 124 */       if (this.m_tracer != null) {
/* 125 */         this.m_tracer.fireGenerateEvent(12, chars, 0, chars.length);
/*     */       }
/*     */ 
/* 131 */       this.count = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void flush()
/*     */     throws IOException
/*     */   {
/* 142 */     if (this.m_writer != null) {
/* 143 */       this.m_writer.flush();
/*     */     }
/*     */ 
/* 146 */     flushBuffer();
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 156 */     if (this.m_writer != null) {
/* 157 */       this.m_writer.close();
/*     */     }
/*     */ 
/* 160 */     flushBuffer();
/*     */   }
/*     */ 
/*     */   public void write(int c)
/*     */     throws IOException
/*     */   {
/* 178 */     if (this.m_writer != null) {
/* 179 */       this.m_writer.write(c);
/*     */     }
/*     */ 
/* 186 */     if (this.count >= this.buf_length) {
/* 187 */       flushBuffer();
/*     */     }
/* 189 */     if (c < 128)
/*     */     {
/* 191 */       this.buf[(this.count++)] = ((byte)c);
/*     */     }
/* 193 */     else if (c < 2048)
/*     */     {
/* 195 */       this.buf[(this.count++)] = ((byte)(192 + (c >> 6)));
/* 196 */       this.buf[(this.count++)] = ((byte)(128 + (c & 0x3F)));
/*     */     }
/*     */     else
/*     */     {
/* 200 */       this.buf[(this.count++)] = ((byte)(224 + (c >> 12)));
/* 201 */       this.buf[(this.count++)] = ((byte)(128 + (c >> 6 & 0x3F)));
/* 202 */       this.buf[(this.count++)] = ((byte)(128 + (c & 0x3F)));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void write(char[] chars, int start, int length)
/*     */     throws IOException
/*     */   {
/* 221 */     if (this.m_writer != null) {
/* 222 */       this.m_writer.write(chars, start, length);
/*     */     }
/*     */ 
/* 225 */     int lengthx3 = (length << 1) + length;
/*     */ 
/* 227 */     if (lengthx3 >= this.buf_length)
/*     */     {
/* 234 */       flushBuffer();
/* 235 */       setBufferSize(2 * lengthx3);
/*     */     }
/*     */ 
/* 239 */     if (lengthx3 > this.buf_length - this.count)
/*     */     {
/* 241 */       flushBuffer();
/*     */     }
/*     */ 
/* 244 */     int n = length + start;
/* 245 */     for (int i = start; i < n; i++)
/*     */     {
/* 247 */       char c = chars[i];
/*     */ 
/* 249 */       if (c < '') {
/* 250 */         this.buf[(this.count++)] = ((byte)c);
/* 251 */       } else if (c < 'ࠀ')
/*     */       {
/* 253 */         this.buf[(this.count++)] = ((byte)(192 + (c >> '\006')));
/* 254 */         this.buf[(this.count++)] = ((byte)('' + (c & 0x3F)));
/*     */       }
/*     */       else
/*     */       {
/* 258 */         this.buf[(this.count++)] = ((byte)(224 + (c >> '\f')));
/* 259 */         this.buf[(this.count++)] = ((byte)(128 + (c >> '\006' & 0x3F)));
/* 260 */         this.buf[(this.count++)] = ((byte)('' + (c & 0x3F)));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void write(String s)
/*     */     throws IOException
/*     */   {
/* 276 */     if (this.m_writer != null) {
/* 277 */       this.m_writer.write(s);
/*     */     }
/*     */ 
/* 280 */     int length = s.length();
/*     */ 
/* 286 */     int lengthx3 = (length << 1) + length;
/*     */ 
/* 288 */     if (lengthx3 >= this.buf_length)
/*     */     {
/* 295 */       flushBuffer();
/* 296 */       setBufferSize(2 * lengthx3);
/*     */     }
/*     */ 
/* 299 */     if (lengthx3 > this.buf_length - this.count)
/*     */     {
/* 301 */       flushBuffer();
/*     */     }
/*     */ 
/* 304 */     for (int i = 0; i < length; i++)
/*     */     {
/* 306 */       char c = s.charAt(i);
/*     */ 
/* 308 */       if (c < '') {
/* 309 */         this.buf[(this.count++)] = ((byte)c);
/* 310 */       } else if (c < 'ࠀ')
/*     */       {
/* 312 */         this.buf[(this.count++)] = ((byte)(192 + (c >> '\006')));
/* 313 */         this.buf[(this.count++)] = ((byte)('' + (c & 0x3F)));
/*     */       }
/*     */       else
/*     */       {
/* 317 */         this.buf[(this.count++)] = ((byte)(224 + (c >> '\f')));
/* 318 */         this.buf[(this.count++)] = ((byte)(128 + (c >> '\006' & 0x3F)));
/* 319 */         this.buf[(this.count++)] = ((byte)('' + (c & 0x3F)));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public Writer getWriter()
/*     */   {
/* 329 */     return this.m_writer;
/*     */   }
/*     */ 
/*     */   public OutputStream getOutputStream()
/*     */   {
/* 338 */     OutputStream retval = null;
/* 339 */     if ((this.m_writer instanceof WriterChain))
/* 340 */       retval = ((WriterChain)this.m_writer).getOutputStream();
/* 341 */     return retval;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.serializer.SerializerTraceWriter
 * JD-Core Version:    0.6.2
 */