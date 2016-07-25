/*      */ package com.sun.xml.internal.messaging.saaj.soap;
/*      */ 
/*      */ import com.sun.xml.internal.messaging.saaj.SOAPExceptionImpl;
/*      */ import com.sun.xml.internal.messaging.saaj.packaging.mime.Header;
/*      */ import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;
/*      */ import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.BMMimeMultipart;
/*      */ import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.ContentType;
/*      */ import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeBodyPart;
/*      */ import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeMultipart;
/*      */ import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimePullMultipart;
/*      */ import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.ParameterList;
/*      */ import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.ParseException;
/*      */ import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.SharedInputStream;
/*      */ import com.sun.xml.internal.messaging.saaj.packaging.mime.util.ASCIIUtility;
/*      */ import com.sun.xml.internal.messaging.saaj.soap.impl.EnvelopeImpl;
/*      */ import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
/*      */ import com.sun.xml.internal.messaging.saaj.util.FastInfosetReflection;
/*      */ import com.sun.xml.internal.messaging.saaj.util.FinalArrayList;
/*      */ import com.sun.xml.internal.messaging.saaj.util.SAAJUtil;
/*      */ import com.sun.xml.internal.org.jvnet.mimepull.MIMEPart;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import javax.activation.DataHandler;
/*      */ import javax.activation.DataSource;
/*      */ import javax.xml.soap.AttachmentPart;
/*      */ import javax.xml.soap.MimeHeaders;
/*      */ import javax.xml.soap.Node;
/*      */ import javax.xml.soap.SOAPBody;
/*      */ import javax.xml.soap.SOAPConstants;
/*      */ import javax.xml.soap.SOAPElement;
/*      */ import javax.xml.soap.SOAPEnvelope;
/*      */ import javax.xml.soap.SOAPException;
/*      */ import javax.xml.soap.SOAPHeader;
/*      */ import javax.xml.soap.SOAPMessage;
/*      */ import javax.xml.soap.SOAPPart;
/*      */ import javax.xml.transform.stream.StreamSource;
/*      */ 
/*      */ public abstract class MessageImpl extends SOAPMessage
/*      */   implements SOAPConstants
/*      */ {
/*      */   public static final String CONTENT_ID = "Content-ID";
/*      */   public static final String CONTENT_LOCATION = "Content-Location";
/*   67 */   protected static final Logger log = Logger.getLogger("com.sun.xml.internal.messaging.saaj.soap", "com.sun.xml.internal.messaging.saaj.soap.LocalStrings");
/*      */   protected static final int PLAIN_XML_FLAG = 1;
/*      */   protected static final int MIME_MULTIPART_FLAG = 2;
/*      */   protected static final int SOAP1_1_FLAG = 4;
/*      */   protected static final int SOAP1_2_FLAG = 8;
/*      */   protected static final int MIME_MULTIPART_XOP_SOAP1_1_FLAG = 6;
/*      */   protected static final int MIME_MULTIPART_XOP_SOAP1_2_FLAG = 10;
/*      */   protected static final int XOP_FLAG = 13;
/*      */   protected static final int FI_ENCODED_FLAG = 16;
/*      */   protected MimeHeaders headers;
/*      */   protected ContentType contentType;
/*      */   protected SOAPPartImpl soapPartImpl;
/*      */   protected FinalArrayList attachments;
/*   85 */   protected boolean saved = false;
/*      */   protected byte[] messageBytes;
/*      */   protected int messageByteCount;
/*   88 */   protected HashMap properties = new HashMap();
/*      */ 
/*   91 */   protected MimeMultipart multiPart = null;
/*   92 */   protected boolean attachmentsInitialized = false;
/*      */ 
/*   98 */   protected boolean isFastInfoset = false;
/*      */ 
/*  104 */   protected boolean acceptFastInfoset = false;
/*      */ 
/*  106 */   protected MimeMultipart mmp = null;
/*      */ 
/*  109 */   private boolean optimizeAttachmentProcessing = true;
/*      */ 
/*  111 */   private InputStream inputStreamAfterSaveChanges = null;
/*      */ 
/*  114 */   private static boolean switchOffBM = false;
/*  115 */   private static boolean switchOffLazyAttachment = false;
/*      */ 
/*  127 */   private static boolean useMimePull = SAAJUtil.getSystemBoolean("saaj.use.mimepull");
/*      */ 
/*  132 */   private boolean lazyAttachments = false;
/*      */ 
/*  815 */   private static final Iterator nullIter = Collections.EMPTY_LIST.iterator();
/*      */ 
/*      */   private static boolean isSoap1_1Type(String primary, String sub)
/*      */   {
/*  153 */     return ((primary.equalsIgnoreCase("text")) && (sub.equalsIgnoreCase("xml"))) || ((primary.equalsIgnoreCase("text")) && (sub.equalsIgnoreCase("xml-soap"))) || ((primary.equals("application")) && (sub.equals("fastinfoset")));
/*      */   }
/*      */ 
/*      */   private static boolean isEqualToSoap1_1Type(String type)
/*      */   {
/*  164 */     return (type.startsWith("text/xml")) || (type.startsWith("application/fastinfoset"));
/*      */   }
/*      */ 
/*      */   private static boolean isSoap1_2Type(String primary, String sub)
/*      */   {
/*  175 */     return (primary.equals("application")) && ((sub.equals("soap+xml")) || (sub.equals("soap+fastinfoset")));
/*      */   }
/*      */ 
/*      */   private static boolean isEqualToSoap1_2Type(String type)
/*      */   {
/*  185 */     return (type.startsWith("application/soap+xml")) || (type.startsWith("application/soap+fastinfoset"));
/*      */   }
/*      */ 
/*      */   protected MessageImpl()
/*      */   {
/*  194 */     this(false, false);
/*  195 */     this.attachmentsInitialized = true;
/*      */   }
/*      */ 
/*      */   protected MessageImpl(boolean isFastInfoset, boolean acceptFastInfoset)
/*      */   {
/*  203 */     this.isFastInfoset = isFastInfoset;
/*  204 */     this.acceptFastInfoset = acceptFastInfoset;
/*      */ 
/*  206 */     this.headers = new MimeHeaders();
/*  207 */     this.headers.setHeader("Accept", getExpectedAcceptHeader());
/*  208 */     this.contentType = new ContentType();
/*      */   }
/*      */ 
/*      */   protected MessageImpl(SOAPMessage msg)
/*      */   {
/*  215 */     if (!(msg instanceof MessageImpl));
/*  218 */     MessageImpl src = (MessageImpl)msg;
/*  219 */     this.headers = src.headers;
/*  220 */     this.soapPartImpl = src.soapPartImpl;
/*  221 */     this.attachments = src.attachments;
/*  222 */     this.saved = src.saved;
/*  223 */     this.messageBytes = src.messageBytes;
/*  224 */     this.messageByteCount = src.messageByteCount;
/*  225 */     this.properties = src.properties;
/*  226 */     this.contentType = src.contentType;
/*      */   }
/*      */ 
/*      */   protected static boolean isSoap1_1Content(int stat)
/*      */   {
/*  234 */     return (stat & 0x4) != 0;
/*      */   }
/*      */ 
/*      */   protected static boolean isSoap1_2Content(int stat)
/*      */   {
/*  242 */     return (stat & 0x8) != 0;
/*      */   }
/*      */ 
/*      */   private static boolean isMimeMultipartXOPSoap1_2Package(ContentType contentType) {
/*  246 */     String type = contentType.getParameter("type");
/*  247 */     if (type == null) {
/*  248 */       return false;
/*      */     }
/*  250 */     type = type.toLowerCase();
/*  251 */     if (!type.startsWith("application/xop+xml")) {
/*  252 */       return false;
/*      */     }
/*  254 */     String startinfo = contentType.getParameter("start-info");
/*  255 */     if (startinfo == null) {
/*  256 */       return false;
/*      */     }
/*  258 */     startinfo = startinfo.toLowerCase();
/*  259 */     return isEqualToSoap1_2Type(startinfo);
/*      */   }
/*      */ 
/*      */   private static boolean isMimeMultipartXOPSoap1_1Package(ContentType contentType)
/*      */   {
/*  265 */     String type = contentType.getParameter("type");
/*  266 */     if (type == null) {
/*  267 */       return false;
/*      */     }
/*  269 */     type = type.toLowerCase();
/*  270 */     if (!type.startsWith("application/xop+xml")) {
/*  271 */       return false;
/*      */     }
/*  273 */     String startinfo = contentType.getParameter("start-info");
/*  274 */     if (startinfo == null)
/*  275 */       return false;
/*  276 */     startinfo = startinfo.toLowerCase();
/*  277 */     return isEqualToSoap1_1Type(startinfo);
/*      */   }
/*      */ 
/*      */   private static boolean isSOAPBodyXOPPackage(ContentType contentType) {
/*  281 */     String primary = contentType.getPrimaryType();
/*  282 */     String sub = contentType.getSubType();
/*      */ 
/*  284 */     if ((primary.equalsIgnoreCase("application")) && 
/*  285 */       (sub.equalsIgnoreCase("xop+xml"))) {
/*  286 */       String type = getTypeParameter(contentType);
/*  287 */       return (isEqualToSoap1_2Type(type)) || (isEqualToSoap1_1Type(type));
/*      */     }
/*      */ 
/*  290 */     return false;
/*      */   }
/*      */ 
/*      */   protected MessageImpl(MimeHeaders headers, InputStream in)
/*      */     throws SOAPExceptionImpl
/*      */   {
/*  300 */     this.contentType = parseContentType(headers);
/*  301 */     init(headers, identifyContentType(this.contentType), this.contentType, in);
/*      */   }
/*      */ 
/*      */   private static ContentType parseContentType(MimeHeaders headers)
/*      */     throws SOAPExceptionImpl
/*      */   {
/*      */     String ct;
/*  306 */     if (headers != null) {
/*  307 */       ct = getContentType(headers);
/*      */     } else {
/*  309 */       log.severe("SAAJ0550.soap.null.headers");
/*  310 */       throw new SOAPExceptionImpl("Cannot create message: Headers can't be null");
/*      */     }
/*      */     String ct;
/*  314 */     if (ct == null) {
/*  315 */       log.severe("SAAJ0532.soap.no.Content-Type");
/*  316 */       throw new SOAPExceptionImpl("Absent Content-Type");
/*      */     }
/*      */     try {
/*  319 */       return new ContentType(ct);
/*      */     } catch (Throwable ex) {
/*  321 */       log.severe("SAAJ0535.soap.cannot.internalize.message");
/*  322 */       throw new SOAPExceptionImpl("Unable to internalize message", ex);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected MessageImpl(MimeHeaders headers, ContentType contentType, int stat, InputStream in)
/*      */     throws SOAPExceptionImpl
/*      */   {
/*  340 */     init(headers, stat, contentType, in);
/*      */   }
/*      */ 
/*      */   private void init(MimeHeaders headers, int stat, final ContentType contentType, final InputStream in) throws SOAPExceptionImpl
/*      */   {
/*  345 */     this.headers = headers;
/*      */     try
/*      */     {
/*  350 */       if ((stat & 0x10) > 0) {
/*  351 */         this.isFastInfoset = (this.acceptFastInfoset = 1);
/*      */       }
/*      */ 
/*  355 */       if (!this.isFastInfoset) {
/*  356 */         String[] values = headers.getHeader("Accept");
/*  357 */         if (values != null) {
/*  358 */           for (int i = 0; i < values.length; i++) {
/*  359 */             StringTokenizer st = new StringTokenizer(values[i], ",");
/*  360 */             while (st.hasMoreTokens()) {
/*  361 */               String token = st.nextToken().trim();
/*  362 */               if ((token.equalsIgnoreCase("application/fastinfoset")) || (token.equalsIgnoreCase("application/soap+fastinfoset")))
/*      */               {
/*  364 */                 this.acceptFastInfoset = true;
/*  365 */                 break;
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*  372 */       if (!isCorrectSoapVersion(stat)) {
/*  373 */         log.log(Level.SEVERE, "SAAJ0533.soap.incorrect.Content-Type", new String[] { contentType.toString(), getExpectedContentType() });
/*      */ 
/*  379 */         throw new SOAPVersionMismatchException("Cannot create message: incorrect content-type for SOAP version. Got: " + contentType + " Expected: " + getExpectedContentType());
/*      */       }
/*      */ 
/*  386 */       if ((stat & 0x1) != 0) {
/*  387 */         if (this.isFastInfoset) {
/*  388 */           getSOAPPart().setContent(FastInfosetReflection.FastInfosetSource_new(in));
/*      */         }
/*      */         else {
/*  391 */           initCharsetProperty(contentType);
/*  392 */           getSOAPPart().setContent(new StreamSource(in));
/*      */         }
/*      */       }
/*  395 */       else if ((stat & 0x2) != 0) {
/*  396 */         DataSource ds = new DataSource() {
/*      */           public InputStream getInputStream() {
/*  398 */             return in;
/*      */           }
/*      */ 
/*      */           public OutputStream getOutputStream() {
/*  402 */             return null;
/*      */           }
/*      */ 
/*      */           public String getContentType() {
/*  406 */             return contentType.toString();
/*      */           }
/*      */ 
/*      */           public String getName() {
/*  410 */             return "";
/*      */           }
/*      */         };
/*  414 */         this.multiPart = null;
/*  415 */         if (useMimePull)
/*  416 */           this.multiPart = new MimePullMultipart(ds, contentType);
/*  417 */         else if (switchOffBM)
/*  418 */           this.multiPart = new MimeMultipart(ds, contentType);
/*      */         else {
/*  420 */           this.multiPart = new BMMimeMultipart(ds, contentType);
/*      */         }
/*      */ 
/*  423 */         String startParam = contentType.getParameter("start");
/*  424 */         MimeBodyPart soapMessagePart = null;
/*  425 */         InputStream soapPartInputStream = null;
/*  426 */         String contentID = null;
/*  427 */         String contentIDNoAngle = null;
/*  428 */         if ((switchOffBM) || (switchOffLazyAttachment)) {
/*  429 */           if (startParam == null) {
/*  430 */             soapMessagePart = this.multiPart.getBodyPart(0);
/*  431 */             for (int i = 1; i < this.multiPart.getCount(); i++)
/*  432 */               initializeAttachment(this.multiPart, i);
/*      */           }
/*      */           else {
/*  435 */             soapMessagePart = this.multiPart.getBodyPart(startParam);
/*  436 */             for (int i = 0; i < this.multiPart.getCount(); i++) {
/*  437 */               contentID = this.multiPart.getBodyPart(i).getContentID();
/*      */ 
/*  440 */               contentIDNoAngle = contentID != null ? contentID.replaceFirst("^<", "").replaceFirst(">$", "") : null;
/*      */ 
/*  442 */               if ((!startParam.equals(contentID)) && (!startParam.equals(contentIDNoAngle)))
/*  443 */                 initializeAttachment(this.multiPart, i);
/*      */             }
/*      */           }
/*      */         }
/*  447 */         else if (useMimePull) {
/*  448 */           MimePullMultipart mpMultipart = (MimePullMultipart)this.multiPart;
/*  449 */           MIMEPart sp = mpMultipart.readAndReturnSOAPPart();
/*  450 */           soapMessagePart = new MimeBodyPart(sp);
/*  451 */           soapPartInputStream = sp.readOnce();
/*      */         } else {
/*  453 */           BMMimeMultipart bmMultipart = (BMMimeMultipart)this.multiPart;
/*      */ 
/*  455 */           InputStream stream = bmMultipart.initStream();
/*      */ 
/*  457 */           SharedInputStream sin = null;
/*  458 */           if ((stream instanceof SharedInputStream)) {
/*  459 */             sin = (SharedInputStream)stream;
/*      */           }
/*      */ 
/*  462 */           String boundary = "--" + contentType.getParameter("boundary");
/*      */ 
/*  464 */           byte[] bndbytes = ASCIIUtility.getBytes(boundary);
/*  465 */           if (startParam == null) {
/*  466 */             soapMessagePart = bmMultipart.getNextPart(stream, bndbytes, sin);
/*      */ 
/*  468 */             bmMultipart.removeBodyPart(soapMessagePart);
/*      */           } else {
/*  470 */             MimeBodyPart bp = null;
/*      */             try {
/*  472 */               while ((!startParam.equals(contentID)) && (!startParam.equals(contentIDNoAngle))) {
/*  473 */                 bp = bmMultipart.getNextPart(stream, bndbytes, sin);
/*      */ 
/*  475 */                 contentID = bp.getContentID();
/*      */ 
/*  478 */                 contentIDNoAngle = contentID != null ? contentID.replaceFirst("^<", "").replaceFirst(">$", "") : null;
/*      */               }
/*      */ 
/*  481 */               soapMessagePart = bp;
/*  482 */               bmMultipart.removeBodyPart(bp);
/*      */             } catch (Exception e) {
/*  484 */               throw new SOAPExceptionImpl(e);
/*      */             }
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  490 */         if ((soapPartInputStream == null) && (soapMessagePart != null)) {
/*  491 */           soapPartInputStream = soapMessagePart.getInputStream();
/*      */         }
/*      */ 
/*  494 */         ContentType soapPartCType = new ContentType(soapMessagePart.getContentType());
/*      */ 
/*  496 */         initCharsetProperty(soapPartCType);
/*  497 */         String baseType = soapPartCType.getBaseType().toLowerCase();
/*  498 */         if ((!isEqualToSoap1_1Type(baseType)) && (!isEqualToSoap1_2Type(baseType)) && (!isSOAPBodyXOPPackage(soapPartCType)))
/*      */         {
/*  501 */           log.log(Level.SEVERE, "SAAJ0549.soap.part.invalid.Content-Type", new Object[] { baseType });
/*      */ 
/*  504 */           throw new SOAPExceptionImpl("Bad Content-Type for SOAP Part : " + baseType);
/*      */         }
/*      */ 
/*  509 */         SOAPPart soapPart = getSOAPPart();
/*  510 */         setMimeHeaders(soapPart, soapMessagePart);
/*  511 */         soapPart.setContent(this.isFastInfoset ? FastInfosetReflection.FastInfosetSource_new(soapPartInputStream) : new StreamSource(soapPartInputStream));
/*      */       }
/*      */       else
/*      */       {
/*  516 */         log.severe("SAAJ0534.soap.unknown.Content-Type");
/*  517 */         throw new SOAPExceptionImpl("Unrecognized Content-Type");
/*      */       }
/*      */     } catch (Throwable ex) {
/*  520 */       log.severe("SAAJ0535.soap.cannot.internalize.message");
/*  521 */       throw new SOAPExceptionImpl("Unable to internalize message", ex);
/*      */     }
/*  523 */     needsSave();
/*      */   }
/*      */ 
/*      */   public boolean isFastInfoset() {
/*  527 */     return this.isFastInfoset;
/*      */   }
/*      */ 
/*      */   public boolean acceptFastInfoset() {
/*  531 */     return this.acceptFastInfoset;
/*      */   }
/*      */ 
/*      */   public void setIsFastInfoset(boolean value) {
/*  535 */     if (value != this.isFastInfoset) {
/*  536 */       this.isFastInfoset = value;
/*  537 */       if (this.isFastInfoset) {
/*  538 */         this.acceptFastInfoset = true;
/*      */       }
/*  540 */       this.saved = false;
/*      */     }
/*      */   }
/*      */ 
/*      */   public Object getProperty(String property) {
/*  545 */     return (String)this.properties.get(property);
/*      */   }
/*      */ 
/*      */   public void setProperty(String property, Object value) {
/*  549 */     verify(property, value);
/*  550 */     this.properties.put(property, value);
/*      */   }
/*      */ 
/*      */   private void verify(String property, Object value) {
/*  554 */     if (property.equalsIgnoreCase("javax.xml.soap.write-xml-declaration")) {
/*  555 */       if ((!"true".equals(value)) && (!"false".equals(value))) {
/*  556 */         throw new RuntimeException(property + " must have value false or true");
/*      */       }
/*      */       try
/*      */       {
/*  560 */         EnvelopeImpl env = (EnvelopeImpl)getSOAPPart().getEnvelope();
/*  561 */         if ("true".equalsIgnoreCase((String)value))
/*  562 */           env.setOmitXmlDecl("no");
/*  563 */         else if ("false".equalsIgnoreCase((String)value))
/*  564 */           env.setOmitXmlDecl("yes");
/*      */       }
/*      */       catch (Exception e) {
/*  567 */         log.log(Level.SEVERE, "SAAJ0591.soap.exception.in.set.property", new Object[] { e.getMessage(), "javax.xml.soap.write-xml-declaration" });
/*      */ 
/*  569 */         throw new RuntimeException(e);
/*      */       }
/*  571 */       return;
/*      */     }
/*      */ 
/*  574 */     if (property.equalsIgnoreCase("javax.xml.soap.character-set-encoding"))
/*      */       try {
/*  576 */         ((EnvelopeImpl)getSOAPPart().getEnvelope()).setCharsetEncoding((String)value);
/*      */       } catch (Exception e) {
/*  578 */         log.log(Level.SEVERE, "SAAJ0591.soap.exception.in.set.property", new Object[] { e.getMessage(), "javax.xml.soap.character-set-encoding" });
/*      */ 
/*  580 */         throw new RuntimeException(e);
/*      */       }
/*      */   }
/*      */ 
/*      */   protected abstract boolean isCorrectSoapVersion(int paramInt);
/*      */ 
/*      */   protected abstract String getExpectedContentType();
/*      */ 
/*      */   protected abstract String getExpectedAcceptHeader();
/*      */ 
/*      */   static int identifyContentType(ContentType ct)
/*      */     throws SOAPExceptionImpl
/*      */   {
/*  608 */     String primary = ct.getPrimaryType().toLowerCase();
/*  609 */     String sub = ct.getSubType().toLowerCase();
/*      */ 
/*  611 */     if (primary.equals("multipart")) {
/*  612 */       if (sub.equals("related")) {
/*  613 */         String type = getTypeParameter(ct);
/*  614 */         if (isEqualToSoap1_1Type(type)) {
/*  615 */           return (type.equals("application/fastinfoset") ? 16 : 0) | 0x2 | 0x4;
/*      */         }
/*      */ 
/*  618 */         if (isEqualToSoap1_2Type(type)) {
/*  619 */           return (type.equals("application/soap+fastinfoset") ? 16 : 0) | 0x2 | 0x8;
/*      */         }
/*      */ 
/*  623 */         if (isMimeMultipartXOPSoap1_1Package(ct))
/*  624 */           return 6;
/*  625 */         if (isMimeMultipartXOPSoap1_2Package(ct)) {
/*  626 */           return 10;
/*      */         }
/*  628 */         log.severe("SAAJ0536.soap.content-type.mustbe.multipart");
/*  629 */         throw new SOAPExceptionImpl("Content-Type needs to be Multipart/Related and with \"type=text/xml\" or \"type=application/soap+xml\"");
/*      */       }
/*      */ 
/*  635 */       log.severe("SAAJ0537.soap.invalid.content-type");
/*  636 */       throw new SOAPExceptionImpl("Invalid Content-Type: " + primary + '/' + sub);
/*      */     }
/*      */ 
/*  640 */     if (isSoap1_1Type(primary, sub)) {
/*  641 */       return ((primary.equalsIgnoreCase("application")) && (sub.equalsIgnoreCase("fastinfoset")) ? 16 : 0) | 0x1 | 0x4;
/*      */     }
/*      */ 
/*  646 */     if (isSoap1_2Type(primary, sub)) {
/*  647 */       return ((primary.equalsIgnoreCase("application")) && (sub.equalsIgnoreCase("soap+fastinfoset")) ? 16 : 0) | 0x1 | 0x8;
/*      */     }
/*      */ 
/*  651 */     if (isSOAPBodyXOPPackage(ct)) {
/*  652 */       return 13;
/*      */     }
/*  654 */     log.severe("SAAJ0537.soap.invalid.content-type");
/*  655 */     throw new SOAPExceptionImpl("Invalid Content-Type:" + primary + '/' + sub + ". Is this an error message instead of a SOAP response?");
/*      */   }
/*      */ 
/*      */   private static String getTypeParameter(ContentType contentType)
/*      */   {
/*  668 */     String p = contentType.getParameter("type");
/*  669 */     if (p != null) {
/*  670 */       return p.toLowerCase();
/*      */     }
/*  672 */     return "text/xml";
/*      */   }
/*      */ 
/*      */   public MimeHeaders getMimeHeaders() {
/*  676 */     return this.headers;
/*      */   }
/*      */ 
/*      */   static final String getContentType(MimeHeaders headers) {
/*  680 */     String[] values = headers.getHeader("Content-Type");
/*  681 */     if (values == null) {
/*  682 */       return null;
/*      */     }
/*  684 */     return values[0];
/*      */   }
/*      */ 
/*      */   public String getContentType()
/*      */   {
/*  691 */     return getContentType(this.headers);
/*      */   }
/*      */ 
/*      */   public void setContentType(String type) {
/*  695 */     this.headers.setHeader("Content-Type", type);
/*  696 */     needsSave();
/*      */   }
/*      */ 
/*      */   private ContentType contentType() {
/*  700 */     ContentType ct = null;
/*      */     try {
/*  702 */       String currentContent = getContentType();
/*  703 */       if (currentContent == null) {
/*  704 */         return this.contentType;
/*      */       }
/*  706 */       ct = new ContentType(currentContent);
/*      */     }
/*      */     catch (Exception e) {
/*      */     }
/*  710 */     return ct;
/*      */   }
/*      */ 
/*      */   public String getBaseType()
/*      */   {
/*  717 */     return contentType().getBaseType();
/*      */   }
/*      */ 
/*      */   public void setBaseType(String type) {
/*  721 */     ContentType ct = contentType();
/*  722 */     ct.setParameter("type", type);
/*  723 */     this.headers.setHeader("Content-Type", ct.toString());
/*  724 */     needsSave();
/*      */   }
/*      */ 
/*      */   public String getAction() {
/*  728 */     return contentType().getParameter("action");
/*      */   }
/*      */ 
/*      */   public void setAction(String action) {
/*  732 */     ContentType ct = contentType();
/*  733 */     ct.setParameter("action", action);
/*  734 */     this.headers.setHeader("Content-Type", ct.toString());
/*  735 */     needsSave();
/*      */   }
/*      */ 
/*      */   public String getCharset() {
/*  739 */     return contentType().getParameter("charset");
/*      */   }
/*      */ 
/*      */   public void setCharset(String charset) {
/*  743 */     ContentType ct = contentType();
/*  744 */     ct.setParameter("charset", charset);
/*  745 */     this.headers.setHeader("Content-Type", ct.toString());
/*  746 */     needsSave();
/*      */   }
/*      */ 
/*      */   private final void needsSave()
/*      */   {
/*  755 */     this.saved = false;
/*      */   }
/*      */ 
/*      */   public boolean saveRequired() {
/*  759 */     return this.saved != true;
/*      */   }
/*      */ 
/*      */   public String getContentDescription() {
/*  763 */     String[] values = this.headers.getHeader("Content-Description");
/*  764 */     if ((values != null) && (values.length > 0))
/*  765 */       return values[0];
/*  766 */     return null;
/*      */   }
/*      */ 
/*      */   public void setContentDescription(String description) {
/*  770 */     this.headers.setHeader("Content-Description", description);
/*  771 */     needsSave();
/*      */   }
/*      */ 
/*      */   public abstract SOAPPart getSOAPPart();
/*      */ 
/*      */   public void removeAllAttachments() {
/*      */     try {
/*  778 */       initializeAllAttachments();
/*      */     } catch (Exception e) {
/*  780 */       throw new RuntimeException(e);
/*      */     }
/*      */ 
/*  783 */     if (this.attachments != null) {
/*  784 */       this.attachments.clear();
/*  785 */       needsSave();
/*      */     }
/*      */   }
/*      */ 
/*      */   public int countAttachments() {
/*      */     try {
/*  791 */       initializeAllAttachments();
/*      */     } catch (Exception e) {
/*  793 */       throw new RuntimeException(e);
/*      */     }
/*  795 */     if (this.attachments != null)
/*  796 */       return this.attachments.size();
/*  797 */     return 0;
/*      */   }
/*      */ 
/*      */   public void addAttachmentPart(AttachmentPart attachment) {
/*      */     try {
/*  802 */       initializeAllAttachments();
/*  803 */       this.optimizeAttachmentProcessing = true;
/*      */     } catch (Exception e) {
/*  805 */       throw new RuntimeException(e);
/*      */     }
/*  807 */     if (this.attachments == null) {
/*  808 */       this.attachments = new FinalArrayList();
/*      */     }
/*  810 */     this.attachments.add(attachment);
/*      */ 
/*  812 */     needsSave();
/*      */   }
/*      */ 
/*      */   public Iterator getAttachments()
/*      */   {
/*      */     try
/*      */     {
/*  819 */       initializeAllAttachments();
/*      */     } catch (Exception e) {
/*  821 */       throw new RuntimeException(e);
/*      */     }
/*  823 */     if (this.attachments == null)
/*  824 */       return nullIter;
/*  825 */     return this.attachments.iterator();
/*      */   }
/*      */ 
/*      */   private void setFinalContentType(String charset) {
/*  829 */     ContentType ct = contentType();
/*  830 */     if (ct == null) {
/*  831 */       ct = new ContentType();
/*      */     }
/*  833 */     String[] split = getExpectedContentType().split("/");
/*  834 */     ct.setPrimaryType(split[0]);
/*  835 */     ct.setSubType(split[1]);
/*  836 */     ct.setParameter("charset", charset);
/*  837 */     this.headers.setHeader("Content-Type", ct.toString());
/*      */   }
/*      */ 
/*      */   public Iterator getAttachments(MimeHeaders headers)
/*      */   {
/*      */     try
/*      */     {
/*  885 */       initializeAllAttachments();
/*      */     } catch (Exception e) {
/*  887 */       throw new RuntimeException(e);
/*      */     }
/*  889 */     if (this.attachments == null) {
/*  890 */       return nullIter;
/*      */     }
/*  892 */     return new MimeMatchingIterator(headers);
/*      */   }
/*      */ 
/*      */   public void removeAttachments(MimeHeaders headers) {
/*      */     try {
/*  897 */       initializeAllAttachments();
/*      */     } catch (Exception e) {
/*  899 */       throw new RuntimeException(e);
/*      */     }
/*  901 */     if (this.attachments == null) {
/*  902 */       return;
/*      */     }
/*  904 */     Iterator it = new MimeMatchingIterator(headers);
/*  905 */     while (it.hasNext()) {
/*  906 */       int index = this.attachments.indexOf(it.next());
/*  907 */       this.attachments.set(index, null);
/*      */     }
/*  909 */     FinalArrayList f = new FinalArrayList();
/*  910 */     for (int i = 0; i < this.attachments.size(); i++) {
/*  911 */       if (this.attachments.get(i) != null) {
/*  912 */         f.add(this.attachments.get(i));
/*      */       }
/*      */     }
/*  915 */     this.attachments = f;
/*      */   }
/*      */ 
/*      */   public AttachmentPart createAttachmentPart()
/*      */   {
/*  920 */     return new AttachmentPartImpl();
/*      */   }
/*      */ 
/*      */   public AttachmentPart getAttachment(SOAPElement element) throws SOAPException
/*      */   {
/*      */     try {
/*  926 */       initializeAllAttachments();
/*      */     } catch (Exception e) {
/*  928 */       throw new RuntimeException(e);
/*      */     }
/*      */ 
/*  931 */     String hrefAttr = element.getAttribute("href");
/*      */     String uri;
/*      */     String uri;
/*  932 */     if ("".equals(hrefAttr)) {
/*  933 */       Node node = getValueNodeStrict(element);
/*  934 */       String swaRef = null;
/*  935 */       if (node != null) {
/*  936 */         swaRef = node.getValue();
/*      */       }
/*  938 */       if ((swaRef == null) || ("".equals(swaRef))) {
/*  939 */         return null;
/*      */       }
/*  941 */       uri = swaRef;
/*      */     }
/*      */     else {
/*  944 */       uri = hrefAttr;
/*      */     }
/*  946 */     return getAttachmentPart(uri);
/*      */   }
/*      */ 
/*      */   private Node getValueNodeStrict(SOAPElement element) {
/*  950 */     Node node = (Node)element.getFirstChild();
/*  951 */     if (node != null) {
/*  952 */       if ((node.getNextSibling() == null) && (node.getNodeType() == 3))
/*      */       {
/*  954 */         return node;
/*      */       }
/*  956 */       return null;
/*      */     }
/*      */ 
/*  959 */     return null;
/*      */   }
/*      */ 
/*      */   private AttachmentPart getAttachmentPart(String uri)
/*      */     throws SOAPException
/*      */   {
/*      */     AttachmentPart _part;
/*      */     try
/*      */     {
/*      */       AttachmentPart _part;
/*  966 */       if (uri.startsWith("cid:"))
/*      */       {
/*  968 */         uri = '<' + uri.substring("cid:".length()) + '>';
/*      */ 
/*  970 */         MimeHeaders headersToMatch = new MimeHeaders();
/*  971 */         headersToMatch.addHeader("Content-ID", uri);
/*      */ 
/*  973 */         Iterator i = getAttachments(headersToMatch);
/*  974 */         _part = i == null ? null : (AttachmentPart)i.next();
/*      */       }
/*      */       else {
/*  977 */         MimeHeaders headersToMatch = new MimeHeaders();
/*  978 */         headersToMatch.addHeader("Content-Location", uri);
/*      */ 
/*  980 */         Iterator i = getAttachments(headersToMatch);
/*  981 */         _part = i == null ? null : (AttachmentPart)i.next();
/*      */       }
/*      */ 
/*  985 */       if (_part == null) {
/*  986 */         Iterator j = getAttachments();
/*      */ 
/*  988 */         while (j.hasNext()) {
/*  989 */           AttachmentPart p = (AttachmentPart)j.next();
/*  990 */           String cl = p.getContentId();
/*  991 */           if (cl != null)
/*      */           {
/*  993 */             int eqIndex = cl.indexOf("=");
/*  994 */             if (eqIndex > -1) {
/*  995 */               cl = cl.substring(1, eqIndex);
/*  996 */               if (cl.equalsIgnoreCase(uri)) {
/*  997 */                 _part = p;
/*  998 */                 break;
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (Exception se) {
/* 1006 */       log.log(Level.SEVERE, "SAAJ0590.soap.unable.to.locate.attachment", new Object[] { uri });
/* 1007 */       throw new SOAPExceptionImpl(se);
/*      */     }
/* 1009 */     return _part;
/*      */   }
/*      */ 
/*      */   private final InputStream getHeaderBytes() throws IOException
/*      */   {
/* 1014 */     SOAPPartImpl sp = (SOAPPartImpl)getSOAPPart();
/* 1015 */     return sp.getContentAsStream();
/*      */   }
/*      */ 
/*      */   private String convertToSingleLine(String contentType) {
/* 1019 */     StringBuffer buffer = new StringBuffer();
/* 1020 */     for (int i = 0; i < contentType.length(); i++) {
/* 1021 */       char c = contentType.charAt(i);
/* 1022 */       if ((c != '\r') && (c != '\n') && (c != '\t'))
/* 1023 */         buffer.append(c);
/*      */     }
/* 1025 */     return buffer.toString();
/*      */   }
/*      */ 
/*      */   private MimeMultipart getMimeMessage() throws SOAPException {
/*      */     try {
/* 1030 */       SOAPPartImpl soapPart = (SOAPPartImpl)getSOAPPart();
/* 1031 */       MimeBodyPart mimeSoapPart = soapPart.getMimePart();
/*      */ 
/* 1037 */       ContentType soapPartCtype = new ContentType(getExpectedContentType());
/*      */ 
/* 1039 */       if (!this.isFastInfoset) {
/* 1040 */         soapPartCtype.setParameter("charset", initCharset());
/*      */       }
/* 1042 */       mimeSoapPart.setHeader("Content-Type", soapPartCtype.toString());
/*      */ 
/* 1044 */       MimeMultipart headerAndBody = null;
/*      */ 
/* 1046 */       if ((!switchOffBM) && (!switchOffLazyAttachment) && (this.multiPart != null) && (!this.attachmentsInitialized))
/*      */       {
/* 1048 */         headerAndBody = new BMMimeMultipart();
/* 1049 */         headerAndBody.addBodyPart(mimeSoapPart);
/* 1050 */         if (this.attachments != null) {
/* 1051 */           Iterator eachAttachment = this.attachments.iterator();
/* 1052 */           while (eachAttachment.hasNext()) {
/* 1053 */             headerAndBody.addBodyPart(((AttachmentPartImpl)eachAttachment.next()).getMimePart());
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1058 */         InputStream in = ((BMMimeMultipart)this.multiPart).getInputStream();
/* 1059 */         if ((!((BMMimeMultipart)this.multiPart).lastBodyPartFound()) && (!((BMMimeMultipart)this.multiPart).isEndOfStream()))
/*      */         {
/* 1061 */           ((BMMimeMultipart)headerAndBody).setInputStream(in);
/* 1062 */           ((BMMimeMultipart)headerAndBody).setBoundary(((BMMimeMultipart)this.multiPart).getBoundary());
/*      */ 
/* 1064 */           ((BMMimeMultipart)headerAndBody).setLazyAttachments(this.lazyAttachments);
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1069 */         headerAndBody = new MimeMultipart();
/* 1070 */         headerAndBody.addBodyPart(mimeSoapPart);
/*      */ 
/* 1072 */         Iterator eachAttachement = getAttachments();
/* 1073 */         while (eachAttachement.hasNext())
/*      */         {
/* 1075 */           headerAndBody.addBodyPart(((AttachmentPartImpl)eachAttachement.next()).getMimePart());
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1081 */       ContentType contentType = headerAndBody.getContentType();
/*      */ 
/* 1083 */       ParameterList l = contentType.getParameterList();
/*      */ 
/* 1086 */       l.set("type", getExpectedContentType());
/* 1087 */       l.set("boundary", contentType.getParameter("boundary"));
/* 1088 */       ContentType nct = new ContentType("multipart", "related", l);
/*      */ 
/* 1090 */       this.headers.setHeader("Content-Type", convertToSingleLine(nct.toString()));
/*      */ 
/* 1096 */       return headerAndBody;
/*      */     } catch (SOAPException ex) {
/* 1098 */       throw ex;
/*      */     } catch (Throwable ex) {
/* 1100 */       log.severe("SAAJ0538.soap.cannot.convert.msg.to.multipart.obj");
/* 1101 */       throw new SOAPExceptionImpl("Unable to convert SOAP message into a MimeMultipart object", ex);
/*      */     }
/*      */   }
/*      */ 
/*      */   private String initCharset()
/*      */   {
/* 1110 */     String charset = null;
/*      */ 
/* 1112 */     String[] cts = getMimeHeaders().getHeader("Content-Type");
/* 1113 */     if ((cts != null) && (cts[0] != null)) {
/* 1114 */       charset = getCharsetString(cts[0]);
/*      */     }
/*      */ 
/* 1117 */     if (charset == null) {
/* 1118 */       charset = (String)getProperty("javax.xml.soap.character-set-encoding");
/*      */     }
/*      */ 
/* 1121 */     if (charset != null) {
/* 1122 */       return charset;
/*      */     }
/*      */ 
/* 1125 */     return "utf-8";
/*      */   }
/*      */ 
/*      */   private String getCharsetString(String s) {
/*      */     try {
/* 1130 */       int index = s.indexOf(";");
/* 1131 */       if (index < 0)
/* 1132 */         return null;
/* 1133 */       ParameterList pl = new ParameterList(s.substring(index));
/* 1134 */       return pl.get("charset"); } catch (Exception e) {
/*      */     }
/* 1136 */     return null;
/*      */   }
/*      */ 
/*      */   public void saveChanges()
/*      */     throws SOAPException
/*      */   {
/* 1145 */     String charset = initCharset();
/*      */ 
/* 1148 */     int attachmentCount = this.attachments == null ? 0 : this.attachments.size();
/* 1149 */     if ((attachmentCount == 0) && 
/* 1150 */       (!switchOffBM) && (!switchOffLazyAttachment) && (!this.attachmentsInitialized) && (this.multiPart != null))
/*      */     {
/* 1153 */       attachmentCount = 1;
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/* 1158 */       if ((attachmentCount == 0) && (!hasXOPContent()))
/*      */       {
/*      */         InputStream in;
/*      */         try
/*      */         {
/* 1166 */           in = getHeaderBytes();
/*      */ 
/* 1168 */           this.optimizeAttachmentProcessing = false;
/* 1169 */           if (SOAPPartImpl.lazyContentLength)
/* 1170 */             this.inputStreamAfterSaveChanges = in;
/*      */         }
/*      */         catch (IOException ex) {
/* 1173 */           log.severe("SAAJ0539.soap.cannot.get.header.stream");
/* 1174 */           throw new SOAPExceptionImpl("Unable to get header stream in saveChanges: ", ex);
/*      */         }
/*      */ 
/* 1179 */         if ((in instanceof ByteInputStream)) {
/* 1180 */           ByteInputStream bIn = (ByteInputStream)in;
/* 1181 */           this.messageBytes = bIn.getBytes();
/* 1182 */           this.messageByteCount = bIn.getCount();
/*      */         }
/*      */ 
/* 1185 */         setFinalContentType(charset);
/*      */ 
/* 1191 */         if (this.messageByteCount > 0) {
/* 1192 */           this.headers.setHeader("Content-Length", Integer.toString(this.messageByteCount));
/*      */         }
/*      */ 
/*      */       }
/* 1197 */       else if (hasXOPContent()) {
/* 1198 */         this.mmp = getXOPMessage();
/*      */       } else {
/* 1200 */         this.mmp = getMimeMessage();
/*      */       }
/*      */     } catch (Throwable ex) {
/* 1203 */       log.severe("SAAJ0540.soap.err.saving.multipart.msg");
/* 1204 */       throw new SOAPExceptionImpl("Error during saving a multipart message", ex);
/*      */     }
/*      */ 
/* 1221 */     this.saved = true;
/*      */   }
/*      */ 
/*      */   private MimeMultipart getXOPMessage() throws SOAPException {
/*      */     try {
/* 1226 */       MimeMultipart headerAndBody = new MimeMultipart();
/* 1227 */       SOAPPartImpl soapPart = (SOAPPartImpl)getSOAPPart();
/* 1228 */       MimeBodyPart mimeSoapPart = soapPart.getMimePart();
/* 1229 */       ContentType soapPartCtype = new ContentType("application/xop+xml");
/*      */ 
/* 1231 */       soapPartCtype.setParameter("type", getExpectedContentType());
/* 1232 */       String charset = initCharset();
/* 1233 */       soapPartCtype.setParameter("charset", charset);
/* 1234 */       mimeSoapPart.setHeader("Content-Type", soapPartCtype.toString());
/* 1235 */       headerAndBody.addBodyPart(mimeSoapPart);
/*      */ 
/* 1237 */       Iterator eachAttachement = getAttachments();
/* 1238 */       while (eachAttachement.hasNext())
/*      */       {
/* 1240 */         headerAndBody.addBodyPart(((AttachmentPartImpl)eachAttachement.next()).getMimePart());
/*      */       }
/*      */ 
/* 1245 */       ContentType contentType = headerAndBody.getContentType();
/*      */ 
/* 1247 */       ParameterList l = contentType.getParameterList();
/*      */ 
/* 1250 */       l.set("start-info", getExpectedContentType());
/*      */ 
/* 1253 */       l.set("type", "application/xop+xml");
/*      */ 
/* 1255 */       if (isCorrectSoapVersion(8)) {
/* 1256 */         String action = getAction();
/* 1257 */         if (action != null) {
/* 1258 */           l.set("action", action);
/*      */         }
/*      */       }
/* 1261 */       l.set("boundary", contentType.getParameter("boundary"));
/* 1262 */       ContentType nct = new ContentType("Multipart", "Related", l);
/* 1263 */       this.headers.setHeader("Content-Type", convertToSingleLine(nct.toString()));
/*      */ 
/* 1269 */       return headerAndBody;
/*      */     } catch (SOAPException ex) {
/* 1271 */       throw ex;
/*      */     } catch (Throwable ex) {
/* 1273 */       log.severe("SAAJ0538.soap.cannot.convert.msg.to.multipart.obj");
/* 1274 */       throw new SOAPExceptionImpl("Unable to convert SOAP message into a MimeMultipart object", ex);
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean hasXOPContent()
/*      */     throws ParseException
/*      */   {
/* 1283 */     String type = getContentType();
/* 1284 */     if (type == null)
/* 1285 */       return false;
/* 1286 */     ContentType ct = new ContentType(type);
/*      */ 
/* 1288 */     return (isMimeMultipartXOPSoap1_1Package(ct)) || (isMimeMultipartXOPSoap1_2Package(ct)) || (isSOAPBodyXOPPackage(ct));
/*      */   }
/*      */ 
/*      */   public void writeTo(OutputStream out)
/*      */     throws SOAPException, IOException
/*      */   {
/* 1294 */     if (saveRequired()) {
/* 1295 */       this.optimizeAttachmentProcessing = true;
/* 1296 */       saveChanges();
/*      */     }
/*      */ 
/* 1299 */     if (!this.optimizeAttachmentProcessing) {
/* 1300 */       if ((SOAPPartImpl.lazyContentLength) && (this.messageByteCount <= 0)) {
/* 1301 */         byte[] buf = new byte[1024];
/*      */ 
/* 1303 */         int length = 0;
/* 1304 */         while ((length = this.inputStreamAfterSaveChanges.read(buf)) != -1) {
/* 1305 */           out.write(buf, 0, length);
/* 1306 */           this.messageByteCount += length;
/*      */         }
/* 1308 */         if (this.messageByteCount > 0) {
/* 1309 */           this.headers.setHeader("Content-Length", Integer.toString(this.messageByteCount));
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1314 */         out.write(this.messageBytes, 0, this.messageByteCount);
/*      */       }
/*      */     }
/*      */     else {
/*      */       try {
/* 1319 */         if (hasXOPContent()) {
/* 1320 */           this.mmp.writeTo(out);
/*      */         } else {
/* 1322 */           this.mmp.writeTo(out);
/* 1323 */           if ((!switchOffBM) && (!switchOffLazyAttachment) && (this.multiPart != null) && (!this.attachmentsInitialized))
/*      */           {
/* 1325 */             ((BMMimeMultipart)this.multiPart).setInputStream(((BMMimeMultipart)this.mmp).getInputStream());
/*      */           }
/*      */         }
/*      */       }
/*      */       catch (Exception ex) {
/* 1330 */         log.severe("SAAJ0540.soap.err.saving.multipart.msg");
/* 1331 */         throw new SOAPExceptionImpl("Error during saving a multipart message", ex);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1337 */     if (isCorrectSoapVersion(4))
/*      */     {
/* 1339 */       String[] soapAction = this.headers.getHeader("SOAPAction");
/*      */ 
/* 1341 */       if ((soapAction == null) || (soapAction.length == 0)) {
/* 1342 */         this.headers.setHeader("SOAPAction", "\"\"");
/*      */       }
/*      */     }
/*      */ 
/* 1346 */     this.messageBytes = null;
/* 1347 */     needsSave();
/*      */   }
/*      */ 
/*      */   public SOAPBody getSOAPBody() throws SOAPException {
/* 1351 */     SOAPBody body = getSOAPPart().getEnvelope().getBody();
/*      */ 
/* 1355 */     return body;
/*      */   }
/*      */ 
/*      */   public SOAPHeader getSOAPHeader() throws SOAPException {
/* 1359 */     SOAPHeader hdr = getSOAPPart().getEnvelope().getHeader();
/*      */ 
/* 1363 */     return hdr;
/*      */   }
/*      */ 
/*      */   private void initializeAllAttachments() throws MessagingException, SOAPException
/*      */   {
/* 1368 */     if ((switchOffBM) || (switchOffLazyAttachment)) {
/* 1369 */       return;
/*      */     }
/*      */ 
/* 1372 */     if ((this.attachmentsInitialized) || (this.multiPart == null)) {
/* 1373 */       return;
/*      */     }
/*      */ 
/* 1376 */     if (this.attachments == null) {
/* 1377 */       this.attachments = new FinalArrayList();
/*      */     }
/* 1379 */     int count = this.multiPart.getCount();
/* 1380 */     for (int i = 0; i < count; i++) {
/* 1381 */       initializeAttachment(this.multiPart.getBodyPart(i));
/*      */     }
/* 1383 */     this.attachmentsInitialized = true;
/*      */ 
/* 1385 */     needsSave();
/*      */   }
/*      */ 
/*      */   private void initializeAttachment(MimeBodyPart mbp) throws SOAPException {
/* 1389 */     AttachmentPartImpl attachmentPart = new AttachmentPartImpl();
/* 1390 */     DataHandler attachmentHandler = mbp.getDataHandler();
/* 1391 */     attachmentPart.setDataHandler(attachmentHandler);
/*      */ 
/* 1393 */     AttachmentPartImpl.copyMimeHeaders(mbp, attachmentPart);
/* 1394 */     this.attachments.add(attachmentPart);
/*      */   }
/*      */ 
/*      */   private void initializeAttachment(MimeMultipart multiPart, int i) throws Exception
/*      */   {
/* 1399 */     MimeBodyPart currentBodyPart = multiPart.getBodyPart(i);
/* 1400 */     AttachmentPartImpl attachmentPart = new AttachmentPartImpl();
/*      */ 
/* 1402 */     DataHandler attachmentHandler = currentBodyPart.getDataHandler();
/* 1403 */     attachmentPart.setDataHandler(attachmentHandler);
/*      */ 
/* 1405 */     AttachmentPartImpl.copyMimeHeaders(currentBodyPart, attachmentPart);
/* 1406 */     addAttachmentPart(attachmentPart);
/*      */   }
/*      */ 
/*      */   private void setMimeHeaders(SOAPPart soapPart, MimeBodyPart soapMessagePart)
/*      */     throws Exception
/*      */   {
/* 1413 */     soapPart.removeAllMimeHeaders();
/*      */ 
/* 1415 */     List headers = soapMessagePart.getAllHeaders();
/* 1416 */     int sz = headers.size();
/* 1417 */     for (int i = 0; i < sz; i++) {
/* 1418 */       Header h = (Header)headers.get(i);
/* 1419 */       soapPart.addMimeHeader(h.getName(), h.getValue());
/*      */     }
/*      */   }
/*      */ 
/*      */   private void initCharsetProperty(ContentType contentType) {
/* 1424 */     String charset = contentType.getParameter("charset");
/* 1425 */     if (charset != null) {
/* 1426 */       ((SOAPPartImpl)getSOAPPart()).setSourceCharsetEncoding(charset);
/* 1427 */       if (!charset.equalsIgnoreCase("utf-8"))
/* 1428 */         setProperty("javax.xml.soap.character-set-encoding", charset);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setLazyAttachments(boolean flag) {
/* 1433 */     this.lazyAttachments = flag;
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  119 */     String s = SAAJUtil.getSystemProperty("saaj.mime.optimization");
/*  120 */     if ((s != null) && (s.equals("false"))) {
/*  121 */       switchOffBM = true;
/*      */     }
/*  123 */     s = SAAJUtil.getSystemProperty("saaj.lazy.mime.optimization");
/*  124 */     if ((s != null) && (s.equals("false")))
/*  125 */       switchOffLazyAttachment = true;
/*      */   }
/*      */ 
/*      */   private class MimeMatchingIterator
/*      */     implements Iterator
/*      */   {
/*      */     private Iterator iter;
/*      */     private MimeHeaders headers;
/*      */     private Object nextAttachment;
/*      */ 
/*      */     public MimeMatchingIterator(MimeHeaders headers)
/*      */     {
/*  842 */       this.headers = headers;
/*  843 */       this.iter = MessageImpl.this.attachments.iterator();
/*      */     }
/*      */ 
/*      */     public boolean hasNext()
/*      */     {
/*  851 */       if (this.nextAttachment == null)
/*  852 */         this.nextAttachment = nextMatch();
/*  853 */       return this.nextAttachment != null;
/*      */     }
/*      */ 
/*      */     public Object next() {
/*  857 */       if (this.nextAttachment != null) {
/*  858 */         Object ret = this.nextAttachment;
/*  859 */         this.nextAttachment = null;
/*  860 */         return ret;
/*      */       }
/*      */ 
/*  863 */       if (hasNext()) {
/*  864 */         return this.nextAttachment;
/*      */       }
/*  866 */       return null;
/*      */     }
/*      */ 
/*      */     Object nextMatch() {
/*  870 */       while (this.iter.hasNext()) {
/*  871 */         AttachmentPartImpl ap = (AttachmentPartImpl)this.iter.next();
/*  872 */         if (ap.hasAllHeaders(this.headers))
/*  873 */           return ap;
/*      */       }
/*  875 */       return null;
/*      */     }
/*      */ 
/*      */     public void remove() {
/*  879 */       this.iter.remove();
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.soap.MessageImpl
 * JD-Core Version:    0.6.2
 */