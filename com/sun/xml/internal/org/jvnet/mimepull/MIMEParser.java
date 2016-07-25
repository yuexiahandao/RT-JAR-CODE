/*     */ package com.sun.xml.internal.org.jvnet.mimepull;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Iterator;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ class MIMEParser
/*     */   implements Iterable<MIMEEvent>
/*     */ {
/*  54 */   private static final Logger LOGGER = Logger.getLogger(MIMEParser.class.getName());
/*     */   private static final int NO_LWSP = 1000;
/*  61 */   private STATE state = STATE.START_MESSAGE;
/*     */   private final InputStream in;
/*     */   private final byte[] bndbytes;
/*     */   private final int bl;
/*     */   private final MIMEConfig config;
/*  67 */   private final int[] bcs = new int[''];
/*     */   private final int[] gss;
/*     */   private boolean parsed;
/*  79 */   private boolean done = false;
/*     */   private boolean eof;
/*     */   private final int capacity;
/*     */   private byte[] buf;
/*     */   private int len;
/*     */   private boolean bol;
/*     */ 
/*     */   MIMEParser(InputStream in, String boundary, MIMEConfig config)
/*     */   {
/*  91 */     this.in = in;
/*  92 */     this.bndbytes = getBytes("--" + boundary);
/*  93 */     this.bl = this.bndbytes.length;
/*  94 */     this.config = config;
/*  95 */     this.gss = new int[this.bl];
/*  96 */     compileBoundaryPattern();
/*     */ 
/*  99 */     this.capacity = (config.chunkSize + 2 + this.bl + 4 + 1000);
/* 100 */     createBuf(this.capacity);
/*     */   }
/*     */ 
/*     */   public Iterator<MIMEEvent> iterator()
/*     */   {
/* 110 */     return new MIMEEventIterator();
/*     */   }
/*     */ 
/*     */   private InternetHeaders readHeaders()
/*     */   {
/* 178 */     if (!this.eof) {
/* 179 */       fillBuf();
/*     */     }
/* 181 */     return new InternetHeaders(new LineInputStream());
/*     */   }
/*     */ 
/*     */   private ByteBuffer readBody()
/*     */   {
/* 193 */     if (!this.eof) {
/* 194 */       fillBuf();
/*     */     }
/* 196 */     int start = match(this.buf, 0, this.len);
/* 197 */     if (start == -1)
/*     */     {
/* 199 */       assert ((this.eof) || (this.len >= this.config.chunkSize));
/* 200 */       int chunkSize = this.eof ? this.len : this.config.chunkSize;
/* 201 */       if (this.eof)
/*     */       {
/* 205 */         this.done = true;
/* 206 */         this.state = STATE.END_PART;
/*     */       }
/* 208 */       return adjustBuf(chunkSize, this.len - chunkSize);
/*     */     }
/*     */ 
/* 212 */     int chunkLen = start;
/* 213 */     if ((!this.bol) || (start != 0))
/*     */     {
/* 215 */       if ((start > 0) && ((this.buf[(start - 1)] == 10) || (this.buf[(start - 1)] == 13))) {
/* 216 */         chunkLen--;
/* 217 */         if ((this.buf[(start - 1)] == 10) && (start > 1) && (this.buf[(start - 2)] == 13))
/* 218 */           chunkLen--;
/*     */       }
/*     */       else {
/* 221 */         return adjustBuf(start + 1, this.len - start - 1);
/*     */       }
/*     */     }
/* 224 */     if ((start + this.bl + 1 < this.len) && (this.buf[(start + this.bl)] == 45) && (this.buf[(start + this.bl + 1)] == 45)) {
/* 225 */       this.state = STATE.END_PART;
/* 226 */       this.done = true;
/* 227 */       return adjustBuf(chunkLen, 0);
/*     */     }
/*     */ 
/* 231 */     int lwsp = 0;
/* 232 */     for (int i = start + this.bl; (i < this.len) && ((this.buf[i] == 32) || (this.buf[i] == 9)); i++) {
/* 233 */       lwsp++;
/*     */     }
/*     */ 
/* 237 */     if ((start + this.bl + lwsp < this.len) && (this.buf[(start + this.bl + lwsp)] == 10)) {
/* 238 */       this.state = STATE.END_PART;
/* 239 */       return adjustBuf(chunkLen, this.len - start - this.bl - lwsp - 1);
/* 240 */     }if ((start + this.bl + lwsp + 1 < this.len) && (this.buf[(start + this.bl + lwsp)] == 13) && (this.buf[(start + this.bl + lwsp + 1)] == 10)) {
/* 241 */       this.state = STATE.END_PART;
/* 242 */       return adjustBuf(chunkLen, this.len - start - this.bl - lwsp - 2);
/* 243 */     }if (start + this.bl + lwsp + 1 < this.len)
/* 244 */       return adjustBuf(chunkLen + 1, this.len - chunkLen - 1);
/* 245 */     if (this.eof) {
/* 246 */       this.done = true;
/* 247 */       this.state = STATE.END_PART;
/* 248 */       return adjustBuf(chunkLen, 0);
/*     */     }
/*     */ 
/* 255 */     return adjustBuf(chunkLen, this.len - chunkLen);
/*     */   }
/*     */ 
/*     */   private ByteBuffer adjustBuf(int chunkSize, int remaining)
/*     */   {
/* 268 */     assert (this.buf != null);
/* 269 */     assert (chunkSize >= 0);
/* 270 */     assert (remaining >= 0);
/*     */ 
/* 272 */     byte[] temp = this.buf;
/*     */ 
/* 274 */     createBuf(remaining);
/* 275 */     System.arraycopy(temp, this.len - remaining, this.buf, 0, remaining);
/* 276 */     this.len = remaining;
/*     */ 
/* 278 */     return ByteBuffer.wrap(temp, 0, chunkSize);
/*     */   }
/*     */ 
/*     */   private void createBuf(int min) {
/* 282 */     this.buf = new byte[min < this.capacity ? this.capacity : min];
/*     */   }
/*     */ 
/*     */   private void skipPreamble()
/*     */   {
/*     */     while (true)
/*     */     {
/* 291 */       if (!this.eof) {
/* 292 */         fillBuf();
/*     */       }
/* 294 */       int start = match(this.buf, 0, this.len);
/* 295 */       if (start == -1)
/*     */       {
/* 297 */         if (this.eof) {
/* 298 */           throw new MIMEParsingException("Missing start boundary");
/*     */         }
/* 300 */         adjustBuf(this.len - this.bl + 1, this.bl - 1);
/*     */       }
/* 305 */       else if (start > this.config.chunkSize) {
/* 306 */         adjustBuf(start, this.len - start);
/*     */       }
/*     */       else
/*     */       {
/* 310 */         int lwsp = 0;
/* 311 */         for (int i = start + this.bl; (i < this.len) && ((this.buf[i] == 32) || (this.buf[i] == 9)); i++) {
/* 312 */           lwsp++;
/*     */         }
/*     */ 
/* 315 */         if ((start + this.bl + lwsp < this.len) && ((this.buf[(start + this.bl + lwsp)] == 10) || (this.buf[(start + this.bl + lwsp)] == 13))) {
/* 316 */           if (this.buf[(start + this.bl + lwsp)] == 10) {
/* 317 */             adjustBuf(start + this.bl + lwsp + 1, this.len - start - this.bl - lwsp - 1);
/* 318 */             break;
/* 319 */           }if ((start + this.bl + lwsp + 1 < this.len) && (this.buf[(start + this.bl + lwsp + 1)] == 10)) {
/* 320 */             adjustBuf(start + this.bl + lwsp + 2, this.len - start - this.bl - lwsp - 2);
/* 321 */             break;
/*     */           }
/*     */         }
/* 324 */         adjustBuf(start + 1, this.len - start - 1);
/*     */       }
/*     */     }
/* 326 */     LOGGER.fine("Skipped the preamble. buffer len=" + this.len);
/*     */   }
/*     */ 
/*     */   private static byte[] getBytes(String s) {
/* 330 */     char[] chars = s.toCharArray();
/* 331 */     int size = chars.length;
/* 332 */     byte[] bytes = new byte[size];
/*     */ 
/* 334 */     for (int i = 0; i < size; )
/* 335 */       bytes[i] = ((byte)chars[(i++)]);
/* 336 */     return bytes;
/*     */   }
/*     */ 
/*     */   private void compileBoundaryPattern()
/*     */   {
/* 354 */     for (int i = 0; i < this.bndbytes.length; i++) {
/* 355 */       this.bcs[(this.bndbytes[i] & 0x7F)] = (i + 1);
/*     */     }
/*     */ 
/* 360 */     label106: for (i = this.bndbytes.length; i > 0; i--)
/*     */     {
/* 362 */       for (int j = this.bndbytes.length - 1; j >= i; j--)
/*     */       {
/* 364 */         if (this.bndbytes[j] != this.bndbytes[(j - i)])
/*     */           break label106;
/* 366 */         this.gss[(j - 1)] = i;
/*     */       }
/*     */ 
/* 376 */       while (j > 0) {
/* 377 */         this.gss[(--j)] = i;
/*     */       }
/*     */     }
/*     */ 
/* 381 */     this.gss[(this.bndbytes.length - 1)] = 1;
/*     */   }
/*     */ 
/*     */   private int match(byte[] mybuf, int off, int len)
/*     */   {
/* 395 */     int last = len - this.bndbytes.length;
/*     */ 
/* 398 */     if (off <= last)
/*     */     {
/* 400 */       for (int j = this.bndbytes.length - 1; ; j--) { if (j < 0) break label86;
/* 401 */         byte ch = mybuf[(off + j)];
/* 402 */         if (ch != this.bndbytes[j])
/*     */         {
/* 405 */           off += Math.max(j + 1 - this.bcs[(ch & 0x7F)], this.gss[j]);
/* 406 */           break;
/*     */         }
/*     */       }
/*     */ 
/* 410 */       label86: return off;
/*     */     }
/* 412 */     return -1;
/*     */   }
/*     */ 
/*     */   private void fillBuf()
/*     */   {
/* 419 */     LOGGER.finer("Before fillBuf() buffer len=" + this.len);
/* 420 */     assert (!this.eof);
/* 421 */     while (this.len < this.buf.length) {
/*     */       int read;
/*     */       try {
/* 424 */         read = this.in.read(this.buf, this.len, this.buf.length - this.len);
/*     */       } catch (IOException ioe) {
/* 426 */         throw new MIMEParsingException(ioe);
/*     */       }
/* 428 */       if (read == -1) {
/* 429 */         this.eof = true;
/*     */         try {
/* 431 */           LOGGER.fine("Closing the input stream.");
/* 432 */           this.in.close();
/*     */         } catch (IOException ioe) {
/* 434 */           throw new MIMEParsingException(ioe);
/*     */         }
/*     */       }
/*     */ 
/* 438 */       this.len += read;
/*     */     }
/*     */ 
/* 441 */     LOGGER.finer("After fillBuf() buffer len=" + this.len);
/*     */   }
/*     */ 
/*     */   private void doubleBuf() {
/* 445 */     byte[] temp = new byte[2 * this.len];
/* 446 */     System.arraycopy(this.buf, 0, temp, 0, this.len);
/* 447 */     this.buf = temp;
/* 448 */     if (!this.eof)
/* 449 */       fillBuf();
/*     */   }
/*     */ 
/*     */   class LineInputStream
/*     */   {
/*     */     private int offset;
/*     */ 
/*     */     LineInputStream()
/*     */     {
/*     */     }
/*     */ 
/*     */     public String readLine()
/*     */       throws IOException
/*     */     {
/* 469 */       int hdrLen = 0;
/* 470 */       int lwsp = 0;
/* 471 */       while (this.offset + hdrLen < MIMEParser.this.len) {
/* 472 */         if (MIMEParser.this.buf[(this.offset + hdrLen)] == 10) {
/* 473 */           lwsp = 1;
/* 474 */           break;
/*     */         }
/* 476 */         if (this.offset + hdrLen + 1 == MIMEParser.this.len) {
/* 477 */           MIMEParser.this.doubleBuf();
/*     */         }
/* 479 */         if (this.offset + hdrLen + 1 >= MIMEParser.this.len) {
/* 480 */           assert (MIMEParser.this.eof);
/* 481 */           return null;
/*     */         }
/* 483 */         if ((MIMEParser.this.buf[(this.offset + hdrLen)] == 13) && (MIMEParser.this.buf[(this.offset + hdrLen + 1)] == 10)) {
/* 484 */           lwsp = 2;
/* 485 */           break;
/*     */         }
/* 487 */         hdrLen++;
/*     */       }
/* 489 */       if (hdrLen == 0) {
/* 490 */         MIMEParser.this.adjustBuf(this.offset + lwsp, MIMEParser.this.len - this.offset - lwsp);
/* 491 */         return null;
/*     */       }
/*     */ 
/* 494 */       String hdr = new String(MIMEParser.this.buf, this.offset, hdrLen);
/* 495 */       this.offset += hdrLen + lwsp;
/* 496 */       return hdr;
/*     */     }
/*     */   }
/*     */ 
/*     */   class MIMEEventIterator
/*     */     implements Iterator<MIMEEvent>
/*     */   {
/*     */     MIMEEventIterator()
/*     */     {
/*     */     }
/*     */ 
/*     */     public boolean hasNext()
/*     */     {
/* 116 */       return !MIMEParser.this.parsed;
/*     */     }
/*     */ 
/*     */     public MIMEEvent next() {
/* 120 */       switch (MIMEParser.1.$SwitchMap$com$sun$xml$internal$org$jvnet$mimepull$MIMEParser$STATE[MIMEParser.this.state.ordinal()]) {
/*     */       case 1:
/* 122 */         MIMEParser.LOGGER.finer("MIMEParser state=" + MIMEParser.STATE.START_MESSAGE);
/* 123 */         MIMEParser.this.state = MIMEParser.STATE.SKIP_PREAMBLE;
/* 124 */         return MIMEEvent.START_MESSAGE;
/*     */       case 2:
/* 127 */         MIMEParser.LOGGER.finer("MIMEParser state=" + MIMEParser.STATE.SKIP_PREAMBLE);
/* 128 */         MIMEParser.this.skipPreamble();
/*     */       case 3:
/* 131 */         MIMEParser.LOGGER.finer("MIMEParser state=" + MIMEParser.STATE.START_PART);
/* 132 */         MIMEParser.this.state = MIMEParser.STATE.HEADERS;
/* 133 */         return MIMEEvent.START_PART;
/*     */       case 4:
/* 136 */         MIMEParser.LOGGER.finer("MIMEParser state=" + MIMEParser.STATE.HEADERS);
/* 137 */         InternetHeaders ih = MIMEParser.this.readHeaders();
/* 138 */         MIMEParser.this.state = MIMEParser.STATE.BODY;
/* 139 */         MIMEParser.this.bol = true;
/* 140 */         return new MIMEEvent.Headers(ih);
/*     */       case 5:
/* 143 */         MIMEParser.LOGGER.finer("MIMEParser state=" + MIMEParser.STATE.BODY);
/* 144 */         ByteBuffer buf = MIMEParser.this.readBody();
/* 145 */         MIMEParser.this.bol = false;
/* 146 */         return new MIMEEvent.Content(buf);
/*     */       case 6:
/* 149 */         MIMEParser.LOGGER.finer("MIMEParser state=" + MIMEParser.STATE.END_PART);
/* 150 */         if (MIMEParser.this.done)
/* 151 */           MIMEParser.this.state = MIMEParser.STATE.END_MESSAGE;
/*     */         else {
/* 153 */           MIMEParser.this.state = MIMEParser.STATE.START_PART;
/*     */         }
/* 155 */         return MIMEEvent.END_PART;
/*     */       case 7:
/* 158 */         MIMEParser.LOGGER.finer("MIMEParser state=" + MIMEParser.STATE.END_MESSAGE);
/* 159 */         MIMEParser.this.parsed = true;
/* 160 */         return MIMEEvent.END_MESSAGE;
/*     */       }
/*     */ 
/* 163 */       throw new MIMEParsingException("Unknown Parser state = " + MIMEParser.this.state);
/*     */     }
/*     */ 
/*     */     public void remove()
/*     */     {
/* 168 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static enum STATE
/*     */   {
/*  60 */     START_MESSAGE, SKIP_PREAMBLE, START_PART, HEADERS, BODY, END_PART, END_MESSAGE;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.org.jvnet.mimepull.MIMEParser
 * JD-Core Version:    0.6.2
 */