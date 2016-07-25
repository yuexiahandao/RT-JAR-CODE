/*     */ package com.sun.org.apache.xerces.internal.impl.io;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.msg.XMLMessageFormatter;
/*     */ import com.sun.org.apache.xerces.internal.util.MessageFormatter;
/*     */ import com.sun.xml.internal.stream.util.BufferAllocator;
/*     */ import com.sun.xml.internal.stream.util.ThreadLocalBufferAllocator;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.util.Locale;
/*     */ 
/*     */ public class UTF8Reader extends Reader
/*     */ {
/*     */   public static final int DEFAULT_BUFFER_SIZE = 2048;
/*     */   private static final boolean DEBUG_READ = false;
/*     */   protected InputStream fInputStream;
/*     */   protected byte[] fBuffer;
/*     */   protected int fOffset;
/*  71 */   private int fSurrogate = -1;
/*     */ 
/*  75 */   private MessageFormatter fFormatter = null;
/*     */ 
/*  78 */   private Locale fLocale = null;
/*     */ 
/*     */   public UTF8Reader(InputStream inputStream)
/*     */   {
/*  91 */     this(inputStream, 2048, new XMLMessageFormatter(), Locale.getDefault());
/*     */   }
/*     */ 
/*     */   public UTF8Reader(InputStream inputStream, MessageFormatter messageFormatter, Locale locale)
/*     */   {
/* 104 */     this(inputStream, 2048, messageFormatter, locale);
/*     */   }
/*     */ 
/*     */   public UTF8Reader(InputStream inputStream, int size, MessageFormatter messageFormatter, Locale locale)
/*     */   {
/* 118 */     this.fInputStream = inputStream;
/* 119 */     BufferAllocator ba = ThreadLocalBufferAllocator.getBufferAllocator();
/* 120 */     this.fBuffer = ba.getByteBuffer(size);
/* 121 */     if (this.fBuffer == null) {
/* 122 */       this.fBuffer = new byte[size];
/*     */     }
/* 124 */     this.fFormatter = messageFormatter;
/* 125 */     this.fLocale = locale;
/*     */   }
/*     */ 
/*     */   public int read()
/*     */     throws IOException
/*     */   {
/* 148 */     int c = this.fSurrogate;
/* 149 */     if (this.fSurrogate == -1)
/*     */     {
/* 152 */       int index = 0;
/*     */ 
/* 155 */       int b0 = index == this.fOffset ? this.fInputStream.read() : this.fBuffer[(index++)] & 0xFF;
/*     */ 
/* 157 */       if (b0 == -1) {
/* 158 */         return -1;
/*     */       }
/*     */ 
/* 163 */       if (b0 < 128) {
/* 164 */         c = (char)b0;
/*     */       }
/* 169 */       else if (((b0 & 0xE0) == 192) && ((b0 & 0x1E) != 0)) {
/* 170 */         int b1 = index == this.fOffset ? this.fInputStream.read() : this.fBuffer[(index++)] & 0xFF;
/*     */ 
/* 172 */         if (b1 == -1) {
/* 173 */           expectedByte(2, 2);
/*     */         }
/* 175 */         if ((b1 & 0xC0) != 128) {
/* 176 */           invalidByte(2, 2, b1);
/*     */         }
/* 178 */         c = b0 << 6 & 0x7C0 | b1 & 0x3F;
/*     */       }
/* 183 */       else if ((b0 & 0xF0) == 224) {
/* 184 */         int b1 = index == this.fOffset ? this.fInputStream.read() : this.fBuffer[(index++)] & 0xFF;
/*     */ 
/* 186 */         if (b1 == -1) {
/* 187 */           expectedByte(2, 3);
/*     */         }
/* 189 */         if (((b1 & 0xC0) != 128) || ((b0 == 237) && (b1 >= 160)) || (((b0 & 0xF) == 0) && ((b1 & 0x20) == 0)))
/*     */         {
/* 192 */           invalidByte(2, 3, b1);
/*     */         }
/* 194 */         int b2 = index == this.fOffset ? this.fInputStream.read() : this.fBuffer[(index++)] & 0xFF;
/*     */ 
/* 196 */         if (b2 == -1) {
/* 197 */           expectedByte(3, 3);
/*     */         }
/* 199 */         if ((b2 & 0xC0) != 128) {
/* 200 */           invalidByte(3, 3, b2);
/*     */         }
/* 202 */         c = b0 << 12 & 0xF000 | b1 << 6 & 0xFC0 | b2 & 0x3F;
/*     */       }
/* 210 */       else if ((b0 & 0xF8) == 240) {
/* 211 */         int b1 = index == this.fOffset ? this.fInputStream.read() : this.fBuffer[(index++)] & 0xFF;
/*     */ 
/* 213 */         if (b1 == -1) {
/* 214 */           expectedByte(2, 4);
/*     */         }
/* 216 */         if (((b1 & 0xC0) != 128) || (((b1 & 0x30) == 0) && ((b0 & 0x7) == 0)))
/*     */         {
/* 218 */           invalidByte(2, 3, b1);
/*     */         }
/* 220 */         int b2 = index == this.fOffset ? this.fInputStream.read() : this.fBuffer[(index++)] & 0xFF;
/*     */ 
/* 222 */         if (b2 == -1) {
/* 223 */           expectedByte(3, 4);
/*     */         }
/* 225 */         if ((b2 & 0xC0) != 128) {
/* 226 */           invalidByte(3, 3, b2);
/*     */         }
/* 228 */         int b3 = index == this.fOffset ? this.fInputStream.read() : this.fBuffer[(index++)] & 0xFF;
/*     */ 
/* 230 */         if (b3 == -1) {
/* 231 */           expectedByte(4, 4);
/*     */         }
/* 233 */         if ((b3 & 0xC0) != 128) {
/* 234 */           invalidByte(4, 4, b3);
/*     */         }
/* 236 */         int uuuuu = b0 << 2 & 0x1C | b1 >> 4 & 0x3;
/* 237 */         if (uuuuu > 16) {
/* 238 */           invalidSurrogate(uuuuu);
/*     */         }
/* 240 */         int wwww = uuuuu - 1;
/* 241 */         int hs = 0xD800 | wwww << 6 & 0x3C0 | b1 << 2 & 0x3C | b2 >> 4 & 0x3;
/*     */ 
/* 244 */         int ls = 0xDC00 | b2 << 6 & 0x3C0 | b3 & 0x3F;
/* 245 */         c = hs;
/* 246 */         this.fSurrogate = ls;
/*     */       }
/*     */       else
/*     */       {
/* 251 */         invalidByte(1, 1, b0);
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 257 */       this.fSurrogate = -1;
/*     */     }
/*     */ 
/* 264 */     return c;
/*     */   }
/*     */ 
/*     */   public int read(char[] ch, int offset, int length)
/*     */     throws IOException
/*     */   {
/* 285 */     int out = offset;
/* 286 */     if (this.fSurrogate != -1) {
/* 287 */       ch[(offset + 1)] = ((char)this.fSurrogate);
/* 288 */       this.fSurrogate = -1;
/* 289 */       length--;
/* 290 */       out++;
/*     */     }
/*     */ 
/* 294 */     int count = 0;
/* 295 */     if (this.fOffset == 0)
/*     */     {
/* 297 */       if (length > this.fBuffer.length) {
/* 298 */         length = this.fBuffer.length;
/*     */       }
/*     */ 
/* 302 */       count = this.fInputStream.read(this.fBuffer, 0, length);
/* 303 */       if (count == -1) {
/* 304 */         return -1;
/*     */       }
/* 306 */       count += out - offset;
/*     */     }
/*     */     else
/*     */     {
/* 317 */       count = this.fOffset;
/* 318 */       this.fOffset = 0;
/*     */     }
/*     */ 
/* 322 */     int total = count;
/*     */ 
/* 325 */     byte byte0 = 0;
/* 326 */     for (int in = 0; in < total; in++) {
/* 327 */       byte byte1 = this.fBuffer[in];
/* 328 */       if (byte1 < 0) break;
/* 329 */       ch[(out++)] = ((char)byte1);
/*     */     }
/*     */ 
/* 335 */     for (; in < total; in++) {
/* 336 */       byte byte1 = this.fBuffer[in];
/*     */ 
/* 340 */       if (byte1 >= 0) {
/* 341 */         ch[(out++)] = ((char)byte1);
/*     */       }
/*     */       else
/*     */       {
/* 347 */         int b0 = byte1 & 0xFF;
/* 348 */         if (((b0 & 0xE0) == 192) && ((b0 & 0x1E) != 0)) {
/* 349 */           int b1 = -1;
/* 350 */           in++; if (in < total) {
/* 351 */             b1 = this.fBuffer[in] & 0xFF;
/*     */           }
/*     */           else {
/* 354 */             b1 = this.fInputStream.read();
/* 355 */             if (b1 == -1) {
/* 356 */               if (out > offset) {
/* 357 */                 this.fBuffer[0] = ((byte)b0);
/* 358 */                 this.fOffset = 1;
/* 359 */                 return out - offset;
/*     */               }
/* 361 */               expectedByte(2, 2);
/*     */             }
/* 363 */             count++;
/*     */           }
/* 365 */           if ((b1 & 0xC0) != 128) {
/* 366 */             if (out > offset) {
/* 367 */               this.fBuffer[0] = ((byte)b0);
/* 368 */               this.fBuffer[1] = ((byte)b1);
/* 369 */               this.fOffset = 2;
/* 370 */               return out - offset;
/*     */             }
/* 372 */             invalidByte(2, 2, b1);
/*     */           }
/* 374 */           int c = b0 << 6 & 0x7C0 | b1 & 0x3F;
/* 375 */           ch[(out++)] = ((char)c);
/* 376 */           count--;
/*     */         }
/* 382 */         else if ((b0 & 0xF0) == 224) {
/* 383 */           int b1 = -1;
/* 384 */           in++; if (in < total) {
/* 385 */             b1 = this.fBuffer[in] & 0xFF;
/*     */           }
/*     */           else {
/* 388 */             b1 = this.fInputStream.read();
/* 389 */             if (b1 == -1) {
/* 390 */               if (out > offset) {
/* 391 */                 this.fBuffer[0] = ((byte)b0);
/* 392 */                 this.fOffset = 1;
/* 393 */                 return out - offset;
/*     */               }
/* 395 */               expectedByte(2, 3);
/*     */             }
/* 397 */             count++;
/*     */           }
/* 399 */           if (((b1 & 0xC0) != 128) || ((b0 == 237) && (b1 >= 160)) || (((b0 & 0xF) == 0) && ((b1 & 0x20) == 0)))
/*     */           {
/* 402 */             if (out > offset) {
/* 403 */               this.fBuffer[0] = ((byte)b0);
/* 404 */               this.fBuffer[1] = ((byte)b1);
/* 405 */               this.fOffset = 2;
/* 406 */               return out - offset;
/*     */             }
/* 408 */             invalidByte(2, 3, b1);
/*     */           }
/* 410 */           int b2 = -1;
/* 411 */           in++; if (in < total) {
/* 412 */             b2 = this.fBuffer[in] & 0xFF;
/*     */           }
/*     */           else {
/* 415 */             b2 = this.fInputStream.read();
/* 416 */             if (b2 == -1) {
/* 417 */               if (out > offset) {
/* 418 */                 this.fBuffer[0] = ((byte)b0);
/* 419 */                 this.fBuffer[1] = ((byte)b1);
/* 420 */                 this.fOffset = 2;
/* 421 */                 return out - offset;
/*     */               }
/* 423 */               expectedByte(3, 3);
/*     */             }
/* 425 */             count++;
/*     */           }
/* 427 */           if ((b2 & 0xC0) != 128) {
/* 428 */             if (out > offset) {
/* 429 */               this.fBuffer[0] = ((byte)b0);
/* 430 */               this.fBuffer[1] = ((byte)b1);
/* 431 */               this.fBuffer[2] = ((byte)b2);
/* 432 */               this.fOffset = 3;
/* 433 */               return out - offset;
/*     */             }
/* 435 */             invalidByte(3, 3, b2);
/*     */           }
/* 437 */           int c = b0 << 12 & 0xF000 | b1 << 6 & 0xFC0 | b2 & 0x3F;
/*     */ 
/* 439 */           ch[(out++)] = ((char)c);
/* 440 */           count -= 2;
/*     */         }
/* 448 */         else if ((b0 & 0xF8) == 240) {
/* 449 */           int b1 = -1;
/* 450 */           in++; if (in < total) {
/* 451 */             b1 = this.fBuffer[in] & 0xFF;
/*     */           }
/*     */           else {
/* 454 */             b1 = this.fInputStream.read();
/* 455 */             if (b1 == -1) {
/* 456 */               if (out > offset) {
/* 457 */                 this.fBuffer[0] = ((byte)b0);
/* 458 */                 this.fOffset = 1;
/* 459 */                 return out - offset;
/*     */               }
/* 461 */               expectedByte(2, 4);
/*     */             }
/* 463 */             count++;
/*     */           }
/* 465 */           if (((b1 & 0xC0) != 128) || (((b1 & 0x30) == 0) && ((b0 & 0x7) == 0)))
/*     */           {
/* 467 */             if (out > offset) {
/* 468 */               this.fBuffer[0] = ((byte)b0);
/* 469 */               this.fBuffer[1] = ((byte)b1);
/* 470 */               this.fOffset = 2;
/* 471 */               return out - offset;
/*     */             }
/* 473 */             invalidByte(2, 4, b1);
/*     */           }
/* 475 */           int b2 = -1;
/* 476 */           in++; if (in < total) {
/* 477 */             b2 = this.fBuffer[in] & 0xFF;
/*     */           }
/*     */           else {
/* 480 */             b2 = this.fInputStream.read();
/* 481 */             if (b2 == -1) {
/* 482 */               if (out > offset) {
/* 483 */                 this.fBuffer[0] = ((byte)b0);
/* 484 */                 this.fBuffer[1] = ((byte)b1);
/* 485 */                 this.fOffset = 2;
/* 486 */                 return out - offset;
/*     */               }
/* 488 */               expectedByte(3, 4);
/*     */             }
/* 490 */             count++;
/*     */           }
/* 492 */           if ((b2 & 0xC0) != 128) {
/* 493 */             if (out > offset) {
/* 494 */               this.fBuffer[0] = ((byte)b0);
/* 495 */               this.fBuffer[1] = ((byte)b1);
/* 496 */               this.fBuffer[2] = ((byte)b2);
/* 497 */               this.fOffset = 3;
/* 498 */               return out - offset;
/*     */             }
/* 500 */             invalidByte(3, 4, b2);
/*     */           }
/* 502 */           int b3 = -1;
/* 503 */           in++; if (in < total) {
/* 504 */             b3 = this.fBuffer[in] & 0xFF;
/*     */           }
/*     */           else {
/* 507 */             b3 = this.fInputStream.read();
/* 508 */             if (b3 == -1) {
/* 509 */               if (out > offset) {
/* 510 */                 this.fBuffer[0] = ((byte)b0);
/* 511 */                 this.fBuffer[1] = ((byte)b1);
/* 512 */                 this.fBuffer[2] = ((byte)b2);
/* 513 */                 this.fOffset = 3;
/* 514 */                 return out - offset;
/*     */               }
/* 516 */               expectedByte(4, 4);
/*     */             }
/* 518 */             count++;
/*     */           }
/* 520 */           if ((b3 & 0xC0) != 128) {
/* 521 */             if (out > offset) {
/* 522 */               this.fBuffer[0] = ((byte)b0);
/* 523 */               this.fBuffer[1] = ((byte)b1);
/* 524 */               this.fBuffer[2] = ((byte)b2);
/* 525 */               this.fBuffer[3] = ((byte)b3);
/* 526 */               this.fOffset = 4;
/* 527 */               return out - offset;
/*     */             }
/* 529 */             invalidByte(4, 4, b2);
/*     */           }
/*     */ 
/* 533 */           int uuuuu = b0 << 2 & 0x1C | b1 >> 4 & 0x3;
/* 534 */           if (uuuuu > 16) {
/* 535 */             invalidSurrogate(uuuuu);
/*     */           }
/* 537 */           int wwww = uuuuu - 1;
/* 538 */           int zzzz = b1 & 0xF;
/* 539 */           int yyyyyy = b2 & 0x3F;
/* 540 */           int xxxxxx = b3 & 0x3F;
/* 541 */           int hs = 0xD800 | wwww << 6 & 0x3C0 | zzzz << 2 | yyyyyy >> 4;
/* 542 */           int ls = 0xDC00 | yyyyyy << 6 & 0x3C0 | xxxxxx;
/*     */ 
/* 545 */           ch[(out++)] = ((char)hs);
/* 546 */           ch[(out++)] = ((char)ls);
/* 547 */           count -= 2;
/*     */         }
/*     */         else
/*     */         {
/* 552 */           if (out > offset) {
/* 553 */             this.fBuffer[0] = ((byte)b0);
/* 554 */             this.fOffset = 1;
/* 555 */             return out - offset;
/*     */           }
/* 557 */           invalidByte(1, 1, b0);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 564 */     return count;
/*     */   }
/*     */ 
/*     */   public long skip(long n)
/*     */     throws IOException
/*     */   {
/* 580 */     long remaining = n;
/* 581 */     char[] ch = new char[this.fBuffer.length];
/*     */     do {
/* 583 */       int length = ch.length < remaining ? ch.length : (int)remaining;
/* 584 */       int count = read(ch, 0, length);
/* 585 */       if (count <= 0) break;
/* 586 */       remaining -= count;
/*     */     }
/*     */ 
/* 591 */     while (remaining > 0L);
/*     */ 
/* 593 */     long skipped = n - remaining;
/* 594 */     return skipped;
/*     */   }
/*     */ 
/*     */   public boolean ready()
/*     */     throws IOException
/*     */   {
/* 608 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean markSupported()
/*     */   {
/* 615 */     return false;
/*     */   }
/*     */ 
/*     */   public void mark(int readAheadLimit)
/*     */     throws IOException
/*     */   {
/* 632 */     throw new IOException(this.fFormatter.formatMessage(this.fLocale, "OperationNotSupported", new Object[] { "mark()", "UTF-8" }));
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */     throws IOException
/*     */   {
/* 649 */     this.fOffset = 0;
/* 650 */     this.fSurrogate = -1;
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 661 */     BufferAllocator ba = ThreadLocalBufferAllocator.getBufferAllocator();
/* 662 */     ba.returnByteBuffer(this.fBuffer);
/* 663 */     this.fBuffer = null;
/* 664 */     this.fInputStream.close();
/*     */   }
/*     */ 
/*     */   private void expectedByte(int position, int count)
/*     */     throws MalformedByteSequenceException
/*     */   {
/* 675 */     throw new MalformedByteSequenceException(this.fFormatter, this.fLocale, "http://www.w3.org/TR/1998/REC-xml-19980210", "ExpectedByte", new Object[] { Integer.toString(position), Integer.toString(count) });
/*     */   }
/*     */ 
/*     */   private void invalidByte(int position, int count, int c)
/*     */     throws MalformedByteSequenceException
/*     */   {
/* 687 */     throw new MalformedByteSequenceException(this.fFormatter, this.fLocale, "http://www.w3.org/TR/1998/REC-xml-19980210", "InvalidByte", new Object[] { Integer.toString(position), Integer.toString(count) });
/*     */   }
/*     */ 
/*     */   private void invalidSurrogate(int uuuuu)
/*     */     throws MalformedByteSequenceException
/*     */   {
/* 698 */     throw new MalformedByteSequenceException(this.fFormatter, this.fLocale, "http://www.w3.org/TR/1998/REC-xml-19980210", "InvalidHighSurrogate", new Object[] { Integer.toHexString(uuuuu) });
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.io.UTF8Reader
 * JD-Core Version:    0.6.2
 */