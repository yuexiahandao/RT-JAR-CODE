/*     */ package com.sun.xml.internal.ws.encoding.fastinfoset;
/*     */ 
/*     */ import com.sun.xml.internal.fastinfoset.stax.StAXDocumentParser;
/*     */ import com.sun.xml.internal.fastinfoset.stax.StAXDocumentSerializer;
/*     */ import com.sun.xml.internal.fastinfoset.vocab.ParserVocabulary;
/*     */ import com.sun.xml.internal.fastinfoset.vocab.SerializerVocabulary;
/*     */ import com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetSource;
/*     */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*     */ import com.sun.xml.internal.ws.api.message.Message;
/*     */ import com.sun.xml.internal.ws.api.message.Messages;
/*     */ import com.sun.xml.internal.ws.api.message.Packet;
/*     */ import com.sun.xml.internal.ws.api.pipe.Codec;
/*     */ import com.sun.xml.internal.ws.api.pipe.ContentType;
/*     */ import com.sun.xml.internal.ws.encoding.ContentTypeImpl;
/*     */ import java.io.BufferedInputStream;
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
/*     */ public class FastInfosetCodec
/*     */   implements Codec
/*     */ {
/*     */   private static final int DEFAULT_INDEXED_STRING_SIZE_LIMIT = 32;
/*     */   private static final int DEFAULT_INDEXED_STRING_MEMORY_LIMIT = 4194304;
/*     */   private StAXDocumentParser _parser;
/*     */   private StAXDocumentSerializer _serializer;
/*     */   private final boolean _retainState;
/*     */   private final ContentType _contentType;
/*     */ 
/*     */   FastInfosetCodec(boolean retainState)
/*     */   {
/*  71 */     this._retainState = retainState;
/*  72 */     this._contentType = (retainState ? new ContentTypeImpl("application/vnd.sun.stateful.fastinfoset") : new ContentTypeImpl("application/fastinfoset"));
/*     */   }
/*     */ 
/*     */   public String getMimeType()
/*     */   {
/*  77 */     return this._contentType.getContentType();
/*     */   }
/*     */ 
/*     */   public Codec copy() {
/*  81 */     return new FastInfosetCodec(this._retainState);
/*     */   }
/*     */ 
/*     */   public ContentType getStaticContentType(Packet packet) {
/*  85 */     return this._contentType;
/*     */   }
/*     */ 
/*     */   public ContentType encode(Packet packet, OutputStream out) {
/*  89 */     Message message = packet.getMessage();
/*  90 */     if ((message != null) && (message.hasPayload())) {
/*  91 */       XMLStreamWriter writer = getXMLStreamWriter(out);
/*     */       try {
/*  93 */         writer.writeStartDocument();
/*  94 */         packet.getMessage().writePayloadTo(writer);
/*  95 */         writer.writeEndDocument();
/*  96 */         writer.flush();
/*     */       } catch (XMLStreamException e) {
/*  98 */         throw new WebServiceException(e);
/*     */       }
/*     */     }
/*     */ 
/* 102 */     return this._contentType;
/*     */   }
/*     */ 
/*     */   public ContentType encode(Packet packet, WritableByteChannel buffer)
/*     */   {
/* 107 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public void decode(InputStream in, String contentType, Packet packet)
/*     */     throws IOException
/*     */   {
/* 113 */     Message message = null;
/* 114 */     in = hasSomeData(in);
/* 115 */     if (in != null) {
/* 116 */       message = Messages.createUsingPayload(new FastInfosetSource(in), SOAPVersion.SOAP_11);
/*     */     }
/*     */     else {
/* 119 */       message = Messages.createEmpty(SOAPVersion.SOAP_11);
/*     */     }
/*     */ 
/* 122 */     packet.setMessage(message);
/*     */   }
/*     */ 
/*     */   public void decode(ReadableByteChannel in, String contentType, Packet response) {
/* 126 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   private XMLStreamWriter getXMLStreamWriter(OutputStream out) {
/* 130 */     if (this._serializer != null) {
/* 131 */       this._serializer.setOutputStream(out);
/* 132 */       return this._serializer;
/*     */     }
/* 134 */     return this._serializer = createNewStreamWriter(out, this._retainState);
/*     */   }
/*     */ 
/*     */   private XMLStreamReader getXMLStreamReader(InputStream in)
/*     */   {
/* 139 */     if (this._parser != null) {
/* 140 */       this._parser.setInputStream(in);
/* 141 */       return this._parser;
/*     */     }
/* 143 */     return this._parser = createNewStreamReader(in, this._retainState);
/*     */   }
/*     */ 
/*     */   public static FastInfosetCodec create()
/*     */   {
/* 153 */     return create(false);
/*     */   }
/*     */ 
/*     */   public static FastInfosetCodec create(boolean retainState)
/*     */   {
/* 164 */     return new FastInfosetCodec(retainState);
/*     */   }
/*     */ 
/*     */   static StAXDocumentSerializer createNewStreamWriter(OutputStream out, boolean retainState)
/*     */   {
/* 176 */     return createNewStreamWriter(out, retainState, 32, 4194304);
/*     */   }
/*     */ 
/*     */   static StAXDocumentSerializer createNewStreamWriter(OutputStream out, boolean retainState, int indexedStringSizeLimit, int stringsMemoryLimit)
/*     */   {
/* 189 */     StAXDocumentSerializer serializer = new StAXDocumentSerializer(out);
/* 190 */     if (retainState)
/*     */     {
/* 197 */       SerializerVocabulary vocabulary = new SerializerVocabulary();
/* 198 */       serializer.setVocabulary(vocabulary);
/* 199 */       serializer.setMinAttributeValueSize(0);
/* 200 */       serializer.setMaxAttributeValueSize(indexedStringSizeLimit);
/* 201 */       serializer.setMinCharacterContentChunkSize(0);
/* 202 */       serializer.setMaxCharacterContentChunkSize(indexedStringSizeLimit);
/* 203 */       serializer.setAttributeValueMapMemoryLimit(stringsMemoryLimit);
/* 204 */       serializer.setCharacterContentChunkMapMemoryLimit(stringsMemoryLimit);
/*     */     }
/* 206 */     return serializer;
/*     */   }
/*     */ 
/*     */   static StAXDocumentParser createNewStreamReader(InputStream in, boolean retainState)
/*     */   {
/* 218 */     StAXDocumentParser parser = new StAXDocumentParser(in);
/* 219 */     parser.setStringInterning(true);
/* 220 */     if (retainState)
/*     */     {
/* 227 */       ParserVocabulary vocabulary = new ParserVocabulary();
/* 228 */       parser.setVocabulary(vocabulary);
/*     */     }
/* 230 */     return parser;
/*     */   }
/*     */ 
/*     */   static StAXDocumentParser createNewStreamReaderRecyclable(InputStream in, boolean retainState)
/*     */   {
/* 242 */     StAXDocumentParser parser = new FastInfosetStreamReaderRecyclable(in);
/* 243 */     parser.setStringInterning(true);
/* 244 */     parser.setForceStreamClose(true);
/* 245 */     if (retainState)
/*     */     {
/* 252 */       ParserVocabulary vocabulary = new ParserVocabulary();
/* 253 */       parser.setVocabulary(vocabulary);
/*     */     }
/* 255 */     return parser;
/*     */   }
/*     */ 
/*     */   private static InputStream hasSomeData(InputStream in)
/*     */     throws IOException
/*     */   {
/* 268 */     if ((in != null) && 
/* 269 */       (in.available() < 1)) {
/* 270 */       if (!in.markSupported()) {
/* 271 */         in = new BufferedInputStream(in);
/*     */       }
/* 273 */       in.mark(1);
/* 274 */       if (in.read() != -1)
/* 275 */         in.reset();
/*     */       else {
/* 277 */         in = null;
/*     */       }
/*     */     }
/*     */ 
/* 281 */     return in;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.encoding.fastinfoset.FastInfosetCodec
 * JD-Core Version:    0.6.2
 */