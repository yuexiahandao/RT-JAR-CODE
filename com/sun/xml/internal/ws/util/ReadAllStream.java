/*     */ package com.sun.xml.internal.ws.util;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ public class ReadAllStream extends InputStream
/*     */ {
/*     */ 
/*     */   @NotNull
/*     */   private final MemoryStream memStream;
/*     */ 
/*     */   @NotNull
/*     */   private final FileStream fileStream;
/*     */   private boolean readAll;
/*     */   private boolean closed;
/*     */ 
/*     */   public ReadAllStream()
/*     */   {
/*  52 */     this.memStream = new MemoryStream(null);
/*  53 */     this.fileStream = new FileStream(null);
/*     */   }
/*     */ 
/*     */   public void readAll(InputStream in, long inMemory)
/*     */     throws IOException
/*     */   {
/*  69 */     assert (!this.readAll);
/*  70 */     this.readAll = true;
/*     */ 
/*  72 */     boolean eof = this.memStream.readAll(in, inMemory);
/*  73 */     if (!eof)
/*  74 */       this.fileStream.readAll(in);
/*     */   }
/*     */ 
/*     */   public int read() throws IOException
/*     */   {
/*  79 */     int ch = this.memStream.read();
/*  80 */     if (ch == -1) {
/*  81 */       ch = this.fileStream.read();
/*     */     }
/*  83 */     return ch;
/*     */   }
/*     */ 
/*     */   public int read(byte[] b, int off, int sz) throws IOException
/*     */   {
/*  88 */     int len = this.memStream.read(b, off, sz);
/*  89 */     if (len == -1) {
/*  90 */       len = this.fileStream.read(b, off, sz);
/*     */     }
/*  92 */     return len;
/*     */   }
/*     */ 
/*     */   public void close() throws IOException {
/*  96 */     if (!this.closed) {
/*  97 */       this.memStream.close();
/*  98 */       this.fileStream.close();
/*  99 */       this.closed = true; }  } 
/*     */   private static class FileStream extends InputStream { 
/*     */     @Nullable
/*     */     private File tempFile;
/*     */ 
/*     */     @Nullable
/*     */     private FileInputStream fin;
/*     */ 
/* 109 */     void readAll(InputStream in) throws IOException { this.tempFile = File.createTempFile("jaxws", ".bin");
/* 110 */       FileOutputStream fileOut = new FileOutputStream(this.tempFile);
/*     */       try {
/* 112 */         byte[] buf = new byte[8192];
/*     */         int len;
/* 114 */         while ((len = in.read(buf)) != -1)
/* 115 */           fileOut.write(buf, 0, len);
/*     */       }
/*     */       finally {
/* 118 */         fileOut.close();
/*     */       }
/* 120 */       this.fin = new FileInputStream(this.tempFile); }
/*     */ 
/*     */     public int read() throws IOException
/*     */     {
/* 124 */       return this.fin != null ? this.fin.read() : -1;
/*     */     }
/*     */ 
/*     */     public int read(byte[] b, int off, int sz) throws IOException
/*     */     {
/* 129 */       return this.fin != null ? this.fin.read(b, off, sz) : -1;
/*     */     }
/*     */ 
/*     */     public void close() throws IOException
/*     */     {
/* 134 */       if (this.fin != null) {
/* 135 */         this.fin.close();
/*     */       }
/* 137 */       if (this.tempFile != null)
/* 138 */         this.tempFile.delete();
/*     */     } }
/*     */ 
/*     */   private static class MemoryStream extends InputStream {
/*     */     private Chunk head;
/*     */     private Chunk tail;
/*     */     private int curOff;
/*     */ 
/*     */     private void add(byte[] buf, int len) {
/* 149 */       if (this.tail != null)
/* 150 */         this.tail = this.tail.createNext(buf, 0, len);
/*     */       else
/* 152 */         this.head = (this.tail = new Chunk(buf, 0, len));
/*     */     }
/*     */ 
/*     */     boolean readAll(InputStream in, long inMemory)
/*     */       throws IOException
/*     */     {
/* 166 */       long total = 0L;
/*     */       while (true) {
/* 168 */         byte[] buf = new byte[8192];
/* 169 */         int read = fill(in, buf);
/* 170 */         total += read;
/* 171 */         if (read != 0)
/* 172 */           add(buf, read);
/* 173 */         if (read != buf.length)
/* 174 */           return true;
/* 175 */         if (total > inMemory)
/* 176 */           return false;
/*     */       }
/*     */     }
/*     */ 
/*     */     private int fill(InputStream in, byte[] buf) throws IOException
/*     */     {
/* 182 */       int total = 0;
/*     */       int read;
/* 183 */       while ((total < buf.length) && ((read = in.read(buf, total, buf.length - total)) != -1)) {
/* 184 */         total += read;
/*     */       }
/* 186 */       return total;
/*     */     }
/*     */ 
/*     */     public int read() throws IOException {
/* 190 */       if (!fetch()) {
/* 191 */         return -1;
/*     */       }
/* 193 */       return this.head.buf[(this.curOff++)] & 0xFF;
/*     */     }
/*     */ 
/*     */     public int read(byte[] b, int off, int sz) throws IOException
/*     */     {
/* 198 */       if (!fetch()) {
/* 199 */         return -1;
/*     */       }
/* 201 */       sz = Math.min(sz, this.head.len - (this.curOff - this.head.off));
/* 202 */       System.arraycopy(this.head.buf, this.curOff, b, off, sz);
/* 203 */       this.curOff += sz;
/* 204 */       return sz;
/*     */     }
/*     */ 
/*     */     private boolean fetch()
/*     */     {
/* 209 */       if (this.head == null) {
/* 210 */         return false;
/*     */       }
/* 212 */       if (this.curOff == this.head.off + this.head.len) {
/* 213 */         this.head = this.head.next;
/* 214 */         if (this.head == null) {
/* 215 */           return false;
/*     */         }
/* 217 */         this.curOff = this.head.off;
/*     */       }
/* 219 */       return true;
/*     */     }
/*     */     private static final class Chunk { Chunk next;
/*     */       final byte[] buf;
/*     */       final int off;
/*     */       final int len;
/*     */ 
/* 229 */       public Chunk(byte[] buf, int off, int len) { this.buf = buf;
/* 230 */         this.off = off;
/* 231 */         this.len = len; }
/*     */ 
/*     */       public Chunk createNext(byte[] buf, int off, int len)
/*     */       {
/* 235 */         return this.next = new Chunk(buf, off, len);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.util.ReadAllStream
 * JD-Core Version:    0.6.2
 */