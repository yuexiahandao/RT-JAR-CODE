/*     */ package com.sun.xml.internal.messaging.saaj.soap;
/*     */ 
/*     */ import com.sun.xml.internal.messaging.saaj.SOAPExceptionImpl;
/*     */ import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.InternetHeaders;
/*     */ import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeBodyPart;
/*     */ import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimePartDataSource;
/*     */ import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeUtility;
/*     */ import com.sun.xml.internal.messaging.saaj.packaging.mime.util.ASCIIUtility;
/*     */ import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
/*     */ import com.sun.xml.internal.org.jvnet.mimepull.MIMEPart;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.activation.CommandInfo;
/*     */ import javax.activation.CommandMap;
/*     */ import javax.activation.DataHandler;
/*     */ import javax.activation.DataSource;
/*     */ import javax.activation.MailcapCommandMap;
/*     */ import javax.xml.soap.AttachmentPart;
/*     */ import javax.xml.soap.MimeHeader;
/*     */ import javax.xml.soap.MimeHeaders;
/*     */ import javax.xml.soap.SOAPException;
/*     */ 
/*     */ public class AttachmentPartImpl extends AttachmentPart
/*     */ {
/*  61 */   protected static final Logger log = Logger.getLogger("com.sun.xml.internal.messaging.saaj.soap", "com.sun.xml.internal.messaging.saaj.soap.LocalStrings");
/*     */   private final MimeHeaders headers;
/*  66 */   private MimeBodyPart rawContent = null;
/*  67 */   private DataHandler dataHandler = null;
/*     */ 
/*  70 */   private MIMEPart mimePart = null;
/*     */ 
/*     */   public AttachmentPartImpl() {
/*  73 */     this.headers = new MimeHeaders();
/*     */ 
/*  79 */     initializeJavaActivationHandlers();
/*     */   }
/*     */ 
/*     */   public AttachmentPartImpl(MIMEPart part) {
/*  83 */     this.headers = new MimeHeaders();
/*  84 */     this.mimePart = part;
/*  85 */     List hdrs = part.getAllHeaders();
/*  86 */     for (com.sun.xml.internal.org.jvnet.mimepull.Header hd : hdrs)
/*  87 */       this.headers.addHeader(hd.getName(), hd.getValue());
/*     */   }
/*     */ 
/*     */   public int getSize()
/*     */     throws SOAPException
/*     */   {
/*  93 */     if (this.mimePart != null) {
/*     */       try {
/*  95 */         return this.mimePart.read().available();
/*     */       } catch (IOException e) {
/*  97 */         return -1;
/*     */       }
/*     */     }
/* 100 */     if ((this.rawContent == null) && (this.dataHandler == null)) {
/* 101 */       return 0;
/*     */     }
/* 103 */     if (this.rawContent != null) {
/*     */       try {
/* 105 */         return this.rawContent.getSize();
/*     */       } catch (Exception ex) {
/* 107 */         log.log(Level.SEVERE, "SAAJ0573.soap.attachment.getrawbytes.ioexception", new String[] { ex.getLocalizedMessage() });
/*     */ 
/* 111 */         throw new SOAPExceptionImpl("Raw InputStream Error: " + ex);
/*     */       }
/*     */     }
/* 114 */     ByteOutputStream bout = new ByteOutputStream();
/*     */     try {
/* 116 */       this.dataHandler.writeTo(bout);
/*     */     } catch (IOException ex) {
/* 118 */       log.log(Level.SEVERE, "SAAJ0501.soap.data.handler.err", new String[] { ex.getLocalizedMessage() });
/*     */ 
/* 122 */       throw new SOAPExceptionImpl("Data handler error: " + ex);
/*     */     }
/* 124 */     return bout.size();
/*     */   }
/*     */ 
/*     */   public void clearContent()
/*     */   {
/* 129 */     if (this.mimePart != null) {
/* 130 */       this.mimePart.close();
/* 131 */       this.mimePart = null;
/*     */     }
/* 133 */     this.dataHandler = null;
/* 134 */     this.rawContent = null;
/*     */   }
/*     */ 
/*     */   public Object getContent() throws SOAPException {
/*     */     try {
/* 139 */       if (this.mimePart != null)
/*     */       {
/* 141 */         return this.mimePart.read();
/*     */       }
/* 143 */       if (this.dataHandler != null)
/* 144 */         return getDataHandler().getContent();
/* 145 */       if (this.rawContent != null) {
/* 146 */         return this.rawContent.getContent();
/*     */       }
/* 148 */       log.severe("SAAJ0572.soap.no.content.for.attachment");
/* 149 */       throw new SOAPExceptionImpl("No data handler/content associated with this attachment");
/*     */     }
/*     */     catch (Exception ex) {
/* 152 */       log.log(Level.SEVERE, "SAAJ0575.soap.attachment.getcontent.exception", ex);
/* 153 */       throw new SOAPExceptionImpl(ex.getLocalizedMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setContent(Object object, String contentType) throws IllegalArgumentException
/*     */   {
/* 159 */     if (this.mimePart != null) {
/* 160 */       this.mimePart.close();
/* 161 */       this.mimePart = null;
/*     */     }
/* 163 */     DataHandler dh = new DataHandler(object, contentType);
/*     */ 
/* 165 */     setDataHandler(dh);
/*     */   }
/*     */ 
/*     */   public DataHandler getDataHandler() throws SOAPException
/*     */   {
/* 170 */     if (this.mimePart != null)
/*     */     {
/* 172 */       return new DataHandler(new DataSource()
/*     */       {
/*     */         public InputStream getInputStream() throws IOException {
/* 175 */           return AttachmentPartImpl.this.mimePart.read();
/*     */         }
/*     */ 
/*     */         public OutputStream getOutputStream() throws IOException {
/* 179 */           throw new UnsupportedOperationException("getOutputStream cannot be supported : You have enabled LazyAttachments Option");
/*     */         }
/*     */ 
/*     */         public String getContentType() {
/* 183 */           return AttachmentPartImpl.this.mimePart.getContentType();
/*     */         }
/*     */ 
/*     */         public String getName() {
/* 187 */           return "MIMEPart Wrapper DataSource";
/*     */         }
/*     */       });
/*     */     }
/* 191 */     if (this.dataHandler == null) {
/* 192 */       if (this.rawContent != null) {
/* 193 */         return new DataHandler(new MimePartDataSource(this.rawContent));
/*     */       }
/* 195 */       log.severe("SAAJ0502.soap.no.handler.for.attachment");
/* 196 */       throw new SOAPExceptionImpl("No data handler associated with this attachment");
/*     */     }
/* 198 */     return this.dataHandler;
/*     */   }
/*     */ 
/*     */   public void setDataHandler(DataHandler dataHandler) throws IllegalArgumentException
/*     */   {
/* 203 */     if (this.mimePart != null) {
/* 204 */       this.mimePart.close();
/* 205 */       this.mimePart = null;
/*     */     }
/* 207 */     if (dataHandler == null) {
/* 208 */       log.severe("SAAJ0503.soap.no.null.to.dataHandler");
/* 209 */       throw new IllegalArgumentException("Null dataHandler argument to setDataHandler");
/*     */     }
/* 211 */     this.dataHandler = dataHandler;
/* 212 */     this.rawContent = null;
/*     */ 
/* 214 */     log.log(Level.FINE, "SAAJ0580.soap.set.Content-Type", new String[] { dataHandler.getContentType() });
/*     */ 
/* 218 */     setMimeHeader("Content-Type", dataHandler.getContentType());
/*     */   }
/*     */ 
/*     */   public void removeAllMimeHeaders() {
/* 222 */     this.headers.removeAllHeaders();
/*     */   }
/*     */ 
/*     */   public void removeMimeHeader(String header) {
/* 226 */     this.headers.removeHeader(header);
/*     */   }
/*     */ 
/*     */   public String[] getMimeHeader(String name) {
/* 230 */     return this.headers.getHeader(name);
/*     */   }
/*     */ 
/*     */   public void setMimeHeader(String name, String value) {
/* 234 */     this.headers.setHeader(name, value);
/*     */   }
/*     */ 
/*     */   public void addMimeHeader(String name, String value) {
/* 238 */     this.headers.addHeader(name, value);
/*     */   }
/*     */ 
/*     */   public Iterator getAllMimeHeaders() {
/* 242 */     return this.headers.getAllHeaders();
/*     */   }
/*     */ 
/*     */   public Iterator getMatchingMimeHeaders(String[] names) {
/* 246 */     return this.headers.getMatchingHeaders(names);
/*     */   }
/*     */ 
/*     */   public Iterator getNonMatchingMimeHeaders(String[] names) {
/* 250 */     return this.headers.getNonMatchingHeaders(names);
/*     */   }
/*     */ 
/*     */   boolean hasAllHeaders(MimeHeaders hdrs) {
/* 254 */     if (hdrs != null) {
/* 255 */       Iterator i = hdrs.getAllHeaders();
/* 256 */       while (i.hasNext()) {
/* 257 */         MimeHeader hdr = (MimeHeader)i.next();
/* 258 */         String[] values = this.headers.getHeader(hdr.getName());
/* 259 */         boolean found = false;
/*     */ 
/* 261 */         if (values != null) {
/* 262 */           for (int j = 0; j < values.length; j++) {
/* 263 */             if (hdr.getValue().equalsIgnoreCase(values[j])) {
/* 264 */               found = true;
/* 265 */               break;
/*     */             }
/*     */           }
/*     */         }
/* 269 */         if (!found) {
/* 270 */           return false;
/*     */         }
/*     */       }
/*     */     }
/* 274 */     return true;
/*     */   }
/*     */ 
/*     */   MimeBodyPart getMimePart() throws SOAPException {
/*     */     try {
/* 279 */       if (this.mimePart != null) {
/* 280 */         return new MimeBodyPart(this.mimePart);
/*     */       }
/* 282 */       if (this.rawContent != null) {
/* 283 */         copyMimeHeaders(this.headers, this.rawContent);
/* 284 */         return this.rawContent;
/*     */       }
/*     */ 
/* 287 */       MimeBodyPart envelope = new MimeBodyPart();
/*     */ 
/* 289 */       envelope.setDataHandler(this.dataHandler);
/* 290 */       copyMimeHeaders(this.headers, envelope);
/*     */ 
/* 292 */       return envelope;
/*     */     } catch (Exception ex) {
/* 294 */       log.severe("SAAJ0504.soap.cannot.externalize.attachment");
/* 295 */       throw new SOAPExceptionImpl("Unable to externalize attachment", ex);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void copyMimeHeaders(MimeHeaders headers, MimeBodyPart mbp)
/*     */     throws SOAPException
/*     */   {
/* 302 */     Iterator i = headers.getAllHeaders();
/*     */ 
/* 304 */     while (i.hasNext())
/*     */       try {
/* 306 */         MimeHeader mh = (MimeHeader)i.next();
/*     */ 
/* 308 */         mbp.setHeader(mh.getName(), mh.getValue());
/*     */       } catch (Exception ex) {
/* 310 */         log.severe("SAAJ0505.soap.cannot.copy.mime.hdr");
/* 311 */         throw new SOAPExceptionImpl("Unable to copy MIME header", ex);
/*     */       }
/*     */   }
/*     */ 
/*     */   public static void copyMimeHeaders(MimeBodyPart mbp, AttachmentPartImpl ap) throws SOAPException
/*     */   {
/*     */     try {
/* 318 */       List hdr = mbp.getAllHeaders();
/* 319 */       int sz = hdr.size();
/* 320 */       for (int i = 0; i < sz; i++) {
/* 321 */         com.sun.xml.internal.messaging.saaj.packaging.mime.Header h = (com.sun.xml.internal.messaging.saaj.packaging.mime.Header)hdr.get(i);
/* 322 */         if (!h.getName().equalsIgnoreCase("Content-Type"))
/*     */         {
/* 324 */           ap.addMimeHeader(h.getName(), h.getValue());
/*     */         }
/*     */       }
/*     */     } catch (Exception ex) { log.severe("SAAJ0506.soap.cannot.copy.mime.hdrs.into.attachment");
/* 328 */       throw new SOAPExceptionImpl("Unable to copy MIME headers into attachment", ex);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setBase64Content(InputStream content, String contentType)
/*     */     throws SOAPException
/*     */   {
/* 337 */     if (this.mimePart != null) {
/* 338 */       this.mimePart.close();
/* 339 */       this.mimePart = null;
/*     */     }
/* 341 */     this.dataHandler = null;
/* 342 */     InputStream decoded = null;
/*     */     try {
/* 344 */       decoded = MimeUtility.decode(content, "base64");
/* 345 */       InternetHeaders hdrs = new InternetHeaders();
/* 346 */       hdrs.setHeader("Content-Type", contentType);
/*     */ 
/* 350 */       ByteOutputStream bos = new ByteOutputStream();
/* 351 */       bos.write(decoded);
/* 352 */       this.rawContent = new MimeBodyPart(hdrs, bos.getBytes(), bos.getCount());
/* 353 */       setMimeHeader("Content-Type", contentType);
/*     */     } catch (Exception e) {
/* 355 */       log.log(Level.SEVERE, "SAAJ0578.soap.attachment.setbase64content.exception", e);
/* 356 */       throw new SOAPExceptionImpl(e.getLocalizedMessage());
/*     */     } finally {
/*     */       try {
/* 359 */         decoded.close();
/*     */       } catch (IOException ex) {
/* 361 */         throw new SOAPException(ex);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public InputStream getBase64Content()
/*     */     throws SOAPException
/*     */   {
/*     */     InputStream stream;
/*     */     InputStream stream;
/* 368 */     if (this.mimePart != null) {
/* 369 */       stream = this.mimePart.read();
/* 370 */     } else if (this.rawContent != null) {
/*     */       try {
/* 372 */         stream = this.rawContent.getInputStream();
/*     */       } catch (Exception e) {
/* 374 */         log.log(Level.SEVERE, "SAAJ0579.soap.attachment.getbase64content.exception", e);
/* 375 */         throw new SOAPExceptionImpl(e.getLocalizedMessage());
/*     */       }
/* 377 */     } else if (this.dataHandler != null) {
/*     */       try {
/* 379 */         stream = this.dataHandler.getInputStream();
/*     */       } catch (IOException e) {
/* 381 */         log.severe("SAAJ0574.soap.attachment.datahandler.ioexception");
/* 382 */         throw new SOAPExceptionImpl("DataHandler error" + e);
/*     */       }
/*     */     } else {
/* 385 */       log.severe("SAAJ0572.soap.no.content.for.attachment");
/* 386 */       throw new SOAPExceptionImpl("No data handler/content associated with this attachment");
/*     */     }
/*     */ 
/* 393 */     int size = 1024;
/*     */ 
/* 395 */     if (stream != null) {
/*     */       try {
/* 397 */         ByteArrayOutputStream bos = new ByteArrayOutputStream(size);
/*     */ 
/* 400 */         OutputStream ret = MimeUtility.encode(bos, "base64");
/* 401 */         byte[] buf = new byte[size];
/*     */         int len;
/* 402 */         while ((len = stream.read(buf, 0, size)) != -1) {
/* 403 */           ret.write(buf, 0, len);
/*     */         }
/* 405 */         ret.flush();
/* 406 */         buf = bos.toByteArray();
/* 407 */         return new ByteArrayInputStream(buf);
/*     */       }
/*     */       catch (Exception e) {
/* 410 */         log.log(Level.SEVERE, "SAAJ0579.soap.attachment.getbase64content.exception", e);
/* 411 */         throw new SOAPExceptionImpl(e.getLocalizedMessage());
/*     */       } finally {
/*     */         try {
/* 414 */           stream.close();
/*     */         }
/*     */         catch (IOException ex)
/*     */         {
/*     */         }
/*     */       }
/*     */     }
/* 421 */     log.log(Level.SEVERE, "SAAJ0572.soap.no.content.for.attachment");
/* 422 */     throw new SOAPExceptionImpl("No data handler/content associated with this attachment");
/*     */   }
/*     */ 
/*     */   public void setRawContent(InputStream content, String contentType)
/*     */     throws SOAPException
/*     */   {
/* 428 */     if (this.mimePart != null) {
/* 429 */       this.mimePart.close();
/* 430 */       this.mimePart = null;
/*     */     }
/* 432 */     this.dataHandler = null;
/*     */     try {
/* 434 */       InternetHeaders hdrs = new InternetHeaders();
/* 435 */       hdrs.setHeader("Content-Type", contentType);
/*     */ 
/* 439 */       ByteOutputStream bos = new ByteOutputStream();
/* 440 */       bos.write(content);
/* 441 */       this.rawContent = new MimeBodyPart(hdrs, bos.getBytes(), bos.getCount());
/* 442 */       setMimeHeader("Content-Type", contentType);
/*     */     } catch (Exception e) {
/* 444 */       log.log(Level.SEVERE, "SAAJ0576.soap.attachment.setrawcontent.exception", e);
/* 445 */       throw new SOAPExceptionImpl(e.getLocalizedMessage());
/*     */     } finally {
/*     */       try {
/* 448 */         content.close();
/*     */       } catch (IOException ex) {
/* 450 */         throw new SOAPException(ex);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setRawContentBytes(byte[] content, int off, int len, String contentType)
/*     */     throws SOAPException
/*     */   {
/* 476 */     if (this.mimePart != null) {
/* 477 */       this.mimePart.close();
/* 478 */       this.mimePart = null;
/*     */     }
/* 480 */     if (content == null) {
/* 481 */       throw new SOAPExceptionImpl("Null content passed to setRawContentBytes");
/*     */     }
/* 483 */     this.dataHandler = null;
/*     */     try {
/* 485 */       InternetHeaders hdrs = new InternetHeaders();
/* 486 */       hdrs.setHeader("Content-Type", contentType);
/* 487 */       this.rawContent = new MimeBodyPart(hdrs, content, off, len);
/* 488 */       setMimeHeader("Content-Type", contentType);
/*     */     } catch (Exception e) {
/* 490 */       log.log(Level.SEVERE, "SAAJ0576.soap.attachment.setrawcontent.exception", e);
/*     */ 
/* 492 */       throw new SOAPExceptionImpl(e.getLocalizedMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public InputStream getRawContent() throws SOAPException {
/* 497 */     if (this.mimePart != null) {
/* 498 */       return this.mimePart.read();
/*     */     }
/* 500 */     if (this.rawContent != null)
/*     */       try {
/* 502 */         return this.rawContent.getInputStream();
/*     */       } catch (Exception e) {
/* 504 */         log.log(Level.SEVERE, "SAAJ0577.soap.attachment.getrawcontent.exception", e);
/* 505 */         throw new SOAPExceptionImpl(e.getLocalizedMessage());
/*     */       }
/* 507 */     if (this.dataHandler != null) {
/*     */       try {
/* 509 */         return this.dataHandler.getInputStream();
/*     */       } catch (IOException e) {
/* 511 */         log.severe("SAAJ0574.soap.attachment.datahandler.ioexception");
/* 512 */         throw new SOAPExceptionImpl("DataHandler error" + e);
/*     */       }
/*     */     }
/* 515 */     log.severe("SAAJ0572.soap.no.content.for.attachment");
/* 516 */     throw new SOAPExceptionImpl("No data handler/content associated with this attachment");
/*     */   }
/*     */ 
/*     */   public byte[] getRawContentBytes()
/*     */     throws SOAPException
/*     */   {
/*     */     InputStream ret;
/* 522 */     if (this.mimePart != null) {
/*     */       try {
/* 524 */         ret = this.mimePart.read();
/* 525 */         return ASCIIUtility.getBytes(ret);
/*     */       } catch (IOException ex) {
/* 527 */         log.log(Level.SEVERE, "SAAJ0577.soap.attachment.getrawcontent.exception", ex);
/* 528 */         throw new SOAPExceptionImpl(ex);
/*     */       }
/*     */     }
/* 531 */     if (this.rawContent != null)
/*     */       try {
/* 533 */         ret = this.rawContent.getInputStream();
/* 534 */         return ASCIIUtility.getBytes(ret);
/*     */       } catch (Exception e) {
/* 536 */         log.log(Level.SEVERE, "SAAJ0577.soap.attachment.getrawcontent.exception", e);
/* 537 */         throw new SOAPExceptionImpl(e);
/*     */       }
/* 539 */     if (this.dataHandler != null) {
/*     */       try {
/* 541 */         ret = this.dataHandler.getInputStream();
/* 542 */         return ASCIIUtility.getBytes(ret);
/*     */       } catch (IOException e) {
/* 544 */         log.severe("SAAJ0574.soap.attachment.datahandler.ioexception");
/* 545 */         throw new SOAPExceptionImpl("DataHandler error" + e);
/*     */       }
/*     */     }
/* 548 */     log.severe("SAAJ0572.soap.no.content.for.attachment");
/* 549 */     throw new SOAPExceptionImpl("No data handler/content associated with this attachment");
/*     */   }
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 555 */     return this == o;
/*     */   }
/*     */ 
/*     */   public MimeHeaders getMimeHeaders() {
/* 559 */     return this.headers;
/*     */   }
/*     */ 
/*     */   public static void initializeJavaActivationHandlers()
/*     */   {
/*     */     try {
/* 565 */       CommandMap map = CommandMap.getDefaultCommandMap();
/* 566 */       if ((map instanceof MailcapCommandMap)) {
/* 567 */         MailcapCommandMap mailMap = (MailcapCommandMap)map;
/*     */ 
/* 570 */         if (!cmdMapInitialized(mailMap)) {
/* 571 */           mailMap.addMailcap("text/xml;;x-java-content-handler=com.sun.xml.internal.messaging.saaj.soap.XmlDataContentHandler");
/* 572 */           mailMap.addMailcap("application/xml;;x-java-content-handler=com.sun.xml.internal.messaging.saaj.soap.XmlDataContentHandler");
/* 573 */           mailMap.addMailcap("application/fastinfoset;;x-java-content-handler=com.sun.xml.internal.messaging.saaj.soap.FastInfosetDataContentHandler");
/*     */ 
/* 576 */           mailMap.addMailcap("image/*;;x-java-content-handler=com.sun.xml.internal.messaging.saaj.soap.ImageDataContentHandler");
/* 577 */           mailMap.addMailcap("text/plain;;x-java-content-handler=com.sun.xml.internal.messaging.saaj.soap.StringDataContentHandler");
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Throwable t)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   private static boolean cmdMapInitialized(MailcapCommandMap mailMap)
/*     */   {
/* 588 */     CommandInfo[] commands = mailMap.getAllCommands("application/fastinfoset");
/* 589 */     if ((commands == null) || (commands.length == 0)) {
/* 590 */       return false;
/*     */     }
/*     */ 
/* 593 */     String saajClassName = "com.sun.xml.internal.ws.binding.FastInfosetDataContentHandler";
/* 594 */     for (CommandInfo command : commands) {
/* 595 */       String commandClass = command.getCommandClass();
/* 596 */       if (saajClassName.equals(commandClass)) {
/* 597 */         return true;
/*     */       }
/*     */     }
/* 600 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.soap.AttachmentPartImpl
 * JD-Core Version:    0.6.2
 */