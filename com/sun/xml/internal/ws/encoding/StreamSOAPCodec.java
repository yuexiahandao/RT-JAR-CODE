/*     */ package com.sun.xml.internal.ws.encoding;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.xml.internal.stream.buffer.MutableXMLStreamBuffer;
/*     */ import com.sun.xml.internal.stream.buffer.XMLStreamBuffer;
/*     */ import com.sun.xml.internal.stream.buffer.XMLStreamBufferMark;
/*     */ import com.sun.xml.internal.stream.buffer.stax.StreamReaderBufferCreator;
/*     */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*     */ import com.sun.xml.internal.ws.api.message.AttachmentSet;
/*     */ import com.sun.xml.internal.ws.api.message.HeaderList;
/*     */ import com.sun.xml.internal.ws.api.message.Message;
/*     */ import com.sun.xml.internal.ws.api.message.Packet;
/*     */ import com.sun.xml.internal.ws.api.pipe.ContentType;
/*     */ import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory;
/*     */ import com.sun.xml.internal.ws.api.streaming.XMLStreamWriterFactory;
/*     */ import com.sun.xml.internal.ws.message.AttachmentSetImpl;
/*     */ import com.sun.xml.internal.ws.message.stream.StreamHeader;
/*     */ import com.sun.xml.internal.ws.message.stream.StreamMessage;
/*     */ import com.sun.xml.internal.ws.protocol.soap.VersionMismatchException;
/*     */ import com.sun.xml.internal.ws.server.UnsupportedMediaException;
/*     */ import com.sun.xml.internal.ws.streaming.TidyXMLStreamReader;
/*     */ import com.sun.xml.internal.ws.streaming.XMLStreamReaderUtil;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.channels.ReadableByteChannel;
/*     */ import java.nio.channels.WritableByteChannel;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ 
/*     */ public abstract class StreamSOAPCodec
/*     */   implements com.sun.xml.internal.ws.api.pipe.StreamSOAPCodec, RootOnlyCodec
/*     */ {
/*     */   private static final String SOAP_ENVELOPE = "Envelope";
/*     */   private static final String SOAP_HEADER = "Header";
/*     */   private static final String SOAP_BODY = "Body";
/*     */   private final String SOAP_NAMESPACE_URI;
/*     */   private final SOAPVersion soapVersion;
/*     */ 
/*     */   StreamSOAPCodec(SOAPVersion soapVersion)
/*     */   {
/*  80 */     this.SOAP_NAMESPACE_URI = soapVersion.nsUri;
/*  81 */     this.soapVersion = soapVersion;
/*     */   }
/*     */ 
/*     */   public ContentType getStaticContentType(Packet packet)
/*     */   {
/*  91 */     return getContentType(packet.soapAction);
/*     */   }
/*     */ 
/*     */   public ContentType encode(Packet packet, OutputStream out) {
/*  95 */     if (packet.getMessage() != null) {
/*  96 */       XMLStreamWriter writer = XMLStreamWriterFactory.create(out);
/*     */       try {
/*  98 */         packet.getMessage().writeTo(writer);
/*  99 */         writer.flush();
/*     */       } catch (XMLStreamException e) {
/* 101 */         throw new WebServiceException(e);
/*     */       }
/* 103 */       XMLStreamWriterFactory.recycle(writer);
/*     */     }
/* 105 */     return getContentType(packet.soapAction);
/*     */   }
/*     */ 
/*     */   protected abstract ContentType getContentType(String paramString);
/*     */ 
/*     */   public ContentType encode(Packet packet, WritableByteChannel buffer)
/*     */   {
/* 112 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   protected abstract List<String> getExpectedContentTypes();
/*     */ 
/*     */   public void decode(InputStream in, String contentType, Packet packet) throws IOException {
/* 118 */     decode(in, contentType, packet, new AttachmentSetImpl());
/*     */   }
/*     */ 
/*     */   private static boolean isContentTypeSupported(String ct, List<String> expected)
/*     */   {
/* 130 */     for (String contentType : expected) {
/* 131 */       if (ct.indexOf(contentType) != -1) {
/* 132 */         return true;
/*     */       }
/*     */     }
/* 135 */     return false;
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public final Message decode(@NotNull XMLStreamReader reader)
/*     */   {
/* 146 */     return decode(reader, new AttachmentSetImpl());
/*     */   }
/*     */ 
/*     */   public final Message decode(XMLStreamReader reader, @NotNull AttachmentSet attachmentSet)
/*     */   {
/* 163 */     if (reader.getEventType() != 1)
/* 164 */       XMLStreamReaderUtil.nextElementContent(reader);
/* 165 */     XMLStreamReaderUtil.verifyReaderState(reader, 1);
/* 166 */     if (("Envelope".equals(reader.getLocalName())) && (!this.SOAP_NAMESPACE_URI.equals(reader.getNamespaceURI()))) {
/* 167 */       throw new VersionMismatchException(this.soapVersion, new Object[] { this.SOAP_NAMESPACE_URI, reader.getNamespaceURI() });
/*     */     }
/* 169 */     XMLStreamReaderUtil.verifyTag(reader, this.SOAP_NAMESPACE_URI, "Envelope");
/*     */ 
/* 171 */     TagInfoset envelopeTag = new TagInfoset(reader);
/*     */ 
/* 174 */     Map namespaces = new HashMap();
/* 175 */     for (int i = 0; i < reader.getNamespaceCount(); i++) {
/* 176 */       namespaces.put(reader.getNamespacePrefix(i), reader.getNamespaceURI(i));
/*     */     }
/*     */ 
/* 180 */     XMLStreamReaderUtil.nextElementContent(reader);
/* 181 */     XMLStreamReaderUtil.verifyReaderState(reader, 1);
/*     */ 
/* 184 */     HeaderList headers = null;
/* 185 */     TagInfoset headerTag = null;
/*     */ 
/* 187 */     if ((reader.getLocalName().equals("Header")) && (reader.getNamespaceURI().equals(this.SOAP_NAMESPACE_URI)))
/*     */     {
/* 189 */       headerTag = new TagInfoset(reader);
/*     */ 
/* 192 */       for (int i = 0; i < reader.getNamespaceCount(); i++) {
/* 193 */         namespaces.put(reader.getNamespacePrefix(i), reader.getNamespaceURI(i));
/*     */       }
/*     */ 
/* 196 */       XMLStreamReaderUtil.nextElementContent(reader);
/*     */ 
/* 199 */       if (reader.getEventType() == 1) {
/* 200 */         headers = new HeaderList();
/*     */         try
/*     */         {
/* 204 */           cacheHeaders(reader, namespaces, headers);
/*     */         }
/*     */         catch (XMLStreamException e) {
/* 207 */           throw new WebServiceException(e);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 212 */       XMLStreamReaderUtil.nextElementContent(reader);
/*     */     }
/*     */ 
/* 216 */     XMLStreamReaderUtil.verifyTag(reader, this.SOAP_NAMESPACE_URI, "Body");
/* 217 */     TagInfoset bodyTag = new TagInfoset(reader);
/*     */ 
/* 219 */     XMLStreamReaderUtil.nextElementContent(reader);
/* 220 */     return new StreamMessage(envelopeTag, headerTag, attachmentSet, headers, bodyTag, reader, this.soapVersion);
/*     */   }
/*     */ 
/*     */   public void decode(ReadableByteChannel in, String contentType, Packet packet)
/*     */   {
/* 227 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public final StreamSOAPCodec copy() {
/* 231 */     return this;
/*     */   }
/*     */ 
/*     */   private XMLStreamBuffer cacheHeaders(XMLStreamReader reader, Map<String, String> namespaces, HeaderList headers) throws XMLStreamException
/*     */   {
/* 236 */     MutableXMLStreamBuffer buffer = createXMLStreamBuffer();
/* 237 */     StreamReaderBufferCreator creator = new StreamReaderBufferCreator();
/* 238 */     creator.setXMLStreamBuffer(buffer);
/*     */ 
/* 241 */     while (reader.getEventType() == 1) {
/* 242 */       Map headerBlockNamespaces = namespaces;
/*     */ 
/* 245 */       if (reader.getNamespaceCount() > 0) {
/* 246 */         headerBlockNamespaces = new HashMap(namespaces);
/* 247 */         for (int i = 0; i < reader.getNamespaceCount(); i++) {
/* 248 */           headerBlockNamespaces.put(reader.getNamespacePrefix(i), reader.getNamespaceURI(i));
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 253 */       XMLStreamBuffer mark = new XMLStreamBufferMark(headerBlockNamespaces, creator);
/*     */ 
/* 255 */       headers.add(createHeader(reader, mark));
/*     */ 
/* 261 */       creator.createElementFragment(reader, false);
/* 262 */       if ((reader.getEventType() != 1) && (reader.getEventType() != 2))
/*     */       {
/* 264 */         XMLStreamReaderUtil.nextElementContent(reader);
/*     */       }
/*     */     }
/*     */ 
/* 268 */     return buffer;
/*     */   }
/*     */ 
/*     */   protected abstract StreamHeader createHeader(XMLStreamReader paramXMLStreamReader, XMLStreamBuffer paramXMLStreamBuffer);
/*     */ 
/*     */   private MutableXMLStreamBuffer createXMLStreamBuffer()
/*     */   {
/* 278 */     return new MutableXMLStreamBuffer();
/*     */   }
/*     */ 
/*     */   public void decode(InputStream in, String contentType, Packet packet, AttachmentSet att) throws IOException {
/* 282 */     List expectedContentTypes = getExpectedContentTypes();
/* 283 */     if ((contentType != null) && (!isContentTypeSupported(contentType, expectedContentTypes))) {
/* 284 */       throw new UnsupportedMediaException(contentType, expectedContentTypes);
/*     */     }
/* 286 */     String charset = new ContentTypeImpl(contentType).getCharSet();
/* 287 */     if ((charset != null) && (!Charset.isSupported(charset))) {
/* 288 */       throw new UnsupportedMediaException(charset);
/*     */     }
/* 290 */     XMLStreamReader reader = XMLStreamReaderFactory.create(null, in, charset, true);
/* 291 */     reader = new TidyXMLStreamReader(reader, in);
/* 292 */     packet.setMessage(decode(reader, att));
/*     */   }
/*     */ 
/*     */   public void decode(ReadableByteChannel in, String contentType, Packet response, AttachmentSet att) {
/* 296 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public static StreamSOAPCodec create(SOAPVersion version)
/*     */   {
/* 305 */     if (version == null)
/*     */     {
/* 307 */       throw new IllegalArgumentException();
/* 308 */     }switch (1.$SwitchMap$com$sun$xml$internal$ws$api$SOAPVersion[version.ordinal()]) {
/*     */     case 1:
/* 310 */       return new StreamSOAP11Codec();
/*     */     case 2:
/* 312 */       return new StreamSOAP12Codec();
/*     */     }
/* 314 */     throw new AssertionError();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.encoding.StreamSOAPCodec
 * JD-Core Version:    0.6.2
 */