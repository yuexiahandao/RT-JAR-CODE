/*      */ package com.sun.org.apache.xml.internal.serializer;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.OutputStream;
/*      */ import java.io.Writer;
/*      */ import java.util.Properties;
/*      */ import java.util.Vector;
/*      */ import javax.xml.transform.SourceLocator;
/*      */ import javax.xml.transform.Transformer;
/*      */ import org.w3c.dom.Node;
/*      */ import org.xml.sax.Attributes;
/*      */ import org.xml.sax.ContentHandler;
/*      */ import org.xml.sax.Locator;
/*      */ import org.xml.sax.SAXException;
/*      */ 
/*      */ public final class ToUnknownStream extends SerializerBase
/*      */ {
/*      */   private SerializationHandler m_handler;
/*      */   private static final String EMPTYSTRING = "";
/*   72 */   private boolean m_wrapped_handler_not_initialized = false;
/*      */   private String m_firstElementPrefix;
/*      */   private String m_firstElementName;
/*      */   private String m_firstElementURI;
/*   92 */   private String m_firstElementLocalName = null;
/*      */ 
/*   97 */   private boolean m_firstTagNotEmitted = true;
/*      */ 
/*  103 */   private Vector m_namespaceURI = null;
/*      */ 
/*  108 */   private Vector m_namespacePrefix = null;
/*      */ 
/*  114 */   private boolean m_needToCallStartDocument = false;
/*      */ 
/*  119 */   private boolean m_setVersion_called = false;
/*      */ 
/*  124 */   private boolean m_setDoctypeSystem_called = false;
/*      */ 
/*  129 */   private boolean m_setDoctypePublic_called = false;
/*      */ 
/*  134 */   private boolean m_setMediaType_called = false;
/*      */ 
/*      */   public ToUnknownStream()
/*      */   {
/*  143 */     this.m_handler = new ToXMLStream();
/*      */   }
/*      */ 
/*      */   public ContentHandler asContentHandler()
/*      */     throws IOException
/*      */   {
/*  157 */     return this;
/*      */   }
/*      */ 
/*      */   public void close()
/*      */   {
/*  165 */     this.m_handler.close();
/*      */   }
/*      */ 
/*      */   public Properties getOutputFormat()
/*      */   {
/*  174 */     return this.m_handler.getOutputFormat();
/*      */   }
/*      */ 
/*      */   public OutputStream getOutputStream()
/*      */   {
/*  183 */     return this.m_handler.getOutputStream();
/*      */   }
/*      */ 
/*      */   public Writer getWriter()
/*      */   {
/*  192 */     return this.m_handler.getWriter();
/*      */   }
/*      */ 
/*      */   public boolean reset()
/*      */   {
/*  202 */     return this.m_handler.reset();
/*      */   }
/*      */ 
/*      */   public void serialize(Node node)
/*      */     throws IOException
/*      */   {
/*  213 */     if (this.m_firstTagNotEmitted)
/*      */     {
/*  215 */       flush();
/*      */     }
/*  217 */     this.m_handler.serialize(node);
/*      */   }
/*      */ 
/*      */   public boolean setEscaping(boolean escape)
/*      */     throws SAXException
/*      */   {
/*  225 */     return this.m_handler.setEscaping(escape);
/*      */   }
/*      */ 
/*      */   public void setOutputFormat(Properties format)
/*      */   {
/*  235 */     this.m_handler.setOutputFormat(format);
/*      */   }
/*      */ 
/*      */   public void setOutputStream(OutputStream output)
/*      */   {
/*  245 */     this.m_handler.setOutputStream(output);
/*      */   }
/*      */ 
/*      */   public void setWriter(Writer writer)
/*      */   {
/*  255 */     this.m_handler.setWriter(writer);
/*      */   }
/*      */ 
/*      */   public void addAttribute(String uri, String localName, String rawName, String type, String value)
/*      */     throws SAXException
/*      */   {
/*  276 */     addAttribute(uri, localName, rawName, type, value, false);
/*      */   }
/*      */ 
/*      */   public void addAttribute(String uri, String localName, String rawName, String type, String value, boolean XSLAttribute)
/*      */     throws SAXException
/*      */   {
/*  298 */     if (this.m_firstTagNotEmitted)
/*      */     {
/*  300 */       flush();
/*      */     }
/*  302 */     this.m_handler.addAttribute(uri, localName, rawName, type, value, XSLAttribute);
/*      */   }
/*      */ 
/*      */   public void addAttribute(String rawName, String value)
/*      */   {
/*  312 */     if (this.m_firstTagNotEmitted)
/*      */     {
/*  314 */       flush();
/*      */     }
/*  316 */     this.m_handler.addAttribute(rawName, value);
/*      */   }
/*      */ 
/*      */   public void addUniqueAttribute(String rawName, String value, int flags)
/*      */     throws SAXException
/*      */   {
/*  326 */     if (this.m_firstTagNotEmitted)
/*      */     {
/*  328 */       flush();
/*      */     }
/*  330 */     this.m_handler.addUniqueAttribute(rawName, value, flags);
/*      */   }
/*      */ 
/*      */   public void characters(String chars)
/*      */     throws SAXException
/*      */   {
/*  342 */     int length = chars.length();
/*  343 */     if (length > this.m_charsBuff.length)
/*      */     {
/*  345 */       this.m_charsBuff = new char[length * 2 + 1];
/*      */     }
/*  347 */     chars.getChars(0, length, this.m_charsBuff, 0);
/*  348 */     characters(this.m_charsBuff, 0, length);
/*      */   }
/*      */ 
/*      */   public void endElement(String elementName)
/*      */     throws SAXException
/*      */   {
/*  357 */     if (this.m_firstTagNotEmitted)
/*      */     {
/*  359 */       flush();
/*      */     }
/*  361 */     this.m_handler.endElement(elementName);
/*      */   }
/*      */ 
/*      */   public void startPrefixMapping(String prefix, String uri)
/*      */     throws SAXException
/*      */   {
/*  372 */     startPrefixMapping(prefix, uri, true);
/*      */   }
/*      */ 
/*      */   public void namespaceAfterStartElement(String prefix, String uri)
/*      */     throws SAXException
/*      */   {
/*  390 */     if ((this.m_firstTagNotEmitted) && (this.m_firstElementURI == null) && (this.m_firstElementName != null))
/*      */     {
/*  392 */       String prefix1 = getPrefixPart(this.m_firstElementName);
/*  393 */       if ((prefix1 == null) && ("".equals(prefix)))
/*      */       {
/*  399 */         this.m_firstElementURI = uri;
/*      */       }
/*      */     }
/*  402 */     startPrefixMapping(prefix, uri, false);
/*      */   }
/*      */ 
/*      */   public boolean startPrefixMapping(String prefix, String uri, boolean shouldFlush)
/*      */     throws SAXException
/*      */   {
/*  408 */     boolean pushed = false;
/*  409 */     if (this.m_firstTagNotEmitted)
/*      */     {
/*  411 */       if ((this.m_firstElementName != null) && (shouldFlush))
/*      */       {
/*  417 */         flush();
/*  418 */         pushed = this.m_handler.startPrefixMapping(prefix, uri, shouldFlush);
/*      */       }
/*      */       else
/*      */       {
/*  422 */         if (this.m_namespacePrefix == null)
/*      */         {
/*  424 */           this.m_namespacePrefix = new Vector();
/*  425 */           this.m_namespaceURI = new Vector();
/*      */         }
/*  427 */         this.m_namespacePrefix.addElement(prefix);
/*  428 */         this.m_namespaceURI.addElement(uri);
/*      */ 
/*  430 */         if (this.m_firstElementURI == null)
/*      */         {
/*  432 */           if (prefix.equals(this.m_firstElementPrefix)) {
/*  433 */             this.m_firstElementURI = uri;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  440 */       pushed = this.m_handler.startPrefixMapping(prefix, uri, shouldFlush);
/*      */     }
/*  442 */     return pushed;
/*      */   }
/*      */ 
/*      */   public void setVersion(String version)
/*      */   {
/*  452 */     this.m_handler.setVersion(version);
/*      */ 
/*  456 */     this.m_setVersion_called = true;
/*      */   }
/*      */ 
/*      */   public void startDocument()
/*      */     throws SAXException
/*      */   {
/*  464 */     this.m_needToCallStartDocument = true;
/*      */   }
/*      */ 
/*      */   public void startElement(String qName)
/*      */     throws SAXException
/*      */   {
/*  471 */     startElement(null, null, qName, null);
/*      */   }
/*      */ 
/*      */   public void startElement(String namespaceURI, String localName, String qName) throws SAXException
/*      */   {
/*  476 */     startElement(namespaceURI, localName, qName, null);
/*      */   }
/*      */ 
/*      */   public void startElement(String namespaceURI, String localName, String elementName, Attributes atts)
/*      */     throws SAXException
/*      */   {
/*  486 */     if (this.m_needToCallSetDocumentInfo) {
/*  487 */       super.setDocumentInfo();
/*  488 */       this.m_needToCallSetDocumentInfo = false;
/*      */     }
/*      */ 
/*  492 */     if (this.m_firstTagNotEmitted)
/*      */     {
/*  495 */       if (this.m_firstElementName != null)
/*      */       {
/*  501 */         flush();
/*  502 */         this.m_handler.startElement(namespaceURI, localName, elementName, atts);
/*      */       }
/*      */       else
/*      */       {
/*  511 */         this.m_wrapped_handler_not_initialized = true;
/*  512 */         this.m_firstElementName = elementName;
/*      */ 
/*  515 */         this.m_firstElementPrefix = getPrefixPartUnknown(elementName);
/*      */ 
/*  518 */         this.m_firstElementURI = namespaceURI;
/*      */ 
/*  521 */         this.m_firstElementLocalName = localName;
/*      */ 
/*  523 */         if (this.m_tracer != null) {
/*  524 */           firePseudoElement(elementName);
/*      */         }
/*      */ 
/*  533 */         if (atts != null) {
/*  534 */           super.addAttributes(atts);
/*      */         }
/*      */ 
/*  539 */         if (atts != null) {
/*  540 */           flush();
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*  548 */       this.m_handler.startElement(namespaceURI, localName, elementName, atts);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void comment(String comment)
/*      */     throws SAXException
/*      */   {
/*  558 */     if ((this.m_firstTagNotEmitted) && (this.m_firstElementName != null))
/*      */     {
/*  560 */       emitFirstTag();
/*      */     }
/*  562 */     else if (this.m_needToCallStartDocument)
/*      */     {
/*  564 */       this.m_handler.startDocument();
/*  565 */       this.m_needToCallStartDocument = false;
/*      */     }
/*      */ 
/*  568 */     this.m_handler.comment(comment);
/*      */   }
/*      */ 
/*      */   public String getDoctypePublic()
/*      */   {
/*  578 */     return this.m_handler.getDoctypePublic();
/*      */   }
/*      */ 
/*      */   public String getDoctypeSystem()
/*      */   {
/*  587 */     return this.m_handler.getDoctypeSystem();
/*      */   }
/*      */ 
/*      */   public String getEncoding()
/*      */   {
/*  596 */     return this.m_handler.getEncoding();
/*      */   }
/*      */ 
/*      */   public boolean getIndent()
/*      */   {
/*  605 */     return this.m_handler.getIndent();
/*      */   }
/*      */ 
/*      */   public int getIndentAmount()
/*      */   {
/*  614 */     return this.m_handler.getIndentAmount();
/*      */   }
/*      */ 
/*      */   public String getMediaType()
/*      */   {
/*  623 */     return this.m_handler.getMediaType();
/*      */   }
/*      */ 
/*      */   public boolean getOmitXMLDeclaration()
/*      */   {
/*  632 */     return this.m_handler.getOmitXMLDeclaration();
/*      */   }
/*      */ 
/*      */   public String getStandalone()
/*      */   {
/*  641 */     return this.m_handler.getStandalone();
/*      */   }
/*      */ 
/*      */   public String getVersion()
/*      */   {
/*  650 */     return this.m_handler.getVersion();
/*      */   }
/*      */ 
/*      */   public void setDoctype(String system, String pub)
/*      */   {
/*  658 */     this.m_handler.setDoctypePublic(pub);
/*  659 */     this.m_handler.setDoctypeSystem(system);
/*      */   }
/*      */ 
/*      */   public void setDoctypePublic(String doctype)
/*      */   {
/*  670 */     this.m_handler.setDoctypePublic(doctype);
/*  671 */     this.m_setDoctypePublic_called = true;
/*      */   }
/*      */ 
/*      */   public void setDoctypeSystem(String doctype)
/*      */   {
/*  682 */     this.m_handler.setDoctypeSystem(doctype);
/*  683 */     this.m_setDoctypeSystem_called = true;
/*      */   }
/*      */ 
/*      */   public void setEncoding(String encoding)
/*      */   {
/*  692 */     this.m_handler.setEncoding(encoding);
/*      */   }
/*      */ 
/*      */   public void setIndent(boolean indent)
/*      */   {
/*  701 */     this.m_handler.setIndent(indent);
/*      */   }
/*      */ 
/*      */   public void setIndentAmount(int value)
/*      */   {
/*  709 */     this.m_handler.setIndentAmount(value);
/*      */   }
/*      */ 
/*      */   public void setMediaType(String mediaType)
/*      */   {
/*  717 */     this.m_handler.setMediaType(mediaType);
/*  718 */     this.m_setMediaType_called = true;
/*      */   }
/*      */ 
/*      */   public void setOmitXMLDeclaration(boolean b)
/*      */   {
/*  727 */     this.m_handler.setOmitXMLDeclaration(b);
/*      */   }
/*      */ 
/*      */   public void setStandalone(String standalone)
/*      */   {
/*  736 */     this.m_handler.setStandalone(standalone);
/*      */   }
/*      */ 
/*      */   public void attributeDecl(String arg0, String arg1, String arg2, String arg3, String arg4)
/*      */     throws SAXException
/*      */   {
/*  755 */     this.m_handler.attributeDecl(arg0, arg1, arg2, arg3, arg4);
/*      */   }
/*      */ 
/*      */   public void elementDecl(String arg0, String arg1)
/*      */     throws SAXException
/*      */   {
/*  764 */     if (this.m_firstTagNotEmitted)
/*      */     {
/*  766 */       emitFirstTag();
/*      */     }
/*  768 */     this.m_handler.elementDecl(arg0, arg1);
/*      */   }
/*      */ 
/*      */   public void externalEntityDecl(String name, String publicId, String systemId)
/*      */     throws SAXException
/*      */   {
/*  781 */     if (this.m_firstTagNotEmitted)
/*      */     {
/*  783 */       flush();
/*      */     }
/*  785 */     this.m_handler.externalEntityDecl(name, publicId, systemId);
/*      */   }
/*      */ 
/*      */   public void internalEntityDecl(String arg0, String arg1)
/*      */     throws SAXException
/*      */   {
/*  795 */     if (this.m_firstTagNotEmitted)
/*      */     {
/*  797 */       flush();
/*      */     }
/*  799 */     this.m_handler.internalEntityDecl(arg0, arg1);
/*      */   }
/*      */ 
/*      */   public void characters(char[] characters, int offset, int length)
/*      */     throws SAXException
/*      */   {
/*  809 */     if (this.m_firstTagNotEmitted)
/*      */     {
/*  811 */       flush();
/*      */     }
/*      */ 
/*  814 */     this.m_handler.characters(characters, offset, length);
/*      */   }
/*      */ 
/*      */   public void endDocument()
/*      */     throws SAXException
/*      */   {
/*  824 */     if (this.m_firstTagNotEmitted)
/*      */     {
/*  826 */       flush();
/*      */     }
/*      */ 
/*  829 */     this.m_handler.endDocument();
/*      */   }
/*      */ 
/*      */   public void endElement(String namespaceURI, String localName, String qName)
/*      */     throws SAXException
/*      */   {
/*  841 */     if (this.m_firstTagNotEmitted)
/*      */     {
/*  843 */       flush();
/*  844 */       if ((namespaceURI == null) && (this.m_firstElementURI != null)) {
/*  845 */         namespaceURI = this.m_firstElementURI;
/*      */       }
/*      */ 
/*  848 */       if ((localName == null) && (this.m_firstElementLocalName != null)) {
/*  849 */         localName = this.m_firstElementLocalName;
/*      */       }
/*      */     }
/*  852 */     this.m_handler.endElement(namespaceURI, localName, qName);
/*      */   }
/*      */ 
/*      */   public void endPrefixMapping(String prefix)
/*      */     throws SAXException
/*      */   {
/*  861 */     this.m_handler.endPrefixMapping(prefix);
/*      */   }
/*      */ 
/*      */   public void ignorableWhitespace(char[] ch, int start, int length)
/*      */     throws SAXException
/*      */   {
/*  871 */     if (this.m_firstTagNotEmitted)
/*      */     {
/*  873 */       flush();
/*      */     }
/*  875 */     this.m_handler.ignorableWhitespace(ch, start, length);
/*      */   }
/*      */ 
/*      */   public void processingInstruction(String target, String data)
/*      */     throws SAXException
/*      */   {
/*  885 */     if (this.m_firstTagNotEmitted)
/*      */     {
/*  887 */       flush();
/*      */     }
/*      */ 
/*  890 */     this.m_handler.processingInstruction(target, data);
/*      */   }
/*      */ 
/*      */   public void setDocumentLocator(Locator locator)
/*      */   {
/*  899 */     super.setDocumentLocator(locator);
/*  900 */     this.m_handler.setDocumentLocator(locator);
/*      */   }
/*      */ 
/*      */   public void skippedEntity(String name)
/*      */     throws SAXException
/*      */   {
/*  909 */     this.m_handler.skippedEntity(name);
/*      */   }
/*      */ 
/*      */   public void comment(char[] ch, int start, int length)
/*      */     throws SAXException
/*      */   {
/*  920 */     if (this.m_firstTagNotEmitted)
/*      */     {
/*  922 */       flush();
/*      */     }
/*      */ 
/*  925 */     this.m_handler.comment(ch, start, length);
/*      */   }
/*      */ 
/*      */   public void endCDATA()
/*      */     throws SAXException
/*      */   {
/*  935 */     this.m_handler.endCDATA();
/*      */   }
/*      */ 
/*      */   public void endDTD()
/*      */     throws SAXException
/*      */   {
/*  945 */     this.m_handler.endDTD();
/*      */   }
/*      */ 
/*      */   public void endEntity(String name)
/*      */     throws SAXException
/*      */   {
/*  954 */     if (this.m_firstTagNotEmitted)
/*      */     {
/*  956 */       emitFirstTag();
/*      */     }
/*  958 */     this.m_handler.endEntity(name);
/*      */   }
/*      */ 
/*      */   public void startCDATA()
/*      */     throws SAXException
/*      */   {
/*  967 */     this.m_handler.startCDATA();
/*      */   }
/*      */ 
/*      */   public void startDTD(String name, String publicId, String systemId)
/*      */     throws SAXException
/*      */   {
/*  977 */     this.m_handler.startDTD(name, publicId, systemId);
/*      */   }
/*      */ 
/*      */   public void startEntity(String name)
/*      */     throws SAXException
/*      */   {
/*  986 */     this.m_handler.startEntity(name);
/*      */   }
/*      */ 
/*      */   private void initStreamOutput()
/*      */     throws SAXException
/*      */   {
/* 1000 */     boolean firstElementIsHTML = isFirstElemHTML();
/*      */ 
/* 1002 */     if (firstElementIsHTML)
/*      */     {
/* 1007 */       SerializationHandler oldHandler = this.m_handler;
/*      */ 
/* 1014 */       Properties htmlProperties = OutputPropertiesFactory.getDefaultMethodProperties("html");
/*      */ 
/* 1016 */       Serializer serializer = SerializerFactory.getSerializer(htmlProperties);
/*      */ 
/* 1023 */       this.m_handler = ((SerializationHandler)serializer);
/*      */ 
/* 1026 */       Writer writer = oldHandler.getWriter();
/*      */ 
/* 1028 */       if (null != writer) {
/* 1029 */         this.m_handler.setWriter(writer);
/*      */       }
/*      */       else {
/* 1032 */         OutputStream os = oldHandler.getOutputStream();
/*      */ 
/* 1034 */         if (null != os) {
/* 1035 */           this.m_handler.setOutputStream(os);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1042 */       this.m_handler.setVersion(oldHandler.getVersion());
/*      */ 
/* 1046 */       this.m_handler.setDoctypeSystem(oldHandler.getDoctypeSystem());
/*      */ 
/* 1050 */       this.m_handler.setDoctypePublic(oldHandler.getDoctypePublic());
/*      */ 
/* 1054 */       this.m_handler.setMediaType(oldHandler.getMediaType());
/*      */ 
/* 1057 */       this.m_handler.setTransformer(oldHandler.getTransformer());
/*      */     }
/*      */ 
/* 1064 */     if (this.m_needToCallStartDocument)
/*      */     {
/* 1066 */       this.m_handler.startDocument();
/* 1067 */       this.m_needToCallStartDocument = false;
/*      */     }
/*      */ 
/* 1071 */     this.m_wrapped_handler_not_initialized = false;
/*      */   }
/*      */ 
/*      */   private void emitFirstTag() throws SAXException
/*      */   {
/* 1076 */     if (this.m_firstElementName != null)
/*      */     {
/* 1078 */       if (this.m_wrapped_handler_not_initialized)
/*      */       {
/* 1080 */         initStreamOutput();
/* 1081 */         this.m_wrapped_handler_not_initialized = false;
/*      */       }
/*      */ 
/* 1084 */       this.m_handler.startElement(this.m_firstElementURI, null, this.m_firstElementName, this.m_attributes);
/*      */ 
/* 1086 */       this.m_attributes = null;
/*      */ 
/* 1089 */       if (this.m_namespacePrefix != null)
/*      */       {
/* 1091 */         int n = this.m_namespacePrefix.size();
/* 1092 */         for (int i = 0; i < n; i++)
/*      */         {
/* 1094 */           String prefix = (String)this.m_namespacePrefix.elementAt(i);
/*      */ 
/* 1096 */           String uri = (String)this.m_namespaceURI.elementAt(i);
/* 1097 */           this.m_handler.startPrefixMapping(prefix, uri, false);
/*      */         }
/* 1099 */         this.m_namespacePrefix = null;
/* 1100 */         this.m_namespaceURI = null;
/*      */       }
/* 1102 */       this.m_firstTagNotEmitted = false;
/*      */     }
/*      */   }
/*      */ 
/*      */   private String getLocalNameUnknown(String value)
/*      */   {
/* 1114 */     int idx = value.lastIndexOf(':');
/* 1115 */     if (idx >= 0)
/* 1116 */       value = value.substring(idx + 1);
/* 1117 */     idx = value.lastIndexOf('@');
/* 1118 */     if (idx >= 0)
/* 1119 */       value = value.substring(idx + 1);
/* 1120 */     return value;
/*      */   }
/*      */ 
/*      */   private String getPrefixPartUnknown(String qname)
/*      */   {
/* 1131 */     int index = qname.indexOf(':');
/* 1132 */     return index > 0 ? qname.substring(0, index) : "";
/*      */   }
/*      */ 
/*      */   private boolean isFirstElemHTML()
/*      */   {
/* 1147 */     boolean isHTML = getLocalNameUnknown(this.m_firstElementName).equalsIgnoreCase("html");
/*      */ 
/* 1151 */     if ((isHTML) && (this.m_firstElementURI != null) && (!"".equals(this.m_firstElementURI)))
/*      */     {
/* 1156 */       isHTML = false;
/*      */     }
/*      */ 
/* 1159 */     if ((isHTML) && (this.m_namespacePrefix != null))
/*      */     {
/* 1165 */       int max = this.m_namespacePrefix.size();
/* 1166 */       for (int i = 0; i < max; i++)
/*      */       {
/* 1168 */         String prefix = (String)this.m_namespacePrefix.elementAt(i);
/* 1169 */         String uri = (String)this.m_namespaceURI.elementAt(i);
/*      */ 
/* 1171 */         if ((this.m_firstElementPrefix != null) && (this.m_firstElementPrefix.equals(prefix)) && (!"".equals(uri)))
/*      */         {
/* 1176 */           isHTML = false;
/* 1177 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1182 */     return isHTML;
/*      */   }
/*      */ 
/*      */   public DOMSerializer asDOMSerializer()
/*      */     throws IOException
/*      */   {
/* 1189 */     return this.m_handler.asDOMSerializer();
/*      */   }
/*      */ 
/*      */   public void setCdataSectionElements(Vector URI_and_localNames)
/*      */   {
/* 1199 */     this.m_handler.setCdataSectionElements(URI_and_localNames);
/*      */   }
/*      */ 
/*      */   public void addAttributes(Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 1206 */     this.m_handler.addAttributes(atts);
/*      */   }
/*      */ 
/*      */   public NamespaceMappings getNamespaceMappings()
/*      */   {
/* 1216 */     NamespaceMappings mappings = null;
/* 1217 */     if (this.m_handler != null)
/*      */     {
/* 1219 */       mappings = this.m_handler.getNamespaceMappings();
/*      */     }
/* 1221 */     return mappings;
/*      */   }
/*      */ 
/*      */   public void flushPending()
/*      */     throws SAXException
/*      */   {
/* 1229 */     flush();
/*      */ 
/* 1231 */     this.m_handler.flushPending();
/*      */   }
/*      */ 
/*      */   private void flush()
/*      */   {
/*      */     try
/*      */     {
/* 1238 */       if (this.m_firstTagNotEmitted)
/*      */       {
/* 1240 */         emitFirstTag();
/*      */       }
/* 1242 */       if (this.m_needToCallStartDocument)
/*      */       {
/* 1244 */         this.m_handler.startDocument();
/* 1245 */         this.m_needToCallStartDocument = false;
/*      */       }
/*      */     }
/*      */     catch (SAXException e)
/*      */     {
/* 1250 */       throw new RuntimeException(e.toString());
/*      */     }
/*      */   }
/*      */ 
/*      */   public String getPrefix(String namespaceURI)
/*      */   {
/* 1261 */     return this.m_handler.getPrefix(namespaceURI);
/*      */   }
/*      */ 
/*      */   public void entityReference(String entityName)
/*      */     throws SAXException
/*      */   {
/* 1268 */     this.m_handler.entityReference(entityName);
/*      */   }
/*      */ 
/*      */   public String getNamespaceURI(String qname, boolean isElement)
/*      */   {
/* 1276 */     return this.m_handler.getNamespaceURI(qname, isElement);
/*      */   }
/*      */ 
/*      */   public String getNamespaceURIFromPrefix(String prefix)
/*      */   {
/* 1281 */     return this.m_handler.getNamespaceURIFromPrefix(prefix);
/*      */   }
/*      */ 
/*      */   public void setTransformer(Transformer t)
/*      */   {
/* 1286 */     this.m_handler.setTransformer(t);
/* 1287 */     if (((t instanceof SerializerTrace)) && (((SerializerTrace)t).hasTraceListeners()))
/*      */     {
/* 1289 */       this.m_tracer = ((SerializerTrace)t);
/*      */     }
/* 1291 */     else this.m_tracer = null;
/*      */   }
/*      */ 
/*      */   public Transformer getTransformer()
/*      */   {
/* 1296 */     return this.m_handler.getTransformer();
/*      */   }
/*      */ 
/*      */   public void setContentHandler(ContentHandler ch)
/*      */   {
/* 1304 */     this.m_handler.setContentHandler(ch);
/*      */   }
/*      */ 
/*      */   public void setSourceLocator(SourceLocator locator)
/*      */   {
/* 1315 */     this.m_handler.setSourceLocator(locator);
/*      */   }
/*      */ 
/*      */   protected void firePseudoElement(String elementName)
/*      */   {
/* 1321 */     if (this.m_tracer != null) {
/* 1322 */       StringBuffer sb = new StringBuffer();
/*      */ 
/* 1324 */       sb.append('<');
/* 1325 */       sb.append(elementName);
/*      */ 
/* 1330 */       char[] ch = sb.toString().toCharArray();
/* 1331 */       this.m_tracer.fireGenerateEvent(11, ch, 0, ch.length);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.serializer.ToUnknownStream
 * JD-Core Version:    0.6.2
 */