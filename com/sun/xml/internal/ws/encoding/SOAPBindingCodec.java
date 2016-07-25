/*     */ package com.sun.xml.internal.ws.encoding;
/*     */ 
/*     */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*     */ import com.sun.xml.internal.ws.api.WSBinding;
/*     */ import com.sun.xml.internal.ws.api.client.SelectOptimalEncodingFeature;
/*     */ import com.sun.xml.internal.ws.api.fastinfoset.FastInfosetFeature;
/*     */ import com.sun.xml.internal.ws.api.message.Attachment;
/*     */ import com.sun.xml.internal.ws.api.message.AttachmentSet;
/*     */ import com.sun.xml.internal.ws.api.message.ExceptionHasMessage;
/*     */ import com.sun.xml.internal.ws.api.message.Message;
/*     */ import com.sun.xml.internal.ws.api.message.Packet;
/*     */ import com.sun.xml.internal.ws.api.pipe.Codec;
/*     */ import com.sun.xml.internal.ws.api.pipe.Codecs;
/*     */ import com.sun.xml.internal.ws.api.pipe.ContentType;
/*     */ import com.sun.xml.internal.ws.api.pipe.StreamSOAPCodec;
/*     */ import com.sun.xml.internal.ws.binding.SOAPBindingImpl;
/*     */ import com.sun.xml.internal.ws.client.ContentNegotiation;
/*     */ import com.sun.xml.internal.ws.protocol.soap.MessageCreationException;
/*     */ import com.sun.xml.internal.ws.resources.StreamingMessages;
/*     */ import com.sun.xml.internal.ws.server.UnsupportedMediaException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.lang.reflect.Method;
/*     */ import java.nio.channels.ReadableByteChannel;
/*     */ import java.nio.channels.WritableByteChannel;
/*     */ import java.util.StringTokenizer;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ import javax.xml.ws.WebServiceFeature;
/*     */ import javax.xml.ws.soap.MTOMFeature;
/*     */ 
/*     */ public class SOAPBindingCodec extends MimeCodec
/*     */   implements com.sun.xml.internal.ws.api.pipe.SOAPBindingCodec
/*     */ {
/*     */   private boolean acceptMtomMessages;
/*     */   private boolean isRequestMtomMessage;
/*  95 */   private TriState decodeFirst = TriState.UNSET;
/*     */   private boolean isFastInfosetDisabled;
/*     */   private boolean useFastInfosetForEncoding;
/*     */   private boolean ignoreContentNegotiationProperty;
/*     */   private final StreamSOAPCodec xmlSoapCodec;
/*     */   private final Codec fiSoapCodec;
/*     */   private final MimeCodec xmlMtomCodec;
/*     */   private final MimeCodec xmlSwaCodec;
/*     */   private final MimeCodec fiSwaCodec;
/*     */   private final SOAPBindingImpl binding;
/*     */   private final String xmlMimeType;
/*     */   private final String fiMimeType;
/*     */   private final String xmlAccept;
/*     */   private final String connegXmlAccept;
/* 185 */   private AcceptContentType _adaptingContentType = new AcceptContentType(null);
/*     */ 
/*     */   public StreamSOAPCodec getXMLCodec()
/*     */   {
/* 155 */     return this.xmlSoapCodec;
/*     */   }
/*     */ 
/*     */   public SOAPBindingCodec(WSBinding binding)
/*     */   {
/* 188 */     this(binding, Codecs.createSOAPEnvelopeXmlCodec(binding.getSOAPVersion()));
/*     */   }
/*     */ 
/*     */   public SOAPBindingCodec(WSBinding binding, StreamSOAPCodec xmlSoapCodec) {
/* 192 */     super(binding.getSOAPVersion(), binding);
/*     */ 
/* 194 */     this.xmlSoapCodec = xmlSoapCodec;
/* 195 */     this.xmlMimeType = xmlSoapCodec.getMimeType();
/*     */ 
/* 197 */     this.xmlMtomCodec = new MtomCodec(this.version, xmlSoapCodec, binding, binding.getFeature(MTOMFeature.class));
/*     */ 
/* 199 */     this.xmlSwaCodec = new SwACodec(this.version, binding, xmlSoapCodec);
/*     */ 
/* 201 */     String clientAcceptedContentTypes = xmlSoapCodec.getMimeType() + ", " + this.xmlMtomCodec.getMimeType();
/*     */ 
/* 204 */     WebServiceFeature fi = binding.getFeature(FastInfosetFeature.class);
/* 205 */     this.isFastInfosetDisabled = ((fi != null) && (!fi.isEnabled()));
/* 206 */     if (!this.isFastInfosetDisabled) {
/* 207 */       this.fiSoapCodec = getFICodec(xmlSoapCodec, this.version);
/* 208 */       if (this.fiSoapCodec != null) {
/* 209 */         this.fiMimeType = this.fiSoapCodec.getMimeType();
/* 210 */         this.fiSwaCodec = new SwACodec(this.version, binding, this.fiSoapCodec);
/* 211 */         this.connegXmlAccept = (this.fiMimeType + ", " + clientAcceptedContentTypes);
/*     */ 
/* 219 */         WebServiceFeature select = binding.getFeature(SelectOptimalEncodingFeature.class);
/* 220 */         if (select != null) {
/* 221 */           this.ignoreContentNegotiationProperty = true;
/* 222 */           if (select.isEnabled())
/*     */           {
/* 224 */             if (fi != null) {
/* 225 */               this.useFastInfosetForEncoding = true;
/*     */             }
/*     */ 
/* 228 */             clientAcceptedContentTypes = this.connegXmlAccept;
/*     */           } else {
/* 230 */             this.isFastInfosetDisabled = true;
/*     */           }
/*     */         }
/*     */       }
/*     */       else {
/* 235 */         this.isFastInfosetDisabled = true;
/* 236 */         this.fiSwaCodec = null;
/* 237 */         this.fiMimeType = "";
/* 238 */         this.connegXmlAccept = clientAcceptedContentTypes;
/* 239 */         this.ignoreContentNegotiationProperty = true;
/*     */       }
/*     */     }
/*     */     else {
/* 243 */       this.fiSoapCodec = (this.fiSwaCodec = null);
/* 244 */       this.fiMimeType = "";
/* 245 */       this.connegXmlAccept = clientAcceptedContentTypes;
/* 246 */       this.ignoreContentNegotiationProperty = true;
/*     */     }
/*     */ 
/* 249 */     this.xmlAccept = clientAcceptedContentTypes;
/*     */ 
/* 251 */     if (!(binding instanceof SOAPBindingImpl))
/* 252 */       throw new WebServiceException("Expecting a SOAP binding but found " + binding);
/* 253 */     this.binding = ((SOAPBindingImpl)binding);
/*     */   }
/*     */ 
/*     */   public String getMimeType() {
/* 257 */     return null;
/*     */   }
/*     */ 
/*     */   public ContentType getStaticContentType(Packet packet) {
/* 261 */     ContentType toAdapt = getEncoder(packet).getStaticContentType(packet);
/* 262 */     return toAdapt != null ? this._adaptingContentType.set(packet, toAdapt) : null;
/*     */   }
/*     */ 
/*     */   public ContentType encode(Packet packet, OutputStream out) throws IOException {
/* 266 */     preEncode(packet);
/* 267 */     ContentType ct = this._adaptingContentType.set(packet, getEncoder(packet).encode(packet, out));
/* 268 */     postEncode();
/* 269 */     return ct;
/*     */   }
/*     */ 
/*     */   public ContentType encode(Packet packet, WritableByteChannel buffer) {
/* 273 */     preEncode(packet);
/* 274 */     ContentType ct = this._adaptingContentType.set(packet, getEncoder(packet).encode(packet, buffer));
/* 275 */     postEncode();
/* 276 */     return ct;
/*     */   }
/*     */ 
/*     */   private void preEncode(Packet p)
/*     */   {
/* 284 */     if (this.decodeFirst == TriState.UNSET)
/* 285 */       this.decodeFirst = TriState.FALSE;
/*     */   }
/*     */ 
/*     */   private void postEncode()
/*     */   {
/* 293 */     this.decodeFirst = TriState.UNSET;
/* 294 */     this.acceptMtomMessages = false;
/* 295 */     this.isRequestMtomMessage = false;
/*     */   }
/*     */ 
/*     */   private void preDecode(Packet p)
/*     */   {
/* 303 */     if (p.contentNegotiation == null)
/* 304 */       this.useFastInfosetForEncoding = false;
/*     */   }
/*     */ 
/*     */   private void postDecode(Packet p)
/*     */   {
/* 312 */     if (this.decodeFirst == TriState.UNSET)
/* 313 */       this.decodeFirst = TriState.TRUE;
/* 314 */     if (this.binding.isFeatureEnabled(MTOMFeature.class))
/* 315 */       this.acceptMtomMessages = isMtomAcceptable(p.acceptableMimeTypes);
/* 316 */     if (!this.useFastInfosetForEncoding)
/* 317 */       this.useFastInfosetForEncoding = isFastInfosetAcceptable(p.acceptableMimeTypes);
/*     */   }
/*     */ 
/*     */   private boolean isServerSide()
/*     */   {
/* 323 */     return this.decodeFirst == TriState.TRUE;
/*     */   }
/*     */ 
/*     */   public void decode(InputStream in, String contentType, Packet packet) throws IOException {
/* 327 */     if (contentType == null) {
/* 328 */       contentType = this.xmlMimeType;
/*     */     }
/*     */ 
/* 331 */     preDecode(packet);
/*     */     try {
/* 333 */       if (isMultipartRelated(contentType))
/*     */       {
/* 335 */         super.decode(in, contentType, packet);
/* 336 */       } else if (isFastInfoset(contentType)) {
/* 337 */         if ((!this.ignoreContentNegotiationProperty) && (packet.contentNegotiation == ContentNegotiation.none)) {
/* 338 */           throw noFastInfosetForDecoding();
/*     */         }
/* 340 */         this.useFastInfosetForEncoding = true;
/* 341 */         this.fiSoapCodec.decode(in, contentType, packet);
/*     */       } else {
/* 343 */         this.xmlSoapCodec.decode(in, contentType, packet);
/*     */       }
/*     */     } catch (RuntimeException we) { if (((we instanceof ExceptionHasMessage)) || ((we instanceof UnsupportedMediaException))) {
/* 346 */         throw we;
/*     */       }
/* 348 */       throw new MessageCreationException(this.binding.getSOAPVersion(), new Object[] { we });
/*     */     }
/*     */ 
/* 351 */     postDecode(packet);
/*     */   }
/*     */ 
/*     */   public void decode(ReadableByteChannel in, String contentType, Packet packet) {
/* 355 */     if (contentType == null) {
/* 356 */       throw new UnsupportedMediaException();
/*     */     }
/*     */ 
/* 359 */     preDecode(packet);
/*     */     try {
/* 361 */       if (isMultipartRelated(contentType)) {
/* 362 */         super.decode(in, contentType, packet);
/* 363 */       } else if (isFastInfoset(contentType)) {
/* 364 */         if (packet.contentNegotiation == ContentNegotiation.none) {
/* 365 */           throw noFastInfosetForDecoding();
/*     */         }
/* 367 */         this.useFastInfosetForEncoding = true;
/* 368 */         this.fiSoapCodec.decode(in, contentType, packet);
/*     */       } else {
/* 370 */         this.xmlSoapCodec.decode(in, contentType, packet);
/*     */       }
/*     */     } catch (RuntimeException we) { if (((we instanceof ExceptionHasMessage)) || ((we instanceof UnsupportedMediaException))) {
/* 373 */         throw we;
/*     */       }
/* 375 */       throw new MessageCreationException(this.binding.getSOAPVersion(), new Object[] { we });
/*     */     }
/*     */ 
/* 378 */     postDecode(packet);
/*     */   }
/*     */ 
/*     */   public SOAPBindingCodec copy() {
/* 382 */     return new SOAPBindingCodec(this.binding, (StreamSOAPCodec)this.xmlSoapCodec.copy());
/*     */   }
/*     */ 
/*     */   protected void decode(MimeMultipartParser mpp, Packet packet)
/*     */     throws IOException
/*     */   {
/* 388 */     String rootContentType = mpp.getRootPart().getContentType();
/*     */ 
/* 390 */     if (isApplicationXopXml(rootContentType)) {
/* 391 */       this.isRequestMtomMessage = true;
/* 392 */       this.xmlMtomCodec.decode(mpp, packet);
/* 393 */     } else if (isFastInfoset(rootContentType)) {
/* 394 */       if (packet.contentNegotiation == ContentNegotiation.none) {
/* 395 */         throw noFastInfosetForDecoding();
/*     */       }
/* 397 */       this.useFastInfosetForEncoding = true;
/* 398 */       this.fiSwaCodec.decode(mpp, packet);
/* 399 */     } else if (isXml(rootContentType)) {
/* 400 */       this.xmlSwaCodec.decode(mpp, packet);
/*     */     }
/*     */     else {
/* 403 */       throw new IOException("");
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean isMultipartRelated(String contentType)
/*     */   {
/* 409 */     return compareStrings(contentType, "multipart/related");
/*     */   }
/*     */ 
/*     */   private boolean isApplicationXopXml(String contentType) {
/* 413 */     return compareStrings(contentType, "application/xop+xml");
/*     */   }
/*     */ 
/*     */   private boolean isXml(String contentType) {
/* 417 */     return compareStrings(contentType, this.xmlMimeType);
/*     */   }
/*     */ 
/*     */   private boolean isFastInfoset(String contentType) {
/* 421 */     if (this.isFastInfosetDisabled) return false;
/*     */ 
/* 423 */     return compareStrings(contentType, this.fiMimeType);
/*     */   }
/*     */ 
/*     */   private boolean compareStrings(String a, String b) {
/* 427 */     return (a.length() >= b.length()) && (b.equalsIgnoreCase(a.substring(0, b.length())));
/*     */   }
/*     */ 
/*     */   private boolean isFastInfosetAcceptable(String accept)
/*     */   {
/* 434 */     if ((accept == null) || (this.isFastInfosetDisabled)) return false;
/*     */ 
/* 436 */     StringTokenizer st = new StringTokenizer(accept, ",");
/* 437 */     while (st.hasMoreTokens()) {
/* 438 */       String token = st.nextToken().trim();
/* 439 */       if (token.equalsIgnoreCase(this.fiMimeType)) {
/* 440 */         return true;
/*     */       }
/*     */     }
/* 443 */     return false;
/*     */   }
/*     */ 
/*     */   private boolean isMtomAcceptable(String accept)
/*     */   {
/* 451 */     if ((accept == null) || (this.isFastInfosetDisabled)) return false;
/*     */ 
/* 453 */     StringTokenizer st = new StringTokenizer(accept, ",");
/* 454 */     while (st.hasMoreTokens()) {
/* 455 */       String token = st.nextToken().trim();
/* 456 */       if (token.toLowerCase().contains("application/xop+xml")) {
/* 457 */         return true;
/*     */       }
/*     */     }
/* 460 */     return false;
/*     */   }
/*     */ 
/*     */   private Codec getEncoder(Packet p)
/*     */   {
/* 472 */     if (!this.ignoreContentNegotiationProperty) {
/* 473 */       if (p.contentNegotiation == ContentNegotiation.none)
/*     */       {
/* 476 */         this.useFastInfosetForEncoding = false;
/* 477 */       } else if (p.contentNegotiation == ContentNegotiation.optimistic)
/*     */       {
/* 479 */         this.useFastInfosetForEncoding = true;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 485 */     if (this.useFastInfosetForEncoding) {
/* 486 */       Message m = p.getMessage();
/* 487 */       if ((m == null) || (m.getAttachments().isEmpty()) || (this.binding.isFeatureEnabled(MTOMFeature.class))) {
/* 488 */         return this.fiSoapCodec;
/*     */       }
/* 490 */       return this.fiSwaCodec;
/*     */     }
/*     */ 
/* 493 */     if (this.binding.isFeatureEnabled(MTOMFeature.class))
/*     */     {
/* 496 */       if ((!isServerSide()) || (this.isRequestMtomMessage) || (this.acceptMtomMessages)) {
/* 497 */         return this.xmlMtomCodec;
/*     */       }
/*     */     }
/* 500 */     Message m = p.getMessage();
/* 501 */     if ((m == null) || (m.getAttachments().isEmpty())) {
/* 502 */       return this.xmlSoapCodec;
/*     */     }
/* 504 */     return this.xmlSwaCodec;
/*     */   }
/*     */ 
/*     */   private RuntimeException noFastInfosetForDecoding() {
/* 508 */     return new RuntimeException(StreamingMessages.FASTINFOSET_DECODING_NOT_ACCEPTED());
/*     */   }
/*     */ 
/*     */   private static Codec getFICodec(StreamSOAPCodec soapCodec, SOAPVersion version)
/*     */   {
/*     */     try
/*     */     {
/* 516 */       Class c = Class.forName("com.sun.xml.internal.ws.encoding.fastinfoset.FastInfosetStreamSOAPCodec");
/* 517 */       Method m = c.getMethod("create", new Class[] { StreamSOAPCodec.class, SOAPVersion.class });
/* 518 */       return (Codec)m.invoke(null, new Object[] { soapCodec, version });
/*     */     } catch (Exception e) {
/*     */     }
/* 521 */     return null;
/*     */   }
/*     */ 
/*     */   private class AcceptContentType
/*     */     implements ContentType
/*     */   {
/*     */     private ContentType _c;
/*     */     private String _accept;
/*     */ 
/*     */     private AcceptContentType()
/*     */     {
/*     */     }
/*     */ 
/*     */     public AcceptContentType set(Packet p, ContentType c)
/*     */     {
/* 163 */       if ((!SOAPBindingCodec.this.ignoreContentNegotiationProperty) && (p.contentNegotiation != ContentNegotiation.none))
/* 164 */         this._accept = SOAPBindingCodec.this.connegXmlAccept;
/*     */       else {
/* 166 */         this._accept = SOAPBindingCodec.this.xmlAccept;
/*     */       }
/* 168 */       this._c = c;
/* 169 */       return this;
/*     */     }
/*     */ 
/*     */     public String getContentType() {
/* 173 */       return this._c.getContentType();
/*     */     }
/*     */ 
/*     */     public String getSOAPActionHeader() {
/* 177 */       return this._c.getSOAPActionHeader();
/*     */     }
/*     */ 
/*     */     public String getAcceptHeader() {
/* 181 */       return this._accept;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static enum TriState
/*     */   {
/*  89 */     UNSET, TRUE, FALSE;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.encoding.SOAPBindingCodec
 * JD-Core Version:    0.6.2
 */