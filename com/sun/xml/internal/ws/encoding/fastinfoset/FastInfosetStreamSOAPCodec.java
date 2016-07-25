/*     */ package com.sun.xml.internal.ws.encoding.fastinfoset;
/*     */ 
/*     */ import com.sun.xml.internal.fastinfoset.stax.StAXDocumentParser;
/*     */ import com.sun.xml.internal.fastinfoset.stax.StAXDocumentSerializer;
/*     */ import com.sun.xml.internal.stream.buffer.XMLStreamBuffer;
/*     */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*     */ import com.sun.xml.internal.ws.api.message.Message;
/*     */ import com.sun.xml.internal.ws.api.message.Packet;
/*     */ import com.sun.xml.internal.ws.api.pipe.Codec;
/*     */ import com.sun.xml.internal.ws.api.pipe.ContentType;
/*     */ import com.sun.xml.internal.ws.api.pipe.StreamSOAPCodec;
/*     */ import com.sun.xml.internal.ws.encoding.ContentTypeImpl;
/*     */ import com.sun.xml.internal.ws.message.stream.StreamHeader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.channels.ReadableByteChannel;
/*     */ import java.nio.channels.WritableByteChannel;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ 
/*     */ public abstract class FastInfosetStreamSOAPCodec
/*     */   implements Codec
/*     */ {
/*  60 */   private static final FastInfosetStreamReaderFactory READER_FACTORY = FastInfosetStreamReaderFactory.getInstance();
/*     */   private StAXDocumentParser _statefulParser;
/*     */   private StAXDocumentSerializer _serializer;
/*     */   private final StreamSOAPCodec _soapCodec;
/*     */   private final boolean _retainState;
/*     */   protected final ContentType _defaultContentType;
/*     */ 
/*     */   FastInfosetStreamSOAPCodec(StreamSOAPCodec soapCodec, SOAPVersion soapVersion, boolean retainState, String mimeType)
/*     */   {
/*  73 */     this._soapCodec = soapCodec;
/*  74 */     this._retainState = retainState;
/*  75 */     this._defaultContentType = new ContentTypeImpl(mimeType);
/*     */   }
/*     */ 
/*     */   FastInfosetStreamSOAPCodec(FastInfosetStreamSOAPCodec that) {
/*  79 */     this._soapCodec = ((StreamSOAPCodec)that._soapCodec.copy());
/*  80 */     this._retainState = that._retainState;
/*  81 */     this._defaultContentType = that._defaultContentType;
/*     */   }
/*     */ 
/*     */   public String getMimeType() {
/*  85 */     return this._defaultContentType.getContentType();
/*     */   }
/*     */ 
/*     */   public ContentType getStaticContentType(Packet packet) {
/*  89 */     return getContentType(packet.soapAction);
/*     */   }
/*     */ 
/*     */   public ContentType encode(Packet packet, OutputStream out) {
/*  93 */     if (packet.getMessage() != null) {
/*  94 */       XMLStreamWriter writer = getXMLStreamWriter(out);
/*     */       try {
/*  96 */         packet.getMessage().writeTo(writer);
/*  97 */         writer.flush();
/*     */       } catch (XMLStreamException e) {
/*  99 */         throw new WebServiceException(e);
/*     */       }
/*     */     }
/* 102 */     return getContentType(packet.soapAction);
/*     */   }
/*     */ 
/*     */   public ContentType encode(Packet packet, WritableByteChannel buffer)
/*     */   {
/* 107 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public void decode(InputStream in, String contentType, Packet response) throws IOException {
/* 111 */     response.setMessage(this._soapCodec.decode(getXMLStreamReader(in)));
/*     */   }
/*     */ 
/*     */   public void decode(ReadableByteChannel in, String contentType, Packet response)
/*     */   {
/* 116 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   protected abstract StreamHeader createHeader(XMLStreamReader paramXMLStreamReader, XMLStreamBuffer paramXMLStreamBuffer);
/*     */ 
/*     */   protected abstract ContentType getContentType(String paramString);
/*     */ 
/*     */   private XMLStreamWriter getXMLStreamWriter(OutputStream out) {
/* 124 */     if (this._serializer != null) {
/* 125 */       this._serializer.setOutputStream(out);
/* 126 */       return this._serializer;
/*     */     }
/* 128 */     return this._serializer = FastInfosetCodec.createNewStreamWriter(out, this._retainState);
/*     */   }
/*     */ 
/*     */   private XMLStreamReader getXMLStreamReader(InputStream in)
/*     */   {
/* 134 */     if (this._retainState) {
/* 135 */       if (this._statefulParser != null) {
/* 136 */         this._statefulParser.setInputStream(in);
/* 137 */         return this._statefulParser;
/*     */       }
/* 139 */       return this._statefulParser = FastInfosetCodec.createNewStreamReader(in, this._retainState);
/*     */     }
/*     */ 
/* 144 */     return READER_FACTORY.doCreate(null, in, false);
/*     */   }
/*     */ 
/*     */   public static FastInfosetStreamSOAPCodec create(StreamSOAPCodec soapCodec, SOAPVersion version)
/*     */   {
/* 154 */     return create(soapCodec, version, false);
/*     */   }
/*     */ 
/*     */   public static FastInfosetStreamSOAPCodec create(StreamSOAPCodec soapCodec, SOAPVersion version, boolean retainState)
/*     */   {
/* 167 */     if (version == null)
/*     */     {
/* 169 */       throw new IllegalArgumentException();
/* 170 */     }switch (1.$SwitchMap$com$sun$xml$internal$ws$api$SOAPVersion[version.ordinal()]) {
/*     */     case 1:
/* 172 */       return new FastInfosetStreamSOAP11Codec(soapCodec, retainState);
/*     */     case 2:
/* 174 */       return new FastInfosetStreamSOAP12Codec(soapCodec, retainState);
/*     */     }
/* 176 */     throw new AssertionError();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.encoding.fastinfoset.FastInfosetStreamSOAPCodec
 * JD-Core Version:    0.6.2
 */