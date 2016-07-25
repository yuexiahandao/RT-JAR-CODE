/*      */ package com.sun.org.apache.xml.internal.serialize;
/*      */ 
/*      */ import com.sun.org.apache.xerces.internal.dom.DOMMessageFormatter;
/*      */ import com.sun.org.apache.xerces.internal.util.NamespaceSupport;
/*      */ import com.sun.org.apache.xerces.internal.util.SymbolTable;
/*      */ import com.sun.org.apache.xerces.internal.util.XMLChar;
/*      */ import com.sun.org.apache.xerces.internal.util.XMLSymbols;
/*      */ import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
/*      */ import java.io.IOException;
/*      */ import java.io.OutputStream;
/*      */ import java.io.Writer;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Hashtable;
/*      */ import org.w3c.dom.Attr;
/*      */ import org.w3c.dom.DOMErrorHandler;
/*      */ import org.w3c.dom.Element;
/*      */ import org.w3c.dom.NamedNodeMap;
/*      */ import org.w3c.dom.Node;
/*      */ import org.w3c.dom.ls.LSSerializerFilter;
/*      */ import org.xml.sax.AttributeList;
/*      */ import org.xml.sax.Attributes;
/*      */ import org.xml.sax.SAXException;
/*      */ import org.xml.sax.helpers.AttributesImpl;
/*      */ 
/*      */ public class XMLSerializer extends BaseMarkupSerializer
/*      */ {
/*      */   protected static final boolean DEBUG = false;
/*      */   protected NamespaceSupport fNSBinder;
/*      */   protected NamespaceSupport fLocalNSBinder;
/*      */   protected SymbolTable fSymbolTable;
/*      */   protected static final String PREFIX = "NS";
/*  128 */   protected boolean fNamespaces = false;
/*      */ 
/*  133 */   protected boolean fNamespacePrefixes = true;
/*      */   private boolean fPreserveSpace;
/*      */ 
/*      */   public XMLSerializer()
/*      */   {
/*  145 */     super(new OutputFormat("xml", null, false));
/*      */   }
/*      */ 
/*      */   public XMLSerializer(OutputFormat format)
/*      */   {
/*  155 */     super(format != null ? format : new OutputFormat("xml", null, false));
/*  156 */     this._format.setMethod("xml");
/*      */   }
/*      */ 
/*      */   public XMLSerializer(Writer writer, OutputFormat format)
/*      */   {
/*  169 */     super(format != null ? format : new OutputFormat("xml", null, false));
/*  170 */     this._format.setMethod("xml");
/*  171 */     setOutputCharStream(writer);
/*      */   }
/*      */ 
/*      */   public XMLSerializer(OutputStream output, OutputFormat format)
/*      */   {
/*  184 */     super(format != null ? format : new OutputFormat("xml", null, false));
/*  185 */     this._format.setMethod("xml");
/*  186 */     setOutputByteStream(output);
/*      */   }
/*      */ 
/*      */   public void setOutputFormat(OutputFormat format)
/*      */   {
/*  191 */     super.setOutputFormat(format != null ? format : new OutputFormat("xml", null, false));
/*      */   }
/*      */ 
/*      */   public void setNamespaces(boolean namespaces)
/*      */   {
/*  203 */     this.fNamespaces = namespaces;
/*  204 */     if (this.fNSBinder == null) {
/*  205 */       this.fNSBinder = new NamespaceSupport();
/*  206 */       this.fLocalNSBinder = new NamespaceSupport();
/*  207 */       this.fSymbolTable = new SymbolTable();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void startElement(String namespaceURI, String localName, String rawName, Attributes attrs)
/*      */     throws SAXException
/*      */   {
/*  225 */     boolean addNSAttr = false;
/*      */     try
/*      */     {
/*  233 */       if (this._printer == null) {
/*  234 */         String msg = DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "NoWriterSupplied", null);
/*  235 */         throw new IllegalStateException(msg);
/*      */       }
/*      */ 
/*  238 */       ElementState state = getElementState();
/*  239 */       if (isDocumentState())
/*      */       {
/*  244 */         if (!this._started) {
/*  245 */           startDocument((localName == null) || (localName.length() == 0) ? rawName : localName);
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*  250 */         if (state.empty) {
/*  251 */           this._printer.printText('>');
/*      */         }
/*  253 */         if (state.inCData) {
/*  254 */           this._printer.printText("]]>");
/*  255 */           state.inCData = false;
/*      */         }
/*      */ 
/*  260 */         if ((this._indenting) && (!state.preserveSpace) && ((state.empty) || (state.afterElement) || (state.afterComment)))
/*      */         {
/*  262 */           this._printer.breakLine();
/*      */         }
/*      */       }
/*  264 */       boolean preserveSpace = state.preserveSpace;
/*      */ 
/*  268 */       attrs = extractNamespaces(attrs);
/*      */ 
/*  272 */       if ((rawName == null) || (rawName.length() == 0)) {
/*  273 */         if (localName == null) {
/*  274 */           String msg = DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "NoName", null);
/*  275 */           throw new SAXException(msg);
/*      */         }
/*  277 */         if ((namespaceURI != null) && (!namespaceURI.equals("")))
/*      */         {
/*  279 */           String prefix = getPrefix(namespaceURI);
/*  280 */           if ((prefix != null) && (prefix.length() > 0))
/*  281 */             rawName = prefix + ":" + localName;
/*      */           else
/*  283 */             rawName = localName;
/*      */         } else {
/*  285 */           rawName = localName;
/*  286 */         }addNSAttr = true;
/*      */       }
/*      */ 
/*  289 */       this._printer.printText('<');
/*  290 */       this._printer.printText(rawName);
/*  291 */       this._printer.indent();
/*      */ 
/*  296 */       if (attrs != null) {
/*  297 */         for (int i = 0; i < attrs.getLength(); i++) {
/*  298 */           this._printer.printSpace();
/*      */ 
/*  300 */           String name = attrs.getQName(i);
/*  301 */           if ((name != null) && (name.length() == 0))
/*      */           {
/*  305 */             name = attrs.getLocalName(i);
/*  306 */             String attrURI = attrs.getURI(i);
/*  307 */             if ((attrURI != null) && (attrURI.length() != 0) && ((namespaceURI == null) || (namespaceURI.length() == 0) || (!attrURI.equals(namespaceURI))))
/*      */             {
/*  310 */               String prefix = getPrefix(attrURI);
/*  311 */               if ((prefix != null) && (prefix.length() > 0)) {
/*  312 */                 name = prefix + ":" + name;
/*      */               }
/*      */             }
/*      */           }
/*  316 */           String value = attrs.getValue(i);
/*  317 */           if (value == null)
/*  318 */             value = "";
/*  319 */           this._printer.printText(name);
/*  320 */           this._printer.printText("=\"");
/*  321 */           printEscaped(value);
/*  322 */           this._printer.printText('"');
/*      */ 
/*  327 */           if (name.equals("xml:space")) {
/*  328 */             if (value.equals("preserve"))
/*  329 */               preserveSpace = true;
/*      */             else {
/*  331 */               preserveSpace = this._format.getPreserveSpace();
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*  336 */       if (this._prefixes != null)
/*      */       {
/*  339 */         Enumeration keys = this._prefixes.keys();
/*  340 */         while (keys.hasMoreElements()) {
/*  341 */           this._printer.printSpace();
/*  342 */           String value = (String)keys.nextElement();
/*  343 */           String name = (String)this._prefixes.get(value);
/*  344 */           if (name.length() == 0) {
/*  345 */             this._printer.printText("xmlns=\"");
/*  346 */             printEscaped(value);
/*  347 */             this._printer.printText('"');
/*      */           } else {
/*  349 */             this._printer.printText("xmlns:");
/*  350 */             this._printer.printText(name);
/*  351 */             this._printer.printText("=\"");
/*  352 */             printEscaped(value);
/*  353 */             this._printer.printText('"');
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  361 */       state = enterElementState(namespaceURI, localName, rawName, preserveSpace);
/*  362 */       String name = namespaceURI + "^" + localName;
/*  363 */       state.doCData = this._format.isCDataElement(name);
/*  364 */       state.unescaped = this._format.isNonEscapingElement(name);
/*      */     } catch (IOException except) {
/*  366 */       throw new SAXException(except);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void endElement(String namespaceURI, String localName, String rawName)
/*      */     throws SAXException
/*      */   {
/*      */     try
/*      */     {
/*  376 */       endElementIO(namespaceURI, localName, rawName);
/*      */     } catch (IOException except) {
/*  378 */       throw new SAXException(except);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void endElementIO(String namespaceURI, String localName, String rawName)
/*      */     throws IOException
/*      */   {
/*  394 */     this._printer.unindent();
/*  395 */     ElementState state = getElementState();
/*  396 */     if (state.empty) {
/*  397 */       this._printer.printText("/>");
/*      */     }
/*      */     else {
/*  400 */       if (state.inCData) {
/*  401 */         this._printer.printText("]]>");
/*      */       }
/*      */ 
/*  405 */       if ((this._indenting) && (!state.preserveSpace) && ((state.afterElement) || (state.afterComment)))
/*  406 */         this._printer.breakLine();
/*  407 */       this._printer.printText("</");
/*  408 */       this._printer.printText(state.rawName);
/*  409 */       this._printer.printText('>');
/*      */     }
/*      */ 
/*  413 */     state = leaveElementState();
/*  414 */     state.afterElement = true;
/*  415 */     state.afterComment = false;
/*  416 */     state.empty = false;
/*  417 */     if (isDocumentState())
/*  418 */       this._printer.flush();
/*      */   }
/*      */ 
/*      */   public void startElement(String tagName, AttributeList attrs)
/*      */     throws SAXException
/*      */   {
/*      */     try
/*      */     {
/*  442 */       if (this._printer == null) {
/*  443 */         String msg = DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "NoWriterSupplied", null);
/*  444 */         throw new IllegalStateException(msg);
/*      */       }
/*      */ 
/*  447 */       ElementState state = getElementState();
/*  448 */       if (isDocumentState())
/*      */       {
/*  453 */         if (!this._started) {
/*  454 */           startDocument(tagName);
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*  459 */         if (state.empty) {
/*  460 */           this._printer.printText('>');
/*      */         }
/*  462 */         if (state.inCData) {
/*  463 */           this._printer.printText("]]>");
/*  464 */           state.inCData = false;
/*      */         }
/*      */ 
/*  469 */         if ((this._indenting) && (!state.preserveSpace) && ((state.empty) || (state.afterElement) || (state.afterComment)))
/*      */         {
/*  471 */           this._printer.breakLine();
/*      */         }
/*      */       }
/*  473 */       boolean preserveSpace = state.preserveSpace;
/*      */ 
/*  478 */       this._printer.printText('<');
/*  479 */       this._printer.printText(tagName);
/*  480 */       this._printer.indent();
/*      */ 
/*  485 */       if (attrs != null) {
/*  486 */         for (int i = 0; i < attrs.getLength(); i++) {
/*  487 */           this._printer.printSpace();
/*  488 */           String name = attrs.getName(i);
/*  489 */           String value = attrs.getValue(i);
/*  490 */           if (value != null) {
/*  491 */             this._printer.printText(name);
/*  492 */             this._printer.printText("=\"");
/*  493 */             printEscaped(value);
/*  494 */             this._printer.printText('"');
/*      */           }
/*      */ 
/*  500 */           if (name.equals("xml:space")) {
/*  501 */             if (value.equals("preserve"))
/*  502 */               preserveSpace = true;
/*      */             else {
/*  504 */               preserveSpace = this._format.getPreserveSpace();
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  511 */       state = enterElementState(null, null, tagName, preserveSpace);
/*  512 */       state.doCData = this._format.isCDataElement(tagName);
/*  513 */       state.unescaped = this._format.isNonEscapingElement(tagName);
/*      */     } catch (IOException except) {
/*  515 */       throw new SAXException(except);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void endElement(String tagName)
/*      */     throws SAXException
/*      */   {
/*  524 */     endElement(null, null, tagName);
/*      */   }
/*      */ 
/*      */   protected void startDocument(String rootTagName)
/*      */     throws IOException
/*      */   {
/*  552 */     String dtd = this._printer.leaveDTD();
/*  553 */     if (!this._started)
/*      */     {
/*  555 */       if (!this._format.getOmitXMLDeclaration())
/*      */       {
/*  560 */         StringBuffer buffer = new StringBuffer("<?xml version=\"");
/*  561 */         if (this._format.getVersion() != null)
/*  562 */           buffer.append(this._format.getVersion());
/*      */         else
/*  564 */           buffer.append("1.0");
/*  565 */         buffer.append('"');
/*  566 */         String format_encoding = this._format.getEncoding();
/*  567 */         if (format_encoding != null) {
/*  568 */           buffer.append(" encoding=\"");
/*  569 */           buffer.append(format_encoding);
/*  570 */           buffer.append('"');
/*      */         }
/*  572 */         if ((this._format.getStandalone()) && (this._docTypeSystemId == null) && (this._docTypePublicId == null))
/*      */         {
/*  574 */           buffer.append(" standalone=\"yes\"");
/*  575 */         }buffer.append("?>");
/*  576 */         this._printer.printText(buffer);
/*  577 */         this._printer.breakLine();
/*      */       }
/*      */ 
/*  580 */       if (!this._format.getOmitDocumentType()) {
/*  581 */         if (this._docTypeSystemId != null)
/*      */         {
/*  585 */           this._printer.printText("<!DOCTYPE ");
/*  586 */           this._printer.printText(rootTagName);
/*  587 */           if (this._docTypePublicId != null) {
/*  588 */             this._printer.printText(" PUBLIC ");
/*  589 */             printDoctypeURL(this._docTypePublicId);
/*  590 */             if (this._indenting) {
/*  591 */               this._printer.breakLine();
/*  592 */               for (int i = 0; i < 18 + rootTagName.length(); i++)
/*  593 */                 this._printer.printText(" ");
/*      */             }
/*  595 */             this._printer.printText(" ");
/*  596 */             printDoctypeURL(this._docTypeSystemId);
/*      */           } else {
/*  598 */             this._printer.printText(" SYSTEM ");
/*  599 */             printDoctypeURL(this._docTypeSystemId);
/*      */           }
/*      */ 
/*  604 */           if ((dtd != null) && (dtd.length() > 0)) {
/*  605 */             this._printer.printText(" [");
/*  606 */             printText(dtd, true, true);
/*  607 */             this._printer.printText(']');
/*      */           }
/*      */ 
/*  610 */           this._printer.printText(">");
/*  611 */           this._printer.breakLine();
/*  612 */         } else if ((dtd != null) && (dtd.length() > 0)) {
/*  613 */           this._printer.printText("<!DOCTYPE ");
/*  614 */           this._printer.printText(rootTagName);
/*  615 */           this._printer.printText(" [");
/*  616 */           printText(dtd, true, true);
/*  617 */           this._printer.printText("]>");
/*  618 */           this._printer.breakLine();
/*      */         }
/*      */       }
/*      */     }
/*  622 */     this._started = true;
/*      */ 
/*  624 */     serializePreRoot();
/*      */   }
/*      */ 
/*      */   protected void serializeElement(Element elem)
/*      */     throws IOException
/*      */   {
/*  647 */     if (this.fNamespaces)
/*      */     {
/*  651 */       this.fLocalNSBinder.reset();
/*      */ 
/*  654 */       this.fNSBinder.pushContext();
/*      */     }
/*      */ 
/*  660 */     String tagName = elem.getTagName();
/*  661 */     ElementState state = getElementState();
/*  662 */     if (isDocumentState())
/*      */     {
/*  668 */       if (!this._started) {
/*  669 */         startDocument(tagName);
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*  675 */       if (state.empty) {
/*  676 */         this._printer.printText('>');
/*      */       }
/*  678 */       if (state.inCData) {
/*  679 */         this._printer.printText("]]>");
/*  680 */         state.inCData = false;
/*      */       }
/*      */ 
/*  685 */       if ((this._indenting) && (!state.preserveSpace) && ((state.empty) || (state.afterElement) || (state.afterComment)))
/*      */       {
/*  687 */         this._printer.breakLine();
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  692 */     this.fPreserveSpace = state.preserveSpace;
/*      */ 
/*  695 */     int length = 0;
/*  696 */     NamedNodeMap attrMap = null;
/*      */ 
/*  698 */     if (elem.hasAttributes()) {
/*  699 */       attrMap = elem.getAttributes();
/*  700 */       length = attrMap.getLength();
/*      */     }
/*      */ 
/*  703 */     if (!this.fNamespaces)
/*      */     {
/*  706 */       this._printer.printText('<');
/*  707 */       this._printer.printText(tagName);
/*  708 */       this._printer.indent();
/*      */ 
/*  713 */       for (int i = 0; i < length; i++) {
/*  714 */         Attr attr = (Attr)attrMap.item(i);
/*  715 */         String name = attr.getName();
/*  716 */         String value = attr.getValue();
/*  717 */         if (value == null)
/*  718 */           value = "";
/*  719 */         printAttribute(name, value, attr.getSpecified(), attr);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  732 */     for (int i = 0; i < length; i++)
/*      */     {
/*  734 */       Attr attr = (Attr)attrMap.item(i);
/*  735 */       String uri = attr.getNamespaceURI();
/*      */ 
/*  737 */       if ((uri != null) && (uri.equals(NamespaceContext.XMLNS_URI)))
/*      */       {
/*  739 */         String value = attr.getNodeValue();
/*  740 */         if (value == null) {
/*  741 */           value = XMLSymbols.EMPTY_STRING;
/*      */         }
/*      */ 
/*  744 */         if (value.equals(NamespaceContext.XMLNS_URI)) {
/*  745 */           if (this.fDOMErrorHandler != null) {
/*  746 */             String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/TR/1998/REC-xml-19980210", "CantBindXMLNS", null);
/*      */ 
/*  748 */             modifyDOMError(msg, (short)2, null, attr);
/*  749 */             boolean continueProcess = this.fDOMErrorHandler.handleError(this.fDOMError);
/*  750 */             if (!continueProcess)
/*      */             {
/*  752 */               throw new RuntimeException(DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "SerializationStopped", null));
/*      */             }
/*      */           }
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*  759 */           String prefix = attr.getPrefix();
/*  760 */           prefix = (prefix == null) || (prefix.length() == 0) ? XMLSymbols.EMPTY_STRING : this.fSymbolTable.addSymbol(prefix);
/*      */ 
/*  762 */           String localpart = this.fSymbolTable.addSymbol(attr.getLocalName());
/*  763 */           if (prefix == XMLSymbols.PREFIX_XMLNS) {
/*  764 */             value = this.fSymbolTable.addSymbol(value);
/*      */ 
/*  766 */             if (value.length() != 0) {
/*  767 */               this.fNSBinder.declarePrefix(localpart, value);
/*      */             }
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/*  776 */             value = this.fSymbolTable.addSymbol(value);
/*  777 */             this.fNSBinder.declarePrefix(XMLSymbols.EMPTY_STRING, value);
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  787 */     String uri = elem.getNamespaceURI();
/*  788 */     String prefix = elem.getPrefix();
/*      */ 
/*  795 */     if ((uri != null) && (prefix != null) && (uri.length() == 0) && (prefix.length() != 0))
/*      */     {
/*  799 */       prefix = null;
/*  800 */       this._printer.printText('<');
/*  801 */       this._printer.printText(elem.getLocalName());
/*  802 */       this._printer.indent();
/*      */     } else {
/*  804 */       this._printer.printText('<');
/*  805 */       this._printer.printText(tagName);
/*  806 */       this._printer.indent();
/*      */     }
/*      */ 
/*  837 */     if (uri != null) {
/*  838 */       uri = this.fSymbolTable.addSymbol(uri);
/*  839 */       prefix = (prefix == null) || (prefix.length() == 0) ? XMLSymbols.EMPTY_STRING : this.fSymbolTable.addSymbol(prefix);
/*      */ 
/*  841 */       if (this.fNSBinder.getURI(prefix) != uri)
/*      */       {
/*  852 */         if (this.fNamespacePrefixes) {
/*  853 */           printNamespaceAttr(prefix, uri);
/*      */         }
/*  855 */         this.fLocalNSBinder.declarePrefix(prefix, uri);
/*  856 */         this.fNSBinder.declarePrefix(prefix, uri);
/*      */       }
/*      */     }
/*  859 */     else if (elem.getLocalName() == null)
/*      */     {
/*  861 */       if (this.fDOMErrorHandler != null) {
/*  862 */         String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NullLocalElementName", new Object[] { elem.getNodeName() });
/*      */ 
/*  865 */         modifyDOMError(msg, (short)2, null, elem);
/*  866 */         boolean continueProcess = this.fDOMErrorHandler.handleError(this.fDOMError);
/*      */ 
/*  868 */         if (!continueProcess) {
/*  869 */           throw new RuntimeException(DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "SerializationStopped", null));
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*  876 */       uri = this.fNSBinder.getURI(XMLSymbols.EMPTY_STRING);
/*      */ 
/*  878 */       if ((uri != null) && (uri.length() > 0))
/*      */       {
/*  881 */         if (this.fNamespacePrefixes) {
/*  882 */           printNamespaceAttr(XMLSymbols.EMPTY_STRING, XMLSymbols.EMPTY_STRING);
/*      */         }
/*  884 */         this.fLocalNSBinder.declarePrefix(XMLSymbols.EMPTY_STRING, XMLSymbols.EMPTY_STRING);
/*  885 */         this.fNSBinder.declarePrefix(XMLSymbols.EMPTY_STRING, XMLSymbols.EMPTY_STRING);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  896 */     for (i = 0; i < length; i++)
/*      */     {
/*  898 */       Attr attr = (Attr)attrMap.item(i);
/*  899 */       String value = attr.getValue();
/*  900 */       String name = attr.getNodeName();
/*      */ 
/*  902 */       uri = attr.getNamespaceURI();
/*      */ 
/*  905 */       if ((uri != null) && (uri.length() == 0)) {
/*  906 */         uri = null;
/*      */ 
/*  908 */         name = attr.getLocalName();
/*      */       }
/*      */ 
/*  915 */       if (value == null) {
/*  916 */         value = XMLSymbols.EMPTY_STRING;
/*      */       }
/*      */ 
/*  919 */       if (uri != null) {
/*  920 */         prefix = attr.getPrefix();
/*  921 */         prefix = prefix == null ? XMLSymbols.EMPTY_STRING : this.fSymbolTable.addSymbol(prefix);
/*  922 */         String localpart = this.fSymbolTable.addSymbol(attr.getLocalName());
/*      */ 
/*  929 */         if ((uri != null) && (uri.equals(NamespaceContext.XMLNS_URI)))
/*      */         {
/*  931 */           prefix = attr.getPrefix();
/*  932 */           prefix = (prefix == null) || (prefix.length() == 0) ? XMLSymbols.EMPTY_STRING : this.fSymbolTable.addSymbol(prefix);
/*      */ 
/*  934 */           localpart = this.fSymbolTable.addSymbol(attr.getLocalName());
/*  935 */           if (prefix == XMLSymbols.PREFIX_XMLNS) {
/*  936 */             String localUri = this.fLocalNSBinder.getURI(localpart);
/*  937 */             value = this.fSymbolTable.addSymbol(value);
/*  938 */             if ((value.length() != 0) && 
/*  939 */               (localUri == null))
/*      */             {
/*  944 */               if (this.fNamespacePrefixes) {
/*  945 */                 printNamespaceAttr(localpart, value);
/*      */               }
/*      */ 
/*  954 */               this.fLocalNSBinder.declarePrefix(localpart, value);
/*      */             }
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/*  964 */             uri = this.fNSBinder.getURI(XMLSymbols.EMPTY_STRING);
/*  965 */             String localUri = this.fLocalNSBinder.getURI(XMLSymbols.EMPTY_STRING);
/*  966 */             value = this.fSymbolTable.addSymbol(value);
/*  967 */             if (localUri == null)
/*      */             {
/*  969 */               if (this.fNamespacePrefixes) {
/*  970 */                 printNamespaceAttr(XMLSymbols.EMPTY_STRING, value);
/*      */               }
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*  979 */           uri = this.fSymbolTable.addSymbol(uri);
/*      */ 
/*  982 */           String declaredURI = this.fNSBinder.getURI(prefix);
/*      */ 
/*  984 */           if ((prefix == XMLSymbols.EMPTY_STRING) || (declaredURI != uri))
/*      */           {
/*  991 */             name = attr.getNodeName();
/*      */ 
/*  994 */             String declaredPrefix = this.fNSBinder.getPrefix(uri);
/*      */ 
/*  996 */             if ((declaredPrefix != null) && (declaredPrefix != XMLSymbols.EMPTY_STRING))
/*      */             {
/*  998 */               prefix = declaredPrefix;
/*  999 */               name = prefix + ":" + localpart;
/*      */             }
/*      */             else
/*      */             {
/* 1005 */               if ((prefix == XMLSymbols.EMPTY_STRING) || (this.fLocalNSBinder.getURI(prefix) != null))
/*      */               {
/* 1012 */                 int counter = 1;
/* 1013 */                 prefix = this.fSymbolTable.addSymbol("NS" + counter++);
/* 1014 */                 while (this.fLocalNSBinder.getURI(prefix) != null) {
/* 1015 */                   prefix = this.fSymbolTable.addSymbol("NS" + counter++);
/*      */                 }
/* 1017 */                 name = prefix + ":" + localpart;
/*      */               }
/*      */ 
/* 1020 */               if (this.fNamespacePrefixes) {
/* 1021 */                 printNamespaceAttr(prefix, uri);
/*      */               }
/* 1023 */               value = this.fSymbolTable.addSymbol(value);
/* 1024 */               this.fLocalNSBinder.declarePrefix(prefix, value);
/* 1025 */               this.fNSBinder.declarePrefix(prefix, uri);
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/* 1031 */           printAttribute(name, value == null ? XMLSymbols.EMPTY_STRING : value, attr.getSpecified(), attr);
/*      */         }
/* 1033 */       } else if (attr.getLocalName() == null) {
/* 1034 */         if (this.fDOMErrorHandler != null) {
/* 1035 */           String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NullLocalAttrName", new Object[] { attr.getNodeName() });
/*      */ 
/* 1038 */           modifyDOMError(msg, (short)2, null, attr);
/* 1039 */           boolean continueProcess = this.fDOMErrorHandler.handleError(this.fDOMError);
/* 1040 */           if (!continueProcess)
/*      */           {
/* 1042 */             throw new RuntimeException(DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "SerializationStopped", null));
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1048 */         printAttribute(name, value, attr.getSpecified(), attr);
/*      */       }
/*      */       else
/*      */       {
/* 1053 */         printAttribute(name, value, attr.getSpecified(), attr);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1063 */     if (elem.hasChildNodes())
/*      */     {
/* 1066 */       state = enterElementState(null, null, tagName, this.fPreserveSpace);
/* 1067 */       state.doCData = this._format.isCDataElement(tagName);
/* 1068 */       state.unescaped = this._format.isNonEscapingElement(tagName);
/* 1069 */       Node child = elem.getFirstChild();
/* 1070 */       while (child != null) {
/* 1071 */         serializeNode(child);
/* 1072 */         child = child.getNextSibling();
/*      */       }
/* 1074 */       if (this.fNamespaces) {
/* 1075 */         this.fNSBinder.popContext();
/*      */       }
/* 1077 */       endElementIO(null, null, tagName);
/*      */     }
/*      */     else
/*      */     {
/* 1082 */       if (this.fNamespaces) {
/* 1083 */         this.fNSBinder.popContext();
/*      */       }
/* 1085 */       this._printer.unindent();
/* 1086 */       this._printer.printText("/>");
/*      */ 
/* 1088 */       state.afterElement = true;
/* 1089 */       state.afterComment = false;
/* 1090 */       state.empty = false;
/* 1091 */       if (isDocumentState())
/* 1092 */         this._printer.flush();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void printNamespaceAttr(String prefix, String uri)
/*      */     throws IOException
/*      */   {
/* 1108 */     this._printer.printSpace();
/* 1109 */     if (prefix == XMLSymbols.EMPTY_STRING)
/*      */     {
/* 1113 */       this._printer.printText(XMLSymbols.PREFIX_XMLNS);
/*      */     }
/*      */     else
/*      */     {
/* 1118 */       this._printer.printText("xmlns:" + prefix);
/*      */     }
/* 1120 */     this._printer.printText("=\"");
/* 1121 */     printEscaped(uri);
/* 1122 */     this._printer.printText('"');
/*      */   }
/*      */ 
/*      */   private void printAttribute(String name, String value, boolean isSpecified, Attr attr)
/*      */     throws IOException
/*      */   {
/* 1138 */     if ((isSpecified) || ((this.features & 0x40) == 0)) {
/* 1139 */       if ((this.fDOMFilter != null) && ((this.fDOMFilter.getWhatToShow() & 0x2) != 0))
/*      */       {
/* 1141 */         short code = this.fDOMFilter.acceptNode(attr);
/* 1142 */         switch (code) {
/*      */         case 2:
/*      */         case 3:
/* 1145 */           return;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1152 */       this._printer.printSpace();
/* 1153 */       this._printer.printText(name);
/* 1154 */       this._printer.printText("=\"");
/* 1155 */       printEscaped(value);
/* 1156 */       this._printer.printText('"');
/*      */     }
/*      */ 
/* 1162 */     if (name.equals("xml:space"))
/* 1163 */       if (value.equals("preserve"))
/* 1164 */         this.fPreserveSpace = true;
/*      */       else
/* 1166 */         this.fPreserveSpace = this._format.getPreserveSpace();
/*      */   }
/*      */ 
/*      */   protected String getEntityRef(int ch)
/*      */   {
/* 1173 */     switch (ch) {
/*      */     case 60:
/* 1175 */       return "lt";
/*      */     case 62:
/* 1177 */       return "gt";
/*      */     case 34:
/* 1179 */       return "quot";
/*      */     case 39:
/* 1181 */       return "apos";
/*      */     case 38:
/* 1183 */       return "amp";
/*      */     }
/* 1185 */     return null;
/*      */   }
/*      */ 
/*      */   private Attributes extractNamespaces(Attributes attrs)
/*      */     throws SAXException
/*      */   {
/* 1202 */     if (attrs == null) {
/* 1203 */       return null;
/*      */     }
/* 1205 */     int length = attrs.getLength();
/* 1206 */     AttributesImpl attrsOnly = new AttributesImpl(attrs);
/*      */ 
/* 1208 */     for (int i = length - 1; i >= 0; i--) {
/* 1209 */       String rawName = attrsOnly.getQName(i);
/*      */ 
/* 1214 */       if (rawName.startsWith("xmlns")) {
/* 1215 */         if (rawName.length() == 5) {
/* 1216 */           startPrefixMapping("", attrs.getValue(i));
/* 1217 */           attrsOnly.removeAttribute(i);
/* 1218 */         } else if (rawName.charAt(5) == ':') {
/* 1219 */           startPrefixMapping(rawName.substring(6), attrs.getValue(i));
/* 1220 */           attrsOnly.removeAttribute(i);
/*      */         }
/*      */       }
/*      */     }
/* 1224 */     return attrsOnly;
/*      */   }
/*      */ 
/*      */   protected void printEscaped(String source)
/*      */     throws IOException
/*      */   {
/* 1231 */     int length = source.length();
/* 1232 */     for (int i = 0; i < length; i++) {
/* 1233 */       int ch = source.charAt(i);
/* 1234 */       if (!XMLChar.isValid(ch)) {
/* 1235 */         i++; if (i < length)
/* 1236 */           surrogates(ch, source.charAt(i));
/*      */         else {
/* 1238 */           fatalError("The character '" + (char)ch + "' is an invalid XML character");
/*      */         }
/*      */ 
/*      */       }
/* 1243 */       else if ((ch == 10) || (ch == 13) || (ch == 9)) {
/* 1244 */         printHex(ch);
/* 1245 */       } else if (ch == 60) {
/* 1246 */         this._printer.printText("&lt;");
/* 1247 */       } else if (ch == 38) {
/* 1248 */         this._printer.printText("&amp;");
/* 1249 */       } else if (ch == 34) {
/* 1250 */         this._printer.printText("&quot;");
/* 1251 */       } else if ((ch >= 32) && (this._encodingInfo.isPrintable((char)ch))) {
/* 1252 */         this._printer.printText((char)ch);
/*      */       } else {
/* 1254 */         printHex(ch);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void printXMLChar(int ch) throws IOException
/*      */   {
/* 1261 */     if (ch == 13)
/* 1262 */       printHex(ch);
/* 1263 */     else if (ch == 60)
/* 1264 */       this._printer.printText("&lt;");
/* 1265 */     else if (ch == 38)
/* 1266 */       this._printer.printText("&amp;");
/* 1267 */     else if (ch == 62)
/*      */     {
/* 1270 */       this._printer.printText("&gt;");
/* 1271 */     } else if ((ch == 10) || (ch == 9) || ((ch >= 32) && (this._encodingInfo.isPrintable((char)ch))))
/*      */     {
/* 1273 */       this._printer.printText((char)ch);
/*      */     }
/* 1275 */     else printHex(ch);
/*      */   }
/*      */ 
/*      */   protected void printText(String text, boolean preserveSpace, boolean unescaped)
/*      */     throws IOException
/*      */   {
/* 1283 */     int length = text.length();
/* 1284 */     if (preserveSpace)
/*      */     {
/* 1289 */       for (int index = 0; index < length; index++) {
/* 1290 */         char ch = text.charAt(index);
/* 1291 */         if (!XMLChar.isValid(ch))
/*      */         {
/* 1293 */           index++; if (index < length)
/* 1294 */             surrogates(ch, text.charAt(index));
/*      */           else {
/* 1296 */             fatalError("The character '" + ch + "' is an invalid XML character");
/*      */           }
/*      */ 
/*      */         }
/* 1300 */         else if (unescaped) {
/* 1301 */           this._printer.printText(ch);
/*      */         } else {
/* 1303 */           printXMLChar(ch);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1311 */     for (int index = 0; index < length; index++) {
/* 1312 */       char ch = text.charAt(index);
/* 1313 */       if (!XMLChar.isValid(ch))
/*      */       {
/* 1315 */         index++; if (index < length)
/* 1316 */           surrogates(ch, text.charAt(index));
/*      */         else {
/* 1318 */           fatalError("The character '" + ch + "' is an invalid XML character");
/*      */         }
/*      */ 
/*      */       }
/* 1323 */       else if (unescaped) {
/* 1324 */         this._printer.printText(ch);
/*      */       } else {
/* 1326 */         printXMLChar(ch);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void printText(char[] chars, int start, int length, boolean preserveSpace, boolean unescaped)
/*      */     throws IOException
/*      */   {
/* 1338 */     if (preserveSpace)
/*      */     {
/* 1343 */       while (length-- > 0) {
/* 1344 */         char ch = chars[(start++)];
/* 1345 */         if (!XMLChar.isValid(ch))
/*      */         {
/* 1347 */           if (length-- > 0)
/* 1348 */             surrogates(ch, chars[(start++)]);
/*      */           else {
/* 1350 */             fatalError("The character '" + ch + "' is an invalid XML character");
/*      */           }
/*      */ 
/*      */         }
/* 1354 */         else if (unescaped)
/* 1355 */           this._printer.printText(ch);
/*      */         else {
/* 1357 */           printXMLChar(ch);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1365 */     while (length-- > 0) {
/* 1366 */       char ch = chars[(start++)];
/* 1367 */       if (!XMLChar.isValid(ch))
/*      */       {
/* 1369 */         if (length-- > 0)
/* 1370 */           surrogates(ch, chars[(start++)]);
/*      */         else {
/* 1372 */           fatalError("The character '" + ch + "' is an invalid XML character");
/*      */         }
/*      */ 
/*      */       }
/* 1376 */       else if (unescaped)
/* 1377 */         this._printer.printText(ch);
/*      */       else
/* 1379 */         printXMLChar(ch);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void checkUnboundNamespacePrefixedNode(Node node)
/*      */     throws IOException
/*      */   {
/* 1393 */     if (this.fNamespaces)
/*      */     {
/*      */       Node next;
/* 1406 */       for (Node child = node.getFirstChild(); child != null; child = next) {
/* 1407 */         next = child.getNextSibling();
/*      */ 
/* 1415 */         String prefix = child.getPrefix();
/* 1416 */         prefix = (prefix == null) || (prefix.length() == 0) ? XMLSymbols.EMPTY_STRING : this.fSymbolTable.addSymbol(prefix);
/*      */ 
/* 1418 */         if ((this.fNSBinder.getURI(prefix) == null) && (prefix != null)) {
/* 1419 */           fatalError("The replacement text of the entity node '" + node.getNodeName() + "' contains an element node '" + child.getNodeName() + "' with an undeclared prefix '" + prefix + "'.");
/*      */         }
/*      */ 
/* 1427 */         if (child.getNodeType() == 1)
/*      */         {
/* 1429 */           NamedNodeMap attrs = child.getAttributes();
/*      */ 
/* 1431 */           for (int i = 0; i < attrs.getLength(); i++)
/*      */           {
/* 1433 */             String attrPrefix = attrs.item(i).getPrefix();
/* 1434 */             attrPrefix = (attrPrefix == null) || (attrPrefix.length() == 0) ? XMLSymbols.EMPTY_STRING : this.fSymbolTable.addSymbol(attrPrefix);
/*      */ 
/* 1436 */             if ((this.fNSBinder.getURI(attrPrefix) == null) && (attrPrefix != null)) {
/* 1437 */               fatalError("The replacement text of the entity node '" + node.getNodeName() + "' contains an element node '" + child.getNodeName() + "' with an attribute '" + attrs.item(i).getNodeName() + "' an undeclared prefix '" + attrPrefix + "'.");
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1451 */         if (child.hasChildNodes())
/* 1452 */           checkUnboundNamespacePrefixedNode(child);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean reset()
/*      */   {
/* 1459 */     super.reset();
/* 1460 */     if (this.fNSBinder != null) {
/* 1461 */       this.fNSBinder.reset();
/*      */ 
/* 1464 */       this.fNSBinder.declarePrefix(XMLSymbols.EMPTY_STRING, XMLSymbols.EMPTY_STRING);
/*      */     }
/* 1466 */     return true;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.serialize.XMLSerializer
 * JD-Core Version:    0.6.2
 */