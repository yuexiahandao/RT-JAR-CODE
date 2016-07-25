/*      */ package com.sun.org.apache.xml.internal.serialize;
/*      */ 
/*      */ import com.sun.org.apache.xerces.internal.dom.DOMErrorImpl;
/*      */ import com.sun.org.apache.xerces.internal.dom.DOMLocatorImpl;
/*      */ import com.sun.org.apache.xerces.internal.dom.DOMMessageFormatter;
/*      */ import com.sun.org.apache.xerces.internal.util.XMLChar;
/*      */ import java.io.IOException;
/*      */ import java.io.OutputStream;
/*      */ import java.io.Writer;
/*      */ import java.lang.reflect.Method;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Vector;
/*      */ import org.w3c.dom.DOMError;
/*      */ import org.w3c.dom.DOMErrorHandler;
/*      */ import org.w3c.dom.DOMImplementation;
/*      */ import org.w3c.dom.Document;
/*      */ import org.w3c.dom.DocumentFragment;
/*      */ import org.w3c.dom.DocumentType;
/*      */ import org.w3c.dom.Element;
/*      */ import org.w3c.dom.Node;
/*      */ import org.w3c.dom.ls.LSException;
/*      */ import org.w3c.dom.ls.LSSerializerFilter;
/*      */ import org.xml.sax.ContentHandler;
/*      */ import org.xml.sax.DTDHandler;
/*      */ import org.xml.sax.DocumentHandler;
/*      */ import org.xml.sax.Locator;
/*      */ import org.xml.sax.SAXException;
/*      */ import org.xml.sax.ext.DeclHandler;
/*      */ import org.xml.sax.ext.LexicalHandler;
/*      */ 
/*      */ public abstract class BaseMarkupSerializer
/*      */   implements ContentHandler, DocumentHandler, LexicalHandler, DTDHandler, DeclHandler, DOMSerializer, Serializer
/*      */ {
/*  137 */   protected short features = -1;
/*      */   protected DOMErrorHandler fDOMErrorHandler;
/*  139 */   protected final DOMErrorImpl fDOMError = new DOMErrorImpl();
/*      */   protected LSSerializerFilter fDOMFilter;
/*      */   protected EncodingInfo _encodingInfo;
/*      */   private ElementState[] _elementStates;
/*      */   private int _elementStateCount;
/*      */   private Vector _preRoot;
/*      */   protected boolean _started;
/*      */   private boolean _prepared;
/*      */   protected Hashtable _prefixes;
/*      */   protected String _docTypePublicId;
/*      */   protected String _docTypeSystemId;
/*      */   protected OutputFormat _format;
/*      */   protected Printer _printer;
/*      */   protected boolean _indenting;
/*  225 */   protected final StringBuffer fStrBuffer = new StringBuffer(40);
/*      */   private Writer _writer;
/*      */   private OutputStream _output;
/*  239 */   protected Node fCurrentNode = null;
/*      */ 
/*      */   protected BaseMarkupSerializer(OutputFormat format)
/*      */   {
/*  258 */     this._elementStates = new ElementState[10];
/*  259 */     for (int i = 0; i < this._elementStates.length; i++)
/*  260 */       this._elementStates[i] = new ElementState();
/*  261 */     this._format = format;
/*      */   }
/*      */ 
/*      */   public DocumentHandler asDocumentHandler()
/*      */     throws IOException
/*      */   {
/*  268 */     prepare();
/*  269 */     return this;
/*      */   }
/*      */ 
/*      */   public ContentHandler asContentHandler()
/*      */     throws IOException
/*      */   {
/*  276 */     prepare();
/*  277 */     return this;
/*      */   }
/*      */ 
/*      */   public DOMSerializer asDOMSerializer()
/*      */     throws IOException
/*      */   {
/*  284 */     prepare();
/*  285 */     return this;
/*      */   }
/*      */ 
/*      */   public void setOutputByteStream(OutputStream output)
/*      */   {
/*  291 */     if (output == null) {
/*  292 */       String msg = DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "ArgumentIsNull", new Object[] { "output" });
/*      */ 
/*  294 */       throw new NullPointerException(msg);
/*      */     }
/*  296 */     this._output = output;
/*  297 */     this._writer = null;
/*  298 */     reset();
/*      */   }
/*      */ 
/*      */   public void setOutputCharStream(Writer writer)
/*      */   {
/*  304 */     if (writer == null) {
/*  305 */       String msg = DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "ArgumentIsNull", new Object[] { "writer" });
/*      */ 
/*  307 */       throw new NullPointerException(msg);
/*      */     }
/*  309 */     this._writer = writer;
/*  310 */     this._output = null;
/*  311 */     reset();
/*      */   }
/*      */ 
/*      */   public void setOutputFormat(OutputFormat format)
/*      */   {
/*  317 */     if (format == null) {
/*  318 */       String msg = DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "ArgumentIsNull", new Object[] { "format" });
/*      */ 
/*  320 */       throw new NullPointerException(msg);
/*      */     }
/*  322 */     this._format = format;
/*  323 */     reset();
/*      */   }
/*      */ 
/*      */   public boolean reset()
/*      */   {
/*  329 */     if (this._elementStateCount > 1) {
/*  330 */       String msg = DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "ResetInMiddle", null);
/*      */ 
/*  332 */       throw new IllegalStateException(msg);
/*      */     }
/*  334 */     this._prepared = false;
/*  335 */     this.fCurrentNode = null;
/*  336 */     this.fStrBuffer.setLength(0);
/*  337 */     return true;
/*      */   }
/*      */ 
/*      */   protected void prepare()
/*      */     throws IOException
/*      */   {
/*  344 */     if (this._prepared) {
/*  345 */       return;
/*      */     }
/*  347 */     if ((this._writer == null) && (this._output == null)) {
/*  348 */       String msg = DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "NoWriterSupplied", null);
/*      */ 
/*  350 */       throw new IOException(msg);
/*      */     }
/*      */ 
/*  356 */     this._encodingInfo = this._format.getEncodingInfo();
/*      */ 
/*  358 */     if (this._output != null) {
/*  359 */       this._writer = this._encodingInfo.getWriter(this._output);
/*      */     }
/*      */ 
/*  362 */     if (this._format.getIndenting()) {
/*  363 */       this._indenting = true;
/*  364 */       this._printer = new IndentPrinter(this._writer, this._format);
/*      */     } else {
/*  366 */       this._indenting = false;
/*  367 */       this._printer = new Printer(this._writer, this._format);
/*      */     }
/*      */ 
/*  372 */     this._elementStateCount = 0;
/*  373 */     ElementState state = this._elementStates[0];
/*  374 */     state.namespaceURI = null;
/*  375 */     state.localName = null;
/*  376 */     state.rawName = null;
/*  377 */     state.preserveSpace = this._format.getPreserveSpace();
/*  378 */     state.empty = true;
/*  379 */     state.afterElement = false;
/*  380 */     state.afterComment = false;
/*  381 */     state.doCData = (state.inCData = 0);
/*  382 */     state.prefixes = null;
/*      */ 
/*  384 */     this._docTypePublicId = this._format.getDoctypePublic();
/*  385 */     this._docTypeSystemId = this._format.getDoctypeSystem();
/*  386 */     this._started = false;
/*  387 */     this._prepared = true;
/*      */   }
/*      */ 
/*      */   public void serialize(Element elem)
/*      */     throws IOException
/*      */   {
/*  409 */     reset();
/*  410 */     prepare();
/*  411 */     serializeNode(elem);
/*  412 */     this._printer.flush();
/*  413 */     if (this._printer.getException() != null)
/*  414 */       throw this._printer.getException();
/*      */   }
/*      */ 
/*      */   public void serialize(Node node)
/*      */     throws IOException
/*      */   {
/*  426 */     reset();
/*  427 */     prepare();
/*  428 */     serializeNode(node);
/*      */ 
/*  430 */     serializePreRoot();
/*  431 */     this._printer.flush();
/*  432 */     if (this._printer.getException() != null)
/*  433 */       throw this._printer.getException();
/*      */   }
/*      */ 
/*      */   public void serialize(DocumentFragment frag)
/*      */     throws IOException
/*      */   {
/*  448 */     reset();
/*  449 */     prepare();
/*  450 */     serializeNode(frag);
/*  451 */     this._printer.flush();
/*  452 */     if (this._printer.getException() != null)
/*  453 */       throw this._printer.getException();
/*      */   }
/*      */ 
/*      */   public void serialize(Document doc)
/*      */     throws IOException
/*      */   {
/*  469 */     reset();
/*  470 */     prepare();
/*  471 */     serializeNode(doc);
/*  472 */     serializePreRoot();
/*  473 */     this._printer.flush();
/*  474 */     if (this._printer.getException() != null)
/*  475 */       throw this._printer.getException();
/*      */   }
/*      */ 
/*      */   public void startDocument()
/*      */     throws SAXException
/*      */   {
/*      */     try
/*      */     {
/*  488 */       prepare();
/*      */     } catch (IOException except) {
/*  490 */       throw new SAXException(except.toString());
/*      */     }
/*      */   }
/*      */ 
/*      */   public void characters(char[] chars, int start, int length)
/*      */     throws SAXException
/*      */   {
/*      */     try
/*      */     {
/*  502 */       ElementState state = content();
/*      */ 
/*  508 */       if ((state.inCData) || (state.doCData))
/*      */       {
/*  514 */         if (!state.inCData) {
/*  515 */           this._printer.printText("<![CDATA[");
/*  516 */           state.inCData = true;
/*      */         }
/*  518 */         int saveIndent = this._printer.getNextIndent();
/*  519 */         this._printer.setNextIndent(0);
/*      */ 
/*  521 */         int end = start + length;
/*  522 */         for (int index = start; index < end; index++) {
/*  523 */           char ch = chars[index];
/*  524 */           if ((ch == ']') && (index + 2 < end) && (chars[(index + 1)] == ']') && (chars[(index + 2)] == '>'))
/*      */           {
/*  526 */             this._printer.printText("]]]]><![CDATA[>");
/*  527 */             index += 2;
/*      */           }
/*  530 */           else if (!XMLChar.isValid(ch))
/*      */           {
/*  532 */             index++; if (index < end) {
/*  533 */               surrogates(ch, chars[index]);
/*      */             }
/*      */             else {
/*  536 */               fatalError("The character '" + ch + "' is an invalid XML character");
/*      */             }
/*      */ 
/*      */           }
/*  540 */           else if (((ch >= ' ') && (this._encodingInfo.isPrintable(ch)) && (ch != '÷')) || (ch == '\n') || (ch == '\r') || (ch == '\t'))
/*      */           {
/*  542 */             this._printer.printText(ch);
/*      */           }
/*      */           else {
/*  545 */             this._printer.printText("]]>&#x");
/*  546 */             this._printer.printText(Integer.toHexString(ch));
/*  547 */             this._printer.printText(";<![CDATA[");
/*      */           }
/*      */         }
/*      */ 
/*  551 */         this._printer.setNextIndent(saveIndent);
/*      */       }
/*  557 */       else if (state.preserveSpace)
/*      */       {
/*  562 */         int saveIndent = this._printer.getNextIndent();
/*  563 */         this._printer.setNextIndent(0);
/*  564 */         printText(chars, start, length, true, state.unescaped);
/*  565 */         this._printer.setNextIndent(saveIndent);
/*      */       } else {
/*  567 */         printText(chars, start, length, false, state.unescaped);
/*      */       }
/*      */     }
/*      */     catch (IOException except) {
/*  571 */       throw new SAXException(except);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void ignorableWhitespace(char[] chars, int start, int length)
/*      */     throws SAXException
/*      */   {
/*      */     try
/*      */     {
/*  582 */       content();
/*      */ 
/*  587 */       if (this._indenting) {
/*  588 */         this._printer.setThisIndent(0);
/*  589 */         for (int i = start; length-- > 0; i++)
/*  590 */           this._printer.printText(chars[i]);
/*      */       }
/*      */     } catch (IOException except) {
/*  593 */       throw new SAXException(except);
/*      */     }
/*      */   }
/*      */ 
/*      */   public final void processingInstruction(String target, String code)
/*      */     throws SAXException
/*      */   {
/*      */     try
/*      */     {
/*  602 */       processingInstructionIO(target, code);
/*      */     } catch (IOException except) {
/*  604 */       throw new SAXException(except);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void processingInstructionIO(String target, String code)
/*      */     throws IOException
/*      */   {
/*  614 */     ElementState state = content();
/*      */ 
/*  618 */     int index = target.indexOf("?>");
/*  619 */     if (index >= 0)
/*  620 */       this.fStrBuffer.append("<?").append(target.substring(0, index));
/*      */     else
/*  622 */       this.fStrBuffer.append("<?").append(target);
/*  623 */     if (code != null) {
/*  624 */       this.fStrBuffer.append(' ');
/*  625 */       index = code.indexOf("?>");
/*  626 */       if (index >= 0)
/*  627 */         this.fStrBuffer.append(code.substring(0, index));
/*      */       else
/*  629 */         this.fStrBuffer.append(code);
/*      */     }
/*  631 */     this.fStrBuffer.append("?>");
/*      */ 
/*  635 */     if (isDocumentState()) {
/*  636 */       if (this._preRoot == null)
/*  637 */         this._preRoot = new Vector();
/*  638 */       this._preRoot.addElement(this.fStrBuffer.toString());
/*      */     } else {
/*  640 */       this._printer.indent();
/*  641 */       printText(this.fStrBuffer.toString(), true, true);
/*  642 */       this._printer.unindent();
/*  643 */       if (this._indenting) {
/*  644 */         state.afterElement = true;
/*      */       }
/*      */     }
/*  647 */     this.fStrBuffer.setLength(0);
/*      */   }
/*      */ 
/*      */   public void comment(char[] chars, int start, int length)
/*      */     throws SAXException
/*      */   {
/*      */     try
/*      */     {
/*  655 */       comment(new String(chars, start, length));
/*      */     } catch (IOException except) {
/*  657 */       throw new SAXException(except);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void comment(String text)
/*      */     throws IOException
/*      */   {
/*  668 */     if (this._format.getOmitComments()) {
/*  669 */       return;
/*      */     }
/*  671 */     ElementState state = content();
/*      */ 
/*  674 */     int index = text.indexOf("-->");
/*  675 */     if (index >= 0)
/*  676 */       this.fStrBuffer.append("<!--").append(text.substring(0, index)).append("-->");
/*      */     else {
/*  678 */       this.fStrBuffer.append("<!--").append(text).append("-->");
/*      */     }
/*      */ 
/*  682 */     if (isDocumentState()) {
/*  683 */       if (this._preRoot == null)
/*  684 */         this._preRoot = new Vector();
/*  685 */       this._preRoot.addElement(this.fStrBuffer.toString());
/*      */     }
/*      */     else
/*      */     {
/*  690 */       if ((this._indenting) && (!state.preserveSpace))
/*  691 */         this._printer.breakLine();
/*  692 */       this._printer.indent();
/*  693 */       printText(this.fStrBuffer.toString(), true, true);
/*  694 */       this._printer.unindent();
/*  695 */       if (this._indenting) {
/*  696 */         state.afterElement = true;
/*      */       }
/*      */     }
/*  699 */     this.fStrBuffer.setLength(0);
/*  700 */     state.afterComment = true;
/*  701 */     state.afterElement = false;
/*      */   }
/*      */ 
/*      */   public void startCDATA()
/*      */   {
/*  709 */     ElementState state = getElementState();
/*  710 */     state.doCData = true;
/*      */   }
/*      */ 
/*      */   public void endCDATA()
/*      */   {
/*  718 */     ElementState state = getElementState();
/*  719 */     state.doCData = false;
/*      */   }
/*      */ 
/*      */   public void startNonEscaping()
/*      */   {
/*  727 */     ElementState state = getElementState();
/*  728 */     state.unescaped = true;
/*      */   }
/*      */ 
/*      */   public void endNonEscaping()
/*      */   {
/*  736 */     ElementState state = getElementState();
/*  737 */     state.unescaped = false;
/*      */   }
/*      */ 
/*      */   public void startPreserving()
/*      */   {
/*  745 */     ElementState state = getElementState();
/*  746 */     state.preserveSpace = true;
/*      */   }
/*      */ 
/*      */   public void endPreserving()
/*      */   {
/*  754 */     ElementState state = getElementState();
/*  755 */     state.preserveSpace = false;
/*      */   }
/*      */ 
/*      */   public void endDocument()
/*      */     throws SAXException
/*      */   {
/*      */     try
/*      */     {
/*  773 */       serializePreRoot();
/*      */ 
/*  775 */       this._printer.flush();
/*      */     } catch (IOException except) {
/*  777 */       throw new SAXException(except);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void startEntity(String name)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void endEntity(String name)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void setDocumentLocator(Locator locator)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void skippedEntity(String name)
/*      */     throws SAXException
/*      */   {
/*      */     try
/*      */     {
/*  809 */       endCDATA();
/*  810 */       content();
/*  811 */       this._printer.printText('&');
/*  812 */       this._printer.printText(name);
/*  813 */       this._printer.printText(';');
/*      */     } catch (IOException except) {
/*  815 */       throw new SAXException(except);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void startPrefixMapping(String prefix, String uri)
/*      */     throws SAXException
/*      */   {
/*  823 */     if (this._prefixes == null)
/*  824 */       this._prefixes = new Hashtable();
/*  825 */     this._prefixes.put(uri, prefix == null ? "" : prefix);
/*      */   }
/*      */ 
/*      */   public void endPrefixMapping(String prefix)
/*      */     throws SAXException
/*      */   {
/*      */   }
/*      */ 
/*      */   public final void startDTD(String name, String publicId, String systemId)
/*      */     throws SAXException
/*      */   {
/*      */     try
/*      */     {
/*  844 */       this._printer.enterDTD();
/*  845 */       this._docTypePublicId = publicId;
/*  846 */       this._docTypeSystemId = systemId;
/*      */     }
/*      */     catch (IOException except) {
/*  849 */       throw new SAXException(except);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void endDTD()
/*      */   {
/*      */   }
/*      */ 
/*      */   public void elementDecl(String name, String model)
/*      */     throws SAXException
/*      */   {
/*      */     try
/*      */     {
/*  864 */       this._printer.enterDTD();
/*  865 */       this._printer.printText("<!ELEMENT ");
/*  866 */       this._printer.printText(name);
/*  867 */       this._printer.printText(' ');
/*  868 */       this._printer.printText(model);
/*  869 */       this._printer.printText('>');
/*  870 */       if (this._indenting)
/*  871 */         this._printer.breakLine();
/*      */     } catch (IOException except) {
/*  873 */       throw new SAXException(except);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void attributeDecl(String eName, String aName, String type, String valueDefault, String value)
/*      */     throws SAXException
/*      */   {
/*      */     try
/*      */     {
/*  883 */       this._printer.enterDTD();
/*  884 */       this._printer.printText("<!ATTLIST ");
/*  885 */       this._printer.printText(eName);
/*  886 */       this._printer.printText(' ');
/*  887 */       this._printer.printText(aName);
/*  888 */       this._printer.printText(' ');
/*  889 */       this._printer.printText(type);
/*  890 */       if (valueDefault != null) {
/*  891 */         this._printer.printText(' ');
/*  892 */         this._printer.printText(valueDefault);
/*      */       }
/*  894 */       if (value != null) {
/*  895 */         this._printer.printText(" \"");
/*  896 */         printEscaped(value);
/*  897 */         this._printer.printText('"');
/*      */       }
/*  899 */       this._printer.printText('>');
/*  900 */       if (this._indenting)
/*  901 */         this._printer.breakLine();
/*      */     } catch (IOException except) {
/*  903 */       throw new SAXException(except);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void internalEntityDecl(String name, String value)
/*      */     throws SAXException
/*      */   {
/*      */     try
/*      */     {
/*  912 */       this._printer.enterDTD();
/*  913 */       this._printer.printText("<!ENTITY ");
/*  914 */       this._printer.printText(name);
/*  915 */       this._printer.printText(" \"");
/*  916 */       printEscaped(value);
/*  917 */       this._printer.printText("\">");
/*  918 */       if (this._indenting)
/*  919 */         this._printer.breakLine();
/*      */     } catch (IOException except) {
/*  921 */       throw new SAXException(except);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void externalEntityDecl(String name, String publicId, String systemId)
/*      */     throws SAXException
/*      */   {
/*      */     try
/*      */     {
/*  930 */       this._printer.enterDTD();
/*  931 */       unparsedEntityDecl(name, publicId, systemId, null);
/*      */     } catch (IOException except) {
/*  933 */       throw new SAXException(except);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void unparsedEntityDecl(String name, String publicId, String systemId, String notationName)
/*      */     throws SAXException
/*      */   {
/*      */     try
/*      */     {
/*  943 */       this._printer.enterDTD();
/*  944 */       if (publicId == null) {
/*  945 */         this._printer.printText("<!ENTITY ");
/*  946 */         this._printer.printText(name);
/*  947 */         this._printer.printText(" SYSTEM ");
/*  948 */         printDoctypeURL(systemId);
/*      */       } else {
/*  950 */         this._printer.printText("<!ENTITY ");
/*  951 */         this._printer.printText(name);
/*  952 */         this._printer.printText(" PUBLIC ");
/*  953 */         printDoctypeURL(publicId);
/*  954 */         this._printer.printText(' ');
/*  955 */         printDoctypeURL(systemId);
/*      */       }
/*  957 */       if (notationName != null) {
/*  958 */         this._printer.printText(" NDATA ");
/*  959 */         this._printer.printText(notationName);
/*      */       }
/*  961 */       this._printer.printText('>');
/*  962 */       if (this._indenting)
/*  963 */         this._printer.breakLine();
/*      */     } catch (IOException except) {
/*  965 */       throw new SAXException(except);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void notationDecl(String name, String publicId, String systemId)
/*      */     throws SAXException
/*      */   {
/*      */     try
/*      */     {
/*  974 */       this._printer.enterDTD();
/*  975 */       if (publicId != null) {
/*  976 */         this._printer.printText("<!NOTATION ");
/*  977 */         this._printer.printText(name);
/*  978 */         this._printer.printText(" PUBLIC ");
/*  979 */         printDoctypeURL(publicId);
/*  980 */         if (systemId != null) {
/*  981 */           this._printer.printText(' ');
/*  982 */           printDoctypeURL(systemId);
/*      */         }
/*      */       } else {
/*  985 */         this._printer.printText("<!NOTATION ");
/*  986 */         this._printer.printText(name);
/*  987 */         this._printer.printText(" SYSTEM ");
/*  988 */         printDoctypeURL(systemId);
/*      */       }
/*  990 */       this._printer.printText('>');
/*  991 */       if (this._indenting)
/*  992 */         this._printer.breakLine();
/*      */     } catch (IOException except) {
/*  994 */       throw new SAXException(except);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void serializeNode(Node node)
/*      */     throws IOException
/*      */   {
/* 1017 */     this.fCurrentNode = node;
/*      */     Node child;
/* 1022 */     switch (node.getNodeType())
/*      */     {
/*      */     case 3:
/* 1026 */       String text = node.getNodeValue();
/* 1027 */       if (text != null)
/* 1028 */         if ((this.fDOMFilter != null) && ((this.fDOMFilter.getWhatToShow() & 0x4) != 0))
/*      */         {
/* 1030 */           short code = this.fDOMFilter.acceptNode(node);
/* 1031 */           switch (code) {
/*      */           case 2:
/*      */           case 3:
/* 1034 */             break;
/*      */           default:
/* 1037 */             characters(text);
/*      */           }
/*      */ 
/*      */         }
/* 1041 */         else if ((!this._indenting) || (getElementState().preserveSpace) || (text.replace('\n', ' ').trim().length() != 0))
/*      */         {
/* 1043 */           characters(text); }  break;
/*      */     case 4:
/* 1050 */       String text = node.getNodeValue();
/* 1051 */       if ((this.features & 0x8) != 0) {
/* 1052 */         if (text != null) {
/* 1053 */           if ((this.fDOMFilter != null) && ((this.fDOMFilter.getWhatToShow() & 0x8) != 0))
/*      */           {
/* 1057 */             short code = this.fDOMFilter.acceptNode(node);
/* 1058 */             switch (code)
/*      */             {
/*      */             case 2:
/*      */             case 3:
/* 1063 */               return;
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/* 1071 */           startCDATA();
/* 1072 */           characters(text);
/* 1073 */           endCDATA();
/*      */         }
/*      */       }
/*      */       else {
/* 1077 */         characters(text);
/*      */       }
/* 1079 */       break;
/*      */     case 8:
/* 1084 */       if (!this._format.getOmitComments()) {
/* 1085 */         String text = node.getNodeValue();
/* 1086 */         if (text != null)
/*      */         {
/* 1088 */           if ((this.fDOMFilter != null) && ((this.fDOMFilter.getWhatToShow() & 0x80) != 0))
/*      */           {
/* 1090 */             short code = this.fDOMFilter.acceptNode(node);
/* 1091 */             switch (code)
/*      */             {
/*      */             case 2:
/*      */             case 3:
/* 1095 */               return;
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/* 1102 */           comment(text); }  } break;
/*      */     case 5:
/* 1111 */       endCDATA();
/* 1112 */       content();
/*      */ 
/* 1114 */       if (((this.features & 0x4) != 0) || (node.getFirstChild() == null))
/*      */       {
/* 1116 */         if ((this.fDOMFilter != null) && ((this.fDOMFilter.getWhatToShow() & 0x10) != 0))
/*      */         {
/* 1118 */           short code = this.fDOMFilter.acceptNode(node);
/* 1119 */           switch (code) {
/*      */           case 2:
/* 1121 */             return;
/*      */           case 3:
/* 1124 */             child = node.getFirstChild();
/* 1125 */             while (child != null) {
/* 1126 */               serializeNode(child);
/* 1127 */               child = child.getNextSibling();
/*      */             }
/* 1129 */             return;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1137 */         checkUnboundNamespacePrefixedNode(node);
/*      */ 
/* 1139 */         this._printer.printText("&");
/* 1140 */         this._printer.printText(node.getNodeName());
/* 1141 */         this._printer.printText(";");
/*      */       }
/*      */       else {
/* 1144 */         child = node.getFirstChild(); } break;
/*      */     case 7:
/*      */     case 1:
/*      */     case 9:
/*      */     case 11:
/* 1145 */       while (child != null) {
/* 1146 */         serializeNode(child);
/* 1147 */         child = child.getNextSibling(); continue;
/*      */ 
/* 1156 */         if ((this.fDOMFilter != null) && ((this.fDOMFilter.getWhatToShow() & 0x40) != 0))
/*      */         {
/* 1158 */           short code = this.fDOMFilter.acceptNode(node);
/* 1159 */           switch (code) {
/*      */           case 2:
/*      */           case 3:
/* 1162 */             return;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1168 */         processingInstructionIO(node.getNodeName(), node.getNodeValue());
/* 1169 */         break;
/*      */ 
/* 1173 */         if ((this.fDOMFilter != null) && ((this.fDOMFilter.getWhatToShow() & 0x1) != 0))
/*      */         {
/* 1175 */           short code = this.fDOMFilter.acceptNode(node);
/* 1176 */           switch (code) {
/*      */           case 2:
/* 1178 */             return;
/*      */           case 3:
/* 1181 */             Node child = node.getFirstChild();
/* 1182 */             while (child != null) {
/* 1183 */               serializeNode(child);
/* 1184 */               child = child.getNextSibling();
/*      */             }
/* 1186 */             return;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1193 */         serializeElement((Element)node);
/* 1194 */         break;
/*      */ 
/* 1204 */         serializeDocument();
/*      */ 
/* 1208 */         DocumentType docType = ((Document)node).getDoctype();
/* 1209 */         if (docType != null)
/*      */         {
/* 1211 */           DOMImplementation domImpl = ((Document)node).getImplementation();
/*      */           try
/*      */           {
/* 1215 */             this._printer.enterDTD();
/* 1216 */             this._docTypePublicId = docType.getPublicId();
/* 1217 */             this._docTypeSystemId = docType.getSystemId();
/* 1218 */             String internal = docType.getInternalSubset();
/* 1219 */             if ((internal != null) && (internal.length() > 0))
/* 1220 */               this._printer.printText(internal);
/* 1221 */             endDTD();
/*      */           }
/*      */           catch (NoSuchMethodError nsme)
/*      */           {
/* 1225 */             Class docTypeClass = docType.getClass();
/*      */ 
/* 1227 */             String docTypePublicId = null;
/* 1228 */             String docTypeSystemId = null;
/*      */             try {
/* 1230 */               Method getPublicId = docTypeClass.getMethod("getPublicId", (Class[])null);
/* 1231 */               if (getPublicId.getReturnType().equals(String.class))
/* 1232 */                 docTypePublicId = (String)getPublicId.invoke(docType, (Object[])null);
/*      */             }
/*      */             catch (Exception e)
/*      */             {
/*      */             }
/*      */             try
/*      */             {
/* 1239 */               Method getSystemId = docTypeClass.getMethod("getSystemId", (Class[])null);
/* 1240 */               if (getSystemId.getReturnType().equals(String.class)) {
/* 1241 */                 docTypeSystemId = (String)getSystemId.invoke(docType, (Object[])null);
/*      */               }
/*      */             }
/*      */             catch (Exception e)
/*      */             {
/*      */             }
/* 1247 */             this._printer.enterDTD();
/* 1248 */             this._docTypePublicId = docTypePublicId;
/* 1249 */             this._docTypeSystemId = docTypeSystemId;
/* 1250 */             endDTD();
/*      */           }
/*      */ 
/* 1253 */           serializeDTD(docType.getName());
/*      */         }
/*      */ 
/* 1256 */         this._started = true;
/*      */ 
/* 1266 */         Node child = node.getFirstChild();
/* 1267 */         while (child != null) {
/* 1268 */           serializeNode(child);
/* 1269 */           child = child.getNextSibling();
/*      */         }
/*      */       }
/*      */     case 2:
/*      */     case 6:
/*      */     case 10:
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void serializeDocument()
/*      */     throws IOException
/*      */   {
/* 1285 */     String dtd = this._printer.leaveDTD();
/* 1286 */     if (!this._started)
/*      */     {
/* 1288 */       if (!this._format.getOmitXMLDeclaration())
/*      */       {
/* 1293 */         StringBuffer buffer = new StringBuffer("<?xml version=\"");
/* 1294 */         if (this._format.getVersion() != null)
/* 1295 */           buffer.append(this._format.getVersion());
/*      */         else
/* 1297 */           buffer.append("1.0");
/* 1298 */         buffer.append('"');
/* 1299 */         String format_encoding = this._format.getEncoding();
/* 1300 */         if (format_encoding != null) {
/* 1301 */           buffer.append(" encoding=\"");
/* 1302 */           buffer.append(format_encoding);
/* 1303 */           buffer.append('"');
/*      */         }
/* 1305 */         if ((this._format.getStandalone()) && (this._docTypeSystemId == null) && (this._docTypePublicId == null))
/*      */         {
/* 1307 */           buffer.append(" standalone=\"yes\"");
/* 1308 */         }buffer.append("?>");
/* 1309 */         this._printer.printText(buffer);
/* 1310 */         this._printer.breakLine();
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1315 */     serializePreRoot();
/*      */   }
/*      */ 
/*      */   protected void serializeDTD(String name)
/*      */     throws IOException
/*      */   {
/* 1323 */     String dtd = this._printer.leaveDTD();
/* 1324 */     if (!this._format.getOmitDocumentType())
/* 1325 */       if (this._docTypeSystemId != null)
/*      */       {
/* 1329 */         this._printer.printText("<!DOCTYPE ");
/* 1330 */         this._printer.printText(name);
/* 1331 */         if (this._docTypePublicId != null) {
/* 1332 */           this._printer.printText(" PUBLIC ");
/* 1333 */           printDoctypeURL(this._docTypePublicId);
/* 1334 */           if (this._indenting) {
/* 1335 */             this._printer.breakLine();
/* 1336 */             for (int i = 0; i < 18 + name.length(); i++)
/* 1337 */               this._printer.printText(" ");
/*      */           } else {
/* 1339 */             this._printer.printText(" ");
/* 1340 */           }printDoctypeURL(this._docTypeSystemId);
/*      */         } else {
/* 1342 */           this._printer.printText(" SYSTEM ");
/* 1343 */           printDoctypeURL(this._docTypeSystemId);
/*      */         }
/*      */ 
/* 1348 */         if ((dtd != null) && (dtd.length() > 0)) {
/* 1349 */           this._printer.printText(" [");
/* 1350 */           printText(dtd, true, true);
/* 1351 */           this._printer.printText(']');
/*      */         }
/*      */ 
/* 1354 */         this._printer.printText(">");
/* 1355 */         this._printer.breakLine();
/* 1356 */       } else if ((dtd != null) && (dtd.length() > 0)) {
/* 1357 */         this._printer.printText("<!DOCTYPE ");
/* 1358 */         this._printer.printText(name);
/* 1359 */         this._printer.printText(" [");
/* 1360 */         printText(dtd, true, true);
/* 1361 */         this._printer.printText("]>");
/* 1362 */         this._printer.breakLine();
/*      */       }
/*      */   }
/*      */ 
/*      */   protected ElementState content()
/*      */     throws IOException
/*      */   {
/* 1383 */     ElementState state = getElementState();
/* 1384 */     if (!isDocumentState())
/*      */     {
/* 1386 */       if ((state.inCData) && (!state.doCData)) {
/* 1387 */         this._printer.printText("]]>");
/* 1388 */         state.inCData = false;
/*      */       }
/*      */ 
/* 1393 */       if (state.empty) {
/* 1394 */         this._printer.printText('>');
/* 1395 */         state.empty = false;
/*      */       }
/*      */ 
/* 1400 */       state.afterElement = false;
/*      */ 
/* 1404 */       state.afterComment = false;
/*      */     }
/* 1406 */     return state;
/*      */   }
/*      */ 
/*      */   protected void characters(String text)
/*      */     throws IOException
/*      */   {
/* 1427 */     ElementState state = content();
/*      */ 
/* 1432 */     if ((state.inCData) || (state.doCData))
/*      */     {
/* 1439 */       if (!state.inCData) {
/* 1440 */         this._printer.printText("<![CDATA[");
/* 1441 */         state.inCData = true;
/*      */       }
/* 1443 */       int saveIndent = this._printer.getNextIndent();
/* 1444 */       this._printer.setNextIndent(0);
/* 1445 */       printCDATAText(text);
/* 1446 */       this._printer.setNextIndent(saveIndent);
/*      */     }
/* 1452 */     else if (state.preserveSpace)
/*      */     {
/* 1457 */       int saveIndent = this._printer.getNextIndent();
/* 1458 */       this._printer.setNextIndent(0);
/* 1459 */       printText(text, true, state.unescaped);
/* 1460 */       this._printer.setNextIndent(saveIndent);
/*      */     } else {
/* 1462 */       printText(text, false, state.unescaped);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected abstract String getEntityRef(int paramInt);
/*      */ 
/*      */   protected abstract void serializeElement(Element paramElement)
/*      */     throws IOException;
/*      */ 
/*      */   protected void serializePreRoot()
/*      */     throws IOException
/*      */   {
/* 1507 */     if (this._preRoot != null) {
/* 1508 */       for (int i = 0; i < this._preRoot.size(); i++) {
/* 1509 */         printText((String)this._preRoot.elementAt(i), true, true);
/* 1510 */         if (this._indenting)
/* 1511 */           this._printer.breakLine();
/*      */       }
/* 1513 */       this._preRoot.removeAllElements();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void printCDATAText(String text)
/*      */     throws IOException
/*      */   {
/* 1523 */     int length = text.length();
/*      */ 
/* 1526 */     for (int index = 0; index < length; index++) {
/* 1527 */       char ch = text.charAt(index);
/* 1528 */       if ((ch == ']') && (index + 2 < length) && (text.charAt(index + 1) == ']') && (text.charAt(index + 2) == '>'))
/*      */       {
/* 1532 */         if (this.fDOMErrorHandler != null)
/*      */         {
/* 1535 */           if ((this.features & 0x10) == 0) {
/* 1536 */             String msg = DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "EndingCDATA", null);
/*      */ 
/* 1540 */             if ((this.features & 0x2) != 0)
/*      */             {
/* 1542 */               modifyDOMError(msg, (short)3, "wf-invalid-character", this.fCurrentNode);
/* 1543 */               this.fDOMErrorHandler.handleError(this.fDOMError);
/* 1544 */               throw new LSException((short)82, msg);
/*      */             }
/*      */ 
/* 1548 */             modifyDOMError(msg, (short)2, "cdata-section-not-splitted", this.fCurrentNode);
/* 1549 */             if (!this.fDOMErrorHandler.handleError(this.fDOMError)) {
/* 1550 */               throw new LSException((short)82, msg);
/*      */             }
/*      */           }
/*      */           else
/*      */           {
/* 1555 */             String msg = DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "SplittingCDATA", null);
/*      */ 
/* 1560 */             modifyDOMError(msg, (short)1, null, this.fCurrentNode);
/*      */ 
/* 1564 */             this.fDOMErrorHandler.handleError(this.fDOMError);
/*      */           }
/*      */         }
/*      */ 
/* 1568 */         this._printer.printText("]]]]><![CDATA[>");
/* 1569 */         index += 2;
/*      */       }
/* 1573 */       else if (!XMLChar.isValid(ch))
/*      */       {
/* 1575 */         index++; if (index < length) {
/* 1576 */           surrogates(ch, text.charAt(index));
/*      */         }
/*      */         else {
/* 1579 */           fatalError("The character '" + ch + "' is an invalid XML character");
/*      */         }
/*      */ 
/*      */       }
/* 1583 */       else if (((ch >= ' ') && (this._encodingInfo.isPrintable(ch)) && (ch != '÷')) || (ch == '\n') || (ch == '\r') || (ch == '\t'))
/*      */       {
/* 1585 */         this._printer.printText(ch);
/*      */       }
/*      */       else
/*      */       {
/* 1589 */         this._printer.printText("]]>&#x");
/* 1590 */         this._printer.printText(Integer.toHexString(ch));
/* 1591 */         this._printer.printText(";<![CDATA[");
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void surrogates(int high, int low)
/*      */     throws IOException
/*      */   {
/* 1599 */     if (XMLChar.isHighSurrogate(high)) {
/* 1600 */       if (!XMLChar.isLowSurrogate(low))
/*      */       {
/* 1602 */         fatalError("The character '" + (char)low + "' is an invalid XML character");
/*      */       }
/*      */       else {
/* 1605 */         int supplemental = XMLChar.supplemental((char)high, (char)low);
/* 1606 */         if (!XMLChar.isValid(supplemental))
/*      */         {
/* 1608 */           fatalError("The character '" + (char)supplemental + "' is an invalid XML character");
/*      */         }
/* 1611 */         else if (content().inCData) {
/* 1612 */           this._printer.printText("]]>&#x");
/* 1613 */           this._printer.printText(Integer.toHexString(supplemental));
/* 1614 */           this._printer.printText(";<![CDATA[");
/*      */         }
/*      */         else {
/* 1617 */           printHex(supplemental);
/*      */         }
/*      */       }
/*      */     }
/*      */     else
/* 1622 */       fatalError("The character '" + (char)high + "' is an invalid XML character");
/*      */   }
/*      */ 
/*      */   protected void printText(char[] chars, int start, int length, boolean preserveSpace, boolean unescaped)
/*      */     throws IOException
/*      */   {
/* 1647 */     if (preserveSpace)
/*      */     {
/* 1652 */       while (length-- > 0) {
/* 1653 */         char ch = chars[start];
/* 1654 */         start++;
/* 1655 */         if ((ch == '\n') || (ch == '\r') || (unescaped))
/* 1656 */           this._printer.printText(ch);
/*      */         else {
/* 1658 */           printEscaped(ch);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1666 */     while (length-- > 0) {
/* 1667 */       char ch = chars[start];
/* 1668 */       start++;
/* 1669 */       if ((ch == ' ') || (ch == '\f') || (ch == '\t') || (ch == '\n') || (ch == '\r'))
/* 1670 */         this._printer.printSpace();
/* 1671 */       else if (unescaped)
/* 1672 */         this._printer.printText(ch);
/*      */       else
/* 1674 */         printEscaped(ch);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void printText(String text, boolean preserveSpace, boolean unescaped)
/*      */     throws IOException
/*      */   {
/* 1686 */     if (preserveSpace)
/*      */     {
/* 1691 */       for (int index = 0; index < text.length(); index++) {
/* 1692 */         char ch = text.charAt(index);
/* 1693 */         if ((ch == '\n') || (ch == '\r') || (unescaped))
/* 1694 */           this._printer.printText(ch);
/*      */         else {
/* 1696 */           printEscaped(ch);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1704 */     for (int index = 0; index < text.length(); index++) {
/* 1705 */       char ch = text.charAt(index);
/* 1706 */       if ((ch == ' ') || (ch == '\f') || (ch == '\t') || (ch == '\n') || (ch == '\r'))
/* 1707 */         this._printer.printSpace();
/* 1708 */       else if (unescaped)
/* 1709 */         this._printer.printText(ch);
/*      */       else
/* 1711 */         printEscaped(ch);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void printDoctypeURL(String url)
/*      */     throws IOException
/*      */   {
/* 1729 */     this._printer.printText('"');
/* 1730 */     for (int i = 0; i < url.length(); i++)
/* 1731 */       if ((url.charAt(i) == '"') || (url.charAt(i) < ' ') || (url.charAt(i) > '')) {
/* 1732 */         this._printer.printText('%');
/* 1733 */         this._printer.printText(Integer.toHexString(url.charAt(i)));
/*      */       } else {
/* 1735 */         this._printer.printText(url.charAt(i));
/*      */       }
/* 1737 */     this._printer.printText('"');
/*      */   }
/*      */ 
/*      */   protected void printEscaped(int ch)
/*      */     throws IOException
/*      */   {
/* 1749 */     String charRef = getEntityRef(ch);
/* 1750 */     if (charRef != null) {
/* 1751 */       this._printer.printText('&');
/* 1752 */       this._printer.printText(charRef);
/* 1753 */       this._printer.printText(';');
/* 1754 */     } else if (((ch >= 32) && (this._encodingInfo.isPrintable((char)ch)) && (ch != 247)) || (ch == 10) || (ch == 13) || (ch == 9))
/*      */     {
/* 1758 */       if (ch < 65536) {
/* 1759 */         this._printer.printText((char)ch);
/*      */       } else {
/* 1761 */         this._printer.printText((char)((ch - 65536 >> 10) + 55296));
/* 1762 */         this._printer.printText((char)((ch - 65536 & 0x3FF) + 56320));
/*      */       }
/*      */     } else {
/* 1765 */       printHex(ch);
/*      */     }
/*      */   }
/*      */ 
/*      */   final void printHex(int ch)
/*      */     throws IOException
/*      */   {
/* 1773 */     this._printer.printText("&#x");
/* 1774 */     this._printer.printText(Integer.toHexString(ch));
/* 1775 */     this._printer.printText(';');
/*      */   }
/*      */ 
/*      */   protected void printEscaped(String source)
/*      */     throws IOException
/*      */   {
/* 1791 */     for (int i = 0; i < source.length(); i++) {
/* 1792 */       int ch = source.charAt(i);
/* 1793 */       if (((ch & 0xFC00) == 55296) && (i + 1 < source.length())) {
/* 1794 */         int lowch = source.charAt(i + 1);
/* 1795 */         if ((lowch & 0xFC00) == 56320) {
/* 1796 */           ch = 65536 + (ch - 55296 << 10) + lowch - 56320;
/* 1797 */           i++;
/*      */         }
/*      */       }
/* 1800 */       printEscaped(ch);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected ElementState getElementState()
/*      */   {
/* 1817 */     return this._elementStates[this._elementStateCount];
/*      */   }
/*      */ 
/*      */   protected ElementState enterElementState(String namespaceURI, String localName, String rawName, boolean preserveSpace)
/*      */   {
/* 1833 */     if (this._elementStateCount + 1 == this._elementStates.length)
/*      */     {
/* 1838 */       ElementState[] newStates = new ElementState[this._elementStates.length + 10];
/* 1839 */       for (int i = 0; i < this._elementStates.length; i++)
/* 1840 */         newStates[i] = this._elementStates[i];
/* 1841 */       for (int i = this._elementStates.length; i < newStates.length; i++)
/* 1842 */         newStates[i] = new ElementState();
/* 1843 */       this._elementStates = newStates;
/*      */     }
/*      */ 
/* 1846 */     this._elementStateCount += 1;
/* 1847 */     ElementState state = this._elementStates[this._elementStateCount];
/* 1848 */     state.namespaceURI = namespaceURI;
/* 1849 */     state.localName = localName;
/* 1850 */     state.rawName = rawName;
/* 1851 */     state.preserveSpace = preserveSpace;
/* 1852 */     state.empty = true;
/* 1853 */     state.afterElement = false;
/* 1854 */     state.afterComment = false;
/* 1855 */     state.doCData = (state.inCData = 0);
/* 1856 */     state.unescaped = false;
/* 1857 */     state.prefixes = this._prefixes;
/*      */ 
/* 1859 */     this._prefixes = null;
/* 1860 */     return state;
/*      */   }
/*      */ 
/*      */   protected ElementState leaveElementState()
/*      */   {
/* 1873 */     if (this._elementStateCount > 0)
/*      */     {
/* 1875 */       this._prefixes = null;
/*      */ 
/* 1877 */       this._elementStateCount -= 1;
/* 1878 */       return this._elementStates[this._elementStateCount];
/*      */     }
/* 1880 */     String msg = DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "Internal", null);
/* 1881 */     throw new IllegalStateException(msg);
/*      */   }
/*      */ 
/*      */   protected boolean isDocumentState()
/*      */   {
/* 1895 */     return this._elementStateCount == 0;
/*      */   }
/*      */ 
/*      */   protected String getPrefix(String namespaceURI)
/*      */   {
/* 1911 */     if (this._prefixes != null) {
/* 1912 */       String prefix = (String)this._prefixes.get(namespaceURI);
/* 1913 */       if (prefix != null)
/* 1914 */         return prefix;
/*      */     }
/* 1916 */     if (this._elementStateCount == 0) {
/* 1917 */       return null;
/*      */     }
/* 1919 */     for (int i = this._elementStateCount; i > 0; i--) {
/* 1920 */       if (this._elementStates[i].prefixes != null) {
/* 1921 */         String prefix = (String)this._elementStates[i].prefixes.get(namespaceURI);
/* 1922 */         if (prefix != null) {
/* 1923 */           return prefix;
/*      */         }
/*      */       }
/*      */     }
/* 1927 */     return null;
/*      */   }
/*      */ 
/*      */   protected DOMError modifyDOMError(String message, short severity, String type, Node node)
/*      */   {
/* 1939 */     this.fDOMError.reset();
/* 1940 */     this.fDOMError.fMessage = message;
/* 1941 */     this.fDOMError.fType = type;
/* 1942 */     this.fDOMError.fSeverity = severity;
/* 1943 */     this.fDOMError.fLocator = new DOMLocatorImpl(-1, -1, -1, node, null);
/* 1944 */     return this.fDOMError;
/*      */   }
/*      */ 
/*      */   protected void fatalError(String message)
/*      */     throws IOException
/*      */   {
/* 1950 */     if (this.fDOMErrorHandler != null) {
/* 1951 */       modifyDOMError(message, (short)3, null, this.fCurrentNode);
/* 1952 */       this.fDOMErrorHandler.handleError(this.fDOMError);
/*      */     }
/*      */     else {
/* 1955 */       throw new IOException(message);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void checkUnboundNamespacePrefixedNode(Node node)
/*      */     throws IOException
/*      */   {
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.serialize.BaseMarkupSerializer
 * JD-Core Version:    0.6.2
 */