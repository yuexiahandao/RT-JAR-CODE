/*      */ package com.sun.xml.internal.stream.writers;
/*      */ 
/*      */ import com.sun.org.apache.xerces.internal.impl.PropertyManager;
/*      */ import com.sun.org.apache.xerces.internal.util.NamespaceSupport;
/*      */ import com.sun.org.apache.xerces.internal.util.SymbolTable;
/*      */ import com.sun.org.apache.xerces.internal.utils.SecuritySupport;
/*      */ import com.sun.org.apache.xerces.internal.xni.QName;
/*      */ import com.sun.xml.internal.stream.util.ReadOnlyIterator;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.OutputStream;
/*      */ import java.io.OutputStreamWriter;
/*      */ import java.io.Writer;
/*      */ import java.nio.charset.Charset;
/*      */ import java.nio.charset.CharsetEncoder;
/*      */ import java.util.AbstractMap;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.Random;
/*      */ import java.util.Set;
/*      */ import java.util.Vector;
/*      */ import javax.xml.namespace.NamespaceContext;
/*      */ import javax.xml.stream.XMLStreamException;
/*      */ import javax.xml.stream.XMLStreamWriter;
/*      */ import javax.xml.transform.stream.StreamResult;
/*      */ 
/*      */ public final class XMLStreamWriterImpl extends AbstractMap
/*      */   implements XMLStreamWriter
/*      */ {
/*      */   public static final String START_COMMENT = "<!--";
/*      */   public static final String END_COMMENT = "-->";
/*      */   public static final String DEFAULT_ENCODING = " encoding=\"utf-8\"";
/*      */   public static final String DEFAULT_XMLDECL = "<?xml version=\"1.0\" ?>";
/*      */   public static final String DEFAULT_XML_VERSION = "1.0";
/*      */   public static final char CLOSE_START_TAG = '>';
/*      */   public static final char OPEN_START_TAG = '<';
/*      */   public static final String OPEN_END_TAG = "</";
/*      */   public static final char CLOSE_END_TAG = '>';
/*      */   public static final String START_CDATA = "<![CDATA[";
/*      */   public static final String END_CDATA = "]]>";
/*      */   public static final String CLOSE_EMPTY_ELEMENT = "/>";
/*      */   public static final String SPACE = " ";
/*      */   public static final String UTF_8 = "UTF-8";
/*      */   public static final String OUTPUTSTREAM_PROPERTY = "sjsxp-outputstream";
/*   97 */   boolean fEscapeCharacters = true;
/*      */ 
/*  102 */   private boolean fIsRepairingNamespace = false;
/*      */   private Writer fWriter;
/*  113 */   private OutputStream fOutputStream = null;
/*      */   private ArrayList fAttributeCache;
/*      */   private ArrayList fNamespaceDecls;
/*  129 */   private NamespaceContextImpl fNamespaceContext = null;
/*      */ 
/*  131 */   private NamespaceSupport fInternalNamespaceContext = null;
/*      */ 
/*  133 */   private Random fPrefixGen = null;
/*      */ 
/*  138 */   private PropertyManager fPropertyManager = null;
/*      */ 
/*  143 */   private boolean fStartTagOpened = false;
/*      */   private boolean fReuse;
/*  150 */   private SymbolTable fSymbolTable = new SymbolTable();
/*      */ 
/*  152 */   private ElementStack fElementStack = new ElementStack();
/*      */ 
/*  154 */   private final String DEFAULT_PREFIX = this.fSymbolTable.addSymbol("");
/*      */ 
/*  156 */   private final ReadOnlyIterator fReadOnlyIterator = new ReadOnlyIterator();
/*      */ 
/*  164 */   private CharsetEncoder fEncoder = null;
/*      */ 
/*  171 */   HashMap fAttrNamespace = null;
/*      */ 
/*      */   public XMLStreamWriterImpl(OutputStream outputStream, PropertyManager props)
/*      */     throws IOException
/*      */   {
/*  187 */     this(new OutputStreamWriter(outputStream), props);
/*      */   }
/*      */ 
/*      */   public XMLStreamWriterImpl(OutputStream outputStream, String encoding, PropertyManager props)
/*      */     throws IOException
/*      */   {
/*  199 */     this(new StreamResult(outputStream), encoding, props);
/*      */   }
/*      */ 
/*      */   public XMLStreamWriterImpl(Writer writer, PropertyManager props)
/*      */     throws IOException
/*      */   {
/*  210 */     this(new StreamResult(writer), null, props);
/*      */   }
/*      */ 
/*      */   public XMLStreamWriterImpl(StreamResult sr, String encoding, PropertyManager props)
/*      */     throws IOException
/*      */   {
/*  222 */     setOutput(sr, encoding);
/*  223 */     this.fPropertyManager = props;
/*  224 */     init();
/*      */   }
/*      */ 
/*      */   private void init()
/*      */   {
/*  232 */     this.fReuse = false;
/*  233 */     this.fNamespaceDecls = new ArrayList();
/*  234 */     this.fPrefixGen = new Random();
/*  235 */     this.fAttributeCache = new ArrayList();
/*  236 */     this.fInternalNamespaceContext = new NamespaceSupport();
/*  237 */     this.fInternalNamespaceContext.reset();
/*  238 */     this.fNamespaceContext = new NamespaceContextImpl();
/*  239 */     this.fNamespaceContext.internalContext = this.fInternalNamespaceContext;
/*      */ 
/*  242 */     Boolean ob = (Boolean)this.fPropertyManager.getProperty("javax.xml.stream.isRepairingNamespaces");
/*  243 */     this.fIsRepairingNamespace = ob.booleanValue();
/*  244 */     ob = (Boolean)this.fPropertyManager.getProperty("escapeCharacters");
/*  245 */     setEscapeCharacters(ob.booleanValue());
/*      */   }
/*      */ 
/*      */   public void reset()
/*      */   {
/*  254 */     reset(false);
/*      */   }
/*      */ 
/*      */   void reset(boolean resetProperties)
/*      */   {
/*  264 */     if (!this.fReuse) {
/*  265 */       throw new IllegalStateException("close() Must be called before calling reset()");
/*      */     }
/*      */ 
/*  269 */     this.fReuse = false;
/*  270 */     this.fNamespaceDecls.clear();
/*  271 */     this.fAttributeCache.clear();
/*      */ 
/*  274 */     this.fElementStack.clear();
/*  275 */     this.fInternalNamespaceContext.reset();
/*      */ 
/*  277 */     this.fStartTagOpened = false;
/*  278 */     this.fNamespaceContext.userContext = null;
/*      */ 
/*  280 */     if (resetProperties) {
/*  281 */       Boolean ob = (Boolean)this.fPropertyManager.getProperty("javax.xml.stream.isRepairingNamespaces");
/*  282 */       this.fIsRepairingNamespace = ob.booleanValue();
/*  283 */       ob = (Boolean)this.fPropertyManager.getProperty("escapeCharacters");
/*  284 */       setEscapeCharacters(ob.booleanValue());
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setOutput(StreamResult sr, String encoding)
/*      */     throws IOException
/*      */   {
/*  298 */     if (sr.getOutputStream() != null) {
/*  299 */       setOutputUsingStream(sr.getOutputStream(), encoding);
/*      */     }
/*  301 */     else if (sr.getWriter() != null) {
/*  302 */       setOutputUsingWriter(sr.getWriter());
/*      */     }
/*  304 */     else if (sr.getSystemId() != null)
/*  305 */       setOutputUsingStream(new FileOutputStream(sr.getSystemId()), encoding);
/*      */   }
/*      */ 
/*      */   private void setOutputUsingWriter(Writer writer)
/*      */     throws IOException
/*      */   {
/*  313 */     this.fWriter = writer;
/*      */ 
/*  315 */     if ((writer instanceof OutputStreamWriter)) {
/*  316 */       String charset = ((OutputStreamWriter)writer).getEncoding();
/*  317 */       if ((charset != null) && (!charset.equalsIgnoreCase("utf-8")))
/*  318 */         this.fEncoder = Charset.forName(charset).newEncoder();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void setOutputUsingStream(OutputStream os, String encoding)
/*      */     throws IOException
/*      */   {
/*  333 */     this.fOutputStream = os;
/*      */ 
/*  335 */     if (encoding != null) {
/*  336 */       if (encoding.equalsIgnoreCase("utf-8")) {
/*  337 */         this.fWriter = new UTF8OutputStreamWriter(os);
/*      */       }
/*      */       else {
/*  340 */         this.fWriter = new XMLWriter(new OutputStreamWriter(os, encoding));
/*  341 */         this.fEncoder = Charset.forName(encoding).newEncoder();
/*      */       }
/*      */     } else {
/*  344 */       encoding = SecuritySupport.getSystemProperty("file.encoding");
/*  345 */       if ((encoding != null) && (encoding.equalsIgnoreCase("utf-8")))
/*  346 */         this.fWriter = new UTF8OutputStreamWriter(os);
/*      */       else
/*  348 */         this.fWriter = new XMLWriter(new OutputStreamWriter(os));
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean canReuse()
/*      */   {
/*  358 */     return this.fReuse;
/*      */   }
/*      */ 
/*      */   public void setEscapeCharacters(boolean escape) {
/*  362 */     this.fEscapeCharacters = escape;
/*      */   }
/*      */ 
/*      */   public boolean getEscapeCharacters() {
/*  366 */     return this.fEscapeCharacters;
/*      */   }
/*      */ 
/*      */   public void close()
/*      */     throws XMLStreamException
/*      */   {
/*  373 */     if (this.fWriter != null) {
/*      */       try
/*      */       {
/*  376 */         this.fWriter.flush();
/*      */       } catch (IOException e) {
/*  378 */         throw new XMLStreamException(e);
/*      */       }
/*      */     }
/*  381 */     this.fWriter = null;
/*  382 */     this.fOutputStream = null;
/*  383 */     this.fNamespaceDecls.clear();
/*  384 */     this.fAttributeCache.clear();
/*  385 */     this.fElementStack.clear();
/*  386 */     this.fInternalNamespaceContext.reset();
/*  387 */     this.fReuse = true;
/*  388 */     this.fStartTagOpened = false;
/*  389 */     this.fNamespaceContext.userContext = null;
/*      */   }
/*      */ 
/*      */   public void flush()
/*      */     throws XMLStreamException
/*      */   {
/*      */     try
/*      */     {
/*  397 */       this.fWriter.flush();
/*      */     } catch (IOException e) {
/*  399 */       throw new XMLStreamException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public NamespaceContext getNamespaceContext()
/*      */   {
/*  409 */     return this.fNamespaceContext;
/*      */   }
/*      */ 
/*      */   public String getPrefix(String uri)
/*      */     throws XMLStreamException
/*      */   {
/*  420 */     return this.fNamespaceContext.getPrefix(uri);
/*      */   }
/*      */ 
/*      */   public Object getProperty(String str)
/*      */     throws IllegalArgumentException
/*      */   {
/*  432 */     if (str == null) {
/*  433 */       throw new NullPointerException();
/*      */     }
/*      */ 
/*  436 */     if (!this.fPropertyManager.containsProperty(str)) {
/*  437 */       throw new IllegalArgumentException("Property '" + str + "' is not supported");
/*      */     }
/*      */ 
/*  441 */     return this.fPropertyManager.getProperty(str);
/*      */   }
/*      */ 
/*      */   public void setDefaultNamespace(String uri)
/*      */     throws XMLStreamException
/*      */   {
/*  450 */     if (uri != null) {
/*  451 */       uri = this.fSymbolTable.addSymbol(uri);
/*      */     }
/*      */ 
/*  454 */     if (this.fIsRepairingNamespace) {
/*  455 */       if (isDefaultNamespace(uri)) {
/*  456 */         return;
/*      */       }
/*      */ 
/*  459 */       QName qname = new QName();
/*  460 */       qname.setValues(this.DEFAULT_PREFIX, "xmlns", null, uri);
/*  461 */       this.fNamespaceDecls.add(qname);
/*      */     } else {
/*  463 */       this.fInternalNamespaceContext.declarePrefix(this.DEFAULT_PREFIX, uri);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setNamespaceContext(NamespaceContext namespaceContext)
/*      */     throws XMLStreamException
/*      */   {
/*  484 */     this.fNamespaceContext.userContext = namespaceContext;
/*      */   }
/*      */ 
/*      */   public void setPrefix(String prefix, String uri)
/*      */     throws XMLStreamException
/*      */   {
/*  498 */     if (prefix == null) {
/*  499 */       throw new XMLStreamException("Prefix cannot be null");
/*      */     }
/*      */ 
/*  502 */     if (uri == null) {
/*  503 */       throw new XMLStreamException("URI cannot be null");
/*      */     }
/*      */ 
/*  506 */     prefix = this.fSymbolTable.addSymbol(prefix);
/*  507 */     uri = this.fSymbolTable.addSymbol(uri);
/*      */ 
/*  509 */     if (this.fIsRepairingNamespace) {
/*  510 */       String tmpURI = this.fInternalNamespaceContext.getURI(prefix);
/*      */ 
/*  512 */       if ((tmpURI != null) && (tmpURI == uri)) {
/*  513 */         return;
/*      */       }
/*      */ 
/*  516 */       if (checkUserNamespaceContext(prefix, uri))
/*  517 */         return;
/*  518 */       QName qname = new QName();
/*  519 */       qname.setValues(prefix, "xmlns", null, uri);
/*  520 */       this.fNamespaceDecls.add(qname);
/*      */ 
/*  522 */       return;
/*      */     }
/*      */ 
/*  525 */     this.fInternalNamespaceContext.declarePrefix(prefix, uri);
/*      */   }
/*      */ 
/*      */   public void writeAttribute(String localName, String value) throws XMLStreamException
/*      */   {
/*      */     try {
/*  531 */       if (!this.fStartTagOpened) {
/*  532 */         throw new XMLStreamException("Attribute not associated with any element");
/*      */       }
/*      */ 
/*  536 */       if (this.fIsRepairingNamespace) {
/*  537 */         Attribute attr = new Attribute(value);
/*  538 */         attr.setValues(null, localName, null, null);
/*  539 */         this.fAttributeCache.add(attr);
/*      */ 
/*  541 */         return;
/*      */       }
/*      */ 
/*  544 */       this.fWriter.write(" ");
/*  545 */       this.fWriter.write(localName);
/*  546 */       this.fWriter.write("=\"");
/*  547 */       writeXMLContent(value, true, true);
/*      */ 
/*  551 */       this.fWriter.write("\"");
/*      */     } catch (IOException e) {
/*  553 */       throw new XMLStreamException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void writeAttribute(String namespaceURI, String localName, String value) throws XMLStreamException
/*      */   {
/*      */     try {
/*  560 */       if (!this.fStartTagOpened) {
/*  561 */         throw new XMLStreamException("Attribute not associated with any element");
/*      */       }
/*      */ 
/*  565 */       if (namespaceURI == null) {
/*  566 */         throw new XMLStreamException("NamespaceURI cannot be null");
/*      */       }
/*      */ 
/*  569 */       namespaceURI = this.fSymbolTable.addSymbol(namespaceURI);
/*      */ 
/*  571 */       String prefix = this.fInternalNamespaceContext.getPrefix(namespaceURI);
/*      */ 
/*  573 */       if (!this.fIsRepairingNamespace) {
/*  574 */         if (prefix == null) {
/*  575 */           throw new XMLStreamException("Prefix cannot be null");
/*      */         }
/*      */ 
/*  578 */         writeAttributeWithPrefix(prefix, localName, value);
/*      */       } else {
/*  580 */         Attribute attr = new Attribute(value);
/*  581 */         attr.setValues(null, localName, null, namespaceURI);
/*  582 */         this.fAttributeCache.add(attr);
/*      */       }
/*      */     } catch (IOException e) {
/*  585 */       throw new XMLStreamException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void writeAttributeWithPrefix(String prefix, String localName, String value) throws IOException
/*      */   {
/*  591 */     this.fWriter.write(" ");
/*      */ 
/*  593 */     if ((prefix != null) && (prefix != "")) {
/*  594 */       this.fWriter.write(prefix);
/*  595 */       this.fWriter.write(":");
/*      */     }
/*      */ 
/*  598 */     this.fWriter.write(localName);
/*  599 */     this.fWriter.write("=\"");
/*  600 */     writeXMLContent(value, true, true);
/*      */ 
/*  603 */     this.fWriter.write("\"");
/*      */   }
/*      */ 
/*      */   public void writeAttribute(String prefix, String namespaceURI, String localName, String value) throws XMLStreamException
/*      */   {
/*      */     try {
/*  609 */       if (!this.fStartTagOpened) {
/*  610 */         throw new XMLStreamException("Attribute not associated with any element");
/*      */       }
/*      */ 
/*  614 */       if (namespaceURI == null) {
/*  615 */         throw new XMLStreamException("NamespaceURI cannot be null");
/*      */       }
/*      */ 
/*  618 */       if (localName == null) {
/*  619 */         throw new XMLStreamException("Local name cannot be null");
/*      */       }
/*      */ 
/*  622 */       if (!this.fIsRepairingNamespace) {
/*  623 */         if ((prefix == null) || (prefix.equals(""))) {
/*  624 */           if (!namespaceURI.equals("")) {
/*  625 */             throw new XMLStreamException("prefix cannot be null or empty");
/*      */           }
/*  627 */           writeAttributeWithPrefix(null, localName, value);
/*  628 */           return;
/*      */         }
/*      */ 
/*  632 */         if ((!prefix.equals("xml")) || (!namespaceURI.equals("http://www.w3.org/XML/1998/namespace")))
/*      */         {
/*  634 */           prefix = this.fSymbolTable.addSymbol(prefix);
/*  635 */           namespaceURI = this.fSymbolTable.addSymbol(namespaceURI);
/*      */ 
/*  637 */           if (this.fInternalNamespaceContext.containsPrefixInCurrentContext(prefix))
/*      */           {
/*  639 */             String tmpURI = this.fInternalNamespaceContext.getURI(prefix);
/*      */ 
/*  641 */             if ((tmpURI != null) && (tmpURI != namespaceURI)) {
/*  642 */               throw new XMLStreamException("Prefix " + prefix + " is " + "already bound to " + tmpURI + ". Trying to rebind it to " + namespaceURI + " is an error.");
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*  647 */           this.fInternalNamespaceContext.declarePrefix(prefix, namespaceURI);
/*      */         }
/*  649 */         writeAttributeWithPrefix(prefix, localName, value);
/*      */       } else {
/*  651 */         if (prefix != null) {
/*  652 */           prefix = this.fSymbolTable.addSymbol(prefix);
/*      */         }
/*      */ 
/*  655 */         namespaceURI = this.fSymbolTable.addSymbol(namespaceURI);
/*      */ 
/*  657 */         Attribute attr = new Attribute(value);
/*  658 */         attr.setValues(prefix, localName, null, namespaceURI);
/*  659 */         this.fAttributeCache.add(attr);
/*      */       }
/*      */     } catch (IOException e) {
/*  662 */       throw new XMLStreamException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void writeCData(String cdata) throws XMLStreamException {
/*      */     try {
/*  668 */       if (cdata == null) {
/*  669 */         throw new XMLStreamException("cdata cannot be null");
/*      */       }
/*      */ 
/*  672 */       if (this.fStartTagOpened) {
/*  673 */         closeStartTag();
/*      */       }
/*      */ 
/*  676 */       this.fWriter.write("<![CDATA[");
/*  677 */       this.fWriter.write(cdata);
/*  678 */       this.fWriter.write("]]>");
/*      */     } catch (IOException e) {
/*  680 */       throw new XMLStreamException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void writeCharacters(String data) throws XMLStreamException {
/*      */     try {
/*  686 */       if (this.fStartTagOpened) {
/*  687 */         closeStartTag();
/*      */       }
/*      */ 
/*  690 */       writeXMLContent(data);
/*      */     } catch (IOException e) {
/*  692 */       throw new XMLStreamException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void writeCharacters(char[] data, int start, int len) throws XMLStreamException
/*      */   {
/*      */     try {
/*  699 */       if (this.fStartTagOpened) {
/*  700 */         closeStartTag();
/*      */       }
/*      */ 
/*  703 */       writeXMLContent(data, start, len, this.fEscapeCharacters);
/*      */     } catch (IOException e) {
/*  705 */       throw new XMLStreamException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void writeComment(String comment) throws XMLStreamException {
/*      */     try {
/*  711 */       if (this.fStartTagOpened) {
/*  712 */         closeStartTag();
/*      */       }
/*      */ 
/*  715 */       this.fWriter.write("<!--");
/*      */ 
/*  717 */       if (comment != null) {
/*  718 */         this.fWriter.write(comment);
/*      */       }
/*      */ 
/*  721 */       this.fWriter.write("-->");
/*      */     } catch (IOException e) {
/*  723 */       throw new XMLStreamException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void writeDTD(String dtd) throws XMLStreamException {
/*      */     try {
/*  729 */       if (this.fStartTagOpened) {
/*  730 */         closeStartTag();
/*      */       }
/*      */ 
/*  733 */       this.fWriter.write(dtd);
/*      */     } catch (IOException e) {
/*  735 */       throw new XMLStreamException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void writeDefaultNamespace(String namespaceURI)
/*      */     throws XMLStreamException
/*      */   {
/*  757 */     String namespaceURINormalized = null;
/*  758 */     if (namespaceURI == null)
/*  759 */       namespaceURINormalized = "";
/*      */     else {
/*  761 */       namespaceURINormalized = namespaceURI;
/*      */     }
/*      */     try
/*      */     {
/*  765 */       if (!this.fStartTagOpened) {
/*  766 */         throw new IllegalStateException("Namespace Attribute not associated with any element");
/*      */       }
/*      */ 
/*  770 */       if (this.fIsRepairingNamespace) {
/*  771 */         QName qname = new QName();
/*  772 */         qname.setValues("", "xmlns", null, namespaceURINormalized);
/*      */ 
/*  774 */         this.fNamespaceDecls.add(qname);
/*      */ 
/*  776 */         return;
/*      */       }
/*      */ 
/*  779 */       namespaceURINormalized = this.fSymbolTable.addSymbol(namespaceURINormalized);
/*      */ 
/*  781 */       if (this.fInternalNamespaceContext.containsPrefixInCurrentContext(""))
/*      */       {
/*  783 */         String tmp = this.fInternalNamespaceContext.getURI("");
/*      */ 
/*  785 */         if ((tmp != null) && (tmp != namespaceURINormalized)) {
/*  786 */           throw new XMLStreamException("xmlns has been already bound to " + tmp + ". Rebinding it to " + namespaceURINormalized + " is an error");
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  792 */       this.fInternalNamespaceContext.declarePrefix("", namespaceURINormalized);
/*      */ 
/*  795 */       writenamespace(null, namespaceURINormalized);
/*      */     } catch (IOException e) {
/*  797 */       throw new XMLStreamException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void writeEmptyElement(String localName) throws XMLStreamException {
/*      */     try {
/*  803 */       if (this.fStartTagOpened) {
/*  804 */         closeStartTag();
/*      */       }
/*      */ 
/*  807 */       openStartTag();
/*  808 */       this.fElementStack.push(null, localName, null, null, true);
/*  809 */       this.fInternalNamespaceContext.pushContext();
/*      */ 
/*  811 */       if (!this.fIsRepairingNamespace)
/*  812 */         this.fWriter.write(localName);
/*      */     }
/*      */     catch (IOException e) {
/*  815 */       throw new XMLStreamException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void writeEmptyElement(String namespaceURI, String localName) throws XMLStreamException
/*      */   {
/*  821 */     if (namespaceURI == null) {
/*  822 */       throw new XMLStreamException("NamespaceURI cannot be null");
/*      */     }
/*      */ 
/*  825 */     namespaceURI = this.fSymbolTable.addSymbol(namespaceURI);
/*      */ 
/*  827 */     String prefix = this.fNamespaceContext.getPrefix(namespaceURI);
/*  828 */     writeEmptyElement(prefix, localName, namespaceURI);
/*      */   }
/*      */ 
/*      */   public void writeEmptyElement(String prefix, String localName, String namespaceURI) throws XMLStreamException
/*      */   {
/*      */     try {
/*  834 */       if (localName == null) {
/*  835 */         throw new XMLStreamException("Local Name cannot be null");
/*      */       }
/*      */ 
/*  838 */       if (namespaceURI == null) {
/*  839 */         throw new XMLStreamException("NamespaceURI cannot be null");
/*      */       }
/*      */ 
/*  842 */       if (prefix != null) {
/*  843 */         prefix = this.fSymbolTable.addSymbol(prefix);
/*      */       }
/*      */ 
/*  846 */       namespaceURI = this.fSymbolTable.addSymbol(namespaceURI);
/*      */ 
/*  848 */       if (this.fStartTagOpened) {
/*  849 */         closeStartTag();
/*      */       }
/*      */ 
/*  852 */       openStartTag();
/*      */ 
/*  854 */       this.fElementStack.push(prefix, localName, null, namespaceURI, true);
/*  855 */       this.fInternalNamespaceContext.pushContext();
/*      */ 
/*  857 */       if (!this.fIsRepairingNamespace) {
/*  858 */         if (prefix == null) {
/*  859 */           throw new XMLStreamException("NamespaceURI " + namespaceURI + " has not been bound to any prefix");
/*      */         }
/*      */       }
/*      */       else {
/*  863 */         return;
/*      */       }
/*      */ 
/*  866 */       if ((prefix != null) && (prefix != "")) {
/*  867 */         this.fWriter.write(prefix);
/*  868 */         this.fWriter.write(":");
/*      */       }
/*      */ 
/*  871 */       this.fWriter.write(localName);
/*      */     } catch (IOException e) {
/*  873 */       throw new XMLStreamException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void writeEndDocument() throws XMLStreamException {
/*      */     try {
/*  879 */       if (this.fStartTagOpened) {
/*  880 */         closeStartTag();
/*      */       }
/*      */ 
/*  883 */       ElementState elem = null;
/*      */ 
/*  885 */       while (!this.fElementStack.empty()) {
/*  886 */         elem = this.fElementStack.pop();
/*  887 */         this.fInternalNamespaceContext.popContext();
/*      */ 
/*  889 */         if (!elem.isEmpty)
/*      */         {
/*  892 */           this.fWriter.write("</");
/*      */ 
/*  894 */           if ((elem.prefix != null) && (!elem.prefix.equals(""))) {
/*  895 */             this.fWriter.write(elem.prefix);
/*  896 */             this.fWriter.write(":");
/*      */           }
/*      */ 
/*  899 */           this.fWriter.write(elem.localpart);
/*  900 */           this.fWriter.write(62);
/*      */         }
/*      */       }
/*      */     } catch (IOException e) {
/*  904 */       throw new XMLStreamException(e);
/*      */     } catch (ArrayIndexOutOfBoundsException e) {
/*  906 */       throw new XMLStreamException("No more elements to write");
/*      */     }
/*      */   }
/*      */ 
/*      */   public void writeEndElement() throws XMLStreamException {
/*      */     try {
/*  912 */       if (this.fStartTagOpened) {
/*  913 */         closeStartTag();
/*      */       }
/*      */ 
/*  916 */       ElementState currentElement = this.fElementStack.pop();
/*      */ 
/*  918 */       if (currentElement == null) {
/*  919 */         throw new XMLStreamException("No element was found to write");
/*      */       }
/*      */ 
/*  922 */       if (currentElement.isEmpty)
/*      */       {
/*  924 */         return;
/*      */       }
/*      */ 
/*  927 */       this.fWriter.write("</");
/*      */ 
/*  929 */       if ((currentElement.prefix != null) && (!currentElement.prefix.equals("")))
/*      */       {
/*  931 */         this.fWriter.write(currentElement.prefix);
/*  932 */         this.fWriter.write(":");
/*      */       }
/*      */ 
/*  935 */       this.fWriter.write(currentElement.localpart);
/*  936 */       this.fWriter.write(62);
/*  937 */       this.fInternalNamespaceContext.popContext();
/*      */     } catch (IOException e) {
/*  939 */       throw new XMLStreamException(e);
/*      */     } catch (ArrayIndexOutOfBoundsException e) {
/*  941 */       throw new XMLStreamException("No element was found to write: " + e.toString(), e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void writeEntityRef(String refName) throws XMLStreamException
/*      */   {
/*      */     try
/*      */     {
/*  949 */       if (this.fStartTagOpened) {
/*  950 */         closeStartTag();
/*      */       }
/*      */ 
/*  953 */       this.fWriter.write(38);
/*  954 */       this.fWriter.write(refName);
/*  955 */       this.fWriter.write(59);
/*      */     } catch (IOException e) {
/*  957 */       throw new XMLStreamException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void writeNamespace(String prefix, String namespaceURI)
/*      */     throws XMLStreamException
/*      */   {
/*  980 */     String namespaceURINormalized = null;
/*  981 */     if (namespaceURI == null)
/*  982 */       namespaceURINormalized = "";
/*      */     else {
/*  984 */       namespaceURINormalized = namespaceURI;
/*      */     }
/*      */     try
/*      */     {
/*  988 */       QName qname = null;
/*      */ 
/*  990 */       if (!this.fStartTagOpened) {
/*  991 */         throw new IllegalStateException("Invalid state: start tag is not opened at writeNamespace(" + prefix + ", " + namespaceURINormalized + ")");
/*      */       }
/*      */ 
/* 1000 */       if ((prefix == null) || (prefix.equals("")) || (prefix.equals("xmlns")))
/*      */       {
/* 1003 */         writeDefaultNamespace(namespaceURINormalized);
/* 1004 */         return;
/*      */       }
/*      */ 
/* 1007 */       if ((prefix.equals("xml")) && (namespaceURINormalized.equals("http://www.w3.org/XML/1998/namespace"))) {
/* 1008 */         return;
/*      */       }
/* 1010 */       prefix = this.fSymbolTable.addSymbol(prefix);
/* 1011 */       namespaceURINormalized = this.fSymbolTable.addSymbol(namespaceURINormalized);
/*      */ 
/* 1013 */       if (this.fIsRepairingNamespace) {
/* 1014 */         String tmpURI = this.fInternalNamespaceContext.getURI(prefix);
/*      */ 
/* 1016 */         if ((tmpURI != null) && (tmpURI == namespaceURINormalized)) {
/* 1017 */           return;
/*      */         }
/*      */ 
/* 1020 */         qname = new QName();
/* 1021 */         qname.setValues(prefix, "xmlns", null, namespaceURINormalized);
/*      */ 
/* 1023 */         this.fNamespaceDecls.add(qname);
/*      */ 
/* 1025 */         return;
/*      */       }
/*      */ 
/* 1029 */       if (this.fInternalNamespaceContext.containsPrefixInCurrentContext(prefix))
/*      */       {
/* 1031 */         String tmp = this.fInternalNamespaceContext.getURI(prefix);
/*      */ 
/* 1033 */         if ((tmp != null) && (tmp != namespaceURINormalized))
/*      */         {
/* 1035 */           throw new XMLStreamException("prefix " + prefix + " has been already bound to " + tmp + ". Rebinding it to " + namespaceURINormalized + " is an error");
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1042 */       this.fInternalNamespaceContext.declarePrefix(prefix, namespaceURINormalized);
/* 1043 */       writenamespace(prefix, namespaceURINormalized);
/*      */     }
/*      */     catch (IOException e) {
/* 1046 */       throw new XMLStreamException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void writenamespace(String prefix, String namespaceURI) throws IOException
/*      */   {
/* 1052 */     this.fWriter.write(" xmlns");
/*      */ 
/* 1054 */     if ((prefix != null) && (prefix != "")) {
/* 1055 */       this.fWriter.write(":");
/* 1056 */       this.fWriter.write(prefix);
/*      */     }
/*      */ 
/* 1059 */     this.fWriter.write("=\"");
/* 1060 */     writeXMLContent(namespaceURI, true, true);
/*      */ 
/* 1064 */     this.fWriter.write("\"");
/*      */   }
/*      */ 
/*      */   public void writeProcessingInstruction(String target) throws XMLStreamException
/*      */   {
/*      */     try {
/* 1070 */       if (this.fStartTagOpened) {
/* 1071 */         closeStartTag();
/*      */       }
/*      */ 
/* 1074 */       if (target != null) {
/* 1075 */         this.fWriter.write("<?");
/* 1076 */         this.fWriter.write(target);
/* 1077 */         this.fWriter.write("?>");
/*      */ 
/* 1079 */         return;
/*      */       }
/*      */     } catch (IOException e) {
/* 1082 */       throw new XMLStreamException(e);
/*      */     }
/*      */ 
/* 1085 */     throw new XMLStreamException("PI target cannot be null");
/*      */   }
/*      */ 
/*      */   public void writeProcessingInstruction(String target, String data)
/*      */     throws XMLStreamException
/*      */   {
/*      */     try
/*      */     {
/* 1096 */       if (this.fStartTagOpened) {
/* 1097 */         closeStartTag();
/*      */       }
/*      */ 
/* 1100 */       if ((target == null) || (data == null)) {
/* 1101 */         throw new XMLStreamException("PI target cannot be null");
/*      */       }
/*      */ 
/* 1104 */       this.fWriter.write("<?");
/* 1105 */       this.fWriter.write(target);
/* 1106 */       this.fWriter.write(" ");
/* 1107 */       this.fWriter.write(data);
/* 1108 */       this.fWriter.write("?>");
/*      */     } catch (IOException e) {
/* 1110 */       throw new XMLStreamException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void writeStartDocument()
/*      */     throws XMLStreamException
/*      */   {
/*      */     try
/*      */     {
/* 1119 */       this.fWriter.write("<?xml version=\"1.0\" ?>");
/*      */     } catch (IOException ex) {
/* 1121 */       throw new XMLStreamException(ex);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void writeStartDocument(String version)
/*      */     throws XMLStreamException
/*      */   {
/*      */     try
/*      */     {
/* 1131 */       if ((version == null) || (version.equals(""))) {
/* 1132 */         writeStartDocument();
/*      */ 
/* 1134 */         return;
/*      */       }
/*      */ 
/* 1137 */       this.fWriter.write("<?xml version=\"");
/* 1138 */       this.fWriter.write(version);
/* 1139 */       this.fWriter.write("\"");
/*      */ 
/* 1142 */       this.fWriter.write("?>");
/*      */     } catch (IOException ex) {
/* 1144 */       throw new XMLStreamException(ex);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void writeStartDocument(String encoding, String version)
/*      */     throws XMLStreamException
/*      */   {
/*      */     try
/*      */     {
/* 1157 */       if ((encoding == null) && (version == null)) {
/* 1158 */         writeStartDocument();
/*      */ 
/* 1160 */         return;
/*      */       }
/*      */ 
/* 1163 */       if (encoding == null) {
/* 1164 */         writeStartDocument(version);
/*      */ 
/* 1166 */         return;
/*      */       }
/*      */ 
/* 1169 */       String streamEncoding = null;
/* 1170 */       if ((this.fWriter instanceof OutputStreamWriter)) {
/* 1171 */         streamEncoding = ((OutputStreamWriter)this.fWriter).getEncoding();
/*      */       }
/* 1173 */       else if ((this.fWriter instanceof UTF8OutputStreamWriter)) {
/* 1174 */         streamEncoding = ((UTF8OutputStreamWriter)this.fWriter).getEncoding();
/*      */       }
/* 1176 */       else if ((this.fWriter instanceof XMLWriter)) {
/* 1177 */         streamEncoding = ((OutputStreamWriter)((XMLWriter)this.fWriter).getWriter()).getEncoding();
/*      */       }
/*      */ 
/* 1180 */       if ((streamEncoding != null) && (!streamEncoding.equalsIgnoreCase(encoding)))
/*      */       {
/* 1182 */         boolean foundAlias = false;
/* 1183 */         Set aliases = Charset.forName(encoding).aliases();
/* 1184 */         for (Iterator it = aliases.iterator(); (!foundAlias) && (it.hasNext()); ) {
/* 1185 */           if (streamEncoding.equalsIgnoreCase((String)it.next())) {
/* 1186 */             foundAlias = true;
/*      */           }
/*      */         }
/*      */ 
/* 1190 */         if (!foundAlias) {
/* 1191 */           throw new XMLStreamException("Underlying stream encoding '" + streamEncoding + "' and input paramter for writeStartDocument() method '" + encoding + "' do not match.");
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1199 */       this.fWriter.write("<?xml version=\"");
/*      */ 
/* 1201 */       if ((version == null) || (version.equals("")))
/* 1202 */         this.fWriter.write("1.0");
/*      */       else {
/* 1204 */         this.fWriter.write(version);
/*      */       }
/*      */ 
/* 1207 */       if (!encoding.equals("")) {
/* 1208 */         this.fWriter.write("\" encoding=\"");
/* 1209 */         this.fWriter.write(encoding);
/*      */       }
/*      */ 
/* 1212 */       this.fWriter.write("\"?>");
/*      */     } catch (IOException ex) {
/* 1214 */       throw new XMLStreamException(ex);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void writeStartElement(String localName)
/*      */     throws XMLStreamException
/*      */   {
/*      */     try
/*      */     {
/* 1224 */       if (localName == null) {
/* 1225 */         throw new XMLStreamException("Local Name cannot be null");
/*      */       }
/*      */ 
/* 1228 */       if (this.fStartTagOpened) {
/* 1229 */         closeStartTag();
/*      */       }
/*      */ 
/* 1232 */       openStartTag();
/* 1233 */       this.fElementStack.push(null, localName, null, null, false);
/* 1234 */       this.fInternalNamespaceContext.pushContext();
/*      */ 
/* 1236 */       if (this.fIsRepairingNamespace) {
/* 1237 */         return;
/*      */       }
/*      */ 
/* 1240 */       this.fWriter.write(localName);
/*      */     } catch (IOException ex) {
/* 1242 */       throw new XMLStreamException(ex);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void writeStartElement(String namespaceURI, String localName)
/*      */     throws XMLStreamException
/*      */   {
/* 1253 */     if (localName == null) {
/* 1254 */       throw new XMLStreamException("Local Name cannot be null");
/*      */     }
/*      */ 
/* 1257 */     if (namespaceURI == null) {
/* 1258 */       throw new XMLStreamException("NamespaceURI cannot be null");
/*      */     }
/*      */ 
/* 1261 */     namespaceURI = this.fSymbolTable.addSymbol(namespaceURI);
/*      */ 
/* 1263 */     String prefix = null;
/*      */ 
/* 1265 */     if (!this.fIsRepairingNamespace) {
/* 1266 */       prefix = this.fNamespaceContext.getPrefix(namespaceURI);
/*      */ 
/* 1268 */       if (prefix != null) {
/* 1269 */         prefix = this.fSymbolTable.addSymbol(prefix);
/*      */       }
/*      */     }
/*      */ 
/* 1273 */     writeStartElement(prefix, localName, namespaceURI);
/*      */   }
/*      */ 
/*      */   public void writeStartElement(String prefix, String localName, String namespaceURI)
/*      */     throws XMLStreamException
/*      */   {
/*      */     try
/*      */     {
/* 1285 */       if (localName == null) {
/* 1286 */         throw new XMLStreamException("Local Name cannot be null");
/*      */       }
/*      */ 
/* 1289 */       if (namespaceURI == null) {
/* 1290 */         throw new XMLStreamException("NamespaceURI cannot be null");
/*      */       }
/*      */ 
/* 1293 */       if ((!this.fIsRepairingNamespace) && 
/* 1294 */         (prefix == null)) {
/* 1295 */         throw new XMLStreamException("Prefix cannot be null");
/*      */       }
/*      */ 
/* 1299 */       if (this.fStartTagOpened) {
/* 1300 */         closeStartTag();
/*      */       }
/*      */ 
/* 1303 */       openStartTag();
/* 1304 */       namespaceURI = this.fSymbolTable.addSymbol(namespaceURI);
/*      */ 
/* 1306 */       if (prefix != null) {
/* 1307 */         prefix = this.fSymbolTable.addSymbol(prefix);
/*      */       }
/*      */ 
/* 1310 */       this.fElementStack.push(prefix, localName, null, namespaceURI, false);
/* 1311 */       this.fInternalNamespaceContext.pushContext();
/*      */ 
/* 1313 */       String tmpPrefix = this.fNamespaceContext.getPrefix(namespaceURI);
/*      */ 
/* 1316 */       if ((prefix != null) && ((tmpPrefix == null) || (!prefix.equals(tmpPrefix))))
/*      */       {
/* 1318 */         this.fInternalNamespaceContext.declarePrefix(prefix, namespaceURI);
/*      */       }
/*      */ 
/* 1322 */       if (this.fIsRepairingNamespace) {
/* 1323 */         if ((prefix == null) || ((tmpPrefix != null) && (prefix.equals(tmpPrefix))))
/*      */         {
/* 1325 */           return;
/*      */         }
/*      */ 
/* 1328 */         QName qname = new QName();
/* 1329 */         qname.setValues(prefix, "xmlns", null, namespaceURI);
/*      */ 
/* 1331 */         this.fNamespaceDecls.add(qname);
/*      */ 
/* 1333 */         return;
/*      */       }
/*      */ 
/* 1336 */       if ((prefix != null) && (prefix != "")) {
/* 1337 */         this.fWriter.write(prefix);
/* 1338 */         this.fWriter.write(":");
/*      */       }
/*      */ 
/* 1341 */       this.fWriter.write(localName);
/*      */     }
/*      */     catch (IOException ex) {
/* 1344 */       throw new XMLStreamException(ex);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void writeXMLContent(char[] content, int start, int length, boolean escapeChars)
/*      */     throws IOException
/*      */   {
/* 1354 */     if (!escapeChars) {
/* 1355 */       this.fWriter.write(content, start, length);
/*      */ 
/* 1357 */       return;
/*      */     }
/*      */ 
/* 1361 */     int startWritePos = start;
/*      */ 
/* 1363 */     int end = start + length;
/*      */ 
/* 1365 */     for (int index = start; index < end; index++) {
/* 1366 */       char ch = content[index];
/*      */ 
/* 1368 */       if ((this.fEncoder != null) && (!this.fEncoder.canEncode(ch))) {
/* 1369 */         this.fWriter.write(content, startWritePos, index - startWritePos);
/*      */ 
/* 1372 */         this.fWriter.write("&#x");
/* 1373 */         this.fWriter.write(Integer.toHexString(ch));
/* 1374 */         this.fWriter.write(59);
/* 1375 */         startWritePos = index + 1;
/*      */       }
/*      */       else
/*      */       {
/* 1379 */         switch (ch) {
/*      */         case '<':
/* 1381 */           this.fWriter.write(content, startWritePos, index - startWritePos);
/* 1382 */           this.fWriter.write("&lt;");
/* 1383 */           startWritePos = index + 1;
/*      */ 
/* 1385 */           break;
/*      */         case '&':
/* 1388 */           this.fWriter.write(content, startWritePos, index - startWritePos);
/* 1389 */           this.fWriter.write("&amp;");
/* 1390 */           startWritePos = index + 1;
/*      */ 
/* 1392 */           break;
/*      */         case '>':
/* 1395 */           this.fWriter.write(content, startWritePos, index - startWritePos);
/* 1396 */           this.fWriter.write("&gt;");
/* 1397 */           startWritePos = index + 1;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1404 */     this.fWriter.write(content, startWritePos, end - startWritePos);
/*      */   }
/*      */ 
/*      */   private void writeXMLContent(String content) throws IOException {
/* 1408 */     if ((content != null) && (content.length() > 0))
/* 1409 */       writeXMLContent(content, this.fEscapeCharacters, false);
/*      */   }
/*      */ 
/*      */   private void writeXMLContent(String content, boolean escapeChars, boolean escapeDoubleQuotes)
/*      */     throws IOException
/*      */   {
/* 1425 */     if (!escapeChars) {
/* 1426 */       this.fWriter.write(content);
/*      */ 
/* 1428 */       return;
/*      */     }
/*      */ 
/* 1432 */     int startWritePos = 0;
/*      */ 
/* 1434 */     int end = content.length();
/*      */ 
/* 1436 */     for (int index = 0; index < end; index++) {
/* 1437 */       char ch = content.charAt(index);
/*      */ 
/* 1439 */       if ((this.fEncoder != null) && (!this.fEncoder.canEncode(ch))) {
/* 1440 */         this.fWriter.write(content, startWritePos, index - startWritePos);
/*      */ 
/* 1443 */         this.fWriter.write("&#x");
/* 1444 */         this.fWriter.write(Integer.toHexString(ch));
/* 1445 */         this.fWriter.write(59);
/* 1446 */         startWritePos = index + 1;
/*      */       }
/*      */       else
/*      */       {
/* 1450 */         switch (ch) {
/*      */         case '<':
/* 1452 */           this.fWriter.write(content, startWritePos, index - startWritePos);
/* 1453 */           this.fWriter.write("&lt;");
/* 1454 */           startWritePos = index + 1;
/*      */ 
/* 1456 */           break;
/*      */         case '&':
/* 1459 */           this.fWriter.write(content, startWritePos, index - startWritePos);
/* 1460 */           this.fWriter.write("&amp;");
/* 1461 */           startWritePos = index + 1;
/*      */ 
/* 1463 */           break;
/*      */         case '>':
/* 1466 */           this.fWriter.write(content, startWritePos, index - startWritePos);
/* 1467 */           this.fWriter.write("&gt;");
/* 1468 */           startWritePos = index + 1;
/*      */ 
/* 1470 */           break;
/*      */         case '"':
/* 1473 */           this.fWriter.write(content, startWritePos, index - startWritePos);
/* 1474 */           if (escapeDoubleQuotes)
/* 1475 */             this.fWriter.write("&quot;");
/*      */           else {
/* 1477 */             this.fWriter.write(34);
/*      */           }
/* 1479 */           startWritePos = index + 1;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1486 */     this.fWriter.write(content, startWritePos, end - startWritePos);
/*      */   }
/*      */ 
/*      */   private void closeStartTag()
/*      */     throws XMLStreamException
/*      */   {
/*      */     try
/*      */     {
/* 1494 */       ElementState currentElement = this.fElementStack.peek();
/*      */ 
/* 1496 */       if (this.fIsRepairingNamespace) {
/* 1497 */         repair();
/* 1498 */         correctPrefix(currentElement, 1);
/*      */ 
/* 1500 */         if ((currentElement.prefix != null) && (currentElement.prefix != ""))
/*      */         {
/* 1502 */           this.fWriter.write(currentElement.prefix);
/* 1503 */           this.fWriter.write(":");
/*      */         }
/*      */ 
/* 1506 */         this.fWriter.write(currentElement.localpart);
/*      */ 
/* 1508 */         int len = this.fNamespaceDecls.size();
/* 1509 */         QName qname = null;
/*      */ 
/* 1511 */         for (int i = 0; i < len; i++) {
/* 1512 */           qname = (QName)this.fNamespaceDecls.get(i);
/*      */ 
/* 1514 */           if ((qname != null) && 
/* 1515 */             (this.fInternalNamespaceContext.declarePrefix(qname.prefix, qname.uri)))
/*      */           {
/* 1517 */             writenamespace(qname.prefix, qname.uri);
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1522 */         this.fNamespaceDecls.clear();
/*      */ 
/* 1524 */         Attribute attr = null;
/*      */ 
/* 1526 */         for (int j = 0; j < this.fAttributeCache.size(); j++) {
/* 1527 */           attr = (Attribute)this.fAttributeCache.get(j);
/*      */ 
/* 1529 */           if ((attr.prefix != null) && (attr.uri != null) && 
/* 1530 */             (!attr.prefix.equals("")) && (!attr.uri.equals(""))) {
/* 1531 */             String tmp = this.fInternalNamespaceContext.getPrefix(attr.uri);
/*      */ 
/* 1533 */             if ((tmp == null) || (tmp != attr.prefix)) {
/* 1534 */               tmp = getAttrPrefix(attr.uri);
/* 1535 */               if (tmp == null) {
/* 1536 */                 if (this.fInternalNamespaceContext.declarePrefix(attr.prefix, attr.uri))
/*      */                 {
/* 1538 */                   writenamespace(attr.prefix, attr.uri);
/*      */                 }
/*      */               }
/* 1541 */               else writenamespace(attr.prefix, attr.uri);
/*      */ 
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/* 1547 */           writeAttributeWithPrefix(attr.prefix, attr.localpart, attr.value);
/*      */         }
/*      */ 
/* 1550 */         this.fAttrNamespace = null;
/* 1551 */         this.fAttributeCache.clear();
/*      */       }
/*      */ 
/* 1554 */       if (currentElement.isEmpty) {
/* 1555 */         this.fElementStack.pop();
/* 1556 */         this.fInternalNamespaceContext.popContext();
/* 1557 */         this.fWriter.write("/>");
/*      */       } else {
/* 1559 */         this.fWriter.write(62);
/*      */       }
/*      */ 
/* 1562 */       this.fStartTagOpened = false;
/*      */     } catch (IOException ex) {
/* 1564 */       this.fStartTagOpened = false;
/* 1565 */       throw new XMLStreamException(ex);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void openStartTag()
/*      */     throws IOException
/*      */   {
/* 1573 */     this.fStartTagOpened = true;
/* 1574 */     this.fWriter.write(60);
/*      */   }
/*      */ 
/*      */   private void correctPrefix(QName attr, int type)
/*      */   {
/* 1583 */     String tmpPrefix = null;
/*      */ 
/* 1586 */     String prefix = attr.prefix;
/* 1587 */     String uri = attr.uri;
/* 1588 */     boolean isSpecialCaseURI = false;
/*      */ 
/* 1590 */     if ((prefix == null) || (prefix.equals(""))) {
/* 1591 */       if (uri == null) {
/* 1592 */         return;
/*      */       }
/*      */ 
/* 1595 */       if ((prefix == "") && (uri == "")) {
/* 1596 */         return;
/*      */       }
/* 1598 */       uri = this.fSymbolTable.addSymbol(uri);
/*      */ 
/* 1600 */       QName decl = null;
/*      */ 
/* 1602 */       for (int i = 0; i < this.fNamespaceDecls.size(); i++) {
/* 1603 */         decl = (QName)this.fNamespaceDecls.get(i);
/*      */ 
/* 1605 */         if ((decl != null) && (decl.uri == attr.uri)) {
/* 1606 */           attr.prefix = decl.prefix;
/*      */ 
/* 1608 */           return;
/*      */         }
/*      */       }
/*      */ 
/* 1612 */       tmpPrefix = this.fNamespaceContext.getPrefix(uri);
/*      */ 
/* 1614 */       if (tmpPrefix == "") {
/* 1615 */         if (type == 1) {
/* 1616 */           return;
/*      */         }
/* 1618 */         if (type == 10)
/*      */         {
/* 1620 */           tmpPrefix = getAttrPrefix(uri);
/* 1621 */           isSpecialCaseURI = true;
/*      */         }
/*      */       }
/*      */ 
/* 1625 */       if (tmpPrefix == null) {
/* 1626 */         StringBuffer genPrefix = new StringBuffer("zdef");
/*      */ 
/* 1628 */         for (int i = 0; i < 1; i++) {
/* 1629 */           genPrefix.append(this.fPrefixGen.nextInt());
/*      */         }
/*      */ 
/* 1632 */         prefix = genPrefix.toString();
/* 1633 */         prefix = this.fSymbolTable.addSymbol(prefix);
/*      */       } else {
/* 1635 */         prefix = this.fSymbolTable.addSymbol(tmpPrefix);
/*      */       }
/*      */ 
/* 1638 */       if (tmpPrefix == null) {
/* 1639 */         if (isSpecialCaseURI) {
/* 1640 */           addAttrNamespace(prefix, uri);
/*      */         } else {
/* 1642 */           QName qname = new QName();
/* 1643 */           qname.setValues(prefix, "xmlns", null, uri);
/* 1644 */           this.fNamespaceDecls.add(qname);
/* 1645 */           this.fInternalNamespaceContext.declarePrefix(this.fSymbolTable.addSymbol(prefix), uri);
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1651 */     attr.prefix = prefix;
/*      */   }
/*      */ 
/*      */   private String getAttrPrefix(String uri)
/*      */   {
/* 1658 */     if (this.fAttrNamespace != null) {
/* 1659 */       return (String)this.fAttrNamespace.get(uri);
/*      */     }
/* 1661 */     return null;
/*      */   }
/*      */   private void addAttrNamespace(String prefix, String uri) {
/* 1664 */     if (this.fAttrNamespace == null) {
/* 1665 */       this.fAttrNamespace = new HashMap();
/*      */     }
/* 1667 */     this.fAttrNamespace.put(prefix, uri);
/*      */   }
/*      */ 
/*      */   private boolean isDefaultNamespace(String uri)
/*      */   {
/* 1674 */     String defaultNamespace = this.fInternalNamespaceContext.getURI(this.DEFAULT_PREFIX);
/*      */ 
/* 1676 */     if (uri == defaultNamespace) {
/* 1677 */       return true;
/*      */     }
/*      */ 
/* 1680 */     return false;
/*      */   }
/*      */ 
/*      */   private boolean checkUserNamespaceContext(String prefix, String uri)
/*      */   {
/* 1689 */     if (this.fNamespaceContext.userContext != null) {
/* 1690 */       String tmpURI = this.fNamespaceContext.userContext.getNamespaceURI(prefix);
/*      */ 
/* 1692 */       if ((tmpURI != null) && (tmpURI.equals(uri))) {
/* 1693 */         return true;
/*      */       }
/*      */     }
/*      */ 
/* 1697 */     return false;
/*      */   }
/*      */ 
/*      */   protected void repair()
/*      */   {
/* 1704 */     Attribute attr = null;
/* 1705 */     Attribute attr2 = null;
/* 1706 */     ElementState currentElement = this.fElementStack.peek();
/* 1707 */     removeDuplicateDecls();
/*      */ 
/* 1709 */     for (int i = 0; i < this.fAttributeCache.size(); i++) {
/* 1710 */       attr = (Attribute)this.fAttributeCache.get(i);
/* 1711 */       if (((attr.prefix != null) && (!attr.prefix.equals(""))) || ((attr.uri != null) && (!attr.uri.equals("")))) {
/* 1712 */         correctPrefix(currentElement, attr);
/*      */       }
/*      */     }
/*      */ 
/* 1716 */     if ((!isDeclared(currentElement)) && 
/* 1717 */       (currentElement.prefix != null) && (currentElement.uri != null))
/*      */     {
/* 1719 */       if ((!currentElement.prefix.equals("")) && (!currentElement.uri.equals(""))) {
/* 1720 */         this.fNamespaceDecls.add(currentElement);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1725 */     for (int i = 0; i < this.fAttributeCache.size(); i++) {
/* 1726 */       attr = (Attribute)this.fAttributeCache.get(i);
/* 1727 */       for (int j = i + 1; j < this.fAttributeCache.size(); j++) {
/* 1728 */         attr2 = (Attribute)this.fAttributeCache.get(j);
/* 1729 */         if ((!"".equals(attr.prefix)) && (!"".equals(attr2.prefix))) {
/* 1730 */           correctPrefix(attr, attr2);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1735 */     repairNamespaceDecl(currentElement);
/*      */ 
/* 1737 */     int i = 0;
/*      */ 
/* 1739 */     for (i = 0; i < this.fAttributeCache.size(); i++) {
/* 1740 */       attr = (Attribute)this.fAttributeCache.get(i);
/*      */ 
/* 1744 */       if ((attr.prefix != null) && (attr.prefix.equals("")) && (attr.uri != null) && (attr.uri.equals(""))) {
/* 1745 */         repairNamespaceDecl(attr);
/*      */       }
/*      */     }
/*      */ 
/* 1749 */     QName qname = null;
/*      */ 
/* 1751 */     for (i = 0; i < this.fNamespaceDecls.size(); i++) {
/* 1752 */       qname = (QName)this.fNamespaceDecls.get(i);
/*      */ 
/* 1754 */       if (qname != null) {
/* 1755 */         this.fInternalNamespaceContext.declarePrefix(qname.prefix, qname.uri);
/*      */       }
/*      */     }
/*      */ 
/* 1759 */     for (i = 0; i < this.fAttributeCache.size(); i++) {
/* 1760 */       attr = (Attribute)this.fAttributeCache.get(i);
/* 1761 */       correctPrefix(attr, 10);
/*      */     }
/*      */   }
/*      */ 
/*      */   void correctPrefix(QName attr1, QName attr2)
/*      */   {
/* 1773 */     String tmpPrefix = null;
/* 1774 */     QName decl = null;
/* 1775 */     boolean done = false;
/*      */ 
/* 1777 */     checkForNull(attr1);
/* 1778 */     checkForNull(attr2);
/*      */ 
/* 1780 */     if ((attr1.prefix.equals(attr2.prefix)) && (!attr1.uri.equals(attr2.uri)))
/*      */     {
/* 1782 */       tmpPrefix = this.fNamespaceContext.getPrefix(attr2.uri);
/*      */ 
/* 1784 */       if (tmpPrefix != null) {
/* 1785 */         attr2.prefix = this.fSymbolTable.addSymbol(tmpPrefix);
/*      */       } else {
/* 1787 */         decl = null;
/* 1788 */         for (int n = 0; n < this.fNamespaceDecls.size(); n++) {
/* 1789 */           decl = (QName)this.fNamespaceDecls.get(n);
/* 1790 */           if ((decl != null) && (decl.uri == attr2.uri)) {
/* 1791 */             attr2.prefix = decl.prefix;
/*      */ 
/* 1793 */             return;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1798 */         StringBuffer genPrefix = new StringBuffer("zdef");
/*      */ 
/* 1800 */         for (int k = 0; k < 1; k++) {
/* 1801 */           genPrefix.append(this.fPrefixGen.nextInt());
/*      */         }
/*      */ 
/* 1804 */         tmpPrefix = genPrefix.toString();
/* 1805 */         tmpPrefix = this.fSymbolTable.addSymbol(tmpPrefix);
/* 1806 */         attr2.prefix = tmpPrefix;
/*      */ 
/* 1808 */         QName qname = new QName();
/* 1809 */         qname.setValues(tmpPrefix, "xmlns", null, attr2.uri);
/*      */ 
/* 1811 */         this.fNamespaceDecls.add(qname);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   void checkForNull(QName attr) {
/* 1817 */     if (attr.prefix == null) attr.prefix = "";
/* 1818 */     if (attr.uri == null) attr.uri = "";
/*      */   }
/*      */ 
/*      */   void removeDuplicateDecls()
/*      */   {
/* 1823 */     for (int i = 0; i < this.fNamespaceDecls.size(); i++) {
/* 1824 */       QName decl1 = (QName)this.fNamespaceDecls.get(i);
/* 1825 */       if (decl1 != null)
/* 1826 */         for (int j = i + 1; j < this.fNamespaceDecls.size(); j++) {
/* 1827 */           QName decl2 = (QName)this.fNamespaceDecls.get(j);
/*      */ 
/* 1830 */           if ((decl2 != null) && (decl1.prefix.equals(decl2.prefix)) && (decl1.uri.equals(decl2.uri)))
/* 1831 */             this.fNamespaceDecls.remove(j);
/*      */         }
/*      */     }
/*      */   }
/*      */ 
/*      */   void repairNamespaceDecl(QName attr)
/*      */   {
/* 1845 */     QName decl = null;
/*      */ 
/* 1849 */     for (int j = 0; j < this.fNamespaceDecls.size(); j++) {
/* 1850 */       decl = (QName)this.fNamespaceDecls.get(j);
/*      */ 
/* 1852 */       if ((decl != null) && 
/* 1853 */         (attr.prefix != null) && (attr.prefix.equals(decl.prefix)) && (!attr.uri.equals(decl.uri)))
/*      */       {
/* 1856 */         String tmpURI = this.fNamespaceContext.getNamespaceURI(attr.prefix);
/*      */ 
/* 1859 */         if (tmpURI != null)
/* 1860 */           if (tmpURI.equals(attr.uri))
/* 1861 */             this.fNamespaceDecls.set(j, null);
/*      */           else
/* 1863 */             decl.uri = attr.uri;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   boolean isDeclared(QName attr)
/*      */   {
/* 1872 */     QName decl = null;
/*      */ 
/* 1874 */     for (int n = 0; n < this.fNamespaceDecls.size(); n++) {
/* 1875 */       decl = (QName)this.fNamespaceDecls.get(n);
/*      */ 
/* 1877 */       if ((attr.prefix != null) && (attr.prefix == decl.prefix) && (decl.uri == attr.uri))
/*      */       {
/* 1879 */         return true;
/*      */       }
/*      */     }
/*      */ 
/* 1883 */     if ((attr.uri != null) && 
/* 1884 */       (this.fNamespaceContext.getPrefix(attr.uri) != null)) {
/* 1885 */       return true;
/*      */     }
/*      */ 
/* 1889 */     return false;
/*      */   }
/*      */ 
/*      */   public int size()
/*      */   {
/* 2138 */     return 1;
/*      */   }
/*      */ 
/*      */   public boolean isEmpty() {
/* 2142 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean containsKey(Object key) {
/* 2146 */     return key.equals("sjsxp-outputstream");
/*      */   }
/*      */ 
/*      */   public Object get(Object key)
/*      */   {
/* 2154 */     if (key.equals("sjsxp-outputstream")) {
/* 2155 */       return this.fOutputStream;
/*      */     }
/* 2157 */     return null;
/*      */   }
/*      */ 
/*      */   public Set entrySet() {
/* 2161 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/* 2171 */     return getClass().getName() + "@" + Integer.toHexString(hashCode());
/*      */   }
/*      */ 
/*      */   public int hashCode()
/*      */   {
/* 2179 */     return this.fElementStack.hashCode();
/*      */   }
/*      */ 
/*      */   public boolean equals(Object obj)
/*      */   {
/* 2186 */     return this == obj;
/*      */   }
/*      */ 
/*      */   class Attribute extends QName
/*      */   {
/*      */     String value;
/*      */ 
/*      */     Attribute(String value)
/*      */     {
/* 2032 */       this.value = value;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class ElementStack
/*      */   {
/*      */     protected XMLStreamWriterImpl.ElementState[] fElements;
/*      */     protected short fDepth;
/*      */ 
/*      */     public ElementStack()
/*      */     {
/* 1905 */       this.fElements = new XMLStreamWriterImpl.ElementState[10];
/*      */ 
/* 1907 */       for (int i = 0; i < this.fElements.length; i++)
/* 1908 */         this.fElements[i] = new XMLStreamWriterImpl.ElementState(XMLStreamWriterImpl.this);
/*      */     }
/*      */ 
/*      */     public XMLStreamWriterImpl.ElementState push(XMLStreamWriterImpl.ElementState element)
/*      */     {
/* 1926 */       if (this.fDepth == this.fElements.length) {
/* 1927 */         XMLStreamWriterImpl.ElementState[] array = new XMLStreamWriterImpl.ElementState[this.fElements.length * 2];
/* 1928 */         System.arraycopy(this.fElements, 0, array, 0, this.fDepth);
/* 1929 */         this.fElements = array;
/*      */ 
/* 1931 */         for (int i = this.fDepth; i < this.fElements.length; i++) {
/* 1932 */           this.fElements[i] = new XMLStreamWriterImpl.ElementState(XMLStreamWriterImpl.this);
/*      */         }
/*      */       }
/*      */ 
/* 1936 */       this.fElements[this.fDepth].setValues(element);
/*      */ 
/* 1938 */       return this.fElements[(this.fDepth++)];
/*      */     }
/*      */ 
/*      */     public XMLStreamWriterImpl.ElementState push(String prefix, String localpart, String rawname, String uri, boolean isEmpty)
/*      */     {
/* 1952 */       if (this.fDepth == this.fElements.length) {
/* 1953 */         XMLStreamWriterImpl.ElementState[] array = new XMLStreamWriterImpl.ElementState[this.fElements.length * 2];
/* 1954 */         System.arraycopy(this.fElements, 0, array, 0, this.fDepth);
/* 1955 */         this.fElements = array;
/*      */ 
/* 1957 */         for (int i = this.fDepth; i < this.fElements.length; i++) {
/* 1958 */           this.fElements[i] = new XMLStreamWriterImpl.ElementState(XMLStreamWriterImpl.this);
/*      */         }
/*      */       }
/*      */ 
/* 1962 */       this.fElements[this.fDepth].setValues(prefix, localpart, rawname, uri, isEmpty);
/*      */ 
/* 1964 */       return this.fElements[(this.fDepth++)];
/*      */     }
/*      */ 
/*      */     public XMLStreamWriterImpl.ElementState pop()
/*      */     {
/* 1976 */       return this.fElements[(this.fDepth = (short)(this.fDepth - 1))];
/*      */     }
/*      */ 
/*      */     public void clear()
/*      */     {
/* 1981 */       this.fDepth = 0;
/*      */     }
/*      */ 
/*      */     public XMLStreamWriterImpl.ElementState peek()
/*      */     {
/* 1992 */       return this.fElements[(this.fDepth - 1)];
/*      */     }
/*      */ 
/*      */     public boolean empty()
/*      */     {
/* 2000 */       return this.fDepth <= 0;
/*      */     }
/*      */   }
/*      */ 
/*      */   class ElementState extends QName
/*      */   {
/* 2008 */     public boolean isEmpty = false;
/*      */ 
/*      */     public ElementState() {
/*      */     }
/*      */ 
/*      */     public ElementState(String prefix, String localpart, String rawname, String uri) {
/* 2014 */       super(localpart, rawname, uri);
/*      */     }
/*      */ 
/*      */     public void setValues(String prefix, String localpart, String rawname, String uri, boolean isEmpty)
/*      */     {
/* 2019 */       super.setValues(prefix, localpart, rawname, uri);
/* 2020 */       this.isEmpty = isEmpty;
/*      */     }
/*      */   }
/*      */ 
/*      */   class NamespaceContextImpl
/*      */     implements NamespaceContext
/*      */   {
/* 2042 */     NamespaceContext userContext = null;
/*      */ 
/* 2045 */     NamespaceSupport internalContext = null;
/*      */ 
/*      */     NamespaceContextImpl() {  } 
/* 2048 */     public String getNamespaceURI(String prefix) { String uri = null;
/*      */ 
/* 2050 */       if (prefix != null) {
/* 2051 */         prefix = XMLStreamWriterImpl.this.fSymbolTable.addSymbol(prefix);
/*      */       }
/*      */ 
/* 2054 */       if (this.internalContext != null) {
/* 2055 */         uri = this.internalContext.getURI(prefix);
/*      */ 
/* 2057 */         if (uri != null) {
/* 2058 */           return uri;
/*      */         }
/*      */       }
/*      */ 
/* 2062 */       if (this.userContext != null) {
/* 2063 */         uri = this.userContext.getNamespaceURI(prefix);
/*      */ 
/* 2065 */         return uri;
/*      */       }
/*      */ 
/* 2068 */       return null; }
/*      */ 
/*      */     public String getPrefix(String uri)
/*      */     {
/* 2072 */       String prefix = null;
/*      */ 
/* 2074 */       if (uri != null) {
/* 2075 */         uri = XMLStreamWriterImpl.this.fSymbolTable.addSymbol(uri);
/*      */       }
/*      */ 
/* 2078 */       if (this.internalContext != null) {
/* 2079 */         prefix = this.internalContext.getPrefix(uri);
/*      */ 
/* 2081 */         if (prefix != null) {
/* 2082 */           return prefix;
/*      */         }
/*      */       }
/*      */ 
/* 2086 */       if (this.userContext != null) {
/* 2087 */         return this.userContext.getPrefix(uri);
/*      */       }
/*      */ 
/* 2090 */       return null;
/*      */     }
/*      */ 
/*      */     public Iterator getPrefixes(String uri) {
/* 2094 */       Vector prefixes = null;
/* 2095 */       Iterator itr = null;
/*      */ 
/* 2097 */       if (uri != null) {
/* 2098 */         uri = XMLStreamWriterImpl.this.fSymbolTable.addSymbol(uri);
/*      */       }
/*      */ 
/* 2101 */       if (this.userContext != null) {
/* 2102 */         itr = this.userContext.getPrefixes(uri);
/*      */       }
/*      */ 
/* 2105 */       if (this.internalContext != null) {
/* 2106 */         prefixes = this.internalContext.getPrefixes(uri);
/*      */       }
/*      */ 
/* 2109 */       if ((prefixes == null) && (itr != null))
/* 2110 */         return itr;
/* 2111 */       if ((prefixes != null) && (itr == null))
/* 2112 */         return new ReadOnlyIterator(prefixes.iterator());
/* 2113 */       if ((prefixes != null) && (itr != null)) {
/* 2114 */         String ob = null;
/*      */ 
/* 2116 */         while (itr.hasNext()) {
/* 2117 */           ob = (String)itr.next();
/*      */ 
/* 2119 */           if (ob != null) {
/* 2120 */             ob = XMLStreamWriterImpl.this.fSymbolTable.addSymbol(ob);
/*      */           }
/*      */ 
/* 2123 */           if (!prefixes.contains(ob)) {
/* 2124 */             prefixes.add(ob);
/*      */           }
/*      */         }
/*      */ 
/* 2128 */         return new ReadOnlyIterator(prefixes.iterator());
/*      */       }
/*      */ 
/* 2131 */       return XMLStreamWriterImpl.this.fReadOnlyIterator;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.stream.writers.XMLStreamWriterImpl
 * JD-Core Version:    0.6.2
 */