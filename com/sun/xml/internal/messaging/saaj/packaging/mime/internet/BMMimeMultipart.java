/*     */ package com.sun.xml.internal.messaging.saaj.packaging.mime.internet;
/*     */ 
/*     */ import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;
/*     */ import com.sun.xml.internal.messaging.saaj.packaging.mime.util.ASCIIUtility;
/*     */ import com.sun.xml.internal.messaging.saaj.packaging.mime.util.OutputUtil;
/*     */ import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
/*     */ import com.sun.xml.internal.messaging.saaj.util.FinalArrayList;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.BitSet;
/*     */ import javax.activation.DataSource;
/*     */ 
/*     */ public class BMMimeMultipart extends MimeMultipart
/*     */ {
/*  80 */   private boolean begining = true;
/*     */ 
/*  82 */   int[] bcs = new int[256];
/*  83 */   int[] gss = null;
/*     */   private static final int BUFFER_SIZE = 4096;
/*  85 */   private byte[] buffer = new byte[4096];
/*  86 */   private byte[] prevBuffer = new byte[4096];
/*  87 */   private BitSet lastPartFound = new BitSet(1);
/*     */ 
/*  90 */   private InputStream in = null;
/*  91 */   private String boundary = null;
/*     */ 
/*  93 */   int b = 0;
/*     */ 
/*  96 */   private boolean lazyAttachments = false;
/*     */ 
/* 698 */   byte[] buf = new byte[1024];
/*     */ 
/*     */   public BMMimeMultipart()
/*     */   {
/*     */   }
/*     */ 
/*     */   public BMMimeMultipart(String subtype)
/*     */   {
/* 121 */     super(subtype);
/*     */   }
/*     */ 
/*     */   public BMMimeMultipart(DataSource ds, ContentType ct)
/*     */     throws MessagingException
/*     */   {
/* 150 */     super(ds, ct);
/* 151 */     this.boundary = ct.getParameter("boundary");
/*     */   }
/*     */ 
/*     */   public InputStream initStream()
/*     */     throws MessagingException
/*     */   {
/* 173 */     if (this.in == null) {
/*     */       try {
/* 175 */         this.in = this.ds.getInputStream();
/* 176 */         if ((!(this.in instanceof ByteArrayInputStream)) && (!(this.in instanceof BufferedInputStream)) && (!(this.in instanceof SharedInputStream)))
/*     */         {
/* 179 */           this.in = new BufferedInputStream(this.in);
/*     */         }
/*     */       } catch (Exception ex) { throw new MessagingException("No inputstream from datasource"); }
/*     */ 
/*     */ 
/* 184 */       if (!this.in.markSupported()) {
/* 185 */         throw new MessagingException("InputStream does not support Marking");
/*     */       }
/*     */     }
/*     */ 
/* 189 */     return this.in;
/*     */   }
/*     */ 
/*     */   protected void parse()
/*     */     throws MessagingException
/*     */   {
/* 202 */     if (this.parsed) {
/* 203 */       return;
/*     */     }
/* 205 */     initStream();
/*     */ 
/* 207 */     SharedInputStream sin = null;
/* 208 */     if ((this.in instanceof SharedInputStream)) {
/* 209 */       sin = (SharedInputStream)this.in;
/*     */     }
/*     */ 
/* 212 */     String bnd = "--" + this.boundary;
/* 213 */     byte[] bndbytes = ASCIIUtility.getBytes(bnd);
/*     */     try {
/* 215 */       parse(this.in, bndbytes, sin);
/*     */     } catch (IOException ioex) {
/* 217 */       throw new MessagingException("IO Error", ioex);
/*     */     } catch (Exception ex) {
/* 219 */       throw new MessagingException("Error", ex);
/*     */     }
/*     */ 
/* 222 */     this.parsed = true;
/*     */   }
/*     */ 
/*     */   public boolean lastBodyPartFound() {
/* 226 */     return this.lastPartFound.get(0);
/*     */   }
/*     */ 
/*     */   public MimeBodyPart getNextPart(InputStream stream, byte[] pattern, SharedInputStream sin)
/*     */     throws Exception
/*     */   {
/* 233 */     if (!stream.markSupported()) {
/* 234 */       throw new Exception("InputStream does not support Marking");
/*     */     }
/*     */ 
/* 237 */     if (this.begining) {
/* 238 */       compile(pattern);
/* 239 */       if (!skipPreamble(stream, pattern, sin)) {
/* 240 */         throw new Exception("Missing Start Boundary, or boundary does not start on a new line");
/*     */       }
/*     */ 
/* 243 */       this.begining = false;
/*     */     }
/*     */ 
/* 246 */     if (lastBodyPartFound()) {
/* 247 */       throw new Exception("No parts found in Multipart InputStream");
/*     */     }
/*     */ 
/* 250 */     if (sin != null) {
/* 251 */       long start = sin.getPosition();
/* 252 */       this.b = readHeaders(stream);
/* 253 */       if (this.b == -1) {
/* 254 */         throw new Exception("End of Stream encountered while reading part headers");
/*     */       }
/*     */ 
/* 257 */       long[] v = new long[1];
/* 258 */       v[0] = -1L;
/* 259 */       this.b = readBody(stream, pattern, v, null, sin);
/*     */ 
/* 262 */       if ((!ignoreMissingEndBoundary) && 
/* 263 */         (this.b == -1) && (!lastBodyPartFound())) {
/* 264 */         throw new MessagingException("Missing End Boundary for Mime Package : EOF while skipping headers");
/*     */       }
/*     */ 
/* 267 */       long end = v[0];
/* 268 */       MimeBodyPart mbp = createMimeBodyPart(sin.newStream(start, end));
/* 269 */       addBodyPart(mbp);
/* 270 */       return mbp;
/*     */     }
/*     */ 
/* 273 */     InternetHeaders headers = createInternetHeaders(stream);
/* 274 */     ByteOutputStream baos = new ByteOutputStream();
/* 275 */     this.b = readBody(stream, pattern, null, baos, null);
/*     */ 
/* 279 */     if ((!ignoreMissingEndBoundary) && 
/* 280 */       (this.b == -1) && (!lastBodyPartFound())) {
/* 281 */       throw new MessagingException("Missing End Boundary for Mime Package : EOF while skipping headers");
/*     */     }
/*     */ 
/* 284 */     MimeBodyPart mbp = createMimeBodyPart(headers, baos.getBytes(), baos.getCount());
/*     */ 
/* 286 */     addBodyPart(mbp);
/* 287 */     return mbp;
/*     */   }
/*     */ 
/*     */   public boolean parse(InputStream stream, byte[] pattern, SharedInputStream sin)
/*     */     throws Exception
/*     */   {
/* 296 */     while ((!this.lastPartFound.get(0)) && (this.b != -1)) {
/* 297 */       getNextPart(stream, pattern, sin);
/*     */     }
/* 299 */     return true;
/*     */   }
/*     */ 
/*     */   private int readHeaders(InputStream is)
/*     */     throws Exception
/*     */   {
/* 305 */     int b = is.read();
/* 306 */     while (b != -1)
/*     */     {
/* 308 */       if (b == 13) {
/* 309 */         b = is.read();
/* 310 */         if (b == 10) {
/* 311 */           b = is.read();
/* 312 */           if (b == 13) {
/* 313 */             b = is.read();
/* 314 */             if (b == 10) {
/* 315 */               return b;
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 326 */         b = is.read();
/*     */       }
/*     */     }
/* 328 */     if (b == -1) {
/* 329 */       throw new Exception("End of inputstream while reading Mime-Part Headers");
/*     */     }
/*     */ 
/* 332 */     return b;
/*     */   }
/*     */ 
/*     */   private int readBody(InputStream is, byte[] pattern, long[] posVector, ByteOutputStream baos, SharedInputStream sin)
/*     */     throws Exception
/*     */   {
/* 339 */     if (!find(is, pattern, posVector, baos, sin)) {
/* 340 */       throw new Exception("Missing boundary delimitier while reading Body Part");
/*     */     }
/*     */ 
/* 343 */     return this.b;
/*     */   }
/*     */ 
/*     */   private boolean skipPreamble(InputStream is, byte[] pattern, SharedInputStream sin)
/*     */     throws Exception
/*     */   {
/* 349 */     if (!find(is, pattern, sin)) {
/* 350 */       return false;
/*     */     }
/* 352 */     if (this.lastPartFound.get(0)) {
/* 353 */       throw new Exception("Found closing boundary delimiter while trying to skip preamble");
/*     */     }
/*     */ 
/* 356 */     return true;
/*     */   }
/*     */ 
/*     */   public int readNext(InputStream is, byte[] buff, int patternLength, BitSet eof, long[] posVector, SharedInputStream sin)
/*     */     throws Exception
/*     */   {
/* 364 */     int bufferLength = is.read(this.buffer, 0, patternLength);
/* 365 */     if (bufferLength == -1) {
/* 366 */       eof.flip(0);
/* 367 */     } else if (bufferLength < patternLength)
/*     */     {
/* 369 */       int temp = 0;
/* 370 */       long pos = 0L;
/* 371 */       for (int i = bufferLength; 
/* 372 */         i < patternLength; i++) {
/* 373 */         if (sin != null) {
/* 374 */           pos = sin.getPosition();
/*     */         }
/* 376 */         temp = is.read();
/* 377 */         if (temp == -1) {
/* 378 */           eof.flip(0);
/* 379 */           if (sin == null) break;
/* 380 */           posVector[0] = pos; break;
/*     */         }
/*     */ 
/* 384 */         this.buffer[i] = ((byte)temp);
/*     */       }
/* 386 */       bufferLength = i;
/*     */     }
/* 388 */     return bufferLength;
/*     */   }
/*     */ 
/*     */   public boolean find(InputStream is, byte[] pattern, SharedInputStream sin)
/*     */     throws Exception
/*     */   {
/* 394 */     int l = pattern.length;
/* 395 */     int lx = l - 1;
/* 396 */     int bufferLength = 0;
/* 397 */     BitSet eof = new BitSet(1);
/* 398 */     long[] posVector = new long[1];
/*     */     while (true)
/*     */     {
/* 401 */       is.mark(l);
/* 402 */       bufferLength = readNext(is, this.buffer, l, eof, posVector, sin);
/* 403 */       if (eof.get(0))
/*     */       {
/* 405 */         return false;
/*     */       }
/*     */ 
/* 414 */       for (int i = lx; (i >= 0) && 
/* 415 */         (this.buffer[i] == pattern[i]); i--);
/* 420 */       if (i < 0)
/*     */       {
/* 422 */         if (!skipLWSPAndCRLF(is)) {
/* 423 */           throw new Exception("Boundary does not terminate with CRLF");
/*     */         }
/* 425 */         return true;
/*     */       }
/*     */ 
/* 428 */       int s = Math.max(i + 1 - this.bcs[(this.buffer[i] & 0x7F)], this.gss[i]);
/* 429 */       is.reset();
/* 430 */       is.skip(s);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean find(InputStream is, byte[] pattern, long[] posVector, ByteOutputStream out, SharedInputStream sin)
/*     */     throws Exception
/*     */   {
/* 438 */     int l = pattern.length;
/* 439 */     int lx = l - 1;
/* 440 */     int bufferLength = 0;
/* 441 */     int s = 0;
/* 442 */     long endPos = -1L;
/* 443 */     byte[] tmp = null;
/*     */ 
/* 445 */     boolean first = true;
/* 446 */     BitSet eof = new BitSet(1);
/*     */     while (true)
/*     */     {
/* 449 */       is.mark(l);
/* 450 */       if (!first) {
/* 451 */         tmp = this.prevBuffer;
/* 452 */         this.prevBuffer = this.buffer;
/* 453 */         this.buffer = tmp;
/*     */       }
/* 455 */       if (sin != null) {
/* 456 */         endPos = sin.getPosition();
/*     */       }
/*     */ 
/* 459 */       bufferLength = readNext(is, this.buffer, l, eof, posVector, sin);
/*     */ 
/* 461 */       if (bufferLength == -1)
/*     */       {
/* 468 */         this.b = -1;
/* 469 */         if ((s == l) && (sin == null)) {
/* 470 */           out.write(this.prevBuffer, 0, s);
/*     */         }
/* 472 */         return true;
/*     */       }
/*     */ 
/* 475 */       if (bufferLength < l) {
/* 476 */         if (sin == null)
/*     */         {
/* 482 */           out.write(this.buffer, 0, bufferLength);
/*     */         }
/*     */ 
/* 487 */         this.b = -1;
/* 488 */         return true;
/*     */       }
/*     */ 
/* 491 */       for (int i = lx; (i >= 0) && 
/* 492 */         (this.buffer[i] == pattern[i]); i--);
/* 497 */       if (i < 0) {
/* 498 */         if (s > 0)
/*     */         {
/* 502 */           if (s <= 2)
/*     */           {
/* 504 */             if (s == 2) {
/* 505 */               if (this.prevBuffer[1] == 10) {
/* 506 */                 if ((this.prevBuffer[0] != 13) && (this.prevBuffer[0] != 10)) {
/* 507 */                   out.write(this.prevBuffer, 0, 1);
/*     */                 }
/* 509 */                 if (sin != null)
/* 510 */                   posVector[0] = endPos;
/*     */               }
/*     */               else
/*     */               {
/* 514 */                 throw new Exception("Boundary characters encountered in part Body without a preceeding CRLF");
/*     */               }
/*     */ 
/*     */             }
/* 519 */             else if (s == 1) {
/* 520 */               if (this.prevBuffer[0] != 10) {
/* 521 */                 throw new Exception("Boundary characters encountered in part Body without a preceeding CRLF");
/*     */               }
/*     */ 
/* 525 */               if (sin != null) {
/* 526 */                 posVector[0] = endPos;
/*     */               }
/*     */             }
/*     */ 
/*     */           }
/* 531 */           else if (s > 2) {
/* 532 */             if ((this.prevBuffer[(s - 2)] == 13) && (this.prevBuffer[(s - 1)] == 10)) {
/* 533 */               if (sin != null)
/* 534 */                 posVector[0] = (endPos - 2L);
/*     */               else
/* 536 */                 out.write(this.prevBuffer, 0, s - 2);
/*     */             }
/* 538 */             else if (this.prevBuffer[(s - 1)] == 10)
/*     */             {
/* 540 */               if (sin != null)
/* 541 */                 posVector[0] = (endPos - 1L);
/*     */               else
/* 543 */                 out.write(this.prevBuffer, 0, s - 1);
/*     */             }
/*     */             else {
/* 546 */               throw new Exception("Boundary characters encountered in part Body without a preceeding CRLF");
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 553 */         if (!skipLWSPAndCRLF(is));
/* 557 */         return true;
/*     */       }
/*     */ 
/* 560 */       if ((s > 0) && (sin == null)) {
/* 561 */         if (this.prevBuffer[(s - 1)] == 13)
/*     */         {
/* 563 */           if (this.buffer[0] == 10) {
/* 564 */             int j = lx - 1;
/* 565 */             for (j = lx - 1; (j > 0) && 
/* 566 */               (this.buffer[(j + 1)] == pattern[j]); j--);
/* 570 */             if (j == 0)
/*     */             {
/* 573 */               out.write(this.prevBuffer, 0, s - 1);
/*     */             }
/* 575 */             else out.write(this.prevBuffer, 0, s); 
/*     */           }
/*     */           else
/*     */           {
/* 578 */             out.write(this.prevBuffer, 0, s);
/*     */           }
/*     */         }
/* 581 */         else out.write(this.prevBuffer, 0, s);
/*     */ 
/*     */       }
/*     */ 
/* 585 */       s = Math.max(i + 1 - this.bcs[(this.buffer[i] & 0x7F)], this.gss[i]);
/* 586 */       is.reset();
/* 587 */       is.skip(s);
/* 588 */       if (first)
/* 589 */         first = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean skipLWSPAndCRLF(InputStream is)
/*     */     throws Exception
/*     */   {
/* 596 */     this.b = is.read();
/*     */ 
/* 598 */     if (this.b == 10) {
/* 599 */       return true;
/*     */     }
/*     */ 
/* 602 */     if (this.b == 13) {
/* 603 */       this.b = is.read();
/*     */ 
/* 605 */       if (this.b == 13) {
/* 606 */         this.b = is.read();
/*     */       }
/* 608 */       if (this.b == 10) {
/* 609 */         return true;
/*     */       }
/* 611 */       throw new Exception("transport padding after a Mime Boundary  should end in a CRLF, found CR only");
/*     */     }
/*     */ 
/* 616 */     if (this.b == 45) {
/* 617 */       this.b = is.read();
/* 618 */       if (this.b != 45) {
/* 619 */         throw new Exception("Unexpected singular '-' character after Mime Boundary");
/*     */       }
/*     */ 
/* 623 */       this.lastPartFound.flip(0);
/*     */ 
/* 625 */       this.b = is.read();
/*     */     }
/*     */ 
/* 629 */     while ((this.b != -1) && ((this.b == 32) || (this.b == 9))) {
/* 630 */       this.b = is.read();
/* 631 */       if (this.b == 10) {
/* 632 */         return true;
/*     */       }
/* 634 */       if (this.b == 13) {
/* 635 */         this.b = is.read();
/*     */ 
/* 637 */         if (this.b == 13) {
/* 638 */           this.b = is.read();
/*     */         }
/* 640 */         if (this.b == 10) {
/* 641 */           return true;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 646 */     if (this.b == -1)
/*     */     {
/* 648 */       if (!this.lastPartFound.get(0)) {
/* 649 */         throw new Exception("End of Multipart Stream before encountering  closing boundary delimiter");
/*     */       }
/*     */ 
/* 652 */       return true;
/*     */     }
/* 654 */     return false;
/*     */   }
/*     */ 
/*     */   private void compile(byte[] pattern) {
/* 658 */     int l = pattern.length;
/*     */ 
/* 667 */     for (int i = 0; i < l; i++) {
/* 668 */       this.bcs[pattern[i]] = (i + 1);
/*     */     }
/*     */ 
/* 672 */     this.gss = new int[l];
/* 673 */     label99: for (i = l; i > 0; i--)
/*     */     {
/* 675 */       for (int j = l - 1; j >= i; j--)
/*     */       {
/* 677 */         if (pattern[j] != pattern[(j - i)])
/*     */           break label99;
/* 679 */         this.gss[(j - 1)] = i;
/*     */       }
/*     */ 
/* 686 */       while (j > 0) {
/* 687 */         this.gss[(--j)] = i;
/*     */       }
/*     */     }
/* 690 */     this.gss[(l - 1)] = 1;
/*     */   }
/*     */ 
/*     */   public void writeTo(OutputStream os)
/*     */     throws IOException, MessagingException
/*     */   {
/* 704 */     if (this.in != null) {
/* 705 */       this.contentType.setParameter("boundary", this.boundary);
/*     */     }
/*     */ 
/* 708 */     String bnd = "--" + this.contentType.getParameter("boundary");
/* 709 */     for (int i = 0; i < this.parts.size(); i++) {
/* 710 */       OutputUtil.writeln(bnd, os);
/* 711 */       ((MimeBodyPart)this.parts.get(i)).writeTo(os);
/* 712 */       OutputUtil.writeln(os);
/*     */     }
/*     */ 
/* 715 */     if (this.in != null) {
/* 716 */       OutputUtil.writeln(bnd, os);
/* 717 */       if (((os instanceof ByteOutputStream)) && (this.lazyAttachments)) {
/* 718 */         ((ByteOutputStream)os).write(this.in);
/*     */       } else {
/* 720 */         ByteOutputStream baos = new ByteOutputStream(this.in.available());
/* 721 */         baos.write(this.in);
/* 722 */         baos.writeTo(os);
/*     */ 
/* 725 */         this.in = baos.newInputStream();
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 731 */       OutputUtil.writeAsAscii(bnd, os);
/* 732 */       OutputUtil.writeAsAscii("--", os);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setInputStream(InputStream is) {
/* 737 */     this.in = is;
/*     */   }
/*     */ 
/*     */   public InputStream getInputStream() {
/* 741 */     return this.in;
/*     */   }
/*     */ 
/*     */   public void setBoundary(String bnd) {
/* 745 */     this.boundary = bnd;
/* 746 */     if (this.contentType != null)
/* 747 */       this.contentType.setParameter("boundary", bnd);
/*     */   }
/*     */ 
/*     */   public String getBoundary() {
/* 751 */     return this.boundary;
/*     */   }
/*     */ 
/*     */   public boolean isEndOfStream() {
/* 755 */     return this.b == -1;
/*     */   }
/*     */ 
/*     */   public void setLazyAttachments(boolean flag) {
/* 759 */     this.lazyAttachments = flag;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.packaging.mime.internet.BMMimeMultipart
 * JD-Core Version:    0.6.2
 */