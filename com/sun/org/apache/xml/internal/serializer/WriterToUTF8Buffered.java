/*     */ package com.sun.org.apache.xml.internal.serializer;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.io.Writer;
/*     */ 
/*     */ final class WriterToUTF8Buffered extends Writer
/*     */   implements WriterChain
/*     */ {
/*     */   private static final int BYTES_MAX = 16384;
/*     */   private static final int CHARS_MAX = 5461;
/*     */   private final OutputStream m_os;
/*     */   private final byte[] m_outputBytes;
/*     */   private final char[] m_inputChars;
/*     */   private int count;
/*     */ 
/*     */   public WriterToUTF8Buffered(OutputStream out)
/*     */     throws UnsupportedEncodingException
/*     */   {
/*  86 */     this.m_os = out;
/*     */ 
/*  89 */     this.m_outputBytes = new byte[16387];
/*     */ 
/*  93 */     this.m_inputChars = new char[5463];
/*  94 */     this.count = 0;
/*     */   }
/*     */ 
/*     */   public void write(int c)
/*     */     throws IOException
/*     */   {
/* 141 */     if (this.count >= 16384) {
/* 142 */       flushBuffer();
/*     */     }
/* 144 */     if (c < 128)
/*     */     {
/* 146 */       this.m_outputBytes[(this.count++)] = ((byte)c);
/*     */     }
/* 148 */     else if (c < 2048)
/*     */     {
/* 150 */       this.m_outputBytes[(this.count++)] = ((byte)(192 + (c >> 6)));
/* 151 */       this.m_outputBytes[(this.count++)] = ((byte)(128 + (c & 0x3F)));
/*     */     }
/* 153 */     else if (c < 65536)
/*     */     {
/* 155 */       this.m_outputBytes[(this.count++)] = ((byte)(224 + (c >> 12)));
/* 156 */       this.m_outputBytes[(this.count++)] = ((byte)(128 + (c >> 6 & 0x3F)));
/* 157 */       this.m_outputBytes[(this.count++)] = ((byte)(128 + (c & 0x3F)));
/*     */     }
/*     */     else
/*     */     {
/* 161 */       this.m_outputBytes[(this.count++)] = ((byte)(240 + (c >> 18)));
/* 162 */       this.m_outputBytes[(this.count++)] = ((byte)(128 + (c >> 12 & 0x3F)));
/* 163 */       this.m_outputBytes[(this.count++)] = ((byte)(128 + (c >> 6 & 0x3F)));
/* 164 */       this.m_outputBytes[(this.count++)] = ((byte)(128 + (c & 0x3F)));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void write(char[] chars, int start, int length)
/*     */     throws IOException
/*     */   {
/* 189 */     int lengthx3 = 3 * length;
/*     */ 
/* 191 */     if (lengthx3 >= 16384 - this.count)
/*     */     {
/* 194 */       flushBuffer();
/*     */ 
/* 196 */       if (lengthx3 > 16384)
/*     */       {
/* 205 */         int split = length / 5461;
/*     */         int chunks;
/*     */         int chunks;
/* 207 */         if (length % 5461 > 0)
/* 208 */           chunks = split + 1;
/*     */         else
/* 210 */           chunks = split;
/* 211 */         int end_chunk = start;
/* 212 */         for (int chunk = 1; chunk <= chunks; chunk++)
/*     */         {
/* 214 */           int start_chunk = end_chunk;
/* 215 */           end_chunk = start + (int)(length * chunk / chunks);
/*     */ 
/* 220 */           char c = chars[(end_chunk - 1)];
/* 221 */           int ic = chars[(end_chunk - 1)];
/* 222 */           if ((c >= 55296) && (c <= 56319))
/*     */           {
/* 228 */             if (end_chunk < start + length)
/*     */             {
/* 231 */               end_chunk++;
/*     */             }
/*     */             else
/*     */             {
/* 241 */               end_chunk--;
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/* 246 */           int len_chunk = end_chunk - start_chunk;
/* 247 */           write(chars, start_chunk, len_chunk);
/*     */         }
/* 249 */         return;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 255 */     int n = length + start;
/* 256 */     byte[] buf_loc = this.m_outputBytes;
/* 257 */     int count_loc = this.count;
/*     */     char c;
/* 258 */     for (int i = start; 
/* 266 */       (i < n) && ((c = chars[i]) < ''); i++) {
/* 267 */       buf_loc[(count_loc++)] = ((byte)c);
/*     */     }
/* 269 */     for (; i < n; i++)
/*     */     {
/* 272 */       char c = chars[i];
/*     */ 
/* 274 */       if (c < '') {
/* 275 */         buf_loc[(count_loc++)] = ((byte)c);
/* 276 */       } else if (c < 'ࠀ')
/*     */       {
/* 278 */         buf_loc[(count_loc++)] = ((byte)(192 + (c >> '\006')));
/* 279 */         buf_loc[(count_loc++)] = ((byte)('' + (c & 0x3F)));
/*     */       }
/* 288 */       else if ((c >= 55296) && (c <= 56319))
/*     */       {
/* 291 */         char high = c;
/* 292 */         i++;
/* 293 */         char low = chars[i];
/*     */ 
/* 295 */         buf_loc[(count_loc++)] = ((byte)(0xF0 | high + '@' >> 8 & 0xF0));
/* 296 */         buf_loc[(count_loc++)] = ((byte)(0x80 | high + '@' >> 2 & 0x3F));
/* 297 */         buf_loc[(count_loc++)] = ((byte)(0x80 | (low >> '\006' & 0xF) + (high << '\004' & 0x30)));
/* 298 */         buf_loc[(count_loc++)] = ((byte)(0x80 | low & 0x3F));
/*     */       }
/*     */       else
/*     */       {
/* 302 */         buf_loc[(count_loc++)] = ((byte)(224 + (c >> '\f')));
/* 303 */         buf_loc[(count_loc++)] = ((byte)(128 + (c >> '\006' & 0x3F)));
/* 304 */         buf_loc[(count_loc++)] = ((byte)('' + (c & 0x3F)));
/*     */       }
/*     */     }
/*     */ 
/* 308 */     this.count = count_loc;
/*     */   }
/*     */ 
/*     */   public void write(String s)
/*     */     throws IOException
/*     */   {
/* 325 */     int length = s.length();
/* 326 */     int lengthx3 = 3 * length;
/*     */ 
/* 328 */     if (lengthx3 >= 16384 - this.count)
/*     */     {
/* 331 */       flushBuffer();
/*     */ 
/* 333 */       if (lengthx3 > 16384)
/*     */       {
/* 339 */         int start = 0;
/* 340 */         int split = length / 5461;
/*     */         int chunks;
/*     */         int chunks;
/* 342 */         if (length % 5461 > 0)
/* 343 */           chunks = split + 1;
/*     */         else
/* 345 */           chunks = split;
/* 346 */         int end_chunk = 0;
/* 347 */         for (int chunk = 1; chunk <= chunks; chunk++)
/*     */         {
/* 349 */           int start_chunk = end_chunk;
/* 350 */           end_chunk = 0 + (int)(length * chunk / chunks);
/* 351 */           s.getChars(start_chunk, end_chunk, this.m_inputChars, 0);
/* 352 */           int len_chunk = end_chunk - start_chunk;
/*     */ 
/* 357 */           char c = this.m_inputChars[(len_chunk - 1)];
/* 358 */           if ((c >= 55296) && (c <= 56319))
/*     */           {
/* 362 */             end_chunk--;
/* 363 */             len_chunk--;
/* 364 */             if (chunk != chunks);
/*     */           }
/*     */ 
/* 374 */           write(this.m_inputChars, 0, len_chunk);
/*     */         }
/* 376 */         return;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 381 */     s.getChars(0, length, this.m_inputChars, 0);
/* 382 */     char[] chars = this.m_inputChars;
/* 383 */     int n = length;
/* 384 */     byte[] buf_loc = this.m_outputBytes;
/* 385 */     int count_loc = this.count;
/*     */     char c;
/* 386 */     for (int i = 0; 
/* 394 */       (i < n) && ((c = chars[i]) < ''); i++) {
/* 395 */       buf_loc[(count_loc++)] = ((byte)c);
/*     */     }
/* 397 */     for (; i < n; i++)
/*     */     {
/* 400 */       char c = chars[i];
/*     */ 
/* 402 */       if (c < '') {
/* 403 */         buf_loc[(count_loc++)] = ((byte)c);
/* 404 */       } else if (c < 'ࠀ')
/*     */       {
/* 406 */         buf_loc[(count_loc++)] = ((byte)(192 + (c >> '\006')));
/* 407 */         buf_loc[(count_loc++)] = ((byte)('' + (c & 0x3F)));
/*     */       }
/* 416 */       else if ((c >= 55296) && (c <= 56319))
/*     */       {
/* 419 */         char high = c;
/* 420 */         i++;
/* 421 */         char low = chars[i];
/*     */ 
/* 423 */         buf_loc[(count_loc++)] = ((byte)(0xF0 | high + '@' >> 8 & 0xF0));
/* 424 */         buf_loc[(count_loc++)] = ((byte)(0x80 | high + '@' >> 2 & 0x3F));
/* 425 */         buf_loc[(count_loc++)] = ((byte)(0x80 | (low >> '\006' & 0xF) + (high << '\004' & 0x30)));
/* 426 */         buf_loc[(count_loc++)] = ((byte)(0x80 | low & 0x3F));
/*     */       }
/*     */       else
/*     */       {
/* 430 */         buf_loc[(count_loc++)] = ((byte)(224 + (c >> '\f')));
/* 431 */         buf_loc[(count_loc++)] = ((byte)(128 + (c >> '\006' & 0x3F)));
/* 432 */         buf_loc[(count_loc++)] = ((byte)('' + (c & 0x3F)));
/*     */       }
/*     */     }
/*     */ 
/* 436 */     this.count = count_loc;
/*     */   }
/*     */ 
/*     */   public void flushBuffer()
/*     */     throws IOException
/*     */   {
/* 448 */     if (this.count > 0)
/*     */     {
/* 450 */       this.m_os.write(this.m_outputBytes, 0, this.count);
/*     */ 
/* 452 */       this.count = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void flush()
/*     */     throws IOException
/*     */   {
/* 469 */     flushBuffer();
/* 470 */     this.m_os.flush();
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 484 */     flushBuffer();
/* 485 */     this.m_os.close();
/*     */   }
/*     */ 
/*     */   public OutputStream getOutputStream()
/*     */   {
/* 496 */     return this.m_os;
/*     */   }
/*     */ 
/*     */   public Writer getWriter()
/*     */   {
/* 503 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.serializer.WriterToUTF8Buffered
 * JD-Core Version:    0.6.2
 */