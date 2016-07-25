/*      */ package com.sun.xml.internal.fastinfoset.sax;
/*      */ 
/*      */ import com.sun.xml.internal.fastinfoset.CommonResourceBundle;
/*      */ import com.sun.xml.internal.fastinfoset.Decoder;
/*      */ import com.sun.xml.internal.fastinfoset.DecoderStateTables;
/*      */ import com.sun.xml.internal.fastinfoset.EncodingConstants;
/*      */ import com.sun.xml.internal.fastinfoset.QualifiedName;
/*      */ import com.sun.xml.internal.fastinfoset.algorithm.BooleanEncodingAlgorithm;
/*      */ import com.sun.xml.internal.fastinfoset.algorithm.BuiltInEncodingAlgorithm;
/*      */ import com.sun.xml.internal.fastinfoset.algorithm.BuiltInEncodingAlgorithmFactory;
/*      */ import com.sun.xml.internal.fastinfoset.algorithm.BuiltInEncodingAlgorithmState;
/*      */ import com.sun.xml.internal.fastinfoset.algorithm.DoubleEncodingAlgorithm;
/*      */ import com.sun.xml.internal.fastinfoset.algorithm.FloatEncodingAlgorithm;
/*      */ import com.sun.xml.internal.fastinfoset.algorithm.IntEncodingAlgorithm;
/*      */ import com.sun.xml.internal.fastinfoset.algorithm.LongEncodingAlgorithm;
/*      */ import com.sun.xml.internal.fastinfoset.algorithm.ShortEncodingAlgorithm;
/*      */ import com.sun.xml.internal.fastinfoset.algorithm.UUIDEncodingAlgorithm;
/*      */ import com.sun.xml.internal.fastinfoset.util.CharArray;
/*      */ import com.sun.xml.internal.fastinfoset.util.CharArrayArray;
/*      */ import com.sun.xml.internal.fastinfoset.util.CharArrayString;
/*      */ import com.sun.xml.internal.fastinfoset.util.ContiguousCharArrayArray;
/*      */ import com.sun.xml.internal.fastinfoset.util.DuplicateAttributeVerifier;
/*      */ import com.sun.xml.internal.fastinfoset.util.PrefixArray;
/*      */ import com.sun.xml.internal.fastinfoset.util.QualifiedNameArray;
/*      */ import com.sun.xml.internal.fastinfoset.util.StringArray;
/*      */ import com.sun.xml.internal.fastinfoset.vocab.ParserVocabulary;
/*      */ import com.sun.xml.internal.org.jvnet.fastinfoset.EncodingAlgorithm;
/*      */ import com.sun.xml.internal.org.jvnet.fastinfoset.EncodingAlgorithmException;
/*      */ import com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetException;
/*      */ import com.sun.xml.internal.org.jvnet.fastinfoset.sax.EncodingAlgorithmContentHandler;
/*      */ import com.sun.xml.internal.org.jvnet.fastinfoset.sax.FastInfosetReader;
/*      */ import com.sun.xml.internal.org.jvnet.fastinfoset.sax.PrimitiveTypeContentHandler;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.net.URL;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import org.xml.sax.ContentHandler;
/*      */ import org.xml.sax.DTDHandler;
/*      */ import org.xml.sax.EntityResolver;
/*      */ import org.xml.sax.ErrorHandler;
/*      */ import org.xml.sax.InputSource;
/*      */ import org.xml.sax.SAXException;
/*      */ import org.xml.sax.SAXNotRecognizedException;
/*      */ import org.xml.sax.SAXNotSupportedException;
/*      */ import org.xml.sax.SAXParseException;
/*      */ import org.xml.sax.ext.DeclHandler;
/*      */ import org.xml.sax.ext.LexicalHandler;
/*      */ import org.xml.sax.helpers.DefaultHandler;
/*      */ 
/*      */ public class SAXDocumentParser extends Decoder
/*      */   implements FastInfosetReader
/*      */ {
/*   76 */   private static final Logger logger = Logger.getLogger(SAXDocumentParser.class.getName());
/*      */ 
/*  119 */   protected boolean _namespacePrefixesFeature = false;
/*      */   protected EntityResolver _entityResolver;
/*      */   protected DTDHandler _dtdHandler;
/*      */   protected ContentHandler _contentHandler;
/*      */   protected ErrorHandler _errorHandler;
/*      */   protected LexicalHandler _lexicalHandler;
/*      */   protected DeclHandler _declHandler;
/*      */   protected EncodingAlgorithmContentHandler _algorithmHandler;
/*      */   protected PrimitiveTypeContentHandler _primitiveHandler;
/*  155 */   protected BuiltInEncodingAlgorithmState builtInAlgorithmState = new BuiltInEncodingAlgorithmState();
/*      */   protected AttributesHolder _attributes;
/*  160 */   protected int[] _namespacePrefixes = new int[16];
/*      */   protected int _namespacePrefixesIndex;
/*  164 */   protected boolean _clearAttributes = false;
/*      */ 
/*      */   public SAXDocumentParser()
/*      */   {
/*  168 */     DefaultHandler handler = new DefaultHandler();
/*  169 */     this._attributes = new AttributesHolder(this._registeredEncodingAlgorithms);
/*      */ 
/*  171 */     this._entityResolver = handler;
/*  172 */     this._dtdHandler = handler;
/*  173 */     this._contentHandler = handler;
/*  174 */     this._errorHandler = handler;
/*  175 */     this._lexicalHandler = new LexicalHandlerImpl(null);
/*  176 */     this._declHandler = new DeclHandlerImpl(null);
/*      */   }
/*      */ 
/*      */   protected void resetOnError() {
/*  180 */     this._clearAttributes = false;
/*  181 */     this._attributes.clear();
/*  182 */     this._namespacePrefixesIndex = 0;
/*      */ 
/*  184 */     if (this._v != null) {
/*  185 */       this._v.prefix.clearCompletely();
/*      */     }
/*  187 */     this._duplicateAttributeVerifier.clear();
/*      */   }
/*      */ 
/*      */   public boolean getFeature(String name)
/*      */     throws SAXNotRecognizedException, SAXNotSupportedException
/*      */   {
/*  194 */     if (name.equals("http://xml.org/sax/features/namespaces"))
/*  195 */       return true;
/*  196 */     if (name.equals("http://xml.org/sax/features/namespace-prefixes"))
/*  197 */       return this._namespacePrefixesFeature;
/*  198 */     if ((name.equals("http://xml.org/sax/features/string-interning")) || (name.equals("http://jvnet.org/fastinfoset/parser/properties/string-interning")))
/*      */     {
/*  200 */       return getStringInterning();
/*      */     }
/*  202 */     throw new SAXNotRecognizedException(CommonResourceBundle.getInstance().getString("message.featureNotSupported") + name);
/*      */   }
/*      */ 
/*      */   public void setFeature(String name, boolean value)
/*      */     throws SAXNotRecognizedException, SAXNotSupportedException
/*      */   {
/*  209 */     if (name.equals("http://xml.org/sax/features/namespaces")) {
/*  210 */       if (!value)
/*  211 */         throw new SAXNotSupportedException(name + ":" + value);
/*      */     }
/*  213 */     else if (name.equals("http://xml.org/sax/features/namespace-prefixes"))
/*  214 */       this._namespacePrefixesFeature = value;
/*  215 */     else if ((name.equals("http://xml.org/sax/features/string-interning")) || (name.equals("http://jvnet.org/fastinfoset/parser/properties/string-interning")))
/*      */     {
/*  217 */       setStringInterning(value);
/*      */     }
/*  219 */     else throw new SAXNotRecognizedException(CommonResourceBundle.getInstance().getString("message.featureNotSupported") + name);
/*      */   }
/*      */ 
/*      */   public Object getProperty(String name)
/*      */     throws SAXNotRecognizedException, SAXNotSupportedException
/*      */   {
/*  226 */     if (name.equals("http://xml.org/sax/properties/lexical-handler"))
/*  227 */       return getLexicalHandler();
/*  228 */     if (name.equals("http://xml.org/sax/properties/declaration-handler"))
/*  229 */       return getDeclHandler();
/*  230 */     if (name.equals("http://jvnet.org/fastinfoset/parser/properties/external-vocabularies"))
/*  231 */       return getExternalVocabularies();
/*  232 */     if (name.equals("http://jvnet.org/fastinfoset/parser/properties/registered-encoding-algorithms"))
/*  233 */       return getRegisteredEncodingAlgorithms();
/*  234 */     if (name.equals("http://jvnet.org/fastinfoset/sax/properties/encoding-algorithm-content-handler"))
/*  235 */       return getEncodingAlgorithmContentHandler();
/*  236 */     if (name.equals("http://jvnet.org/fastinfoset/sax/properties/primitive-type-content-handler")) {
/*  237 */       return getPrimitiveTypeContentHandler();
/*      */     }
/*  239 */     throw new SAXNotRecognizedException(CommonResourceBundle.getInstance().getString("message.propertyNotRecognized", new Object[] { name }));
/*      */   }
/*      */ 
/*      */   public void setProperty(String name, Object value)
/*      */     throws SAXNotRecognizedException, SAXNotSupportedException
/*      */   {
/*  246 */     if (name.equals("http://xml.org/sax/properties/lexical-handler")) {
/*  247 */       if ((value instanceof LexicalHandler))
/*  248 */         setLexicalHandler((LexicalHandler)value);
/*      */       else
/*  250 */         throw new SAXNotSupportedException("http://xml.org/sax/properties/lexical-handler");
/*      */     }
/*  252 */     else if (name.equals("http://xml.org/sax/properties/declaration-handler")) {
/*  253 */       if ((value instanceof DeclHandler))
/*  254 */         setDeclHandler((DeclHandler)value);
/*      */       else
/*  256 */         throw new SAXNotSupportedException("http://xml.org/sax/properties/lexical-handler");
/*      */     }
/*  258 */     else if (name.equals("http://jvnet.org/fastinfoset/parser/properties/external-vocabularies")) {
/*  259 */       if ((value instanceof Map))
/*  260 */         setExternalVocabularies((Map)value);
/*      */       else
/*  262 */         throw new SAXNotSupportedException("http://jvnet.org/fastinfoset/parser/properties/external-vocabularies");
/*      */     }
/*  264 */     else if (name.equals("http://jvnet.org/fastinfoset/parser/properties/registered-encoding-algorithms")) {
/*  265 */       if ((value instanceof Map))
/*  266 */         setRegisteredEncodingAlgorithms((Map)value);
/*      */       else
/*  268 */         throw new SAXNotSupportedException("http://jvnet.org/fastinfoset/parser/properties/registered-encoding-algorithms");
/*      */     }
/*  270 */     else if (name.equals("http://jvnet.org/fastinfoset/sax/properties/encoding-algorithm-content-handler")) {
/*  271 */       if ((value instanceof EncodingAlgorithmContentHandler))
/*  272 */         setEncodingAlgorithmContentHandler((EncodingAlgorithmContentHandler)value);
/*      */       else
/*  274 */         throw new SAXNotSupportedException("http://jvnet.org/fastinfoset/sax/properties/encoding-algorithm-content-handler");
/*      */     }
/*  276 */     else if (name.equals("http://jvnet.org/fastinfoset/sax/properties/primitive-type-content-handler")) {
/*  277 */       if ((value instanceof PrimitiveTypeContentHandler))
/*  278 */         setPrimitiveTypeContentHandler((PrimitiveTypeContentHandler)value);
/*      */       else
/*  280 */         throw new SAXNotSupportedException("http://jvnet.org/fastinfoset/sax/properties/primitive-type-content-handler");
/*      */     }
/*  282 */     else if (name.equals("http://jvnet.org/fastinfoset/parser/properties/buffer-size")) {
/*  283 */       if ((value instanceof Integer))
/*  284 */         setBufferSize(((Integer)value).intValue());
/*      */       else
/*  286 */         throw new SAXNotSupportedException("http://jvnet.org/fastinfoset/parser/properties/buffer-size");
/*      */     }
/*      */     else
/*  289 */       throw new SAXNotRecognizedException(CommonResourceBundle.getInstance().getString("message.propertyNotRecognized", new Object[] { name }));
/*      */   }
/*      */ 
/*      */   public void setEntityResolver(EntityResolver resolver)
/*      */   {
/*  295 */     this._entityResolver = resolver;
/*      */   }
/*      */ 
/*      */   public EntityResolver getEntityResolver() {
/*  299 */     return this._entityResolver;
/*      */   }
/*      */ 
/*      */   public void setDTDHandler(DTDHandler handler) {
/*  303 */     this._dtdHandler = handler;
/*      */   }
/*      */ 
/*      */   public DTDHandler getDTDHandler() {
/*  307 */     return this._dtdHandler;
/*      */   }
/*      */   public void setContentHandler(ContentHandler handler) {
/*  310 */     this._contentHandler = handler;
/*      */   }
/*      */ 
/*      */   public ContentHandler getContentHandler() {
/*  314 */     return this._contentHandler;
/*      */   }
/*      */ 
/*      */   public void setErrorHandler(ErrorHandler handler) {
/*  318 */     this._errorHandler = handler;
/*      */   }
/*      */ 
/*      */   public ErrorHandler getErrorHandler() {
/*  322 */     return this._errorHandler;
/*      */   }
/*      */ 
/*      */   public void parse(InputSource input) throws IOException, SAXException {
/*      */     try {
/*  327 */       InputStream s = input.getByteStream();
/*  328 */       if (s == null) {
/*  329 */         String systemId = input.getSystemId();
/*  330 */         if (systemId == null) {
/*  331 */           throw new SAXException(CommonResourceBundle.getInstance().getString("message.inputSource"));
/*      */         }
/*  333 */         parse(systemId);
/*      */       } else {
/*  335 */         parse(s);
/*      */       }
/*      */     } catch (FastInfosetException e) {
/*  338 */       logger.log(Level.FINE, "parsing error", e);
/*  339 */       throw new SAXException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void parse(String systemId) throws IOException, SAXException {
/*      */     try {
/*  345 */       systemId = SystemIdResolver.getAbsoluteURI(systemId);
/*  346 */       parse(new URL(systemId).openStream());
/*      */     } catch (FastInfosetException e) {
/*  348 */       logger.log(Level.FINE, "parsing error", e);
/*  349 */       throw new SAXException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public final void parse(InputStream s)
/*      */     throws IOException, FastInfosetException, SAXException
/*      */   {
/*  359 */     setInputStream(s);
/*  360 */     parse();
/*      */   }
/*      */ 
/*      */   public void setLexicalHandler(LexicalHandler handler) {
/*  364 */     this._lexicalHandler = handler;
/*      */   }
/*      */ 
/*      */   public LexicalHandler getLexicalHandler() {
/*  368 */     return this._lexicalHandler;
/*      */   }
/*      */ 
/*      */   public void setDeclHandler(DeclHandler handler) {
/*  372 */     this._declHandler = handler;
/*      */   }
/*      */ 
/*      */   public DeclHandler getDeclHandler() {
/*  376 */     return this._declHandler;
/*      */   }
/*      */ 
/*      */   public void setEncodingAlgorithmContentHandler(EncodingAlgorithmContentHandler handler) {
/*  380 */     this._algorithmHandler = handler;
/*      */   }
/*      */ 
/*      */   public EncodingAlgorithmContentHandler getEncodingAlgorithmContentHandler() {
/*  384 */     return this._algorithmHandler;
/*      */   }
/*      */ 
/*      */   public void setPrimitiveTypeContentHandler(PrimitiveTypeContentHandler handler) {
/*  388 */     this._primitiveHandler = handler;
/*      */   }
/*      */ 
/*      */   public PrimitiveTypeContentHandler getPrimitiveTypeContentHandler() {
/*  392 */     return this._primitiveHandler;
/*      */   }
/*      */ 
/*      */   public final void parse()
/*      */     throws FastInfosetException, IOException
/*      */   {
/*  399 */     if (this._octetBuffer.length < this._bufferSize) {
/*  400 */       this._octetBuffer = new byte[this._bufferSize];
/*      */     }
/*      */     try
/*      */     {
/*  404 */       reset();
/*  405 */       decodeHeader();
/*  406 */       if (this._parseFragments)
/*  407 */         processDIIFragment();
/*      */       else
/*  409 */         processDII();
/*      */     } catch (RuntimeException e) {
/*      */       try {
/*  412 */         this._errorHandler.fatalError(new SAXParseException(e.getClass().getName(), null, e));
/*      */       } catch (Exception ee) {
/*      */       }
/*  415 */       resetOnError();
/*      */ 
/*  417 */       throw new FastInfosetException(e);
/*      */     } catch (FastInfosetException e) {
/*      */       try {
/*  420 */         this._errorHandler.fatalError(new SAXParseException(e.getClass().getName(), null, e));
/*      */       } catch (Exception ee) {
/*      */       }
/*  423 */       resetOnError();
/*  424 */       throw e;
/*      */     } catch (IOException e) {
/*      */       try {
/*  427 */         this._errorHandler.fatalError(new SAXParseException(e.getClass().getName(), null, e));
/*      */       } catch (Exception ee) {
/*      */       }
/*  430 */       resetOnError();
/*  431 */       throw e;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void processDII() throws FastInfosetException, IOException {
/*      */     try {
/*  437 */       this._contentHandler.startDocument();
/*      */     } catch (SAXException e) {
/*  439 */       throw new FastInfosetException("processDII", e);
/*      */     }
/*      */ 
/*  442 */     this._b = read();
/*  443 */     if (this._b > 0) {
/*  444 */       processDIIOptionalProperties();
/*      */     }
/*      */ 
/*  448 */     boolean firstElementHasOccured = false;
/*  449 */     boolean documentTypeDeclarationOccured = false;
/*  450 */     while ((!this._terminate) || (!firstElementHasOccured)) {
/*  451 */       this._b = read();
/*  452 */       switch (DecoderStateTables.DII(this._b)) {
/*      */       case 0:
/*  454 */         processEII(this._elementNameTable._array[this._b], false);
/*  455 */         firstElementHasOccured = true;
/*  456 */         break;
/*      */       case 1:
/*  458 */         processEII(this._elementNameTable._array[(this._b & 0x1F)], true);
/*  459 */         firstElementHasOccured = true;
/*  460 */         break;
/*      */       case 2:
/*  462 */         processEII(decodeEIIIndexMedium(), (this._b & 0x40) > 0);
/*  463 */         firstElementHasOccured = true;
/*  464 */         break;
/*      */       case 3:
/*  466 */         processEII(decodeEIIIndexLarge(), (this._b & 0x40) > 0);
/*  467 */         firstElementHasOccured = true;
/*  468 */         break;
/*      */       case 5:
/*  471 */         QualifiedName qn = decodeLiteralQualifiedName(this._b & 0x3, this._elementNameTable.getNext());
/*      */ 
/*  474 */         this._elementNameTable.add(qn);
/*  475 */         processEII(qn, (this._b & 0x40) > 0);
/*  476 */         firstElementHasOccured = true;
/*  477 */         break;
/*      */       case 4:
/*  480 */         processEIIWithNamespaces();
/*  481 */         firstElementHasOccured = true;
/*  482 */         break;
/*      */       case 20:
/*  485 */         if (documentTypeDeclarationOccured) {
/*  486 */           throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.secondOccurenceOfDTDII"));
/*      */         }
/*  488 */         documentTypeDeclarationOccured = true;
/*      */ 
/*  490 */         String system_identifier = (this._b & 0x2) > 0 ? decodeIdentifyingNonEmptyStringOnFirstBit(this._v.otherURI) : "";
/*      */ 
/*  492 */         String public_identifier = (this._b & 0x1) > 0 ? decodeIdentifyingNonEmptyStringOnFirstBit(this._v.otherURI) : "";
/*      */ 
/*  495 */         this._b = read();
/*  496 */         while (this._b == 225) {
/*  497 */           switch (decodeNonIdentifyingStringOnFirstBit()) {
/*      */           case 0:
/*  499 */             if (this._addToTable)
/*  500 */               this._v.otherString.add(new CharArray(this._charBuffer, 0, this._charBufferLength, true)); break;
/*      */           case 2:
/*  504 */             throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.processingIIWithEncodingAlgorithm"));
/*      */           case 1:
/*  506 */             break;
/*      */           case 3:
/*      */           }
/*      */ 
/*  510 */           this._b = read();
/*      */         }
/*  512 */         if ((this._b & 0xF0) != 240) {
/*  513 */           throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.processingInstructionIIsNotTerminatedCorrectly"));
/*      */         }
/*  515 */         if (this._b == 255) {
/*  516 */           this._terminate = true;
/*      */         }
/*      */ 
/*  519 */         if (this._notations != null) this._notations.clear();
/*  520 */         if (this._unparsedEntities != null) this._unparsedEntities.clear(); break;
/*      */       case 18:
/*  528 */         processCommentII();
/*  529 */         break;
/*      */       case 19:
/*  531 */         processProcessingII();
/*  532 */         break;
/*      */       case 23:
/*  534 */         this._doubleTerminate = true;
/*      */       case 22:
/*  536 */         this._terminate = true;
/*  537 */         break;
/*      */       case 6:
/*      */       case 7:
/*      */       case 8:
/*      */       case 9:
/*      */       case 10:
/*      */       case 11:
/*      */       case 12:
/*      */       case 13:
/*      */       case 14:
/*      */       case 15:
/*      */       case 16:
/*      */       case 17:
/*      */       case 21:
/*      */       default:
/*  539 */         throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.IllegalStateDecodingDII"));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  544 */     while (!this._terminate) {
/*  545 */       this._b = read();
/*  546 */       switch (DecoderStateTables.DII(this._b)) {
/*      */       case 18:
/*  548 */         processCommentII();
/*  549 */         break;
/*      */       case 19:
/*  551 */         processProcessingII();
/*  552 */         break;
/*      */       case 23:
/*  554 */         this._doubleTerminate = true;
/*      */       case 22:
/*  556 */         this._terminate = true;
/*  557 */         break;
/*      */       case 20:
/*      */       case 21:
/*      */       default:
/*  559 */         throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.IllegalStateDecodingDII"));
/*      */       }
/*      */     }
/*      */     try
/*      */     {
/*  564 */       this._contentHandler.endDocument();
/*      */     } catch (SAXException e) {
/*  566 */       throw new FastInfosetException("processDII", e);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void processDIIFragment() throws FastInfosetException, IOException {
/*      */     try {
/*  572 */       this._contentHandler.startDocument();
/*      */     } catch (SAXException e) {
/*  574 */       throw new FastInfosetException("processDII", e);
/*      */     }
/*      */ 
/*  577 */     this._b = read();
/*  578 */     if (this._b > 0) {
/*  579 */       processDIIOptionalProperties();
/*      */     }
/*      */ 
/*  582 */     while (!this._terminate) {
/*  583 */       this._b = read();
/*  584 */       switch (DecoderStateTables.EII(this._b)) {
/*      */       case 0:
/*  586 */         processEII(this._elementNameTable._array[this._b], false);
/*  587 */         break;
/*      */       case 1:
/*  589 */         processEII(this._elementNameTable._array[(this._b & 0x1F)], true);
/*  590 */         break;
/*      */       case 2:
/*  592 */         processEII(decodeEIIIndexMedium(), (this._b & 0x40) > 0);
/*  593 */         break;
/*      */       case 3:
/*  595 */         processEII(decodeEIIIndexLarge(), (this._b & 0x40) > 0);
/*  596 */         break;
/*      */       case 5:
/*  599 */         QualifiedName qn = decodeLiteralQualifiedName(this._b & 0x3, this._elementNameTable.getNext());
/*      */ 
/*  602 */         this._elementNameTable.add(qn);
/*  603 */         processEII(qn, (this._b & 0x40) > 0);
/*  604 */         break;
/*      */       case 4:
/*  607 */         processEIIWithNamespaces();
/*  608 */         break;
/*      */       case 6:
/*  610 */         this._octetBufferLength = ((this._b & 0x1) + 1);
/*      */ 
/*  612 */         processUtf8CharacterString();
/*  613 */         break;
/*      */       case 7:
/*  615 */         this._octetBufferLength = (read() + 3);
/*  616 */         processUtf8CharacterString();
/*  617 */         break;
/*      */       case 8:
/*  619 */         this._octetBufferLength = ((read() << 24 | read() << 16 | read() << 8 | read()) + 259);
/*      */ 
/*  624 */         processUtf8CharacterString();
/*  625 */         break;
/*      */       case 9:
/*  627 */         this._octetBufferLength = ((this._b & 0x1) + 1);
/*      */ 
/*  629 */         decodeUtf16StringAsCharBuffer();
/*  630 */         if ((this._b & 0x10) > 0) {
/*  631 */           this._characterContentChunkTable.add(this._charBuffer, this._charBufferLength);
/*      */         }
/*      */         try
/*      */         {
/*  635 */           this._contentHandler.characters(this._charBuffer, 0, this._charBufferLength);
/*      */         } catch (SAXException e) {
/*  637 */           throw new FastInfosetException("processCII", e);
/*      */         }
/*      */ 
/*      */       case 10:
/*  641 */         this._octetBufferLength = (read() + 3);
/*  642 */         decodeUtf16StringAsCharBuffer();
/*  643 */         if ((this._b & 0x10) > 0) {
/*  644 */           this._characterContentChunkTable.add(this._charBuffer, this._charBufferLength);
/*      */         }
/*      */         try
/*      */         {
/*  648 */           this._contentHandler.characters(this._charBuffer, 0, this._charBufferLength);
/*      */         } catch (SAXException e) {
/*  650 */           throw new FastInfosetException("processCII", e);
/*      */         }
/*      */ 
/*      */       case 11:
/*  654 */         this._octetBufferLength = ((read() << 24 | read() << 16 | read() << 8 | read()) + 259);
/*      */ 
/*  659 */         decodeUtf16StringAsCharBuffer();
/*  660 */         if ((this._b & 0x10) > 0) {
/*  661 */           this._characterContentChunkTable.add(this._charBuffer, this._charBufferLength);
/*      */         }
/*      */         try
/*      */         {
/*  665 */           this._contentHandler.characters(this._charBuffer, 0, this._charBufferLength);
/*      */         } catch (SAXException e) {
/*  667 */           throw new FastInfosetException("processCII", e);
/*      */         }
/*      */ 
/*      */       case 12:
/*  672 */         boolean addToTable = (this._b & 0x10) > 0;
/*      */ 
/*  675 */         this._identifier = ((this._b & 0x2) << 6);
/*  676 */         this._b = read();
/*  677 */         this._identifier |= (this._b & 0xFC) >> 2;
/*      */ 
/*  679 */         decodeOctetsOnSeventhBitOfNonIdentifyingStringOnThirdBit(this._b);
/*      */ 
/*  681 */         decodeRestrictedAlphabetAsCharBuffer();
/*      */ 
/*  683 */         if (addToTable) {
/*  684 */           this._characterContentChunkTable.add(this._charBuffer, this._charBufferLength);
/*      */         }
/*      */         try
/*      */         {
/*  688 */           this._contentHandler.characters(this._charBuffer, 0, this._charBufferLength);
/*      */         } catch (SAXException e) {
/*  690 */           throw new FastInfosetException("processCII", e);
/*      */         }
/*      */ 
/*      */       case 13:
/*  696 */         boolean addToTable = (this._b & 0x10) > 0;
/*      */ 
/*  699 */         this._identifier = ((this._b & 0x2) << 6);
/*  700 */         this._b = read();
/*  701 */         this._identifier |= (this._b & 0xFC) >> 2;
/*      */ 
/*  703 */         decodeOctetsOnSeventhBitOfNonIdentifyingStringOnThirdBit(this._b);
/*      */ 
/*  705 */         processCIIEncodingAlgorithm(addToTable);
/*  706 */         break;
/*      */       case 14:
/*  710 */         int index = this._b & 0xF;
/*      */         try {
/*  712 */           this._contentHandler.characters(this._characterContentChunkTable._array, this._characterContentChunkTable._offset[index], this._characterContentChunkTable._length[index]);
/*      */         }
/*      */         catch (SAXException e)
/*      */         {
/*  716 */           throw new FastInfosetException("processCII", e);
/*      */         }
/*      */ 
/*      */       case 15:
/*  722 */         int index = ((this._b & 0x3) << 8 | read()) + 16;
/*      */         try
/*      */         {
/*  725 */           this._contentHandler.characters(this._characterContentChunkTable._array, this._characterContentChunkTable._offset[index], this._characterContentChunkTable._length[index]);
/*      */         }
/*      */         catch (SAXException e)
/*      */         {
/*  729 */           throw new FastInfosetException("processCII", e);
/*      */         }
/*      */ 
/*      */       case 16:
/*  735 */         int index = ((this._b & 0x3) << 16 | read() << 8 | read()) + 1040;
/*      */         try
/*      */         {
/*  741 */           this._contentHandler.characters(this._characterContentChunkTable._array, this._characterContentChunkTable._offset[index], this._characterContentChunkTable._length[index]);
/*      */         }
/*      */         catch (SAXException e)
/*      */         {
/*  745 */           throw new FastInfosetException("processCII", e);
/*      */         }
/*      */ 
/*      */       case 17:
/*  751 */         int index = (read() << 16 | read() << 8 | read()) + 263184;
/*      */         try
/*      */         {
/*  757 */           this._contentHandler.characters(this._characterContentChunkTable._array, this._characterContentChunkTable._offset[index], this._characterContentChunkTable._length[index]);
/*      */         }
/*      */         catch (SAXException e)
/*      */         {
/*  761 */           throw new FastInfosetException("processCII", e);
/*      */         }
/*      */ 
/*      */       case 18:
/*  766 */         processCommentII();
/*  767 */         break;
/*      */       case 19:
/*  769 */         processProcessingII();
/*  770 */         break;
/*      */       case 21:
/*  773 */         String entity_reference_name = decodeIdentifyingNonEmptyStringOnFirstBit(this._v.otherNCName);
/*      */ 
/*  775 */         String system_identifier = (this._b & 0x2) > 0 ? decodeIdentifyingNonEmptyStringOnFirstBit(this._v.otherURI) : "";
/*      */ 
/*  777 */         String public_identifier = (this._b & 0x1) > 0 ? decodeIdentifyingNonEmptyStringOnFirstBit(this._v.otherURI) : "";
/*      */         try
/*      */         {
/*  788 */           this._contentHandler.skippedEntity(entity_reference_name);
/*      */         } catch (SAXException e) {
/*  790 */           throw new FastInfosetException("processUnexpandedEntityReferenceII", e);
/*      */         }
/*      */ 
/*      */       case 23:
/*  795 */         this._doubleTerminate = true;
/*      */       case 22:
/*  797 */         this._terminate = true;
/*  798 */         break;
/*      */       case 20:
/*      */       default:
/*  800 */         throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.IllegalStateDecodingEII"));
/*      */       }
/*      */     }
/*      */     try
/*      */     {
/*  805 */       this._contentHandler.endDocument();
/*      */     } catch (SAXException e) {
/*  807 */       throw new FastInfosetException("processDII", e);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void processDIIOptionalProperties() throws FastInfosetException, IOException
/*      */   {
/*  813 */     if (this._b == 32) {
/*  814 */       decodeInitialVocabulary();
/*  815 */       return;
/*      */     }
/*      */ 
/*  818 */     if ((this._b & 0x40) > 0) {
/*  819 */       decodeAdditionalData();
/*      */     }
/*      */ 
/*  826 */     if ((this._b & 0x20) > 0) {
/*  827 */       decodeInitialVocabulary();
/*      */     }
/*      */ 
/*  830 */     if ((this._b & 0x10) > 0) {
/*  831 */       decodeNotations();
/*      */     }
/*      */ 
/*  841 */     if ((this._b & 0x8) > 0)
/*  842 */       decodeUnparsedEntities();
/*      */     String characterEncodingScheme;
/*  852 */     if ((this._b & 0x4) > 0)
/*  853 */       characterEncodingScheme = decodeCharacterEncodingScheme();
/*      */     boolean standalone;
/*  860 */     if ((this._b & 0x2) > 0) {
/*  861 */       standalone = read() > 0;
/*      */     }
/*      */ 
/*  868 */     if ((this._b & 0x1) > 0)
/*  869 */       decodeVersion();
/*      */   }
/*      */ 
/*      */   protected final void processEII(QualifiedName name, boolean hasAttributes)
/*      */     throws FastInfosetException, IOException
/*      */   {
/*  878 */     if (this._prefixTable._currentInScope[name.prefixIndex] != name.namespaceNameIndex) {
/*  879 */       throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.qNameOfEIINotInScope"));
/*      */     }
/*      */ 
/*  882 */     if (hasAttributes) {
/*  883 */       processAIIs();
/*      */     }
/*      */     try
/*      */     {
/*  887 */       this._contentHandler.startElement(name.namespaceName, name.localName, name.qName, this._attributes);
/*      */     } catch (SAXException e) {
/*  889 */       logger.log(Level.FINE, "processEII error", e);
/*  890 */       throw new FastInfosetException("processEII", e);
/*      */     }
/*      */ 
/*  893 */     if (this._clearAttributes) {
/*  894 */       this._attributes.clear();
/*  895 */       this._clearAttributes = false;
/*      */     }
/*      */ 
/*  898 */     while (!this._terminate) {
/*  899 */       this._b = read();
/*  900 */       switch (DecoderStateTables.EII(this._b)) {
/*      */       case 0:
/*  902 */         processEII(this._elementNameTable._array[this._b], false);
/*  903 */         break;
/*      */       case 1:
/*  905 */         processEII(this._elementNameTable._array[(this._b & 0x1F)], true);
/*  906 */         break;
/*      */       case 2:
/*  908 */         processEII(decodeEIIIndexMedium(), (this._b & 0x40) > 0);
/*  909 */         break;
/*      */       case 3:
/*  911 */         processEII(decodeEIIIndexLarge(), (this._b & 0x40) > 0);
/*  912 */         break;
/*      */       case 5:
/*  915 */         QualifiedName qn = decodeLiteralQualifiedName(this._b & 0x3, this._elementNameTable.getNext());
/*      */ 
/*  918 */         this._elementNameTable.add(qn);
/*  919 */         processEII(qn, (this._b & 0x40) > 0);
/*  920 */         break;
/*      */       case 4:
/*  923 */         processEIIWithNamespaces();
/*  924 */         break;
/*      */       case 6:
/*  926 */         this._octetBufferLength = ((this._b & 0x1) + 1);
/*      */ 
/*  928 */         processUtf8CharacterString();
/*  929 */         break;
/*      */       case 7:
/*  931 */         this._octetBufferLength = (read() + 3);
/*  932 */         processUtf8CharacterString();
/*  933 */         break;
/*      */       case 8:
/*  935 */         this._octetBufferLength = ((read() << 24 | read() << 16 | read() << 8 | read()) + 259);
/*      */ 
/*  940 */         processUtf8CharacterString();
/*  941 */         break;
/*      */       case 9:
/*  943 */         this._octetBufferLength = ((this._b & 0x1) + 1);
/*      */ 
/*  945 */         decodeUtf16StringAsCharBuffer();
/*  946 */         if ((this._b & 0x10) > 0) {
/*  947 */           this._characterContentChunkTable.add(this._charBuffer, this._charBufferLength);
/*      */         }
/*      */         try
/*      */         {
/*  951 */           this._contentHandler.characters(this._charBuffer, 0, this._charBufferLength);
/*      */         } catch (SAXException e) {
/*  953 */           throw new FastInfosetException("processCII", e);
/*      */         }
/*      */ 
/*      */       case 10:
/*  957 */         this._octetBufferLength = (read() + 3);
/*  958 */         decodeUtf16StringAsCharBuffer();
/*  959 */         if ((this._b & 0x10) > 0) {
/*  960 */           this._characterContentChunkTable.add(this._charBuffer, this._charBufferLength);
/*      */         }
/*      */         try
/*      */         {
/*  964 */           this._contentHandler.characters(this._charBuffer, 0, this._charBufferLength);
/*      */         } catch (SAXException e) {
/*  966 */           throw new FastInfosetException("processCII", e);
/*      */         }
/*      */ 
/*      */       case 11:
/*  970 */         this._octetBufferLength = ((read() << 24 | read() << 16 | read() << 8 | read()) + 259);
/*      */ 
/*  975 */         decodeUtf16StringAsCharBuffer();
/*  976 */         if ((this._b & 0x10) > 0) {
/*  977 */           this._characterContentChunkTable.add(this._charBuffer, this._charBufferLength);
/*      */         }
/*      */         try
/*      */         {
/*  981 */           this._contentHandler.characters(this._charBuffer, 0, this._charBufferLength);
/*      */         } catch (SAXException e) {
/*  983 */           throw new FastInfosetException("processCII", e);
/*      */         }
/*      */ 
/*      */       case 12:
/*  988 */         boolean addToTable = (this._b & 0x10) > 0;
/*      */ 
/*  991 */         this._identifier = ((this._b & 0x2) << 6);
/*  992 */         this._b = read();
/*  993 */         this._identifier |= (this._b & 0xFC) >> 2;
/*      */ 
/*  995 */         decodeOctetsOnSeventhBitOfNonIdentifyingStringOnThirdBit(this._b);
/*      */ 
/*  997 */         decodeRestrictedAlphabetAsCharBuffer();
/*      */ 
/*  999 */         if (addToTable) {
/* 1000 */           this._characterContentChunkTable.add(this._charBuffer, this._charBufferLength);
/*      */         }
/*      */         try
/*      */         {
/* 1004 */           this._contentHandler.characters(this._charBuffer, 0, this._charBufferLength);
/*      */         } catch (SAXException e) {
/* 1006 */           throw new FastInfosetException("processCII", e);
/*      */         }
/*      */ 
/*      */       case 13:
/* 1012 */         boolean addToTable = (this._b & 0x10) > 0;
/*      */ 
/* 1014 */         this._identifier = ((this._b & 0x2) << 6);
/* 1015 */         this._b = read();
/* 1016 */         this._identifier |= (this._b & 0xFC) >> 2;
/*      */ 
/* 1018 */         decodeOctetsOnSeventhBitOfNonIdentifyingStringOnThirdBit(this._b);
/*      */ 
/* 1020 */         processCIIEncodingAlgorithm(addToTable);
/* 1021 */         break;
/*      */       case 14:
/* 1025 */         int index = this._b & 0xF;
/*      */         try {
/* 1027 */           this._contentHandler.characters(this._characterContentChunkTable._array, this._characterContentChunkTable._offset[index], this._characterContentChunkTable._length[index]);
/*      */         }
/*      */         catch (SAXException e)
/*      */         {
/* 1031 */           throw new FastInfosetException("processCII", e);
/*      */         }
/*      */ 
/*      */       case 15:
/* 1037 */         int index = ((this._b & 0x3) << 8 | read()) + 16;
/*      */         try
/*      */         {
/* 1040 */           this._contentHandler.characters(this._characterContentChunkTable._array, this._characterContentChunkTable._offset[index], this._characterContentChunkTable._length[index]);
/*      */         }
/*      */         catch (SAXException e)
/*      */         {
/* 1044 */           throw new FastInfosetException("processCII", e);
/*      */         }
/*      */ 
/*      */       case 16:
/* 1050 */         int index = ((this._b & 0x3) << 16 | read() << 8 | read()) + 1040;
/*      */         try
/*      */         {
/* 1056 */           this._contentHandler.characters(this._characterContentChunkTable._array, this._characterContentChunkTable._offset[index], this._characterContentChunkTable._length[index]);
/*      */         }
/*      */         catch (SAXException e)
/*      */         {
/* 1060 */           throw new FastInfosetException("processCII", e);
/*      */         }
/*      */ 
/*      */       case 17:
/* 1066 */         int index = (read() << 16 | read() << 8 | read()) + 263184;
/*      */         try
/*      */         {
/* 1072 */           this._contentHandler.characters(this._characterContentChunkTable._array, this._characterContentChunkTable._offset[index], this._characterContentChunkTable._length[index]);
/*      */         }
/*      */         catch (SAXException e)
/*      */         {
/* 1076 */           throw new FastInfosetException("processCII", e);
/*      */         }
/*      */ 
/*      */       case 18:
/* 1081 */         processCommentII();
/* 1082 */         break;
/*      */       case 19:
/* 1084 */         processProcessingII();
/* 1085 */         break;
/*      */       case 21:
/* 1088 */         String entity_reference_name = decodeIdentifyingNonEmptyStringOnFirstBit(this._v.otherNCName);
/*      */ 
/* 1090 */         String system_identifier = (this._b & 0x2) > 0 ? decodeIdentifyingNonEmptyStringOnFirstBit(this._v.otherURI) : "";
/*      */ 
/* 1092 */         String public_identifier = (this._b & 0x1) > 0 ? decodeIdentifyingNonEmptyStringOnFirstBit(this._v.otherURI) : "";
/*      */         try
/*      */         {
/* 1103 */           this._contentHandler.skippedEntity(entity_reference_name);
/*      */         } catch (SAXException e) {
/* 1105 */           throw new FastInfosetException("processUnexpandedEntityReferenceII", e);
/*      */         }
/*      */ 
/*      */       case 23:
/* 1110 */         this._doubleTerminate = true;
/*      */       case 22:
/* 1112 */         this._terminate = true;
/* 1113 */         break;
/*      */       case 20:
/*      */       default:
/* 1115 */         throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.IllegalStateDecodingEII"));
/*      */       }
/*      */     }
/*      */ 
/* 1119 */     this._terminate = this._doubleTerminate;
/* 1120 */     this._doubleTerminate = false;
/*      */     try
/*      */     {
/* 1123 */       this._contentHandler.endElement(name.namespaceName, name.localName, name.qName);
/*      */     } catch (SAXException e) {
/* 1125 */       throw new FastInfosetException("processEII", e);
/*      */     }
/*      */   }
/*      */ 
/*      */   private final void processUtf8CharacterString() throws FastInfosetException, IOException {
/* 1130 */     if ((this._b & 0x10) > 0) {
/* 1131 */       this._characterContentChunkTable.ensureSize(this._octetBufferLength);
/* 1132 */       int charactersOffset = this._characterContentChunkTable._arrayIndex;
/* 1133 */       decodeUtf8StringAsCharBuffer(this._characterContentChunkTable._array, charactersOffset);
/* 1134 */       this._characterContentChunkTable.add(this._charBufferLength);
/*      */       try {
/* 1136 */         this._contentHandler.characters(this._characterContentChunkTable._array, charactersOffset, this._charBufferLength);
/*      */       } catch (SAXException e) {
/* 1138 */         throw new FastInfosetException("processCII", e);
/*      */       }
/*      */     } else {
/* 1141 */       decodeUtf8StringAsCharBuffer();
/*      */       try {
/* 1143 */         this._contentHandler.characters(this._charBuffer, 0, this._charBufferLength);
/*      */       } catch (SAXException e) {
/* 1145 */         throw new FastInfosetException("processCII", e);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void processEIIWithNamespaces() throws FastInfosetException, IOException {
/* 1151 */     boolean hasAttributes = (this._b & 0x40) > 0;
/*      */ 
/* 1153 */     this._clearAttributes = (this._namespacePrefixesFeature);
/*      */ 
/* 1155 */     if (++this._prefixTable._declarationId == 2147483647) {
/* 1156 */       this._prefixTable.clearDeclarationIds();
/*      */     }
/*      */ 
/* 1159 */     String prefix = ""; String namespaceName = "";
/* 1160 */     int start = this._namespacePrefixesIndex;
/* 1161 */     int b = read();
/* 1162 */     while ((b & 0xFC) == 204) {
/* 1163 */       if (this._namespacePrefixesIndex == this._namespacePrefixes.length) {
/* 1164 */         int[] namespaceAIIs = new int[this._namespacePrefixesIndex * 3 / 2 + 1];
/* 1165 */         System.arraycopy(this._namespacePrefixes, 0, namespaceAIIs, 0, this._namespacePrefixesIndex);
/* 1166 */         this._namespacePrefixes = namespaceAIIs;
/*      */       }
/*      */ 
/* 1169 */       switch (b & 0x3)
/*      */       {
/*      */       case 0:
/* 1173 */         prefix = namespaceName = "";
/* 1174 */         this._namespaceNameIndex = (this._prefixIndex = this._namespacePrefixes[(this._namespacePrefixesIndex++)] = -1);
/* 1175 */         break;
/*      */       case 1:
/* 1179 */         prefix = "";
/* 1180 */         namespaceName = decodeIdentifyingNonEmptyStringOnFirstBitAsNamespaceName(false);
/*      */ 
/* 1182 */         this._prefixIndex = (this._namespacePrefixes[(this._namespacePrefixesIndex++)] = -1);
/* 1183 */         break;
/*      */       case 2:
/* 1187 */         prefix = decodeIdentifyingNonEmptyStringOnFirstBitAsPrefix(false);
/* 1188 */         namespaceName = "";
/*      */ 
/* 1190 */         this._namespaceNameIndex = -1;
/* 1191 */         this._namespacePrefixes[(this._namespacePrefixesIndex++)] = this._prefixIndex;
/* 1192 */         break;
/*      */       case 3:
/* 1196 */         prefix = decodeIdentifyingNonEmptyStringOnFirstBitAsPrefix(true);
/* 1197 */         namespaceName = decodeIdentifyingNonEmptyStringOnFirstBitAsNamespaceName(true);
/*      */ 
/* 1199 */         this._namespacePrefixes[(this._namespacePrefixesIndex++)] = this._prefixIndex;
/*      */       }
/*      */ 
/* 1203 */       this._prefixTable.pushScope(this._prefixIndex, this._namespaceNameIndex);
/*      */ 
/* 1205 */       if (this._namespacePrefixesFeature)
/*      */       {
/* 1207 */         if (prefix != "") {
/* 1208 */           this._attributes.addAttribute(new QualifiedName("xmlns", "http://www.w3.org/2000/xmlns/", prefix), namespaceName);
/*      */         }
/*      */         else
/*      */         {
/* 1214 */           this._attributes.addAttribute(EncodingConstants.DEFAULT_NAMESPACE_DECLARATION, namespaceName);
/*      */         }
/*      */       }
/*      */ 
/*      */       try
/*      */       {
/* 1220 */         this._contentHandler.startPrefixMapping(prefix, namespaceName);
/*      */       } catch (SAXException e) {
/* 1222 */         throw new IOException("processStartNamespaceAII");
/*      */       }
/*      */ 
/* 1225 */       b = read();
/*      */     }
/* 1227 */     if (b != 240) {
/* 1228 */       throw new IOException(CommonResourceBundle.getInstance().getString("message.EIInamespaceNameNotTerminatedCorrectly"));
/*      */     }
/* 1230 */     int end = this._namespacePrefixesIndex;
/*      */ 
/* 1232 */     this._b = read();
/* 1233 */     switch (DecoderStateTables.EII(this._b)) {
/*      */     case 0:
/* 1235 */       processEII(this._elementNameTable._array[this._b], hasAttributes);
/* 1236 */       break;
/*      */     case 2:
/* 1238 */       processEII(decodeEIIIndexMedium(), hasAttributes);
/* 1239 */       break;
/*      */     case 3:
/* 1241 */       processEII(decodeEIIIndexLarge(), hasAttributes);
/* 1242 */       break;
/*      */     case 5:
/* 1245 */       QualifiedName qn = decodeLiteralQualifiedName(this._b & 0x3, this._elementNameTable.getNext());
/*      */ 
/* 1248 */       this._elementNameTable.add(qn);
/* 1249 */       processEII(qn, hasAttributes);
/* 1250 */       break;
/*      */     case 1:
/*      */     case 4:
/*      */     default:
/* 1253 */       throw new IOException(CommonResourceBundle.getInstance().getString("message.IllegalStateDecodingEIIAfterAIIs"));
/*      */     }
/*      */     try
/*      */     {
/* 1257 */       for (int i = end - 1; i >= start; i--) {
/* 1258 */         int prefixIndex = this._namespacePrefixes[i];
/* 1259 */         this._prefixTable.popScope(prefixIndex);
/* 1260 */         prefix = prefixIndex == -1 ? "" : prefixIndex > 0 ? this._prefixTable.get(prefixIndex - 1) : "xml";
/*      */ 
/* 1262 */         this._contentHandler.endPrefixMapping(prefix);
/*      */       }
/* 1264 */       this._namespacePrefixesIndex = start;
/*      */     } catch (SAXException e) {
/* 1266 */       throw new IOException("processStartNamespaceAII");
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void processAIIs()
/*      */     throws FastInfosetException, IOException
/*      */   {
/* 1275 */     this._clearAttributes = true;
/*      */ 
/* 1277 */     if (++this._duplicateAttributeVerifier._currentIteration == 2147483647) {
/* 1278 */       this._duplicateAttributeVerifier.clear();
/*      */     }
/*      */ 
/*      */     do
/*      */     {
/* 1283 */       int b = read();
/*      */       QualifiedName name;
/* 1284 */       switch (DecoderStateTables.AII(b)) {
/*      */       case 0:
/* 1286 */         name = this._attributeNameTable._array[b];
/* 1287 */         break;
/*      */       case 1:
/* 1290 */         int i = ((b & 0x1F) << 8 | read()) + 64;
/*      */ 
/* 1292 */         name = this._attributeNameTable._array[i];
/* 1293 */         break;
/*      */       case 2:
/* 1297 */         int i = ((b & 0xF) << 16 | read() << 8 | read()) + 8256;
/*      */ 
/* 1299 */         name = this._attributeNameTable._array[i];
/* 1300 */         break;
/*      */       case 3:
/* 1303 */         name = decodeLiteralQualifiedName(b & 0x3, this._attributeNameTable.getNext());
/*      */ 
/* 1306 */         name.createAttributeValues(256);
/* 1307 */         this._attributeNameTable.add(name);
/* 1308 */         break;
/*      */       case 5:
/* 1310 */         this._doubleTerminate = true;
/*      */       case 4:
/* 1312 */         this._terminate = true;
/*      */ 
/* 1314 */         break;
/*      */       default:
/* 1316 */         throw new IOException(CommonResourceBundle.getInstance().getString("message.decodingAIIs"));
/*      */       }
/*      */ 
/* 1319 */       if ((name.prefixIndex > 0) && (this._prefixTable._currentInScope[name.prefixIndex] != name.namespaceNameIndex)) {
/* 1320 */         throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.AIIqNameNotInScope"));
/*      */       }
/*      */ 
/* 1323 */       this._duplicateAttributeVerifier.checkForDuplicateAttribute(name.attributeHash, name.attributeId);
/*      */ 
/* 1327 */       b = read();
/*      */       String value;
/* 1328 */       switch (DecoderStateTables.NISTRING(b)) {
/*      */       case 0:
/* 1330 */         this._octetBufferLength = ((b & 0x7) + 1);
/* 1331 */         value = decodeUtf8StringAsString();
/* 1332 */         if ((b & 0x40) > 0) {
/* 1333 */           this._attributeValueTable.add(value);
/*      */         }
/*      */ 
/* 1336 */         this._attributes.addAttribute(name, value);
/* 1337 */         break;
/*      */       case 1:
/* 1339 */         this._octetBufferLength = (read() + 9);
/* 1340 */         value = decodeUtf8StringAsString();
/* 1341 */         if ((b & 0x40) > 0) {
/* 1342 */           this._attributeValueTable.add(value);
/*      */         }
/*      */ 
/* 1345 */         this._attributes.addAttribute(name, value);
/* 1346 */         break;
/*      */       case 2:
/* 1348 */         this._octetBufferLength = ((read() << 24 | read() << 16 | read() << 8 | read()) + 265);
/*      */ 
/* 1353 */         value = decodeUtf8StringAsString();
/* 1354 */         if ((b & 0x40) > 0) {
/* 1355 */           this._attributeValueTable.add(value);
/*      */         }
/*      */ 
/* 1358 */         this._attributes.addAttribute(name, value);
/* 1359 */         break;
/*      */       case 3:
/* 1361 */         this._octetBufferLength = ((b & 0x7) + 1);
/* 1362 */         value = decodeUtf16StringAsString();
/* 1363 */         if ((b & 0x40) > 0) {
/* 1364 */           this._attributeValueTable.add(value);
/*      */         }
/*      */ 
/* 1367 */         this._attributes.addAttribute(name, value);
/* 1368 */         break;
/*      */       case 4:
/* 1370 */         this._octetBufferLength = (read() + 9);
/* 1371 */         value = decodeUtf16StringAsString();
/* 1372 */         if ((b & 0x40) > 0) {
/* 1373 */           this._attributeValueTable.add(value);
/*      */         }
/*      */ 
/* 1376 */         this._attributes.addAttribute(name, value);
/* 1377 */         break;
/*      */       case 5:
/* 1379 */         this._octetBufferLength = ((read() << 24 | read() << 16 | read() << 8 | read()) + 265);
/*      */ 
/* 1384 */         value = decodeUtf16StringAsString();
/* 1385 */         if ((b & 0x40) > 0) {
/* 1386 */           this._attributeValueTable.add(value);
/*      */         }
/*      */ 
/* 1389 */         this._attributes.addAttribute(name, value);
/* 1390 */         break;
/*      */       case 6:
/* 1393 */         boolean addToTable = (b & 0x40) > 0;
/*      */ 
/* 1395 */         this._identifier = ((b & 0xF) << 4);
/* 1396 */         b = read();
/* 1397 */         this._identifier |= (b & 0xF0) >> 4;
/*      */ 
/* 1399 */         decodeOctetsOnFifthBitOfNonIdentifyingStringOnFirstBit(b);
/*      */ 
/* 1401 */         value = decodeRestrictedAlphabetAsString();
/* 1402 */         if (addToTable) {
/* 1403 */           this._attributeValueTable.add(value);
/*      */         }
/*      */ 
/* 1406 */         this._attributes.addAttribute(name, value);
/* 1407 */         break;
/*      */       case 7:
/* 1411 */         boolean addToTable = (b & 0x40) > 0;
/*      */ 
/* 1413 */         this._identifier = ((b & 0xF) << 4);
/* 1414 */         b = read();
/* 1415 */         this._identifier |= (b & 0xF0) >> 4;
/*      */ 
/* 1417 */         decodeOctetsOnFifthBitOfNonIdentifyingStringOnFirstBit(b);
/*      */ 
/* 1419 */         processAIIEncodingAlgorithm(name, addToTable);
/* 1420 */         break;
/*      */       case 8:
/* 1423 */         this._attributes.addAttribute(name, this._attributeValueTable._array[(b & 0x3F)]);
/*      */ 
/* 1425 */         break;
/*      */       case 9:
/* 1428 */         int index = ((b & 0x1F) << 8 | read()) + 64;
/*      */ 
/* 1431 */         this._attributes.addAttribute(name, this._attributeValueTable._array[index]);
/*      */ 
/* 1433 */         break;
/*      */       case 10:
/* 1437 */         int index = ((b & 0xF) << 16 | read() << 8 | read()) + 8256;
/*      */ 
/* 1440 */         this._attributes.addAttribute(name, this._attributeValueTable._array[index]);
/*      */ 
/* 1442 */         break;
/*      */       case 11:
/* 1445 */         this._attributes.addAttribute(name, "");
/* 1446 */         break;
/*      */       default:
/* 1448 */         throw new IOException(CommonResourceBundle.getInstance().getString("message.decodingAIIValue"));
/*      */       }
/*      */     }
/* 1451 */     while (!this._terminate);
/*      */ 
/* 1454 */     this._duplicateAttributeVerifier._poolCurrent = this._duplicateAttributeVerifier._poolHead;
/*      */ 
/* 1456 */     this._terminate = this._doubleTerminate;
/* 1457 */     this._doubleTerminate = false;
/*      */   }
/*      */ 
/*      */   protected final void processCommentII() throws FastInfosetException, IOException {
/* 1461 */     switch (decodeNonIdentifyingStringOnFirstBit()) {
/*      */     case 0:
/* 1463 */       if (this._addToTable) {
/* 1464 */         this._v.otherString.add(new CharArray(this._charBuffer, 0, this._charBufferLength, true));
/*      */       }
/*      */       try
/*      */       {
/* 1468 */         this._lexicalHandler.comment(this._charBuffer, 0, this._charBufferLength);
/*      */       } catch (SAXException e) {
/* 1470 */         throw new FastInfosetException("processCommentII", e);
/*      */       }
/*      */ 
/*      */     case 2:
/* 1474 */       throw new IOException(CommonResourceBundle.getInstance().getString("message.commentIIAlgorithmNotSupported"));
/*      */     case 1:
/* 1476 */       CharArray ca = this._v.otherString.get(this._integer);
/*      */       try
/*      */       {
/* 1479 */         this._lexicalHandler.comment(ca.ch, ca.start, ca.length);
/*      */       } catch (SAXException e) {
/* 1481 */         throw new FastInfosetException("processCommentII", e);
/*      */       }
/*      */     case 3:
/*      */       try
/*      */       {
/* 1486 */         this._lexicalHandler.comment(this._charBuffer, 0, 0);
/*      */       } catch (SAXException e) {
/* 1488 */         throw new FastInfosetException("processCommentII", e);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void processProcessingII() throws FastInfosetException, IOException
/*      */   {
/* 1495 */     String target = decodeIdentifyingNonEmptyStringOnFirstBit(this._v.otherNCName);
/*      */ 
/* 1497 */     switch (decodeNonIdentifyingStringOnFirstBit()) {
/*      */     case 0:
/* 1499 */       String data = new String(this._charBuffer, 0, this._charBufferLength);
/* 1500 */       if (this._addToTable)
/* 1501 */         this._v.otherString.add(new CharArrayString(data));
/*      */       try
/*      */       {
/* 1504 */         this._contentHandler.processingInstruction(target, data);
/*      */       } catch (SAXException e) {
/* 1506 */         throw new FastInfosetException("processProcessingII", e);
/*      */       }
/*      */ 
/*      */     case 2:
/* 1510 */       throw new IOException(CommonResourceBundle.getInstance().getString("message.processingIIWithEncodingAlgorithm"));
/*      */     case 1:
/*      */       try {
/* 1513 */         this._contentHandler.processingInstruction(target, this._v.otherString.get(this._integer).toString());
/*      */       } catch (SAXException e) {
/* 1515 */         throw new FastInfosetException("processProcessingII", e);
/*      */       }
/*      */     case 3:
/*      */       try
/*      */       {
/* 1520 */         this._contentHandler.processingInstruction(target, "");
/*      */       } catch (SAXException e) {
/* 1522 */         throw new FastInfosetException("processProcessingII", e);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void processCIIEncodingAlgorithm(boolean addToTable) throws FastInfosetException, IOException
/*      */   {
/* 1529 */     if (this._identifier < 9) {
/* 1530 */       if (this._primitiveHandler != null) {
/* 1531 */         processCIIBuiltInEncodingAlgorithmAsPrimitive();
/* 1532 */       } else if (this._algorithmHandler != null) {
/* 1533 */         Object array = processBuiltInEncodingAlgorithmAsObject();
/*      */         try
/*      */         {
/* 1536 */           this._algorithmHandler.object(null, this._identifier, array);
/*      */         } catch (SAXException e) {
/* 1538 */           throw new FastInfosetException(e);
/*      */         }
/*      */       } else {
/* 1541 */         StringBuffer buffer = new StringBuffer();
/* 1542 */         processBuiltInEncodingAlgorithmAsCharacters(buffer);
/*      */         try
/*      */         {
/* 1545 */           this._contentHandler.characters(buffer.toString().toCharArray(), 0, buffer.length());
/*      */         } catch (SAXException e) {
/* 1547 */           throw new FastInfosetException(e);
/*      */         }
/*      */       }
/*      */ 
/* 1551 */       if (addToTable) {
/* 1552 */         StringBuffer buffer = new StringBuffer();
/* 1553 */         processBuiltInEncodingAlgorithmAsCharacters(buffer);
/* 1554 */         this._characterContentChunkTable.add(buffer.toString().toCharArray(), buffer.length());
/*      */       }
/* 1556 */     } else if (this._identifier == 9)
/*      */     {
/* 1558 */       this._octetBufferOffset -= this._octetBufferLength;
/* 1559 */       decodeUtf8StringIntoCharBuffer();
/*      */       try
/*      */       {
/* 1562 */         this._lexicalHandler.startCDATA();
/* 1563 */         this._contentHandler.characters(this._charBuffer, 0, this._charBufferLength);
/* 1564 */         this._lexicalHandler.endCDATA();
/*      */       } catch (SAXException e) {
/* 1566 */         throw new FastInfosetException(e);
/*      */       }
/*      */ 
/* 1569 */       if (addToTable)
/* 1570 */         this._characterContentChunkTable.add(this._charBuffer, this._charBufferLength);
/*      */     }
/* 1572 */     else if ((this._identifier >= 32) && (this._algorithmHandler != null)) {
/* 1573 */       String URI = this._v.encodingAlgorithm.get(this._identifier - 32);
/* 1574 */       if (URI == null) {
/* 1575 */         throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.URINotPresent", new Object[] { Integer.valueOf(this._identifier) }));
/*      */       }
/*      */ 
/* 1579 */       EncodingAlgorithm ea = (EncodingAlgorithm)this._registeredEncodingAlgorithms.get(URI);
/* 1580 */       if (ea != null) {
/* 1581 */         Object data = ea.decodeFromBytes(this._octetBuffer, this._octetBufferStart, this._octetBufferLength);
/*      */         try {
/* 1583 */           this._algorithmHandler.object(URI, this._identifier, data);
/*      */         } catch (SAXException e) {
/* 1585 */           throw new FastInfosetException(e);
/*      */         }
/*      */       } else {
/*      */         try {
/* 1589 */           this._algorithmHandler.octets(URI, this._identifier, this._octetBuffer, this._octetBufferStart, this._octetBufferLength);
/*      */         } catch (SAXException e) {
/* 1591 */           throw new FastInfosetException(e);
/*      */         }
/*      */       }
/* 1594 */       if (addToTable)
/* 1595 */         throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.addToTableNotSupported"));
/*      */     } else {
/* 1597 */       if (this._identifier >= 32)
/*      */       {
/* 1599 */         throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.algorithmDataCannotBeReported"));
/*      */       }
/*      */ 
/* 1605 */       throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.identifiers10to31Reserved"));
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void processCIIBuiltInEncodingAlgorithmAsPrimitive()
/*      */     throws FastInfosetException, IOException
/*      */   {
/*      */     try
/*      */     {
/*      */       int length;
/* 1612 */       switch (this._identifier) {
/*      */       case 0:
/*      */       case 1:
/* 1615 */         this._primitiveHandler.bytes(this._octetBuffer, this._octetBufferStart, this._octetBufferLength);
/* 1616 */         break;
/*      */       case 2:
/* 1618 */         length = BuiltInEncodingAlgorithmFactory.shortEncodingAlgorithm.getPrimtiveLengthFromOctetLength(this._octetBufferLength);
/*      */ 
/* 1620 */         if (length > this.builtInAlgorithmState.shortArray.length) {
/* 1621 */           short[] array = new short[length * 3 / 2 + 1];
/* 1622 */           System.arraycopy(this.builtInAlgorithmState.shortArray, 0, array, 0, this.builtInAlgorithmState.shortArray.length);
/*      */ 
/* 1624 */           this.builtInAlgorithmState.shortArray = array;
/*      */         }
/*      */ 
/* 1627 */         BuiltInEncodingAlgorithmFactory.shortEncodingAlgorithm.decodeFromBytesToShortArray(this.builtInAlgorithmState.shortArray, 0, this._octetBuffer, this._octetBufferStart, this._octetBufferLength);
/*      */ 
/* 1630 */         this._primitiveHandler.shorts(this.builtInAlgorithmState.shortArray, 0, length);
/* 1631 */         break;
/*      */       case 3:
/* 1633 */         length = BuiltInEncodingAlgorithmFactory.intEncodingAlgorithm.getPrimtiveLengthFromOctetLength(this._octetBufferLength);
/*      */ 
/* 1635 */         if (length > this.builtInAlgorithmState.intArray.length) {
/* 1636 */           int[] array = new int[length * 3 / 2 + 1];
/* 1637 */           System.arraycopy(this.builtInAlgorithmState.intArray, 0, array, 0, this.builtInAlgorithmState.intArray.length);
/*      */ 
/* 1639 */           this.builtInAlgorithmState.intArray = array;
/*      */         }
/*      */ 
/* 1642 */         BuiltInEncodingAlgorithmFactory.intEncodingAlgorithm.decodeFromBytesToIntArray(this.builtInAlgorithmState.intArray, 0, this._octetBuffer, this._octetBufferStart, this._octetBufferLength);
/*      */ 
/* 1645 */         this._primitiveHandler.ints(this.builtInAlgorithmState.intArray, 0, length);
/* 1646 */         break;
/*      */       case 4:
/* 1648 */         length = BuiltInEncodingAlgorithmFactory.longEncodingAlgorithm.getPrimtiveLengthFromOctetLength(this._octetBufferLength);
/*      */ 
/* 1650 */         if (length > this.builtInAlgorithmState.longArray.length) {
/* 1651 */           long[] array = new long[length * 3 / 2 + 1];
/* 1652 */           System.arraycopy(this.builtInAlgorithmState.longArray, 0, array, 0, this.builtInAlgorithmState.longArray.length);
/*      */ 
/* 1654 */           this.builtInAlgorithmState.longArray = array;
/*      */         }
/*      */ 
/* 1657 */         BuiltInEncodingAlgorithmFactory.longEncodingAlgorithm.decodeFromBytesToLongArray(this.builtInAlgorithmState.longArray, 0, this._octetBuffer, this._octetBufferStart, this._octetBufferLength);
/*      */ 
/* 1660 */         this._primitiveHandler.longs(this.builtInAlgorithmState.longArray, 0, length);
/* 1661 */         break;
/*      */       case 5:
/* 1663 */         length = BuiltInEncodingAlgorithmFactory.booleanEncodingAlgorithm.getPrimtiveLengthFromOctetLength(this._octetBufferLength, this._octetBuffer[this._octetBufferStart] & 0xFF);
/*      */ 
/* 1665 */         if (length > this.builtInAlgorithmState.booleanArray.length) {
/* 1666 */           boolean[] array = new boolean[length * 3 / 2 + 1];
/* 1667 */           System.arraycopy(this.builtInAlgorithmState.booleanArray, 0, array, 0, this.builtInAlgorithmState.booleanArray.length);
/*      */ 
/* 1669 */           this.builtInAlgorithmState.booleanArray = array;
/*      */         }
/*      */ 
/* 1672 */         BuiltInEncodingAlgorithmFactory.booleanEncodingAlgorithm.decodeFromBytesToBooleanArray(this.builtInAlgorithmState.booleanArray, 0, length, this._octetBuffer, this._octetBufferStart, this._octetBufferLength);
/*      */ 
/* 1676 */         this._primitiveHandler.booleans(this.builtInAlgorithmState.booleanArray, 0, length);
/* 1677 */         break;
/*      */       case 6:
/* 1679 */         length = BuiltInEncodingAlgorithmFactory.floatEncodingAlgorithm.getPrimtiveLengthFromOctetLength(this._octetBufferLength);
/*      */ 
/* 1681 */         if (length > this.builtInAlgorithmState.floatArray.length) {
/* 1682 */           float[] array = new float[length * 3 / 2 + 1];
/* 1683 */           System.arraycopy(this.builtInAlgorithmState.floatArray, 0, array, 0, this.builtInAlgorithmState.floatArray.length);
/*      */ 
/* 1685 */           this.builtInAlgorithmState.floatArray = array;
/*      */         }
/*      */ 
/* 1688 */         BuiltInEncodingAlgorithmFactory.floatEncodingAlgorithm.decodeFromBytesToFloatArray(this.builtInAlgorithmState.floatArray, 0, this._octetBuffer, this._octetBufferStart, this._octetBufferLength);
/*      */ 
/* 1691 */         this._primitiveHandler.floats(this.builtInAlgorithmState.floatArray, 0, length);
/* 1692 */         break;
/*      */       case 7:
/* 1694 */         length = BuiltInEncodingAlgorithmFactory.doubleEncodingAlgorithm.getPrimtiveLengthFromOctetLength(this._octetBufferLength);
/*      */ 
/* 1696 */         if (length > this.builtInAlgorithmState.doubleArray.length) {
/* 1697 */           double[] array = new double[length * 3 / 2 + 1];
/* 1698 */           System.arraycopy(this.builtInAlgorithmState.doubleArray, 0, array, 0, this.builtInAlgorithmState.doubleArray.length);
/*      */ 
/* 1700 */           this.builtInAlgorithmState.doubleArray = array;
/*      */         }
/*      */ 
/* 1703 */         BuiltInEncodingAlgorithmFactory.doubleEncodingAlgorithm.decodeFromBytesToDoubleArray(this.builtInAlgorithmState.doubleArray, 0, this._octetBuffer, this._octetBufferStart, this._octetBufferLength);
/*      */ 
/* 1706 */         this._primitiveHandler.doubles(this.builtInAlgorithmState.doubleArray, 0, length);
/* 1707 */         break;
/*      */       case 8:
/* 1709 */         length = BuiltInEncodingAlgorithmFactory.uuidEncodingAlgorithm.getPrimtiveLengthFromOctetLength(this._octetBufferLength);
/*      */ 
/* 1711 */         if (length > this.builtInAlgorithmState.longArray.length) {
/* 1712 */           long[] array = new long[length * 3 / 2 + 1];
/* 1713 */           System.arraycopy(this.builtInAlgorithmState.longArray, 0, array, 0, this.builtInAlgorithmState.longArray.length);
/*      */ 
/* 1715 */           this.builtInAlgorithmState.longArray = array;
/*      */         }
/*      */ 
/* 1718 */         BuiltInEncodingAlgorithmFactory.uuidEncodingAlgorithm.decodeFromBytesToLongArray(this.builtInAlgorithmState.longArray, 0, this._octetBuffer, this._octetBufferStart, this._octetBufferLength);
/*      */ 
/* 1721 */         this._primitiveHandler.uuids(this.builtInAlgorithmState.longArray, 0, length);
/* 1722 */         break;
/*      */       case 9:
/* 1724 */         throw new UnsupportedOperationException("CDATA");
/*      */       default:
/* 1726 */         throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.unsupportedAlgorithm", new Object[] { Integer.valueOf(this._identifier) }));
/*      */       }
/*      */     }
/*      */     catch (SAXException e) {
/* 1730 */       throw new FastInfosetException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void processAIIEncodingAlgorithm(QualifiedName name, boolean addToTable) throws FastInfosetException, IOException
/*      */   {
/* 1736 */     if (this._identifier < 9) {
/* 1737 */       if ((this._primitiveHandler != null) || (this._algorithmHandler != null)) {
/* 1738 */         Object data = processBuiltInEncodingAlgorithmAsObject();
/* 1739 */         this._attributes.addAttributeWithAlgorithmData(name, null, this._identifier, data);
/*      */       } else {
/* 1741 */         StringBuffer buffer = new StringBuffer();
/* 1742 */         processBuiltInEncodingAlgorithmAsCharacters(buffer);
/* 1743 */         this._attributes.addAttribute(name, buffer.toString());
/*      */       }
/* 1745 */     } else if ((this._identifier >= 32) && (this._algorithmHandler != null)) {
/* 1746 */       String URI = this._v.encodingAlgorithm.get(this._identifier - 32);
/* 1747 */       if (URI == null) {
/* 1748 */         throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.URINotPresent", new Object[] { Integer.valueOf(this._identifier) }));
/*      */       }
/*      */ 
/* 1752 */       EncodingAlgorithm ea = (EncodingAlgorithm)this._registeredEncodingAlgorithms.get(URI);
/* 1753 */       if (ea != null) {
/* 1754 */         Object data = ea.decodeFromBytes(this._octetBuffer, this._octetBufferStart, this._octetBufferLength);
/* 1755 */         this._attributes.addAttributeWithAlgorithmData(name, URI, this._identifier, data);
/*      */       } else {
/* 1757 */         byte[] data = new byte[this._octetBufferLength];
/* 1758 */         System.arraycopy(this._octetBuffer, this._octetBufferStart, data, 0, this._octetBufferLength);
/* 1759 */         this._attributes.addAttributeWithAlgorithmData(name, URI, this._identifier, data);
/*      */       }
/*      */     } else { if (this._identifier >= 32)
/*      */       {
/* 1763 */         throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.algorithmDataCannotBeReported"));
/*      */       }
/* 1765 */       if (this._identifier == 9) {
/* 1766 */         throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.CDATAAlgorithmNotSupported"));
/*      */       }
/*      */ 
/* 1771 */       throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.identifiers10to31Reserved"));
/*      */     }
/*      */ 
/* 1774 */     if (addToTable)
/* 1775 */       this._attributeValueTable.add(this._attributes.getValue(this._attributes.getIndex(name.qName)));
/*      */   }
/*      */ 
/*      */   protected final void processBuiltInEncodingAlgorithmAsCharacters(StringBuffer buffer)
/*      */     throws FastInfosetException, IOException
/*      */   {
/* 1781 */     Object array = BuiltInEncodingAlgorithmFactory.getAlgorithm(this._identifier).decodeFromBytes(this._octetBuffer, this._octetBufferStart, this._octetBufferLength);
/*      */ 
/* 1784 */     BuiltInEncodingAlgorithmFactory.getAlgorithm(this._identifier).convertToCharacters(array, buffer);
/*      */   }
/*      */ 
/*      */   protected final Object processBuiltInEncodingAlgorithmAsObject() throws FastInfosetException, IOException {
/* 1788 */     return BuiltInEncodingAlgorithmFactory.getAlgorithm(this._identifier).decodeFromBytes(this._octetBuffer, this._octetBufferStart, this._octetBufferLength);
/*      */   }
/*      */ 
/*      */   private static final class DeclHandlerImpl
/*      */     implements DeclHandler
/*      */   {
/*      */     public void elementDecl(String name, String model)
/*      */       throws SAXException
/*      */     {
/*      */     }
/*      */ 
/*      */     public void attributeDecl(String eName, String aName, String type, String mode, String value)
/*      */       throws SAXException
/*      */     {
/*      */     }
/*      */ 
/*      */     public void internalEntityDecl(String name, String value)
/*      */       throws SAXException
/*      */     {
/*      */     }
/*      */ 
/*      */     public void externalEntityDecl(String name, String publicId, String systemId)
/*      */       throws SAXException
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   private static final class LexicalHandlerImpl
/*      */     implements LexicalHandler
/*      */   {
/*      */     public void comment(char[] ch, int start, int end)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void startDTD(String name, String publicId, String systemId)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void endDTD()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void startEntity(String name)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void endEntity(String name)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void startCDATA()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void endCDATA()
/*      */     {
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.sax.SAXDocumentParser
 * JD-Core Version:    0.6.2
 */