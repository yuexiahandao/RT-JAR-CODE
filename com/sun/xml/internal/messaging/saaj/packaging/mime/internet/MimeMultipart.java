/*     */ package com.sun.xml.internal.messaging.saaj.packaging.mime.internet;
/*     */ 
/*     */ import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;
/*     */ import com.sun.xml.internal.messaging.saaj.packaging.mime.MultipartDataSource;
/*     */ import com.sun.xml.internal.messaging.saaj.packaging.mime.util.ASCIIUtility;
/*     */ import com.sun.xml.internal.messaging.saaj.packaging.mime.util.LineInputStream;
/*     */ import com.sun.xml.internal.messaging.saaj.packaging.mime.util.OutputUtil;
/*     */ import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
/*     */ import com.sun.xml.internal.messaging.saaj.util.FinalArrayList;
/*     */ import com.sun.xml.internal.messaging.saaj.util.SAAJUtil;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import javax.activation.DataSource;
/*     */ 
/*     */ public class MimeMultipart
/*     */ {
/*  83 */   protected DataSource ds = null;
/*     */ 
/*  91 */   protected boolean parsed = true;
/*     */ 
/*  96 */   protected FinalArrayList parts = new FinalArrayList();
/*     */   protected ContentType contentType;
/*     */   protected MimeBodyPart parent;
/* 114 */   protected static boolean ignoreMissingEndBoundary = SAAJUtil.getSystemBoolean("saaj.mime.multipart.ignoremissingendboundary");
/*     */ 
/*     */   public MimeMultipart()
/*     */   {
/* 126 */     this("mixed");
/*     */   }
/*     */ 
/*     */   public MimeMultipart(String subtype)
/*     */   {
/* 142 */     String boundary = UniqueValue.getUniqueBoundaryValue();
/* 143 */     this.contentType = new ContentType("multipart", subtype, null);
/* 144 */     this.contentType.setParameter("boundary", boundary);
/*     */   }
/*     */ 
/*     */   public MimeMultipart(DataSource ds, ContentType ct)
/*     */     throws MessagingException
/*     */   {
/* 170 */     this.parsed = false;
/* 171 */     this.ds = ds;
/* 172 */     if (ct == null)
/* 173 */       this.contentType = new ContentType(ds.getContentType());
/*     */     else
/* 175 */       this.contentType = ct;
/*     */   }
/*     */ 
/*     */   public void setSubType(String subtype)
/*     */   {
/* 186 */     this.contentType.setSubType(subtype);
/*     */   }
/*     */ 
/*     */   public int getCount()
/*     */     throws MessagingException
/*     */   {
/* 195 */     parse();
/* 196 */     if (this.parts == null) {
/* 197 */       return 0;
/*     */     }
/* 199 */     return this.parts.size();
/*     */   }
/*     */ 
/*     */   public MimeBodyPart getBodyPart(int index)
/*     */     throws MessagingException
/*     */   {
/* 211 */     parse();
/* 212 */     if (this.parts == null) {
/* 213 */       throw new IndexOutOfBoundsException("No such BodyPart");
/*     */     }
/* 215 */     return (MimeBodyPart)this.parts.get(index);
/*     */   }
/*     */ 
/*     */   public MimeBodyPart getBodyPart(String CID)
/*     */     throws MessagingException
/*     */   {
/* 227 */     parse();
/*     */ 
/* 229 */     int count = getCount();
/* 230 */     for (int i = 0; i < count; i++) {
/* 231 */       MimeBodyPart part = getBodyPart(i);
/* 232 */       String s = part.getContentID();
/*     */ 
/* 235 */       String sNoAngle = s != null ? s.replaceFirst("^<", "").replaceFirst(">$", "") : null;
/*     */ 
/* 237 */       if ((s != null) && ((s.equals(CID)) || (CID.equals(sNoAngle))))
/* 238 */         return part;
/*     */     }
/* 240 */     return null;
/*     */   }
/*     */ 
/*     */   protected void updateHeaders()
/*     */     throws MessagingException
/*     */   {
/* 261 */     for (int i = 0; i < this.parts.size(); i++)
/* 262 */       ((MimeBodyPart)this.parts.get(i)).updateHeaders();
/*     */   }
/*     */ 
/*     */   public void writeTo(OutputStream os)
/*     */     throws IOException, MessagingException
/*     */   {
/* 271 */     parse();
/*     */ 
/* 273 */     String boundary = "--" + this.contentType.getParameter("boundary");
/*     */ 
/* 275 */     for (int i = 0; i < this.parts.size(); i++) {
/* 276 */       OutputUtil.writeln(boundary, os);
/* 277 */       getBodyPart(i).writeTo(os);
/* 278 */       OutputUtil.writeln(os);
/*     */     }
/*     */ 
/* 282 */     OutputUtil.writeAsAscii(boundary, os);
/* 283 */     OutputUtil.writeAsAscii("--", os);
/* 284 */     os.flush();
/*     */   }
/*     */ 
/*     */   protected void parse()
/*     */     throws MessagingException
/*     */   {
/* 297 */     if (this.parsed) {
/* 298 */       return; } 
/*     */ SharedInputStream sin = null;
/* 302 */     long start = 0L; long end = 0L;
/* 303 */     boolean foundClosingBoundary = false;
/*     */     InputStream in;
/*     */     try { in = this.ds.getInputStream();
/* 307 */       if ((!(in instanceof ByteArrayInputStream)) && (!(in instanceof BufferedInputStream)) && (!(in instanceof SharedInputStream)))
/*     */       {
/* 310 */         in = new BufferedInputStream(in);
/*     */       } } catch (Exception ex) {
/* 312 */       throw new MessagingException("No inputstream from datasource");
/*     */     }
/* 314 */     if ((in instanceof SharedInputStream)) {
/* 315 */       sin = (SharedInputStream)in;
/*     */     }
/* 317 */     String boundary = "--" + this.contentType.getParameter("boundary");
/* 318 */     byte[] bndbytes = ASCIIUtility.getBytes(boundary);
/* 319 */     int bl = bndbytes.length;
/*     */     try
/*     */     {
/* 323 */       LineInputStream lin = new LineInputStream(in);
/*     */       String line;
/* 325 */       while ((line = lin.readLine()) != null)
/*     */       {
/* 333 */         for (int i = line.length() - 1; i >= 0; i--) {
/* 334 */           char c = line.charAt(i);
/* 335 */           if ((c != ' ') && (c != '\t'))
/*     */             break;
/*     */         }
/* 338 */         line = line.substring(0, i + 1);
/* 339 */         if (line.equals(boundary))
/*     */           break;
/*     */       }
/* 342 */       if (line == null) {
/* 343 */         throw new MessagingException("Missing start boundary");
/*     */       }
/*     */ 
/* 349 */       boolean done = false;
/*     */ 
/* 351 */       while (!done) {
/* 352 */         InternetHeaders headers = null;
/* 353 */         if (sin != null) {
/* 354 */           start = sin.getPosition();
/*     */ 
/* 356 */           while (((line = lin.readLine()) != null) && (line.length() > 0));
/* 358 */           if (line == null) {
/* 359 */             if (ignoreMissingEndBoundary) break;
/* 360 */             throw new MessagingException("Missing End Boundary for Mime Package : EOF while skipping headers");
/*     */           }
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 367 */           headers = createInternetHeaders(in);
/*     */         }
/*     */ 
/* 370 */         if (!in.markSupported()) {
/* 371 */           throw new MessagingException("Stream doesn't support mark");
/*     */         }
/* 373 */         ByteOutputStream buf = null;
/*     */ 
/* 375 */         if (sin == null) {
/* 376 */           buf = new ByteOutputStream();
/*     */         }
/* 378 */         boolean bol = true;
/*     */ 
/* 380 */         int eol1 = -1; int eol2 = -1;
/*     */         while (true)
/*     */         {
/* 386 */           if (bol)
/*     */           {
/* 392 */             in.mark(bl + 4 + 1000);
/*     */ 
/* 394 */             for (int i = 0; (i < bl) && 
/* 395 */               (in.read() == bndbytes[i]); i++);
/* 397 */             if (i == bl)
/*     */             {
/* 399 */               int b2 = in.read();
/* 400 */               if ((b2 == 45) && 
/* 401 */                 (in.read() == 45)) {
/* 402 */                 done = true;
/* 403 */                 foundClosingBoundary = true;
/* 404 */                 break;
/*     */               }
/*     */ 
/* 408 */               while ((b2 == 32) || (b2 == 9)) {
/* 409 */                 b2 = in.read();
/*     */               }
/* 411 */               if (b2 == 10)
/*     */                 break;
/* 413 */               if (b2 == 13) {
/* 414 */                 in.mark(1);
/* 415 */                 if (in.read() == 10) break;
/* 416 */                 in.reset(); break;
/*     */               }
/*     */ 
/*     */             }
/*     */ 
/* 421 */             in.reset();
/*     */ 
/* 425 */             if ((buf != null) && (eol1 != -1)) {
/* 426 */               buf.write(eol1);
/* 427 */               if (eol2 != -1)
/* 428 */                 buf.write(eol2);
/* 429 */               eol1 = eol2 = -1;
/*     */             }
/*     */           }
/*     */           int b;
/* 434 */           if ((b = in.read()) < 0) {
/* 435 */             done = true;
/* 436 */             break;
/*     */           }
/*     */ 
/* 443 */           if ((b == 13) || (b == 10)) {
/* 444 */             bol = true;
/* 445 */             if (sin != null)
/* 446 */               end = sin.getPosition() - 1L;
/* 447 */             eol1 = b;
/* 448 */             if (b == 13) {
/* 449 */               in.mark(1);
/* 450 */               if ((b = in.read()) == 10)
/* 451 */                 eol2 = b;
/*     */               else
/* 453 */                 in.reset();
/*     */             }
/*     */           } else {
/* 456 */             bol = false;
/* 457 */             if (buf != null)
/* 458 */               buf.write(b);
/*     */           }
/*     */         }
/*     */         MimeBodyPart part;
/*     */         MimeBodyPart part;
/* 466 */         if (sin != null)
/* 467 */           part = createMimeBodyPart(sin.newStream(start, end));
/*     */         else
/* 469 */           part = createMimeBodyPart(headers, buf.getBytes(), buf.getCount());
/* 470 */         addBodyPart(part);
/*     */       }
/*     */     } catch (IOException ioex) {
/* 473 */       throw new MessagingException("IO Error", ioex);
/*     */     }
/*     */ 
/* 476 */     if ((!ignoreMissingEndBoundary) && (!foundClosingBoundary) && (sin == null)) {
/* 477 */       throw new MessagingException("Missing End Boundary for Mime Package : EOF while skipping headers");
/*     */     }
/* 479 */     this.parsed = true;
/*     */   }
/*     */ 
/*     */   protected InternetHeaders createInternetHeaders(InputStream is)
/*     */     throws MessagingException
/*     */   {
/* 495 */     return new InternetHeaders(is);
/*     */   }
/*     */ 
/*     */   protected MimeBodyPart createMimeBodyPart(InternetHeaders headers, byte[] content, int len)
/*     */   {
/* 510 */     return new MimeBodyPart(headers, content, len);
/*     */   }
/*     */ 
/*     */   protected MimeBodyPart createMimeBodyPart(InputStream is)
/*     */     throws MessagingException
/*     */   {
/* 525 */     return new MimeBodyPart(is);
/*     */   }
/*     */ 
/*     */   protected void setMultipartDataSource(MultipartDataSource mp)
/*     */     throws MessagingException
/*     */   {
/* 546 */     this.contentType = new ContentType(mp.getContentType());
/*     */ 
/* 548 */     int count = mp.getCount();
/* 549 */     for (int i = 0; i < count; i++)
/* 550 */       addBodyPart(mp.getBodyPart(i));
/*     */   }
/*     */ 
/*     */   public ContentType getContentType()
/*     */   {
/* 563 */     return this.contentType;
/*     */   }
/*     */ 
/*     */   public boolean removeBodyPart(MimeBodyPart part)
/*     */     throws MessagingException
/*     */   {
/* 575 */     if (this.parts == null) {
/* 576 */       throw new MessagingException("No such body part");
/*     */     }
/* 578 */     boolean ret = this.parts.remove(part);
/* 579 */     part.setParent(null);
/* 580 */     return ret;
/*     */   }
/*     */ 
/*     */   public void removeBodyPart(int index)
/*     */   {
/* 592 */     if (this.parts == null) {
/* 593 */       throw new IndexOutOfBoundsException("No such BodyPart");
/*     */     }
/* 595 */     MimeBodyPart part = (MimeBodyPart)this.parts.get(index);
/* 596 */     this.parts.remove(index);
/* 597 */     part.setParent(null);
/*     */   }
/*     */ 
/*     */   public synchronized void addBodyPart(MimeBodyPart part)
/*     */   {
/* 607 */     if (this.parts == null) {
/* 608 */       this.parts = new FinalArrayList();
/*     */     }
/* 610 */     this.parts.add(part);
/* 611 */     part.setParent(this);
/*     */   }
/*     */ 
/*     */   public synchronized void addBodyPart(MimeBodyPart part, int index)
/*     */   {
/* 625 */     if (this.parts == null) {
/* 626 */       this.parts = new FinalArrayList();
/*     */     }
/* 628 */     this.parts.add(index, part);
/* 629 */     part.setParent(this);
/*     */   }
/*     */ 
/*     */   MimeBodyPart getParent()
/*     */   {
/* 638 */     return this.parent;
/*     */   }
/*     */ 
/*     */   void setParent(MimeBodyPart parent)
/*     */   {
/* 651 */     this.parent = parent;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeMultipart
 * JD-Core Version:    0.6.2
 */