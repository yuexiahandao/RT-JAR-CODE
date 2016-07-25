/*     */ package sun.net.www.http;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import sun.net.www.MessageHeader;
/*     */ 
/*     */ public class ChunkedInputStream extends InputStream
/*     */   implements Hurryable
/*     */ {
/*     */   private InputStream in;
/*     */   private HttpClient hc;
/*     */   private MessageHeader responses;
/*     */   private int chunkSize;
/*     */   private int chunkRead;
/*  81 */   private byte[] chunkData = new byte[4096];
/*     */   private int chunkPos;
/*     */   private int chunkCount;
/* 101 */   private byte[] rawData = new byte[32];
/*     */   private int rawPos;
/*     */   private int rawCount;
/*     */   private boolean error;
/*     */   private boolean closed;
/*     */   private static final int MAX_CHUNK_HEADER_SIZE = 2050;
/*     */   static final int STATE_AWAITING_CHUNK_HEADER = 1;
/*     */   static final int STATE_READING_CHUNK = 2;
/*     */   static final int STATE_AWAITING_CHUNK_EOL = 3;
/*     */   static final int STATE_AWAITING_TRAILERS = 4;
/*     */   static final int STATE_DONE = 5;
/*     */   private int state;
/*     */ 
/*     */   private void ensureOpen()
/*     */     throws IOException
/*     */   {
/* 173 */     if (this.closed)
/* 174 */       throw new IOException("stream is closed");
/*     */   }
/*     */ 
/*     */   private void ensureRawAvailable(int paramInt)
/*     */   {
/* 186 */     if (this.rawCount + paramInt > this.rawData.length) {
/* 187 */       int i = this.rawCount - this.rawPos;
/* 188 */       if (i + paramInt > this.rawData.length) {
/* 189 */         byte[] arrayOfByte = new byte[i + paramInt];
/* 190 */         if (i > 0) {
/* 191 */           System.arraycopy(this.rawData, this.rawPos, arrayOfByte, 0, i);
/*     */         }
/* 193 */         this.rawData = arrayOfByte;
/*     */       }
/* 195 */       else if (i > 0) {
/* 196 */         System.arraycopy(this.rawData, this.rawPos, this.rawData, 0, i);
/*     */       }
/*     */ 
/* 199 */       this.rawCount = i;
/* 200 */       this.rawPos = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void closeUnderlying()
/*     */     throws IOException
/*     */   {
/* 214 */     if (this.in == null) {
/* 215 */       return;
/*     */     }
/*     */ 
/* 218 */     if ((!this.error) && (this.state == 5)) {
/* 219 */       this.hc.finished();
/*     */     }
/* 221 */     else if (!hurry()) {
/* 222 */       this.hc.closeServer();
/*     */     }
/*     */ 
/* 226 */     this.in = null;
/*     */   }
/*     */ 
/*     */   private int fastRead(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 239 */     int i = this.chunkSize - this.chunkRead;
/* 240 */     int j = i < paramInt2 ? i : paramInt2;
/* 241 */     if (j > 0) {
/*     */       int k;
/*     */       try {
/* 244 */         k = this.in.read(paramArrayOfByte, paramInt1, j);
/*     */       } catch (IOException localIOException) {
/* 246 */         this.error = true;
/* 247 */         throw localIOException;
/*     */       }
/* 249 */       if (k > 0) {
/* 250 */         this.chunkRead += k;
/* 251 */         if (this.chunkRead >= this.chunkSize) {
/* 252 */           this.state = 3;
/*     */         }
/* 254 */         return k;
/*     */       }
/* 256 */       this.error = true;
/* 257 */       throw new IOException("Premature EOF");
/*     */     }
/* 259 */     return 0;
/*     */   }
/*     */ 
/*     */   private void processRaw()
/*     */     throws IOException
/*     */   {
/* 280 */     while (this.state != 5)
/*     */     {
/*     */       int i;
/*     */       int j;
/*     */       Object localObject;
/* 282 */       switch (this.state)
/*     */       {
/*     */       case 1:
/* 292 */         i = this.rawPos;
/* 293 */         while ((i < this.rawCount) && 
/* 294 */           (this.rawData[i] != 10))
/*     */         {
/* 297 */           i++;
/* 298 */           if (i - this.rawPos >= 2050) {
/* 299 */             this.error = true;
/* 300 */             throw new IOException("Chunk header too long");
/*     */           }
/*     */         }
/* 303 */         if (i >= this.rawCount) {
/* 304 */           return;
/*     */         }
/*     */ 
/* 310 */         String str1 = new String(this.rawData, this.rawPos, i - this.rawPos + 1, "US-ASCII");
/* 311 */         for (j = 0; (j < str1.length()) && 
/* 312 */           (Character.digit(str1.charAt(j), 16) != -1); j++);
/*     */         try
/*     */         {
/* 316 */           this.chunkSize = Integer.parseInt(str1.substring(0, j), 16);
/*     */         } catch (NumberFormatException localNumberFormatException) {
/* 318 */           this.error = true;
/* 319 */           throw new IOException("Bogus chunk size");
/*     */         }
/*     */ 
/* 326 */         this.rawPos = (i + 1);
/* 327 */         this.chunkRead = 0;
/*     */ 
/* 332 */         if (this.chunkSize > 0)
/* 333 */           this.state = 2;
/*     */         else {
/* 335 */           this.state = 4;
/*     */         }
/* 337 */         break;
/*     */       case 2:
/* 347 */         if (this.rawPos >= this.rawCount) {
/* 348 */           return;
/*     */         }
/*     */ 
/* 355 */         int k = Math.min(this.chunkSize - this.chunkRead, this.rawCount - this.rawPos);
/*     */ 
/* 360 */         if (this.chunkData.length < this.chunkCount + k) {
/* 361 */           int m = this.chunkCount - this.chunkPos;
/* 362 */           if (this.chunkData.length < m + k) {
/* 363 */             localObject = new byte[m + k];
/* 364 */             System.arraycopy(this.chunkData, this.chunkPos, localObject, 0, m);
/* 365 */             this.chunkData = ((byte[])localObject);
/*     */           } else {
/* 367 */             System.arraycopy(this.chunkData, this.chunkPos, this.chunkData, 0, m);
/*     */           }
/* 369 */           this.chunkPos = 0;
/* 370 */           this.chunkCount = m;
/*     */         }
/*     */ 
/* 377 */         System.arraycopy(this.rawData, this.rawPos, this.chunkData, this.chunkCount, k);
/* 378 */         this.rawPos += k;
/* 379 */         this.chunkCount += k;
/* 380 */         this.chunkRead += k;
/*     */ 
/* 386 */         if (this.chunkSize - this.chunkRead <= 0) {
/* 387 */           this.state = 3;
/*     */         }
/*     */         else
/*     */         {
/*     */           return;
/*     */         }
/*     */ 
/*     */         break;
/*     */       case 3:
/* 399 */         if (this.rawPos + 1 >= this.rawCount) {
/* 400 */           return;
/*     */         }
/*     */ 
/* 403 */         if (this.rawData[this.rawPos] != 13) {
/* 404 */           this.error = true;
/* 405 */           throw new IOException("missing CR");
/*     */         }
/* 407 */         if (this.rawData[(this.rawPos + 1)] != 10) {
/* 408 */           this.error = true;
/* 409 */           throw new IOException("missing LF");
/*     */         }
/* 411 */         this.rawPos += 2;
/*     */ 
/* 416 */         this.state = 1;
/* 417 */         break;
/*     */       case 4:
/* 429 */         i = this.rawPos;
/* 430 */         while ((i < this.rawCount) && 
/* 431 */           (this.rawData[i] != 10))
/*     */         {
/* 434 */           i++;
/*     */         }
/* 436 */         if (i >= this.rawCount) {
/* 437 */           return;
/*     */         }
/*     */ 
/* 440 */         if (i == this.rawPos) {
/* 441 */           this.error = true;
/* 442 */           throw new IOException("LF should be proceeded by CR");
/*     */         }
/* 444 */         if (this.rawData[(i - 1)] != 13) {
/* 445 */           this.error = true;
/* 446 */           throw new IOException("LF should be proceeded by CR");
/*     */         }
/*     */ 
/* 452 */         if (i == this.rawPos + 1)
/*     */         {
/* 454 */           this.state = 5;
/* 455 */           closeUnderlying();
/*     */ 
/* 457 */           return;
/*     */         }
/*     */ 
/* 464 */         String str2 = new String(this.rawData, this.rawPos, i - this.rawPos, "US-ASCII");
/* 465 */         j = str2.indexOf(':');
/* 466 */         if (j == -1) {
/* 467 */           throw new IOException("Malformed tailer - format should be key:value");
/*     */         }
/* 469 */         localObject = str2.substring(0, j).trim();
/* 470 */         String str3 = str2.substring(j + 1, str2.length()).trim();
/*     */ 
/* 472 */         this.responses.add((String)localObject, str3);
/*     */ 
/* 477 */         this.rawPos = (i + 1);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private int readAheadNonBlocking()
/*     */     throws IOException
/*     */   {
/* 498 */     int i = this.in.available();
/* 499 */     if (i > 0)
/*     */     {
/* 502 */       ensureRawAvailable(i);
/*     */       int j;
/*     */       try
/*     */       {
/* 506 */         j = this.in.read(this.rawData, this.rawCount, i);
/*     */       } catch (IOException localIOException) {
/* 508 */         this.error = true;
/* 509 */         throw localIOException;
/*     */       }
/* 511 */       if (j < 0) {
/* 512 */         this.error = true;
/* 513 */         return -1;
/*     */       }
/* 515 */       this.rawCount += j;
/*     */ 
/* 520 */       processRaw();
/*     */     }
/*     */ 
/* 526 */     return this.chunkCount - this.chunkPos;
/*     */   }
/*     */ 
/*     */   private int readAheadBlocking()
/*     */     throws IOException
/*     */   {
/*     */     do
/*     */     {
/* 540 */       if (this.state == 5) {
/* 541 */         return -1;
/*     */       }
/*     */ 
/* 549 */       ensureRawAvailable(32);
/*     */       int i;
/*     */       try
/*     */       {
/* 552 */         i = this.in.read(this.rawData, this.rawCount, this.rawData.length - this.rawCount);
/*     */       } catch (IOException localIOException) {
/* 554 */         this.error = true;
/* 555 */         throw localIOException;
/*     */       }
/*     */ 
/* 563 */       if (i < 0) {
/* 564 */         this.error = true;
/* 565 */         throw new IOException("Premature EOF");
/*     */       }
/*     */ 
/* 571 */       this.rawCount += i;
/* 572 */       processRaw();
/*     */     }
/* 574 */     while (this.chunkCount <= 0);
/*     */ 
/* 579 */     return this.chunkCount - this.chunkPos;
/*     */   }
/*     */ 
/*     */   private int readAhead(boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 593 */     if (this.state == 5) {
/* 594 */       return -1;
/*     */     }
/*     */ 
/* 600 */     if (this.chunkPos >= this.chunkCount) {
/* 601 */       this.chunkCount = 0;
/* 602 */       this.chunkPos = 0;
/*     */     }
/*     */ 
/* 608 */     if (paramBoolean) {
/* 609 */       return readAheadBlocking();
/*     */     }
/* 611 */     return readAheadNonBlocking();
/*     */   }
/*     */ 
/*     */   public ChunkedInputStream(InputStream paramInputStream, HttpClient paramHttpClient, MessageHeader paramMessageHeader)
/*     */     throws IOException
/*     */   {
/* 627 */     this.in = paramInputStream;
/* 628 */     this.responses = paramMessageHeader;
/* 629 */     this.hc = paramHttpClient;
/*     */ 
/* 635 */     this.state = 1;
/*     */   }
/*     */ 
/*     */   public synchronized int read()
/*     */     throws IOException
/*     */   {
/* 649 */     ensureOpen();
/* 650 */     if ((this.chunkPos >= this.chunkCount) && 
/* 651 */       (readAhead(true) <= 0)) {
/* 652 */       return -1;
/*     */     }
/*     */ 
/* 655 */     return this.chunkData[(this.chunkPos++)] & 0xFF;
/*     */   }
/*     */ 
/*     */   public synchronized int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 673 */     ensureOpen();
/* 674 */     if ((paramInt1 < 0) || (paramInt1 > paramArrayOfByte.length) || (paramInt2 < 0) || (paramInt1 + paramInt2 > paramArrayOfByte.length) || (paramInt1 + paramInt2 < 0))
/*     */     {
/* 676 */       throw new IndexOutOfBoundsException();
/* 677 */     }if (paramInt2 == 0) {
/* 678 */       return 0;
/*     */     }
/*     */ 
/* 681 */     int i = this.chunkCount - this.chunkPos;
/* 682 */     if (i <= 0)
/*     */     {
/* 688 */       if (this.state == 2) {
/* 689 */         return fastRead(paramArrayOfByte, paramInt1, paramInt2);
/*     */       }
/*     */ 
/* 696 */       i = readAhead(true);
/* 697 */       if (i < 0) {
/* 698 */         return -1;
/*     */       }
/*     */     }
/* 701 */     int j = i < paramInt2 ? i : paramInt2;
/* 702 */     System.arraycopy(this.chunkData, this.chunkPos, paramArrayOfByte, paramInt1, j);
/* 703 */     this.chunkPos += j;
/*     */ 
/* 705 */     return j;
/*     */   }
/*     */ 
/*     */   public synchronized int available()
/*     */     throws IOException
/*     */   {
/* 718 */     ensureOpen();
/*     */ 
/* 720 */     int i = this.chunkCount - this.chunkPos;
/* 721 */     if (i > 0) {
/* 722 */       return i;
/*     */     }
/*     */ 
/* 725 */     i = readAhead(false);
/*     */ 
/* 727 */     if (i < 0) {
/* 728 */       return 0;
/*     */     }
/* 730 */     return i;
/*     */   }
/*     */ 
/*     */   public synchronized void close()
/*     */     throws IOException
/*     */   {
/* 746 */     if (this.closed) {
/* 747 */       return;
/*     */     }
/* 749 */     closeUnderlying();
/* 750 */     this.closed = true;
/*     */   }
/*     */ 
/*     */   public synchronized boolean hurry()
/*     */   {
/* 763 */     if ((this.in == null) || (this.error)) {
/* 764 */       return false;
/*     */     }
/*     */     try
/*     */     {
/* 768 */       readAhead(false);
/*     */     } catch (Exception localException) {
/* 770 */       return false;
/*     */     }
/*     */ 
/* 773 */     if (this.error) {
/* 774 */       return false;
/*     */     }
/*     */ 
/* 777 */     return this.state == 5;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.www.http.ChunkedInputStream
 * JD-Core Version:    0.6.2
 */