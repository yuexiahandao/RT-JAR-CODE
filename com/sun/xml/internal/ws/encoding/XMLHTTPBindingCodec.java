/*     */ package com.sun.xml.internal.ws.encoding;
/*     */ 
/*     */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*     */ import com.sun.xml.internal.ws.api.WSBinding;
/*     */ import com.sun.xml.internal.ws.api.message.Message;
/*     */ import com.sun.xml.internal.ws.api.message.Packet;
/*     */ import com.sun.xml.internal.ws.api.pipe.Codec;
/*     */ import com.sun.xml.internal.ws.api.pipe.ContentType;
/*     */ import com.sun.xml.internal.ws.client.ContentNegotiation;
/*     */ import com.sun.xml.internal.ws.encoding.xml.XMLCodec;
/*     */ import com.sun.xml.internal.ws.encoding.xml.XMLMessage;
/*     */ import com.sun.xml.internal.ws.encoding.xml.XMLMessage.MessageDataSource;
/*     */ import com.sun.xml.internal.ws.encoding.xml.XMLMessage.UnknownContent;
/*     */ import com.sun.xml.internal.ws.encoding.xml.XMLMessage.XMLMultiPart;
/*     */ import com.sun.xml.internal.ws.resources.StreamingMessages;
/*     */ import com.sun.xml.internal.ws.util.ByteArrayBuffer;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.lang.reflect.Method;
/*     */ import java.nio.channels.WritableByteChannel;
/*     */ import java.util.StringTokenizer;
/*     */ import javax.activation.DataSource;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ 
/*     */ public final class XMLHTTPBindingCodec extends MimeCodec
/*     */ {
/*     */   private static final String BASE_ACCEPT_VALUE = "*";
/*     */   private static final String APPLICATION_FAST_INFOSET_MIME_TYPE = "application/fastinfoset";
/*     */   private boolean useFastInfosetForEncoding;
/*     */   private final Codec xmlCodec;
/*     */   private final Codec fiCodec;
/*  91 */   private static final String xmlAccept = null;
/*     */   private static final String fiXmlAccept = "application/fastinfoset, *";
/* 127 */   private AcceptContentType _adaptingContentType = new AcceptContentType(null);
/*     */ 
/*     */   public XMLHTTPBindingCodec(WSBinding binding) {
/* 130 */     super(SOAPVersion.SOAP_11, binding);
/*     */ 
/* 132 */     this.xmlCodec = new XMLCodec(binding);
/*     */ 
/* 134 */     this.fiCodec = getFICodec();
/*     */   }
/*     */ 
/*     */   public String getMimeType() {
/* 138 */     return null;
/*     */   }
/*     */ 
/*     */   public ContentType getStaticContentType(Packet packet)
/*     */   {
/* 143 */     setRootCodec(packet);
/*     */ 
/* 145 */     ContentType ct = null;
/* 146 */     if ((packet.getMessage() instanceof XMLMessage.MessageDataSource)) {
/* 147 */       XMLMessage.MessageDataSource mds = (XMLMessage.MessageDataSource)packet.getMessage();
/* 148 */       if (mds.hasUnconsumedDataSource()) {
/* 149 */         ct = getStaticContentType(mds);
/* 150 */         return ct != null ? this._adaptingContentType.set(packet, ct) : null;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 156 */     ct = super.getStaticContentType(packet);
/* 157 */     return ct != null ? this._adaptingContentType.set(packet, ct) : null;
/*     */   }
/*     */ 
/*     */   public ContentType encode(Packet packet, OutputStream out)
/*     */     throws IOException
/*     */   {
/* 164 */     setRootCodec(packet);
/*     */ 
/* 166 */     if ((packet.getMessage() instanceof XMLMessage.MessageDataSource)) {
/* 167 */       XMLMessage.MessageDataSource mds = (XMLMessage.MessageDataSource)packet.getMessage();
/* 168 */       if (mds.hasUnconsumedDataSource()) {
/* 169 */         return this._adaptingContentType.set(packet, encode(mds, out));
/*     */       }
/*     */     }
/* 172 */     return this._adaptingContentType.set(packet, super.encode(packet, out));
/*     */   }
/*     */ 
/*     */   public ContentType encode(Packet packet, WritableByteChannel buffer) {
/* 176 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public void decode(InputStream in, String contentType, Packet packet)
/*     */     throws IOException
/*     */   {
/* 185 */     if (packet.contentNegotiation == null) {
/* 186 */       this.useFastInfosetForEncoding = false;
/*     */     }
/* 188 */     if (contentType == null) {
/* 189 */       this.xmlCodec.decode(in, contentType, packet);
/* 190 */     } else if (isMultipartRelated(contentType)) {
/* 191 */       packet.setMessage(new XMLMessage.XMLMultiPart(contentType, in, this.binding));
/* 192 */     } else if (isFastInfoset(contentType)) {
/* 193 */       if (this.fiCodec == null) {
/* 194 */         throw new RuntimeException(StreamingMessages.FASTINFOSET_NO_IMPLEMENTATION());
/*     */       }
/*     */ 
/* 197 */       this.useFastInfosetForEncoding = true;
/* 198 */       this.fiCodec.decode(in, contentType, packet);
/* 199 */     } else if (isXml(contentType)) {
/* 200 */       this.xmlCodec.decode(in, contentType, packet);
/*     */     } else {
/* 202 */       packet.setMessage(new XMLMessage.UnknownContent(contentType, in));
/*     */     }
/*     */ 
/* 205 */     if (!this.useFastInfosetForEncoding)
/* 206 */       this.useFastInfosetForEncoding = isFastInfosetAcceptable(packet.acceptableMimeTypes);
/*     */   }
/*     */ 
/*     */   protected void decode(MimeMultipartParser mpp, Packet packet) throws IOException
/*     */   {
/*     */   }
/*     */ 
/*     */   public MimeCodec copy()
/*     */   {
/* 215 */     return new XMLHTTPBindingCodec(this.binding);
/*     */   }
/*     */ 
/*     */   private boolean isMultipartRelated(String contentType) {
/* 219 */     return compareStrings(contentType, "multipart/related");
/*     */   }
/*     */ 
/*     */   private boolean isApplicationXopXml(String contentType) {
/* 223 */     return compareStrings(contentType, "application/xop+xml");
/*     */   }
/*     */ 
/*     */   private boolean isXml(String contentType) {
/* 227 */     return (compareStrings(contentType, "application/xml")) || (compareStrings(contentType, "text/xml")) || ((compareStrings(contentType, "application/")) && (contentType.toLowerCase().indexOf("+xml") != -1));
/*     */   }
/*     */ 
/*     */   private boolean isFastInfoset(String contentType)
/*     */   {
/* 233 */     return compareStrings(contentType, "application/fastinfoset");
/*     */   }
/*     */ 
/*     */   private boolean compareStrings(String a, String b) {
/* 237 */     return (a.length() >= b.length()) && (b.equalsIgnoreCase(a.substring(0, b.length())));
/*     */   }
/*     */ 
/*     */   private boolean isFastInfosetAcceptable(String accept)
/*     */   {
/* 244 */     if (accept == null) return false;
/*     */ 
/* 246 */     StringTokenizer st = new StringTokenizer(accept, ",");
/* 247 */     while (st.hasMoreTokens()) {
/* 248 */       String token = st.nextToken().trim();
/* 249 */       if (token.equalsIgnoreCase("application/fastinfoset")) {
/* 250 */         return true;
/*     */       }
/*     */     }
/* 253 */     return false;
/*     */   }
/*     */ 
/*     */   private ContentType getStaticContentType(XMLMessage.MessageDataSource mds) {
/* 257 */     String contentType = mds.getDataSource().getContentType();
/* 258 */     boolean isFastInfoset = XMLMessage.isFastInfoset(contentType);
/*     */ 
/* 260 */     if (!requiresTransformationOfDataSource(isFastInfoset, this.useFastInfosetForEncoding))
/*     */     {
/* 262 */       return new ContentTypeImpl(contentType);
/*     */     }
/* 264 */     return null;
/*     */   }
/*     */ 
/*     */   private ContentType encode(XMLMessage.MessageDataSource mds, OutputStream out)
/*     */   {
/*     */     try {
/* 270 */       boolean isFastInfoset = XMLMessage.isFastInfoset(mds.getDataSource().getContentType());
/*     */ 
/* 272 */       DataSource ds = transformDataSource(mds.getDataSource(), isFastInfoset, this.useFastInfosetForEncoding, this.binding);
/*     */ 
/* 275 */       InputStream is = ds.getInputStream();
/* 276 */       byte[] buf = new byte[1024];
/*     */       int count;
/* 278 */       while ((count = is.read(buf)) != -1) {
/* 279 */         out.write(buf, 0, count);
/*     */       }
/* 281 */       return new ContentTypeImpl(ds.getContentType());
/*     */     } catch (IOException ioe) {
/* 283 */       throw new WebServiceException(ioe);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void setRootCodec(Packet p)
/*     */   {
/* 293 */     if (p.contentNegotiation == ContentNegotiation.none)
/*     */     {
/* 296 */       this.useFastInfosetForEncoding = false;
/* 297 */     } else if (p.contentNegotiation == ContentNegotiation.optimistic)
/*     */     {
/* 299 */       this.useFastInfosetForEncoding = true;
/*     */     }
/*     */ 
/* 302 */     this.rootCodec = ((this.useFastInfosetForEncoding) && (this.fiCodec != null) ? this.fiCodec : this.xmlCodec);
/*     */   }
/*     */ 
/*     */   public static boolean requiresTransformationOfDataSource(boolean isFastInfoset, boolean useFastInfoset)
/*     */   {
/* 308 */     return ((isFastInfoset) && (!useFastInfoset)) || ((!isFastInfoset) && (useFastInfoset));
/*     */   }
/*     */ 
/*     */   public static DataSource transformDataSource(DataSource in, boolean isFastInfoset, boolean useFastInfoset, WSBinding binding)
/*     */   {
/*     */     try {
/* 314 */       if ((isFastInfoset) && (!useFastInfoset))
/*     */       {
/* 316 */         Codec codec = new XMLHTTPBindingCodec(binding);
/* 317 */         Packet p = new Packet();
/* 318 */         codec.decode(in.getInputStream(), in.getContentType(), p);
/*     */ 
/* 320 */         p.getMessage().getAttachments();
/* 321 */         codec.getStaticContentType(p);
/*     */ 
/* 323 */         ByteArrayBuffer bos = new ByteArrayBuffer();
/* 324 */         ContentType ct = codec.encode(p, bos);
/* 325 */         return XMLMessage.createDataSource(ct.getContentType(), bos.newInputStream());
/* 326 */       }if ((!isFastInfoset) && (useFastInfoset))
/*     */       {
/* 328 */         Codec codec = new XMLHTTPBindingCodec(binding);
/* 329 */         Packet p = new Packet();
/* 330 */         codec.decode(in.getInputStream(), in.getContentType(), p);
/*     */ 
/* 332 */         p.contentNegotiation = ContentNegotiation.optimistic;
/* 333 */         p.getMessage().getAttachments();
/* 334 */         codec.getStaticContentType(p);
/*     */ 
/* 336 */         ByteArrayBuffer bos = new ByteArrayBuffer();
/* 337 */         ContentType ct = codec.encode(p, bos);
/* 338 */         return XMLMessage.createDataSource(ct.getContentType(), bos.newInputStream());
/*     */       }
/*     */     } catch (Exception ex) {
/* 341 */       throw new WebServiceException(ex);
/*     */     }
/*     */ 
/* 344 */     return in;
/*     */   }
/*     */ 
/*     */   private static Codec getFICodec()
/*     */   {
/*     */     try
/*     */     {
/* 352 */       Class c = Class.forName("com.sun.xml.internal.ws.encoding.fastinfoset.FastInfosetCodec");
/* 353 */       Method m = c.getMethod("create", new Class[0]);
/* 354 */       return (Codec)m.invoke(null, new Object[0]); } catch (Exception e) {
/*     */     }
/* 356 */     return null;
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
/* 104 */       if ((p.contentNegotiation == ContentNegotiation.optimistic) || (p.contentNegotiation == ContentNegotiation.pessimistic))
/*     */       {
/* 106 */         this._accept = "application/fastinfoset, *";
/*     */       }
/* 108 */       else this._accept = XMLHTTPBindingCodec.xmlAccept;
/*     */ 
/* 110 */       this._c = c;
/* 111 */       return this;
/*     */     }
/*     */ 
/*     */     public String getContentType() {
/* 115 */       return this._c.getContentType();
/*     */     }
/*     */ 
/*     */     public String getSOAPActionHeader() {
/* 119 */       return this._c.getSOAPActionHeader();
/*     */     }
/*     */ 
/*     */     public String getAcceptHeader() {
/* 123 */       return this._accept;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.encoding.XMLHTTPBindingCodec
 * JD-Core Version:    0.6.2
 */