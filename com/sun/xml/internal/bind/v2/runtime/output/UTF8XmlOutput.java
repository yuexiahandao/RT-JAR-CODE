/*     */ package com.sun.xml.internal.bind.v2.runtime.output;
/*     */ 
/*     */ import com.sun.xml.internal.bind.DatatypeConverterImpl;
/*     */ import com.sun.xml.internal.bind.marshaller.CharacterEscapeHandler;
/*     */ import com.sun.xml.internal.bind.v2.runtime.Name;
/*     */ import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.StringWriter;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class UTF8XmlOutput extends XmlOutputAbstractImpl
/*     */ {
/*     */   protected final OutputStream out;
/*  52 */   private Encoded[] prefixes = new Encoded[8];
/*     */   private int prefixCount;
/*     */   private final Encoded[] localNames;
/*  71 */   private final Encoded textBuffer = new Encoded();
/*     */ 
/*  75 */   protected final byte[] octetBuffer = new byte[1024];
/*     */   protected int octetBufferIndex;
/*  85 */   protected boolean closeStartTagPending = false;
/*     */   private String header;
/*  92 */   private CharacterEscapeHandler escapeHandler = null;
/*     */ 
/* 416 */   private final byte[] XMLNS_EQUALS = (byte[])_XMLNS_EQUALS.clone();
/* 417 */   private final byte[] XMLNS_COLON = (byte[])_XMLNS_COLON.clone();
/* 418 */   private final byte[] EQUALS = (byte[])_EQUALS.clone();
/* 419 */   private final byte[] CLOSE_TAG = (byte[])_CLOSE_TAG.clone();
/* 420 */   private final byte[] EMPTY_TAG = (byte[])_EMPTY_TAG.clone();
/* 421 */   private final byte[] XML_DECL = (byte[])_XML_DECL.clone();
/*     */ 
/* 424 */   private static final byte[] _XMLNS_EQUALS = toBytes(" xmlns=\"");
/* 425 */   private static final byte[] _XMLNS_COLON = toBytes(" xmlns:");
/* 426 */   private static final byte[] _EQUALS = toBytes("=\"");
/* 427 */   private static final byte[] _CLOSE_TAG = toBytes("</");
/* 428 */   private static final byte[] _EMPTY_TAG = toBytes("/>");
/* 429 */   private static final byte[] _XML_DECL = toBytes("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
/*     */ 
/* 432 */   private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
/*     */ 
/*     */   public UTF8XmlOutput(OutputStream out, Encoded[] localNames, CharacterEscapeHandler escapeHandler)
/*     */   {
/* 100 */     this.out = out;
/* 101 */     this.localNames = localNames;
/* 102 */     for (int i = 0; i < this.prefixes.length; i++)
/* 103 */       this.prefixes[i] = new Encoded();
/* 104 */     this.escapeHandler = escapeHandler;
/*     */   }
/*     */ 
/*     */   public void setHeader(String header) {
/* 108 */     this.header = header;
/*     */   }
/*     */ 
/*     */   public void startDocument(XMLSerializer serializer, boolean fragment, int[] nsUriIndex2prefixIndex, NamespaceContextImpl nsContext) throws IOException, SAXException, XMLStreamException
/*     */   {
/* 113 */     super.startDocument(serializer, fragment, nsUriIndex2prefixIndex, nsContext);
/*     */ 
/* 115 */     this.octetBufferIndex = 0;
/* 116 */     if (!fragment) {
/* 117 */       write(this.XML_DECL);
/*     */     }
/* 119 */     if (this.header != null) {
/* 120 */       this.textBuffer.set(this.header);
/* 121 */       this.textBuffer.write(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void endDocument(boolean fragment) throws IOException, SAXException, XMLStreamException
/*     */   {
/* 127 */     flushBuffer();
/* 128 */     super.endDocument(fragment);
/*     */   }
/*     */ 
/*     */   protected final void closeStartTag()
/*     */     throws IOException
/*     */   {
/* 135 */     if (this.closeStartTagPending) {
/* 136 */       write(62);
/* 137 */       this.closeStartTagPending = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void beginStartTag(int prefix, String localName) throws IOException {
/* 142 */     closeStartTag();
/* 143 */     int base = pushNsDecls();
/* 144 */     write(60);
/* 145 */     writeName(prefix, localName);
/* 146 */     writeNsDecls(base);
/*     */   }
/*     */ 
/*     */   public void beginStartTag(Name name) throws IOException
/*     */   {
/* 151 */     closeStartTag();
/* 152 */     int base = pushNsDecls();
/* 153 */     write(60);
/* 154 */     writeName(name);
/* 155 */     writeNsDecls(base);
/*     */   }
/*     */ 
/*     */   private int pushNsDecls() {
/* 159 */     int total = this.nsContext.count();
/* 160 */     NamespaceContextImpl.Element ns = this.nsContext.getCurrent();
/*     */ 
/* 162 */     if (total > this.prefixes.length)
/*     */     {
/* 164 */       int m = Math.max(total, this.prefixes.length * 2);
/* 165 */       Encoded[] buf = new Encoded[m];
/* 166 */       System.arraycopy(this.prefixes, 0, buf, 0, this.prefixes.length);
/* 167 */       for (int i = this.prefixes.length; i < buf.length; i++)
/* 168 */         buf[i] = new Encoded();
/* 169 */       this.prefixes = buf;
/*     */     }
/*     */ 
/* 172 */     int base = Math.min(this.prefixCount, ns.getBase());
/* 173 */     int size = this.nsContext.count();
/* 174 */     for (int i = base; i < size; i++) {
/* 175 */       String p = this.nsContext.getPrefix(i);
/*     */ 
/* 177 */       Encoded e = this.prefixes[i];
/*     */ 
/* 179 */       if (p.length() == 0) {
/* 180 */         e.buf = EMPTY_BYTE_ARRAY;
/* 181 */         e.len = 0;
/*     */       } else {
/* 183 */         e.set(p);
/* 184 */         e.append(':');
/*     */       }
/*     */     }
/* 187 */     this.prefixCount = size;
/* 188 */     return base;
/*     */   }
/*     */ 
/*     */   protected void writeNsDecls(int base) throws IOException {
/* 192 */     NamespaceContextImpl.Element ns = this.nsContext.getCurrent();
/* 193 */     int size = this.nsContext.count();
/*     */ 
/* 195 */     for (int i = ns.getBase(); i < size; i++)
/* 196 */       writeNsDecl(i);
/*     */   }
/*     */ 
/*     */   protected final void writeNsDecl(int prefixIndex)
/*     */     throws IOException
/*     */   {
/* 203 */     String p = this.nsContext.getPrefix(prefixIndex);
/*     */ 
/* 205 */     if (p.length() == 0) {
/* 206 */       if ((this.nsContext.getCurrent().isRootElement()) && (this.nsContext.getNamespaceURI(prefixIndex).length() == 0))
/*     */       {
/* 208 */         return;
/* 209 */       }write(this.XMLNS_EQUALS);
/*     */     } else {
/* 211 */       Encoded e = this.prefixes[prefixIndex];
/* 212 */       write(this.XMLNS_COLON);
/* 213 */       write(e.buf, 0, e.len - 1);
/* 214 */       write(this.EQUALS);
/*     */     }
/* 216 */     doText(this.nsContext.getNamespaceURI(prefixIndex), true);
/* 217 */     write(34);
/*     */   }
/*     */ 
/*     */   private void writePrefix(int prefix) throws IOException {
/* 221 */     this.prefixes[prefix].write(this);
/*     */   }
/*     */ 
/*     */   private void writeName(Name name) throws IOException {
/* 225 */     writePrefix(this.nsUriIndex2prefixIndex[name.nsUriIndex]);
/* 226 */     this.localNames[name.localNameIndex].write(this);
/*     */   }
/*     */ 
/*     */   private void writeName(int prefix, String localName) throws IOException {
/* 230 */     writePrefix(prefix);
/* 231 */     this.textBuffer.set(localName);
/* 232 */     this.textBuffer.write(this);
/*     */   }
/*     */ 
/*     */   public void attribute(Name name, String value) throws IOException
/*     */   {
/* 237 */     write(32);
/* 238 */     if (name.nsUriIndex == -1)
/* 239 */       this.localNames[name.localNameIndex].write(this);
/*     */     else
/* 241 */       writeName(name);
/* 242 */     write(this.EQUALS);
/* 243 */     doText(value, true);
/* 244 */     write(34);
/*     */   }
/*     */ 
/*     */   public void attribute(int prefix, String localName, String value) throws IOException {
/* 248 */     write(32);
/* 249 */     if (prefix == -1) {
/* 250 */       this.textBuffer.set(localName);
/* 251 */       this.textBuffer.write(this);
/*     */     } else {
/* 253 */       writeName(prefix, localName);
/* 254 */     }write(this.EQUALS);
/* 255 */     doText(value, true);
/* 256 */     write(34);
/*     */   }
/*     */ 
/*     */   public void endStartTag() throws IOException {
/* 260 */     this.closeStartTagPending = true;
/*     */   }
/*     */ 
/*     */   public void endTag(Name name) throws IOException
/*     */   {
/* 265 */     if (this.closeStartTagPending) {
/* 266 */       write(this.EMPTY_TAG);
/* 267 */       this.closeStartTagPending = false;
/*     */     } else {
/* 269 */       write(this.CLOSE_TAG);
/* 270 */       writeName(name);
/* 271 */       write(62);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void endTag(int prefix, String localName) throws IOException {
/* 276 */     if (this.closeStartTagPending) {
/* 277 */       write(this.EMPTY_TAG);
/* 278 */       this.closeStartTagPending = false;
/*     */     } else {
/* 280 */       write(this.CLOSE_TAG);
/* 281 */       writeName(prefix, localName);
/* 282 */       write(62);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void text(String value, boolean needSP) throws IOException {
/* 287 */     closeStartTag();
/* 288 */     if (needSP)
/* 289 */       write(32);
/* 290 */     doText(value, false);
/*     */   }
/*     */ 
/*     */   public void text(Pcdata value, boolean needSP) throws IOException {
/* 294 */     closeStartTag();
/* 295 */     if (needSP)
/* 296 */       write(32);
/* 297 */     value.writeTo(this);
/*     */   }
/*     */ 
/*     */   private void doText(String value, boolean isAttribute) throws IOException {
/* 301 */     if (this.escapeHandler != null) {
/* 302 */       StringWriter sw = new StringWriter();
/* 303 */       this.escapeHandler.escape(value.toCharArray(), 0, value.length(), isAttribute, sw);
/* 304 */       this.textBuffer.set(sw.toString());
/*     */     } else {
/* 306 */       this.textBuffer.setEscape(value, isAttribute);
/*     */     }
/* 308 */     this.textBuffer.write(this);
/*     */   }
/*     */ 
/*     */   public final void text(int value) throws IOException {
/* 312 */     closeStartTag();
/*     */ 
/* 319 */     boolean minus = value < 0;
/* 320 */     this.textBuffer.ensureSize(11);
/* 321 */     byte[] buf = this.textBuffer.buf;
/* 322 */     int idx = 11;
/*     */     do
/*     */     {
/* 325 */       int r = value % 10;
/* 326 */       if (r < 0) r = -r;
/* 327 */       buf[(--idx)] = ((byte)(0x30 | r));
/* 328 */       value /= 10;
/* 329 */     }while (value != 0);
/*     */ 
/* 331 */     if (minus) buf[(--idx)] = 45;
/*     */ 
/* 333 */     write(buf, idx, 11 - idx);
/*     */   }
/*     */ 
/*     */   public void text(byte[] data, int dataLen)
/*     */     throws IOException
/*     */   {
/* 344 */     closeStartTag();
/*     */ 
/* 346 */     int start = 0;
/*     */ 
/* 348 */     while (dataLen > 0)
/*     */     {
/* 350 */       int batchSize = Math.min((this.octetBuffer.length - this.octetBufferIndex) / 4 * 3, dataLen);
/*     */ 
/* 353 */       this.octetBufferIndex = DatatypeConverterImpl._printBase64Binary(data, start, batchSize, this.octetBuffer, this.octetBufferIndex);
/*     */ 
/* 355 */       if (batchSize < dataLen) {
/* 356 */         flushBuffer();
/*     */       }
/* 358 */       start += batchSize;
/* 359 */       dataLen -= batchSize;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void write(int i)
/*     */     throws IOException
/*     */   {
/* 378 */     if (this.octetBufferIndex < this.octetBuffer.length) {
/* 379 */       this.octetBuffer[(this.octetBufferIndex++)] = ((byte)i);
/*     */     } else {
/* 381 */       this.out.write(this.octetBuffer);
/* 382 */       this.octetBufferIndex = 1;
/* 383 */       this.octetBuffer[0] = ((byte)i);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final void write(byte[] b) throws IOException {
/* 388 */     write(b, 0, b.length);
/*     */   }
/*     */ 
/*     */   protected final void write(byte[] b, int start, int length) throws IOException {
/* 392 */     if (this.octetBufferIndex + length < this.octetBuffer.length) {
/* 393 */       System.arraycopy(b, start, this.octetBuffer, this.octetBufferIndex, length);
/* 394 */       this.octetBufferIndex += length;
/*     */     } else {
/* 396 */       this.out.write(this.octetBuffer, 0, this.octetBufferIndex);
/* 397 */       this.out.write(b, start, length);
/* 398 */       this.octetBufferIndex = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final void flushBuffer() throws IOException {
/* 403 */     this.out.write(this.octetBuffer, 0, this.octetBufferIndex);
/* 404 */     this.octetBufferIndex = 0;
/*     */   }
/*     */ 
/*     */   static byte[] toBytes(String s) {
/* 408 */     byte[] buf = new byte[s.length()];
/* 409 */     for (int i = s.length() - 1; i >= 0; i--)
/* 410 */       buf[i] = ((byte)s.charAt(i));
/* 411 */     return buf;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.output.UTF8XmlOutput
 * JD-Core Version:    0.6.2
 */