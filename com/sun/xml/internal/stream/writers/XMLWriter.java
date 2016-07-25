/*     */ package com.sun.xml.internal.stream.writers;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.util.XMLStringBuffer;
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ 
/*     */ public class XMLWriter extends Writer
/*     */ {
/*     */   private Writer writer;
/*     */   private int size;
/*  55 */   private XMLStringBuffer buffer = new XMLStringBuffer(12288);
/*     */   private static final int THRESHHOLD_LENGTH = 4096;
/*     */   private static final boolean DEBUG = false;
/*     */ 
/*     */   public XMLWriter(Writer writer)
/*     */   {
/*  63 */     this(writer, 4096);
/*     */   }
/*     */ 
/*     */   public XMLWriter(Writer writer, int size)
/*     */   {
/*  73 */     this.writer = writer;
/*  74 */     this.size = size;
/*     */   }
/*     */ 
/*     */   public void write(int c)
/*     */     throws IOException
/*     */   {
/*  90 */     ensureOpen();
/*  91 */     this.buffer.append((char)c);
/*  92 */     conditionalWrite();
/*     */   }
/*     */ 
/*     */   public void write(char[] cbuf)
/*     */     throws IOException
/*     */   {
/* 104 */     write(cbuf, 0, cbuf.length);
/*     */   }
/*     */ 
/*     */   public void write(char[] cbuf, int off, int len)
/*     */     throws IOException
/*     */   {
/* 118 */     ensureOpen();
/*     */ 
/* 121 */     if (len > this.size)
/*     */     {
/* 123 */       writeBufferedData();
/*     */ 
/* 125 */       this.writer.write(cbuf, off, len);
/*     */     } else {
/* 127 */       this.buffer.append(cbuf, off, len);
/* 128 */       conditionalWrite();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void write(String str, int off, int len)
/*     */     throws IOException
/*     */   {
/* 142 */     write(str.toCharArray(), off, len);
/*     */   }
/*     */ 
/*     */   public void write(String str)
/*     */     throws IOException
/*     */   {
/* 155 */     if (str.length() > this.size)
/*     */     {
/* 157 */       writeBufferedData();
/*     */ 
/* 159 */       this.writer.write(str);
/*     */     } else {
/* 161 */       this.buffer.append(str);
/* 162 */       conditionalWrite();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 174 */     if (this.writer == null) return;
/*     */ 
/* 176 */     flush();
/* 177 */     this.writer.close();
/* 178 */     this.writer = null;
/*     */   }
/*     */ 
/*     */   public void flush()
/*     */     throws IOException
/*     */   {
/* 192 */     ensureOpen();
/*     */ 
/* 194 */     writeBufferedData();
/* 195 */     this.writer.flush();
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/* 203 */     this.writer = null;
/* 204 */     this.buffer.clear();
/* 205 */     this.size = 4096;
/*     */   }
/*     */ 
/*     */   public void setWriter(Writer writer)
/*     */   {
/* 214 */     this.writer = writer;
/* 215 */     this.buffer.clear();
/* 216 */     this.size = 4096;
/*     */   }
/*     */ 
/*     */   public void setWriter(Writer writer, int size)
/*     */   {
/* 225 */     this.writer = writer;
/* 226 */     this.size = size;
/*     */   }
/*     */ 
/*     */   protected Writer getWriter()
/*     */   {
/* 233 */     return this.writer;
/*     */   }
/*     */ 
/*     */   private void conditionalWrite()
/*     */     throws IOException
/*     */   {
/* 239 */     if (this.buffer.length > this.size)
/*     */     {
/* 244 */       writeBufferedData();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void writeBufferedData()
/*     */     throws IOException
/*     */   {
/* 252 */     this.writer.write(this.buffer.ch, this.buffer.offset, this.buffer.length);
/* 253 */     this.buffer.clear();
/*     */   }
/*     */ 
/*     */   private void ensureOpen() throws IOException
/*     */   {
/* 258 */     if (this.writer == null) throw new IOException("Stream closed");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.stream.writers.XMLWriter
 * JD-Core Version:    0.6.2
 */