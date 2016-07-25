/*     */ package com.sun.org.apache.xml.internal.serialize;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.StringWriter;
/*     */ import java.io.Writer;
/*     */ 
/*     */ public class Printer
/*     */ {
/*     */   protected final OutputFormat _format;
/*     */   protected Writer _writer;
/*     */   protected StringWriter _dtdWriter;
/*     */   protected Writer _docWriter;
/*     */   protected IOException _exception;
/*     */   private static final int BufferSize = 4096;
/*  95 */   private final char[] _buffer = new char[4096];
/*     */ 
/* 101 */   private int _pos = 0;
/*     */ 
/*     */   public Printer(Writer writer, OutputFormat format)
/*     */   {
/* 106 */     this._writer = writer;
/* 107 */     this._format = format;
/* 108 */     this._exception = null;
/* 109 */     this._dtdWriter = null;
/* 110 */     this._docWriter = null;
/* 111 */     this._pos = 0;
/*     */   }
/*     */ 
/*     */   public IOException getException()
/*     */   {
/* 117 */     return this._exception;
/*     */   }
/*     */ 
/*     */   public void enterDTD()
/*     */     throws IOException
/*     */   {
/* 134 */     if (this._dtdWriter == null) {
/* 135 */       flushLine(false);
/*     */ 
/* 137 */       this._dtdWriter = new StringWriter();
/* 138 */       this._docWriter = this._writer;
/* 139 */       this._writer = this._dtdWriter;
/*     */     }
/*     */   }
/*     */ 
/*     */   public String leaveDTD()
/*     */     throws IOException
/*     */   {
/* 153 */     if (this._writer == this._dtdWriter) {
/* 154 */       flushLine(false);
/*     */ 
/* 156 */       this._writer = this._docWriter;
/* 157 */       return this._dtdWriter.toString();
/*     */     }
/* 159 */     return null;
/*     */   }
/*     */ 
/*     */   public void printText(String text)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 167 */       int length = text.length();
/* 168 */       for (int i = 0; i < length; i++) {
/* 169 */         if (this._pos == 4096) {
/* 170 */           this._writer.write(this._buffer);
/* 171 */           this._pos = 0;
/*     */         }
/* 173 */         this._buffer[this._pos] = text.charAt(i);
/* 174 */         this._pos += 1;
/*     */       }
/*     */     }
/*     */     catch (IOException except)
/*     */     {
/* 179 */       if (this._exception == null)
/* 180 */         this._exception = except;
/* 181 */       throw except;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void printText(StringBuffer text)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 190 */       int length = text.length();
/* 191 */       for (int i = 0; i < length; i++) {
/* 192 */         if (this._pos == 4096) {
/* 193 */           this._writer.write(this._buffer);
/* 194 */           this._pos = 0;
/*     */         }
/* 196 */         this._buffer[this._pos] = text.charAt(i);
/* 197 */         this._pos += 1;
/*     */       }
/*     */     }
/*     */     catch (IOException except)
/*     */     {
/* 202 */       if (this._exception == null)
/* 203 */         this._exception = except;
/* 204 */       throw except;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void printText(char[] chars, int start, int length)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 213 */       while (length-- > 0) {
/* 214 */         if (this._pos == 4096) {
/* 215 */           this._writer.write(this._buffer);
/* 216 */           this._pos = 0;
/*     */         }
/* 218 */         this._buffer[this._pos] = chars[start];
/* 219 */         start++;
/* 220 */         this._pos += 1;
/*     */       }
/*     */     }
/*     */     catch (IOException except)
/*     */     {
/* 225 */       if (this._exception == null)
/* 226 */         this._exception = except;
/* 227 */       throw except;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void printText(char ch)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 236 */       if (this._pos == 4096) {
/* 237 */         this._writer.write(this._buffer);
/* 238 */         this._pos = 0;
/*     */       }
/* 240 */       this._buffer[this._pos] = ch;
/* 241 */       this._pos += 1;
/*     */     }
/*     */     catch (IOException except)
/*     */     {
/* 245 */       if (this._exception == null)
/* 246 */         this._exception = except;
/* 247 */       throw except;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void printSpace()
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 256 */       if (this._pos == 4096) {
/* 257 */         this._writer.write(this._buffer);
/* 258 */         this._pos = 0;
/*     */       }
/* 260 */       this._buffer[this._pos] = ' ';
/* 261 */       this._pos += 1;
/*     */     }
/*     */     catch (IOException except)
/*     */     {
/* 265 */       if (this._exception == null)
/* 266 */         this._exception = except;
/* 267 */       throw except;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void breakLine()
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 276 */       if (this._pos == 4096) {
/* 277 */         this._writer.write(this._buffer);
/* 278 */         this._pos = 0;
/*     */       }
/* 280 */       this._buffer[this._pos] = '\n';
/* 281 */       this._pos += 1;
/*     */     }
/*     */     catch (IOException except)
/*     */     {
/* 285 */       if (this._exception == null)
/* 286 */         this._exception = except;
/* 287 */       throw except;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void breakLine(boolean preserveSpace)
/*     */     throws IOException
/*     */   {
/* 295 */     breakLine();
/*     */   }
/*     */ 
/*     */   public void flushLine(boolean preserveSpace)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 304 */       this._writer.write(this._buffer, 0, this._pos);
/*     */     }
/*     */     catch (IOException except)
/*     */     {
/* 308 */       if (this._exception == null)
/* 309 */         this._exception = except;
/*     */     }
/* 311 */     this._pos = 0;
/*     */   }
/*     */ 
/*     */   public void flush()
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 323 */       this._writer.write(this._buffer, 0, this._pos);
/* 324 */       this._writer.flush();
/*     */     }
/*     */     catch (IOException except)
/*     */     {
/* 328 */       if (this._exception == null)
/* 329 */         this._exception = except;
/* 330 */       throw except;
/*     */     }
/* 332 */     this._pos = 0;
/*     */   }
/*     */ 
/*     */   public void indent()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void unindent()
/*     */   {
/*     */   }
/*     */ 
/*     */   public int getNextIndent()
/*     */   {
/* 350 */     return 0;
/*     */   }
/*     */ 
/*     */   public void setNextIndent(int indent)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setThisIndent(int indent)
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.serialize.Printer
 * JD-Core Version:    0.6.2
 */