/*      */ package com.sun.org.apache.xerces.internal.impl;
/*      */ 
/*      */ import com.sun.org.apache.xerces.internal.util.NamespaceContextWrapper;
/*      */ import com.sun.org.apache.xerces.internal.util.NamespaceSupport;
/*      */ import com.sun.org.apache.xerces.internal.util.SymbolTable;
/*      */ import com.sun.org.apache.xerces.internal.util.XMLAttributesImpl;
/*      */ import com.sun.org.apache.xerces.internal.util.XMLAttributesIteratorImpl;
/*      */ import com.sun.org.apache.xerces.internal.util.XMLChar;
/*      */ import com.sun.org.apache.xerces.internal.util.XMLStringBuffer;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLString;
/*      */ import com.sun.org.apache.xerces.internal.xni.XNIException;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
/*      */ import com.sun.xml.internal.stream.Entity;
/*      */ import com.sun.xml.internal.stream.Entity.ExternalEntity;
/*      */ import com.sun.xml.internal.stream.Entity.InternalEntity;
/*      */ import com.sun.xml.internal.stream.StaxErrorReporter;
/*      */ import com.sun.xml.internal.stream.XMLEntityStorage;
/*      */ import com.sun.xml.internal.stream.dtd.nonvalidating.DTDGrammar;
/*      */ import com.sun.xml.internal.stream.dtd.nonvalidating.XMLNotationDecl;
/*      */ import com.sun.xml.internal.stream.events.EntityDeclarationImpl;
/*      */ import com.sun.xml.internal.stream.events.NotationDeclarationImpl;
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.io.Reader;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.NoSuchElementException;
/*      */ import javax.xml.stream.Location;
/*      */ import javax.xml.stream.XMLStreamException;
/*      */ import javax.xml.stream.XMLStreamReader;
/*      */ 
/*      */ public class XMLStreamReaderImpl
/*      */   implements XMLStreamReader
/*      */ {
/*      */   protected static final String ENTITY_MANAGER = "http://apache.org/xml/properties/internal/entity-manager";
/*      */   protected static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
/*      */   protected static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
/*      */   protected static final String READER_IN_DEFINED_STATE = "http://java.sun.com/xml/stream/properties/reader-in-defined-state";
/*   91 */   private SymbolTable fSymbolTable = new SymbolTable();
/*      */ 
/*   94 */   protected XMLDocumentScannerImpl fScanner = new XMLNSDocumentScannerImpl();
/*      */ 
/*   98 */   protected NamespaceContextWrapper fNamespaceContextWrapper = new NamespaceContextWrapper((NamespaceSupport)this.fScanner.getNamespaceContext());
/*   99 */   protected XMLEntityManager fEntityManager = new XMLEntityManager();
/*  100 */   protected StaxErrorReporter fErrorReporter = new StaxErrorReporter();
/*      */ 
/*  104 */   protected XMLEntityScanner fEntityScanner = null;
/*      */ 
/*  107 */   protected XMLInputSource fInputSource = null;
/*      */ 
/*  109 */   protected PropertyManager fPropertyManager = null;
/*      */   private int fEventType;
/*      */   static final boolean DEBUG = false;
/*  116 */   private boolean fReuse = true;
/*  117 */   private boolean fReaderInDefinedState = true;
/*  118 */   private boolean fBindNamespaces = true;
/*  119 */   private String fDTDDecl = null;
/*  120 */   private String versionStr = null;
/*      */ 
/*      */   public XMLStreamReaderImpl(InputStream inputStream, PropertyManager props)
/*      */     throws XMLStreamException
/*      */   {
/*  128 */     init(props);
/*      */ 
/*  130 */     XMLInputSource inputSource = new XMLInputSource(null, null, null, inputStream, null);
/*      */ 
/*  132 */     setInputSource(inputSource);
/*      */   }
/*      */ 
/*      */   public XMLDocumentScannerImpl getScanner() {
/*  136 */     System.out.println("returning scanner");
/*  137 */     return this.fScanner;
/*      */   }
/*      */ 
/*      */   public XMLStreamReaderImpl(String systemid, PropertyManager props)
/*      */     throws XMLStreamException
/*      */   {
/*  145 */     init(props);
/*      */ 
/*  147 */     XMLInputSource inputSource = new XMLInputSource(null, systemid, null);
/*      */ 
/*  149 */     setInputSource(inputSource);
/*      */   }
/*      */ 
/*      */   public XMLStreamReaderImpl(InputStream inputStream, String encoding, PropertyManager props)
/*      */     throws XMLStreamException
/*      */   {
/*  160 */     init(props);
/*      */ 
/*  162 */     XMLInputSource inputSource = new XMLInputSource(null, null, null, new BufferedInputStream(inputStream), encoding);
/*      */ 
/*  164 */     setInputSource(inputSource);
/*      */   }
/*      */ 
/*      */   public XMLStreamReaderImpl(Reader reader, PropertyManager props)
/*      */     throws XMLStreamException
/*      */   {
/*  173 */     init(props);
/*      */ 
/*  176 */     XMLInputSource inputSource = new XMLInputSource(null, null, null, new BufferedReader(reader), null);
/*      */ 
/*  178 */     setInputSource(inputSource);
/*      */   }
/*      */ 
/*      */   public XMLStreamReaderImpl(XMLInputSource inputSource, PropertyManager props)
/*      */     throws XMLStreamException
/*      */   {
/*  187 */     init(props);
/*      */ 
/*  189 */     setInputSource(inputSource);
/*      */   }
/*      */ 
/*      */   public void setInputSource(XMLInputSource inputSource)
/*      */     throws XMLStreamException
/*      */   {
/*  200 */     this.fReuse = false;
/*      */     try
/*      */     {
/*  204 */       this.fScanner.setInputSource(inputSource);
/*      */ 
/*  206 */       if (this.fReaderInDefinedState) {
/*  207 */         this.fEventType = this.fScanner.next();
/*  208 */         if (this.versionStr == null) {
/*  209 */           this.versionStr = getVersion();
/*      */         }
/*  211 */         if ((this.fEventType == 7) && (this.versionStr != null) && (this.versionStr.equals("1.1")))
/*  212 */           switchToXML11Scanner();
/*      */       }
/*      */     }
/*      */     catch (IOException ex)
/*      */     {
/*  217 */       throw new XMLStreamException(ex);
/*      */     } catch (XNIException ex) {
/*  219 */       throw new XMLStreamException(ex.getMessage(), getLocation(), ex.getException());
/*      */     }
/*      */   }
/*      */ 
/*      */   void init(PropertyManager propertyManager) throws XMLStreamException {
/*  224 */     this.fPropertyManager = propertyManager;
/*      */ 
/*  231 */     propertyManager.setProperty("http://apache.org/xml/properties/internal/symbol-table", this.fSymbolTable);
/*      */ 
/*  233 */     propertyManager.setProperty("http://apache.org/xml/properties/internal/error-reporter", this.fErrorReporter);
/*      */ 
/*  235 */     propertyManager.setProperty("http://apache.org/xml/properties/internal/entity-manager", this.fEntityManager);
/*      */ 
/*  237 */     reset();
/*      */   }
/*      */ 
/*      */   public boolean canReuse()
/*      */   {
/*  251 */     return this.fReuse;
/*      */   }
/*      */ 
/*      */   public void reset()
/*      */   {
/*  258 */     this.fReuse = true;
/*  259 */     this.fEventType = 0;
/*      */ 
/*  261 */     this.fEntityManager.reset(this.fPropertyManager);
/*      */ 
/*  263 */     this.fScanner.reset(this.fPropertyManager);
/*      */ 
/*  266 */     this.fDTDDecl = null;
/*  267 */     this.fEntityScanner = this.fEntityManager.getEntityScanner();
/*      */ 
/*  270 */     this.fReaderInDefinedState = ((Boolean)this.fPropertyManager.getProperty("http://java.sun.com/xml/stream/properties/reader-in-defined-state")).booleanValue();
/*  271 */     this.fBindNamespaces = ((Boolean)this.fPropertyManager.getProperty("javax.xml.stream.isNamespaceAware")).booleanValue();
/*  272 */     this.versionStr = null;
/*      */   }
/*      */ 
/*      */   public void close()
/*      */     throws XMLStreamException
/*      */   {
/*  282 */     this.fReuse = true;
/*      */   }
/*      */ 
/*      */   public String getCharacterEncodingScheme()
/*      */   {
/*  290 */     return this.fScanner.getCharacterEncodingScheme();
/*      */   }
/*      */ 
/*      */   public int getColumnNumber()
/*      */   {
/*  299 */     return this.fEntityScanner.getColumnNumber();
/*      */   }
/*      */ 
/*      */   public String getEncoding()
/*      */   {
/*  306 */     return this.fEntityScanner.getEncoding();
/*      */   }
/*      */ 
/*      */   public int getEventType()
/*      */   {
/*  313 */     return this.fEventType;
/*      */   }
/*      */ 
/*      */   public int getLineNumber()
/*      */   {
/*  320 */     return this.fEntityScanner.getLineNumber();
/*      */   }
/*      */ 
/*      */   public String getLocalName() {
/*  324 */     if ((this.fEventType == 1) || (this.fEventType == 2))
/*      */     {
/*  326 */       return this.fScanner.getElementQName().localpart;
/*      */     }
/*  328 */     if (this.fEventType == 9) {
/*  329 */       return this.fScanner.getEntityName();
/*      */     }
/*  331 */     throw new IllegalStateException("Method getLocalName() cannot be called for " + getEventTypeString(this.fEventType) + " event.");
/*      */   }
/*      */ 
/*      */   public String getNamespaceURI()
/*      */   {
/*  340 */     if ((this.fEventType == 1) || (this.fEventType == 2)) {
/*  341 */       return this.fScanner.getElementQName().uri;
/*      */     }
/*  343 */     return null;
/*      */   }
/*      */ 
/*      */   public String getPIData()
/*      */   {
/*  351 */     if (this.fEventType == 3) {
/*  352 */       return this.fScanner.getPIData().toString();
/*      */     }
/*  354 */     throw new IllegalStateException("Current state of the parser is " + getEventTypeString(this.fEventType) + " But Expected state is " + 3);
/*      */   }
/*      */ 
/*      */   public String getPITarget()
/*      */   {
/*  363 */     if (this.fEventType == 3) {
/*  364 */       return this.fScanner.getPITarget();
/*      */     }
/*  366 */     throw new IllegalStateException("Current state of the parser is " + getEventTypeString(this.fEventType) + " But Expected state is " + 3);
/*      */   }
/*      */ 
/*      */   public String getPrefix()
/*      */   {
/*  378 */     if ((this.fEventType == 1) || (this.fEventType == 2)) {
/*  379 */       String prefix = this.fScanner.getElementQName().prefix;
/*  380 */       return prefix == null ? "" : prefix;
/*      */     }
/*  382 */     return null;
/*      */   }
/*      */ 
/*      */   public char[] getTextCharacters()
/*      */   {
/*  391 */     if ((this.fEventType == 4) || (this.fEventType == 5) || (this.fEventType == 12) || (this.fEventType == 6))
/*      */     {
/*  393 */       return this.fScanner.getCharacterData().ch;
/*      */     }
/*  395 */     throw new IllegalStateException("Current state = " + getEventTypeString(this.fEventType) + " is not among the states " + getEventTypeString(4) + " , " + getEventTypeString(5) + " , " + getEventTypeString(12) + " , " + getEventTypeString(6) + " valid for getTextCharacters() ");
/*      */   }
/*      */ 
/*      */   public int getTextLength()
/*      */   {
/*  406 */     if ((this.fEventType == 4) || (this.fEventType == 5) || (this.fEventType == 12) || (this.fEventType == 6))
/*      */     {
/*  408 */       return this.fScanner.getCharacterData().length;
/*      */     }
/*  410 */     throw new IllegalStateException("Current state = " + getEventTypeString(this.fEventType) + " is not among the states " + getEventTypeString(4) + " , " + getEventTypeString(5) + " , " + getEventTypeString(12) + " , " + getEventTypeString(6) + " valid for getTextLength() ");
/*      */   }
/*      */ 
/*      */   public int getTextStart()
/*      */   {
/*  422 */     if ((this.fEventType == 4) || (this.fEventType == 5) || (this.fEventType == 12) || (this.fEventType == 6)) {
/*  423 */       return this.fScanner.getCharacterData().offset;
/*      */     }
/*  425 */     throw new IllegalStateException("Current state = " + getEventTypeString(this.fEventType) + " is not among the states " + getEventTypeString(4) + " , " + getEventTypeString(5) + " , " + getEventTypeString(12) + " , " + getEventTypeString(6) + " valid for getTextStart() ");
/*      */   }
/*      */ 
/*      */   public String getValue()
/*      */   {
/*  436 */     if (this.fEventType == 3)
/*  437 */       return this.fScanner.getPIData().toString();
/*  438 */     if (this.fEventType == 5)
/*  439 */       return this.fScanner.getComment();
/*  440 */     if ((this.fEventType == 1) || (this.fEventType == 2))
/*  441 */       return this.fScanner.getElementQName().localpart;
/*  442 */     if (this.fEventType == 4) {
/*  443 */       return this.fScanner.getCharacterData().toString();
/*      */     }
/*  445 */     return null;
/*      */   }
/*      */ 
/*      */   public String getVersion()
/*      */   {
/*  454 */     String version = this.fEntityScanner.getXMLVersion();
/*      */ 
/*  456 */     return ("1.0".equals(version)) && (!this.fEntityScanner.xmlVersionSetExplicitly) ? null : version;
/*      */   }
/*      */ 
/*      */   public boolean hasAttributes()
/*      */   {
/*  463 */     return this.fScanner.getAttributeIterator().getLength() > 0;
/*      */   }
/*      */ 
/*      */   public boolean hasName()
/*      */   {
/*  468 */     if ((this.fEventType == 1) || (this.fEventType == 2)) {
/*  469 */       return true;
/*      */     }
/*  471 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean hasNext()
/*      */     throws XMLStreamException
/*      */   {
/*  481 */     if (this.fEventType == -1) return false;
/*      */ 
/*  484 */     return this.fEventType != 8;
/*      */   }
/*      */ 
/*      */   public boolean hasValue()
/*      */   {
/*  491 */     if ((this.fEventType == 1) || (this.fEventType == 2) || (this.fEventType == 9) || (this.fEventType == 3) || (this.fEventType == 5) || (this.fEventType == 4))
/*      */     {
/*  494 */       return true;
/*      */     }
/*  496 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean isEndElement()
/*      */   {
/*  505 */     return this.fEventType == 2;
/*      */   }
/*      */ 
/*      */   public boolean isStandalone()
/*      */   {
/*  512 */     return this.fScanner.isStandAlone();
/*      */   }
/*      */ 
/*      */   public boolean isStartElement()
/*      */   {
/*  519 */     return this.fEventType == 1;
/*      */   }
/*      */ 
/*      */   public boolean isWhiteSpace()
/*      */   {
/*  529 */     if ((isCharacters()) || (this.fEventType == 12)) {
/*  530 */       char[] ch = getTextCharacters();
/*  531 */       int start = getTextStart();
/*  532 */       int end = start + getTextLength();
/*  533 */       for (int i = start; i < end; i++) {
/*  534 */         if (!XMLChar.isSpace(ch[i])) {
/*  535 */           return false;
/*      */         }
/*      */       }
/*  538 */       return true;
/*      */     }
/*  540 */     return false;
/*      */   }
/*      */ 
/*      */   public int next()
/*      */     throws XMLStreamException
/*      */   {
/*  550 */     if (!hasNext()) {
/*  551 */       if (this.fEventType != -1) {
/*  552 */         throw new NoSuchElementException("END_DOCUMENT reached: no more elements on the stream.");
/*      */       }
/*  554 */       throw new XMLStreamException("Error processing input source. The input stream is not complete.");
/*      */     }
/*      */     try
/*      */     {
/*  558 */       this.fEventType = this.fScanner.next();
/*      */ 
/*  560 */       if (this.versionStr == null) {
/*  561 */         this.versionStr = getVersion();
/*      */       }
/*      */ 
/*  564 */       if ((this.fEventType == 7) && (this.versionStr != null) && (this.versionStr.equals("1.1")))
/*      */       {
/*  567 */         switchToXML11Scanner();
/*      */       }
/*      */ 
/*  570 */       return this.fEventType;
/*      */     }
/*      */     catch (IOException ex)
/*      */     {
/*  574 */       if (this.fScanner.fScannerState == 46) {
/*  575 */         Boolean isValidating = (Boolean)this.fPropertyManager.getProperty("javax.xml.stream.isValidating");
/*      */ 
/*  577 */         if ((isValidating != null) && (!isValidating.booleanValue()))
/*      */         {
/*  580 */           this.fEventType = 11;
/*  581 */           this.fScanner.setScannerState(43);
/*  582 */           this.fScanner.setDriver(this.fScanner.fPrologDriver);
/*  583 */           if ((this.fDTDDecl == null) || (this.fDTDDecl.length() == 0))
/*      */           {
/*  585 */             this.fDTDDecl = "<!-- Exception scanning External DTD Subset.  True contents of DTD cannot be determined.  Processing will continue as XMLInputFactory.IS_VALIDATING == false. -->";
/*      */           }
/*      */ 
/*  591 */           return 11;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  596 */       throw new XMLStreamException(ex.getMessage(), getLocation(), ex);
/*      */     } catch (XNIException ex) {
/*  598 */       throw new XMLStreamException(ex.getMessage(), getLocation(), ex.getException());
/*      */     }
/*      */   }
/*      */ 
/*      */   private void switchToXML11Scanner()
/*      */     throws IOException
/*      */   {
/*  607 */     int oldEntityDepth = this.fScanner.fEntityDepth;
/*  608 */     com.sun.org.apache.xerces.internal.xni.NamespaceContext oldNamespaceContext = this.fScanner.fNamespaceContext;
/*      */ 
/*  610 */     this.fScanner = new XML11NSDocumentScannerImpl();
/*      */ 
/*  613 */     this.fScanner.reset(this.fPropertyManager);
/*  614 */     this.fScanner.setPropertyManager(this.fPropertyManager);
/*  615 */     this.fEntityScanner = this.fEntityManager.getEntityScanner();
/*  616 */     this.fEntityManager.fCurrentEntity.mayReadChunks = true;
/*  617 */     this.fScanner.setScannerState(7);
/*      */ 
/*  619 */     this.fScanner.fEntityDepth = oldEntityDepth;
/*  620 */     this.fScanner.fNamespaceContext = oldNamespaceContext;
/*  621 */     this.fEventType = this.fScanner.next();
/*      */   }
/*      */ 
/*      */   static final String getEventTypeString(int eventType)
/*      */   {
/*  627 */     switch (eventType) {
/*      */     case 1:
/*  629 */       return "START_ELEMENT";
/*      */     case 2:
/*  631 */       return "END_ELEMENT";
/*      */     case 3:
/*  633 */       return "PROCESSING_INSTRUCTION";
/*      */     case 4:
/*  635 */       return "CHARACTERS";
/*      */     case 5:
/*  637 */       return "COMMENT";
/*      */     case 7:
/*  639 */       return "START_DOCUMENT";
/*      */     case 8:
/*  641 */       return "END_DOCUMENT";
/*      */     case 9:
/*  643 */       return "ENTITY_REFERENCE";
/*      */     case 10:
/*  645 */       return "ATTRIBUTE";
/*      */     case 11:
/*  647 */       return "DTD";
/*      */     case 12:
/*  649 */       return "CDATA";
/*      */     case 6:
/*  651 */       return "SPACE";
/*      */     }
/*  653 */     return "UNKNOWN_EVENT_TYPE, " + String.valueOf(eventType);
/*      */   }
/*      */ 
/*      */   public int getAttributeCount()
/*      */   {
/*  668 */     if ((this.fEventType == 1) || (this.fEventType == 10)) {
/*  669 */       return this.fScanner.getAttributeIterator().getLength();
/*      */     }
/*  671 */     throw new IllegalStateException("Current state is not among the states " + getEventTypeString(1) + " , " + getEventTypeString(10) + "valid for getAttributeCount()");
/*      */   }
/*      */ 
/*      */   public javax.xml.namespace.QName getAttributeName(int index)
/*      */   {
/*  686 */     if ((this.fEventType == 1) || (this.fEventType == 10)) {
/*  687 */       return convertXNIQNametoJavaxQName(this.fScanner.getAttributeIterator().getQualifiedName(index));
/*      */     }
/*  689 */     throw new IllegalStateException("Current state is not among the states " + getEventTypeString(1) + " , " + getEventTypeString(10) + "valid for getAttributeName()");
/*      */   }
/*      */ 
/*      */   public String getAttributeLocalName(int index)
/*      */   {
/*  702 */     if ((this.fEventType == 1) || (this.fEventType == 10)) {
/*  703 */       return this.fScanner.getAttributeIterator().getLocalName(index);
/*      */     }
/*  705 */     throw new IllegalStateException();
/*      */   }
/*      */ 
/*      */   public String getAttributeNamespace(int index)
/*      */   {
/*  717 */     if ((this.fEventType == 1) || (this.fEventType == 10)) {
/*  718 */       return this.fScanner.getAttributeIterator().getURI(index);
/*      */     }
/*  720 */     throw new IllegalStateException("Current state is not among the states " + getEventTypeString(1) + " , " + getEventTypeString(10) + "valid for getAttributeNamespace()");
/*      */   }
/*      */ 
/*      */   public String getAttributePrefix(int index)
/*      */   {
/*  736 */     if ((this.fEventType == 1) || (this.fEventType == 10)) {
/*  737 */       return this.fScanner.getAttributeIterator().getPrefix(index);
/*      */     }
/*  739 */     throw new IllegalStateException("Current state is not among the states " + getEventTypeString(1) + " , " + getEventTypeString(10) + "valid for getAttributePrefix()");
/*      */   }
/*      */ 
/*      */   public javax.xml.namespace.QName getAttributeQName(int index)
/*      */   {
/*  754 */     if ((this.fEventType == 1) || (this.fEventType == 10))
/*      */     {
/*  756 */       String localName = this.fScanner.getAttributeIterator().getLocalName(index);
/*  757 */       String uri = this.fScanner.getAttributeIterator().getURI(index);
/*  758 */       return new javax.xml.namespace.QName(uri, localName);
/*      */     }
/*  760 */     throw new IllegalStateException("Current state is not among the states " + getEventTypeString(1) + " , " + getEventTypeString(10) + "valid for getAttributeQName()");
/*      */   }
/*      */ 
/*      */   public String getAttributeType(int index)
/*      */   {
/*  775 */     if ((this.fEventType == 1) || (this.fEventType == 10)) {
/*  776 */       return this.fScanner.getAttributeIterator().getType(index);
/*      */     }
/*  778 */     throw new IllegalStateException("Current state is not among the states " + getEventTypeString(1) + " , " + getEventTypeString(10) + "valid for getAttributeType()");
/*      */   }
/*      */ 
/*      */   public String getAttributeValue(int index)
/*      */   {
/*  794 */     if ((this.fEventType == 1) || (this.fEventType == 10)) {
/*  795 */       return this.fScanner.getAttributeIterator().getValue(index);
/*      */     }
/*  797 */     throw new IllegalStateException("Current state is not among the states " + getEventTypeString(1) + " , " + getEventTypeString(10) + "valid for getAttributeValue()");
/*      */   }
/*      */ 
/*      */   public String getAttributeValue(String namespaceURI, String localName)
/*      */   {
/*  812 */     if ((this.fEventType == 1) || (this.fEventType == 10)) {
/*  813 */       XMLAttributesImpl attributes = this.fScanner.getAttributeIterator();
/*  814 */       if (namespaceURI == null) {
/*  815 */         return attributes.getValue(attributes.getIndexByLocalName(localName));
/*      */       }
/*  817 */       return this.fScanner.getAttributeIterator().getValue(namespaceURI.length() == 0 ? null : namespaceURI, localName);
/*      */     }
/*      */ 
/*  822 */     throw new IllegalStateException("Current state is not among the states " + getEventTypeString(1) + " , " + getEventTypeString(10) + "valid for getAttributeValue()");
/*      */   }
/*      */ 
/*      */   public String getElementText()
/*      */     throws XMLStreamException
/*      */   {
/*  838 */     if (getEventType() != 1) {
/*  839 */       throw new XMLStreamException("parser must be on START_ELEMENT to read next text", getLocation());
/*      */     }
/*      */ 
/*  842 */     int eventType = next();
/*  843 */     StringBuffer content = new StringBuffer();
/*  844 */     while (eventType != 2) {
/*  845 */       if ((eventType == 4) || (eventType == 12) || (eventType == 6) || (eventType == 9))
/*      */       {
/*  849 */         content.append(getText());
/*  850 */       } else if ((eventType != 3) && (eventType != 5))
/*      */       {
/*  853 */         if (eventType == 8)
/*  854 */           throw new XMLStreamException("unexpected end of document when reading element text content");
/*  855 */         if (eventType == 1) {
/*  856 */           throw new XMLStreamException("elementGetText() function expects text only elment but START_ELEMENT was encountered.", getLocation());
/*      */         }
/*      */ 
/*  859 */         throw new XMLStreamException("Unexpected event type " + eventType, getLocation());
/*      */       }
/*      */ 
/*  862 */       eventType = next();
/*      */     }
/*  864 */     return content.toString();
/*      */   }
/*      */ 
/*      */   public Location getLocation()
/*      */   {
/*  875 */     return new Location() {
/*  876 */       String _systemId = XMLStreamReaderImpl.this.fEntityScanner.getExpandedSystemId();
/*  877 */       String _publicId = XMLStreamReaderImpl.this.fEntityScanner.getPublicId();
/*  878 */       int _offset = XMLStreamReaderImpl.this.fEntityScanner.getCharacterOffset();
/*  879 */       int _columnNumber = XMLStreamReaderImpl.this.fEntityScanner.getColumnNumber();
/*  880 */       int _lineNumber = XMLStreamReaderImpl.this.fEntityScanner.getLineNumber();
/*      */ 
/*  882 */       public String getLocationURI() { return this._systemId; }
/*      */ 
/*      */       public int getCharacterOffset()
/*      */       {
/*  886 */         return this._offset;
/*      */       }
/*      */ 
/*      */       public int getColumnNumber() {
/*  890 */         return this._columnNumber;
/*      */       }
/*      */ 
/*      */       public int getLineNumber() {
/*  894 */         return this._lineNumber;
/*      */       }
/*      */ 
/*      */       public String getPublicId() {
/*  898 */         return this._publicId;
/*      */       }
/*      */ 
/*      */       public String getSystemId() {
/*  902 */         return this._systemId;
/*      */       }
/*      */ 
/*      */       public String toString() {
/*  906 */         StringBuffer sbuffer = new StringBuffer();
/*  907 */         sbuffer.append("Line number = " + getLineNumber());
/*  908 */         sbuffer.append("\n");
/*  909 */         sbuffer.append("Column number = " + getColumnNumber());
/*  910 */         sbuffer.append("\n");
/*  911 */         sbuffer.append("System Id = " + getSystemId());
/*  912 */         sbuffer.append("\n");
/*  913 */         sbuffer.append("Public Id = " + getPublicId());
/*  914 */         sbuffer.append("\n");
/*  915 */         sbuffer.append("Location Uri= " + getLocationURI());
/*  916 */         sbuffer.append("\n");
/*  917 */         sbuffer.append("CharacterOffset = " + getCharacterOffset());
/*  918 */         sbuffer.append("\n");
/*  919 */         return sbuffer.toString();
/*      */       }
/*      */     };
/*      */   }
/*      */ 
/*      */   public javax.xml.namespace.QName getName()
/*      */   {
/*  929 */     if ((this.fEventType == 1) || (this.fEventType == 2)) {
/*  930 */       return convertXNIQNametoJavaxQName(this.fScanner.getElementQName());
/*      */     }
/*  932 */     throw new IllegalStateException("Illegal to call getName() when event type is " + getEventTypeString(this.fEventType) + "." + " Valid states are " + getEventTypeString(1) + ", " + getEventTypeString(2));
/*      */   }
/*      */ 
/*      */   public javax.xml.namespace.NamespaceContext getNamespaceContext()
/*      */   {
/*  944 */     return this.fNamespaceContextWrapper;
/*      */   }
/*      */ 
/*      */   public int getNamespaceCount()
/*      */   {
/*  958 */     if ((this.fEventType == 1) || (this.fEventType == 2) || (this.fEventType == 13)) {
/*  959 */       return this.fScanner.getNamespaceContext().getDeclaredPrefixCount();
/*      */     }
/*  961 */     throw new IllegalStateException("Current event state is " + getEventTypeString(this.fEventType) + " is not among the states " + getEventTypeString(1) + ", " + getEventTypeString(2) + ", " + getEventTypeString(13) + " valid for getNamespaceCount().");
/*      */   }
/*      */ 
/*      */   public String getNamespacePrefix(int index)
/*      */   {
/*  978 */     if ((this.fEventType == 1) || (this.fEventType == 2) || (this.fEventType == 13))
/*      */     {
/*  980 */       String prefix = this.fScanner.getNamespaceContext().getDeclaredPrefixAt(index);
/*  981 */       return prefix.equals("") ? null : prefix;
/*      */     }
/*      */ 
/*  984 */     throw new IllegalStateException("Current state " + getEventTypeString(this.fEventType) + " is not among the states " + getEventTypeString(1) + ", " + getEventTypeString(2) + ", " + getEventTypeString(13) + " valid for getNamespacePrefix().");
/*      */   }
/*      */ 
/*      */   public String getNamespaceURI(int index)
/*      */   {
/* 1000 */     if ((this.fEventType == 1) || (this.fEventType == 2) || (this.fEventType == 13))
/*      */     {
/* 1002 */       return this.fScanner.getNamespaceContext().getURI(this.fScanner.getNamespaceContext().getDeclaredPrefixAt(index));
/*      */     }
/*      */ 
/* 1005 */     throw new IllegalStateException("Current state " + getEventTypeString(this.fEventType) + " is not among the states " + getEventTypeString(1) + ", " + getEventTypeString(2) + ", " + getEventTypeString(13) + " valid for getNamespaceURI().");
/*      */   }
/*      */ 
/*      */   public Object getProperty(String name)
/*      */     throws IllegalArgumentException
/*      */   {
/* 1020 */     if (name == null) throw new IllegalArgumentException();
/* 1021 */     if (this.fPropertyManager != null) {
/* 1022 */       if (name.equals("javax.xml.stream.notations"))
/* 1023 */         return getNotationDecls();
/* 1024 */       if (name.equals("javax.xml.stream.entities")) {
/* 1025 */         return getEntityDecls();
/*      */       }
/* 1027 */       return this.fPropertyManager.getProperty(name);
/*      */     }
/* 1029 */     return null;
/*      */   }
/*      */ 
/*      */   public String getText()
/*      */   {
/* 1042 */     if ((this.fEventType == 4) || (this.fEventType == 5) || (this.fEventType == 12) || (this.fEventType == 6))
/*      */     {
/* 1046 */       return this.fScanner.getCharacterData().toString();
/* 1047 */     }if (this.fEventType == 9) {
/* 1048 */       String name = this.fScanner.getEntityName();
/* 1049 */       if (name != null) {
/* 1050 */         if (this.fScanner.foundBuiltInRefs) {
/* 1051 */           return this.fScanner.getCharacterData().toString();
/*      */         }
/* 1053 */         XMLEntityStorage entityStore = this.fEntityManager.getEntityStore();
/* 1054 */         Entity en = entityStore.getEntity(name);
/* 1055 */         if (en == null)
/* 1056 */           return null;
/* 1057 */         if (en.isExternal()) {
/* 1058 */           return ((Entity.ExternalEntity)en).entityLocation.getExpandedSystemId();
/*      */         }
/* 1060 */         return ((Entity.InternalEntity)en).text;
/*      */       }
/* 1062 */       return null;
/*      */     }
/* 1064 */     if (this.fEventType == 11) {
/* 1065 */       if (this.fDTDDecl != null) {
/* 1066 */         return this.fDTDDecl;
/*      */       }
/* 1068 */       XMLStringBuffer tmpBuffer = this.fScanner.getDTDDecl();
/* 1069 */       this.fDTDDecl = tmpBuffer.toString();
/* 1070 */       return this.fDTDDecl;
/*      */     }
/* 1072 */     throw new IllegalStateException("Current state " + getEventTypeString(this.fEventType) + " is not among the states" + getEventTypeString(4) + ", " + getEventTypeString(5) + ", " + getEventTypeString(12) + ", " + getEventTypeString(6) + ", " + getEventTypeString(9) + ", " + getEventTypeString(11) + " valid for getText() ");
/*      */   }
/*      */ 
/*      */   public void require(int type, String namespaceURI, String localName)
/*      */     throws XMLStreamException
/*      */   {
/* 1091 */     if (type != this.fEventType) {
/* 1092 */       throw new XMLStreamException("Event type " + getEventTypeString(type) + " specified did " + "not match with current parser event " + getEventTypeString(this.fEventType));
/*      */     }
/* 1094 */     if ((namespaceURI != null) && (!namespaceURI.equals(getNamespaceURI()))) {
/* 1095 */       throw new XMLStreamException("Namespace URI " + namespaceURI + " specified did not match " + "with current namespace URI");
/*      */     }
/* 1097 */     if ((localName != null) && (!localName.equals(getLocalName())))
/* 1098 */       throw new XMLStreamException("LocalName " + localName + " specified did not match with " + "current local name");
/*      */   }
/*      */ 
/*      */   public int getTextCharacters(int sourceStart, char[] target, int targetStart, int length)
/*      */     throws XMLStreamException
/*      */   {
/* 1141 */     if (target == null) {
/* 1142 */       throw new NullPointerException("target char array can't be null");
/*      */     }
/*      */ 
/* 1145 */     if ((targetStart < 0) || (length < 0) || (sourceStart < 0) || (targetStart >= target.length) || (targetStart + length > target.length))
/*      */     {
/* 1147 */       throw new IndexOutOfBoundsException();
/*      */     }
/*      */ 
/* 1152 */     int copiedLength = 0;
/*      */ 
/* 1154 */     int available = getTextLength() - sourceStart;
/* 1155 */     if (available < 0) {
/* 1156 */       throw new IndexOutOfBoundsException("sourceStart is greater thannumber of characters associated with this event");
/*      */     }
/*      */ 
/* 1159 */     if (available < length)
/* 1160 */       copiedLength = available;
/*      */     else {
/* 1162 */       copiedLength = length;
/*      */     }
/*      */ 
/* 1165 */     System.arraycopy(getTextCharacters(), getTextStart() + sourceStart, target, targetStart, copiedLength);
/* 1166 */     return copiedLength;
/*      */   }
/*      */ 
/*      */   public boolean hasText()
/*      */   {
/* 1175 */     if ((this.fEventType == 4) || (this.fEventType == 5) || (this.fEventType == 12))
/* 1176 */       return this.fScanner.getCharacterData().length > 0;
/* 1177 */     if (this.fEventType == 9) {
/* 1178 */       String name = this.fScanner.getEntityName();
/* 1179 */       if (name != null) {
/* 1180 */         if (this.fScanner.foundBuiltInRefs) {
/* 1181 */           return true;
/*      */         }
/* 1183 */         XMLEntityStorage entityStore = this.fEntityManager.getEntityStore();
/* 1184 */         Entity en = entityStore.getEntity(name);
/* 1185 */         if (en == null)
/* 1186 */           return false;
/* 1187 */         if (en.isExternal()) {
/* 1188 */           return ((Entity.ExternalEntity)en).entityLocation.getExpandedSystemId() != null;
/*      */         }
/* 1190 */         return ((Entity.InternalEntity)en).text != null;
/*      */       }
/*      */ 
/* 1193 */       return false;
/*      */     }
/* 1195 */     if (this.fEventType == 11) {
/* 1196 */       return this.fScanner.fSeenDoctypeDecl;
/*      */     }
/* 1198 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean isAttributeSpecified(int index)
/*      */   {
/* 1209 */     if ((this.fEventType == 1) || (this.fEventType == 10)) {
/* 1210 */       return this.fScanner.getAttributeIterator().isSpecified(index);
/*      */     }
/* 1212 */     throw new IllegalStateException("Current state is not among the states " + getEventTypeString(1) + " , " + getEventTypeString(10) + "valid for isAttributeSpecified()");
/*      */   }
/*      */ 
/*      */   public boolean isCharacters()
/*      */   {
/* 1223 */     return this.fEventType == 4;
/*      */   }
/*      */ 
/*      */   public int nextTag()
/*      */     throws XMLStreamException
/*      */   {
/* 1238 */     int eventType = next();
/*      */ 
/* 1244 */     while (((eventType == 4) && (isWhiteSpace())) || ((eventType == 12) && (isWhiteSpace())) || (eventType == 6) || (eventType == 3) || (eventType == 5))
/*      */     {
/* 1246 */       eventType = next();
/*      */     }
/*      */ 
/* 1249 */     if ((eventType != 1) && (eventType != 2)) {
/* 1250 */       throw new XMLStreamException("found: " + getEventTypeString(eventType) + ", expected " + getEventTypeString(1) + " or " + getEventTypeString(2), getLocation());
/*      */     }
/*      */ 
/* 1257 */     return eventType;
/*      */   }
/*      */ 
/*      */   public boolean standaloneSet()
/*      */   {
/* 1266 */     return this.fScanner.standaloneSet();
/*      */   }
/*      */ 
/*      */   public javax.xml.namespace.QName convertXNIQNametoJavaxQName(com.sun.org.apache.xerces.internal.xni.QName qname)
/*      */   {
/* 1274 */     if (qname == null) return null;
/*      */ 
/* 1276 */     if (qname.prefix == null) {
/* 1277 */       return new javax.xml.namespace.QName(qname.uri, qname.localpart);
/*      */     }
/* 1279 */     return new javax.xml.namespace.QName(qname.uri, qname.localpart, qname.prefix);
/*      */   }
/*      */ 
/*      */   public String getNamespaceURI(String prefix)
/*      */   {
/* 1297 */     if (prefix == null) throw new IllegalArgumentException("prefix cannot be null.");
/*      */ 
/* 1300 */     return this.fScanner.getNamespaceContext().getURI(this.fSymbolTable.addSymbol(prefix));
/*      */   }
/*      */ 
/*      */   protected void setPropertyManager(PropertyManager propertyManager)
/*      */   {
/* 1305 */     this.fPropertyManager = propertyManager;
/*      */ 
/* 1307 */     this.fScanner.setProperty("stax-properties", propertyManager);
/* 1308 */     this.fScanner.setPropertyManager(propertyManager);
/*      */   }
/*      */ 
/*      */   protected PropertyManager getPropertyManager()
/*      */   {
/* 1315 */     return this.fPropertyManager;
/*      */   }
/*      */ 
/*      */   static void pr(String str) {
/* 1319 */     System.out.println(str);
/*      */   }
/*      */ 
/*      */   protected List getEntityDecls() {
/* 1323 */     if (this.fEventType == 11) {
/* 1324 */       XMLEntityStorage entityStore = this.fEntityManager.getEntityStore();
/* 1325 */       ArrayList list = null;
/* 1326 */       if (entityStore.hasEntities()) {
/* 1327 */         EntityDeclarationImpl decl = null;
/* 1328 */         list = new ArrayList(entityStore.getEntitySize());
/* 1329 */         Enumeration enu = entityStore.getEntityKeys();
/* 1330 */         while (enu.hasMoreElements()) {
/* 1331 */           String key = (String)enu.nextElement();
/* 1332 */           Entity en = entityStore.getEntity(key);
/* 1333 */           decl = new EntityDeclarationImpl();
/* 1334 */           decl.setEntityName(key);
/* 1335 */           if (en.isExternal()) {
/* 1336 */             decl.setXMLResourceIdentifier(((Entity.ExternalEntity)en).entityLocation);
/* 1337 */             decl.setNotationName(((Entity.ExternalEntity)en).notation);
/*      */           }
/*      */           else {
/* 1340 */             decl.setEntityReplacementText(((Entity.InternalEntity)en).text);
/* 1341 */           }list.add(decl);
/*      */         }
/*      */       }
/* 1344 */       return list;
/*      */     }
/* 1346 */     return null;
/*      */   }
/*      */ 
/*      */   protected List getNotationDecls() {
/* 1350 */     if (this.fEventType == 11) {
/* 1351 */       if (this.fScanner.fDTDScanner == null) return null;
/* 1352 */       DTDGrammar grammar = ((XMLDTDScannerImpl)this.fScanner.fDTDScanner).getGrammar();
/* 1353 */       if (grammar == null) return null;
/* 1354 */       List notations = grammar.getNotationDecls();
/*      */ 
/* 1356 */       Iterator it = notations.iterator();
/* 1357 */       ArrayList list = new ArrayList();
/* 1358 */       while (it.hasNext()) {
/* 1359 */         XMLNotationDecl ni = (XMLNotationDecl)it.next();
/* 1360 */         if (ni != null) {
/* 1361 */           list.add(new NotationDeclarationImpl(ni));
/*      */         }
/*      */       }
/* 1364 */       return list;
/*      */     }
/* 1366 */     return null;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.XMLStreamReaderImpl
 * JD-Core Version:    0.6.2
 */