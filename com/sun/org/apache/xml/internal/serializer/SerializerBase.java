/*      */ package com.sun.org.apache.xml.internal.serializer;
/*      */ 
/*      */ import com.sun.org.apache.xml.internal.serializer.utils.Messages;
/*      */ import com.sun.org.apache.xml.internal.serializer.utils.Utils;
/*      */ import java.io.IOException;
/*      */ import java.io.Writer;
/*      */ import java.util.Vector;
/*      */ import javax.xml.transform.SourceLocator;
/*      */ import javax.xml.transform.Transformer;
/*      */ import org.w3c.dom.Node;
/*      */ import org.xml.sax.Attributes;
/*      */ import org.xml.sax.ContentHandler;
/*      */ import org.xml.sax.Locator;
/*      */ import org.xml.sax.SAXException;
/*      */ import org.xml.sax.SAXParseException;
/*      */ import org.xml.sax.ext.Locator2;
/*      */ 
/*      */ public abstract class SerializerBase
/*      */   implements SerializationHandler, SerializerConstants
/*      */ {
/*   86 */   protected boolean m_needToCallStartDocument = true;
/*      */ 
/*   91 */   protected boolean m_cdataTagOpen = false;
/*      */ 
/*   98 */   protected AttributesImplSerializer m_attributes = new AttributesImplSerializer();
/*      */ 
/*  103 */   protected boolean m_inEntityRef = false;
/*      */ 
/*  106 */   protected boolean m_inExternalDTD = false;
/*      */   private String m_doctypeSystem;
/*      */   private String m_doctypePublic;
/*  122 */   boolean m_needToOutputDocTypeDecl = true;
/*      */ 
/*  128 */   private String m_encoding = null;
/*      */ 
/*  133 */   private boolean m_shouldNotWriteXMLHeader = false;
/*      */   private String m_standalone;
/*  143 */   protected boolean m_standaloneWasSpecified = false;
/*      */ 
/*  148 */   protected boolean m_isStandalone = false;
/*      */ 
/*  153 */   protected boolean m_doIndent = false;
/*      */ 
/*  157 */   protected int m_indentAmount = 0;
/*      */ 
/*  162 */   private String m_version = null;
/*      */   private String m_mediatype;
/*      */   private Transformer m_transformer;
/*  180 */   protected Vector m_cdataSectionElements = null;
/*      */   protected NamespaceMappings m_prefixMap;
/*      */   protected SerializerTrace m_tracer;
/*      */   protected SourceLocator m_sourceLocator;
/*  203 */   protected Writer m_writer = null;
/*      */ 
/*  211 */   protected ElemContext m_elemContext = new ElemContext();
/*      */ 
/*  219 */   protected char[] m_charsBuff = new char[60];
/*      */ 
/*  227 */   protected char[] m_attrBuff = new char[30];
/*      */ 
/*  229 */   private Locator m_locator = null;
/*      */ 
/*  231 */   protected boolean m_needToCallSetDocumentInfo = true;
/*      */ 
/*      */   protected void fireEndElem(String name)
/*      */     throws SAXException
/*      */   {
/*   60 */     if (this.m_tracer != null)
/*      */     {
/*   62 */       flushMyWriter();
/*   63 */       this.m_tracer.fireGenerateEvent(4, name, (Attributes)null);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void fireCharEvent(char[] chars, int start, int length)
/*      */     throws SAXException
/*      */   {
/*   76 */     if (this.m_tracer != null)
/*      */     {
/*   78 */       flushMyWriter();
/*   79 */       this.m_tracer.fireGenerateEvent(5, chars, start, length);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void comment(String data)
/*      */     throws SAXException
/*      */   {
/*  240 */     int length = data.length();
/*  241 */     if (length > this.m_charsBuff.length)
/*      */     {
/*  243 */       this.m_charsBuff = new char[length * 2 + 1];
/*      */     }
/*  245 */     data.getChars(0, length, this.m_charsBuff, 0);
/*  246 */     comment(this.m_charsBuff, 0, length);
/*      */   }
/*      */ 
/*      */   protected String patchName(String qname)
/*      */   {
/*  263 */     int lastColon = qname.lastIndexOf(':');
/*      */ 
/*  265 */     if (lastColon > 0) {
/*  266 */       int firstColon = qname.indexOf(':');
/*  267 */       String prefix = qname.substring(0, firstColon);
/*  268 */       String localName = qname.substring(lastColon + 1);
/*      */ 
/*  271 */       String uri = this.m_prefixMap.lookupNamespace(prefix);
/*  272 */       if ((uri != null) && (uri.length() == 0)) {
/*  273 */         return localName;
/*      */       }
/*  275 */       if (firstColon != lastColon) {
/*  276 */         return prefix + ':' + localName;
/*      */       }
/*      */     }
/*  279 */     return qname;
/*      */   }
/*      */ 
/*      */   protected static String getLocalName(String qname)
/*      */   {
/*  290 */     int col = qname.lastIndexOf(':');
/*  291 */     return col > 0 ? qname.substring(col + 1) : qname;
/*      */   }
/*      */ 
/*      */   public void setDocumentLocator(Locator locator)
/*      */   {
/*  322 */     this.m_locator = locator;
/*      */   }
/*      */ 
/*      */   public void addAttribute(String uri, String localName, String rawName, String type, String value, boolean XSLAttribute)
/*      */     throws SAXException
/*      */   {
/*  352 */     if (this.m_elemContext.m_startTagOpen)
/*      */     {
/*  354 */       addAttributeAlways(uri, localName, rawName, type, value, XSLAttribute);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean addAttributeAlways(String uri, String localName, String rawName, String type, String value, boolean XSLAttribute)
/*      */   {
/*      */     int index;
/*      */     int index;
/*  393 */     if ((localName == null) || (uri == null) || (uri.length() == 0))
/*  394 */       index = this.m_attributes.getIndex(rawName);
/*      */     else
/*  396 */       index = this.m_attributes.getIndex(uri, localName);
/*      */     boolean was_added;
/*      */     boolean was_added;
/*  398 */     if (index >= 0)
/*      */     {
/*  404 */       this.m_attributes.setValue(index, value);
/*  405 */       was_added = false;
/*      */     }
/*      */     else
/*      */     {
/*  410 */       this.m_attributes.addAttribute(uri, localName, rawName, type, value);
/*  411 */       was_added = true;
/*      */     }
/*  413 */     return was_added;
/*      */   }
/*      */ 
/*      */   public void addAttribute(String name, String value)
/*      */   {
/*  427 */     if (this.m_elemContext.m_startTagOpen)
/*      */     {
/*  429 */       String patchedName = patchName(name);
/*  430 */       String localName = getLocalName(patchedName);
/*  431 */       String uri = getNamespaceURI(patchedName, false);
/*      */ 
/*  433 */       addAttributeAlways(uri, localName, patchedName, "CDATA", value, false);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void addXSLAttribute(String name, String value, String uri)
/*      */   {
/*  447 */     if (this.m_elemContext.m_startTagOpen)
/*      */     {
/*  449 */       String patchedName = patchName(name);
/*  450 */       String localName = getLocalName(patchedName);
/*      */ 
/*  452 */       addAttributeAlways(uri, localName, patchedName, "CDATA", value, true);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void addAttributes(Attributes atts)
/*      */     throws SAXException
/*      */   {
/*  465 */     int nAtts = atts.getLength();
/*  466 */     for (int i = 0; i < nAtts; i++)
/*      */     {
/*  468 */       String uri = atts.getURI(i);
/*      */ 
/*  470 */       if (null == uri) {
/*  471 */         uri = "";
/*      */       }
/*  473 */       addAttributeAlways(uri, atts.getLocalName(i), atts.getQName(i), atts.getType(i), atts.getValue(i), false);
/*      */     }
/*      */   }
/*      */ 
/*      */   public ContentHandler asContentHandler()
/*      */     throws IOException
/*      */   {
/*  495 */     return this;
/*      */   }
/*      */ 
/*      */   public void endEntity(String name)
/*      */     throws SAXException
/*      */   {
/*  507 */     if (name.equals("[dtd]"))
/*  508 */       this.m_inExternalDTD = false;
/*  509 */     this.m_inEntityRef = false;
/*      */ 
/*  511 */     if (this.m_tracer != null)
/*  512 */       fireEndEntity(name);
/*      */   }
/*      */ 
/*      */   public void close()
/*      */   {
/*      */   }
/*      */ 
/*      */   protected void initCDATA()
/*      */   {
/*      */   }
/*      */ 
/*      */   public String getEncoding()
/*      */   {
/*  541 */     return this.m_encoding;
/*      */   }
/*      */ 
/*      */   public void setEncoding(String m_encoding)
/*      */   {
/*  550 */     this.m_encoding = m_encoding;
/*      */   }
/*      */ 
/*      */   public void setOmitXMLDeclaration(boolean b)
/*      */   {
/*  560 */     this.m_shouldNotWriteXMLHeader = b;
/*      */   }
/*      */ 
/*      */   public boolean getOmitXMLDeclaration()
/*      */   {
/*  570 */     return this.m_shouldNotWriteXMLHeader;
/*      */   }
/*      */ 
/*      */   public String getDoctypePublic()
/*      */   {
/*  582 */     return this.m_doctypePublic;
/*      */   }
/*      */ 
/*      */   public void setDoctypePublic(String doctypePublic)
/*      */   {
/*  591 */     this.m_doctypePublic = doctypePublic;
/*      */   }
/*      */ 
/*      */   public String getDoctypeSystem()
/*      */   {
/*  604 */     return this.m_doctypeSystem;
/*      */   }
/*      */ 
/*      */   public void setDoctypeSystem(String doctypeSystem)
/*      */   {
/*  613 */     this.m_doctypeSystem = doctypeSystem;
/*      */   }
/*      */ 
/*      */   public void setDoctype(String doctypeSystem, String doctypePublic)
/*      */   {
/*  624 */     this.m_doctypeSystem = doctypeSystem;
/*  625 */     this.m_doctypePublic = doctypePublic;
/*      */   }
/*      */ 
/*      */   public void setStandalone(String standalone)
/*      */   {
/*  637 */     if (standalone != null)
/*      */     {
/*  639 */       this.m_standaloneWasSpecified = true;
/*  640 */       setStandaloneInternal(standalone);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void setStandaloneInternal(String standalone)
/*      */   {
/*  650 */     if ("yes".equals(standalone))
/*  651 */       this.m_standalone = "yes";
/*      */     else
/*  653 */       this.m_standalone = "no";
/*      */   }
/*      */ 
/*      */   public String getStandalone()
/*      */   {
/*  665 */     return this.m_standalone;
/*      */   }
/*      */ 
/*      */   public boolean getIndent()
/*      */   {
/*  674 */     return this.m_doIndent;
/*      */   }
/*      */ 
/*      */   public String getMediaType()
/*      */   {
/*  684 */     return this.m_mediatype;
/*      */   }
/*      */ 
/*      */   public String getVersion()
/*      */   {
/*  693 */     return this.m_version;
/*      */   }
/*      */ 
/*      */   public void setVersion(String version)
/*      */   {
/*  703 */     this.m_version = version;
/*      */   }
/*      */ 
/*      */   public void setMediaType(String mediaType)
/*      */   {
/*  715 */     this.m_mediatype = mediaType;
/*      */   }
/*      */ 
/*      */   public int getIndentAmount()
/*      */   {
/*  723 */     return this.m_indentAmount;
/*      */   }
/*      */ 
/*      */   public void setIndentAmount(int m_indentAmount)
/*      */   {
/*  732 */     this.m_indentAmount = m_indentAmount;
/*      */   }
/*      */ 
/*      */   public void setIndent(boolean doIndent)
/*      */   {
/*  744 */     this.m_doIndent = doIndent;
/*      */   }
/*      */ 
/*      */   public void setIsStandalone(boolean isStandalone)
/*      */   {
/*  754 */     this.m_isStandalone = isStandalone;
/*      */   }
/*      */ 
/*      */   public void namespaceAfterStartElement(String uri, String prefix)
/*      */     throws SAXException
/*      */   {
/*      */   }
/*      */ 
/*      */   public DOMSerializer asDOMSerializer()
/*      */     throws IOException
/*      */   {
/*  786 */     return this;
/*      */   }
/*      */ 
/*      */   protected boolean isCdataSection()
/*      */   {
/*  803 */     boolean b = false;
/*      */ 
/*  805 */     if (null != this.m_cdataSectionElements)
/*      */     {
/*  807 */       if (this.m_elemContext.m_elementLocalName == null) {
/*  808 */         this.m_elemContext.m_elementLocalName = getLocalName(this.m_elemContext.m_elementName);
/*      */       }
/*  810 */       if (this.m_elemContext.m_elementURI == null)
/*      */       {
/*  812 */         String prefix = getPrefixPart(this.m_elemContext.m_elementName);
/*  813 */         if (prefix != null) {
/*  814 */           this.m_elemContext.m_elementURI = this.m_prefixMap.lookupNamespace(prefix);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  819 */       if ((null != this.m_elemContext.m_elementURI) && (this.m_elemContext.m_elementURI.length() == 0))
/*      */       {
/*  821 */         this.m_elemContext.m_elementURI = null;
/*      */       }
/*  823 */       int nElems = this.m_cdataSectionElements.size();
/*      */ 
/*  826 */       for (int i = 0; i < nElems; i += 2)
/*      */       {
/*  828 */         String uri = (String)this.m_cdataSectionElements.elementAt(i);
/*  829 */         String loc = (String)this.m_cdataSectionElements.elementAt(i + 1);
/*  830 */         if ((loc.equals(this.m_elemContext.m_elementLocalName)) && (subPartMatch(this.m_elemContext.m_elementURI, uri)))
/*      */         {
/*  833 */           b = true;
/*      */ 
/*  835 */           break;
/*      */         }
/*      */       }
/*      */     }
/*  839 */     return b;
/*      */   }
/*      */ 
/*      */   private static final boolean subPartMatch(String p, String t)
/*      */   {
/*  852 */     return (p == t) || ((null != p) && (p.equals(t)));
/*      */   }
/*      */ 
/*      */   protected static final String getPrefixPart(String qname)
/*      */   {
/*  866 */     int col = qname.indexOf(':');
/*  867 */     return col > 0 ? qname.substring(0, col) : null;
/*      */   }
/*      */ 
/*      */   public NamespaceMappings getNamespaceMappings()
/*      */   {
/*  878 */     return this.m_prefixMap;
/*      */   }
/*      */ 
/*      */   public String getPrefix(String namespaceURI)
/*      */   {
/*  889 */     String prefix = this.m_prefixMap.lookupPrefix(namespaceURI);
/*  890 */     return prefix;
/*      */   }
/*      */ 
/*      */   public String getNamespaceURI(String qname, boolean isElement)
/*      */   {
/*  903 */     String uri = "";
/*  904 */     int col = qname.lastIndexOf(':');
/*  905 */     String prefix = col > 0 ? qname.substring(0, col) : "";
/*      */ 
/*  907 */     if ((!"".equals(prefix)) || (isElement))
/*      */     {
/*  909 */       if (this.m_prefixMap != null)
/*      */       {
/*  911 */         uri = this.m_prefixMap.lookupNamespace(prefix);
/*  912 */         if ((uri == null) && (!prefix.equals("xmlns")))
/*      */         {
/*  914 */           throw new RuntimeException(Utils.messages.createMessage("ER_NAMESPACE_PREFIX", new Object[] { qname.substring(0, col) }));
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  921 */     return uri;
/*      */   }
/*      */ 
/*      */   public String getNamespaceURIFromPrefix(String prefix)
/*      */   {
/*  933 */     String uri = null;
/*  934 */     if (this.m_prefixMap != null)
/*  935 */       uri = this.m_prefixMap.lookupNamespace(prefix);
/*  936 */     return uri;
/*      */   }
/*      */ 
/*      */   public void entityReference(String name)
/*      */     throws SAXException
/*      */   {
/*  949 */     flushPending();
/*      */ 
/*  951 */     startEntity(name);
/*  952 */     endEntity(name);
/*      */ 
/*  954 */     if (this.m_tracer != null)
/*  955 */       fireEntityReference(name);
/*      */   }
/*      */ 
/*      */   public void setTransformer(Transformer t)
/*      */   {
/*  965 */     this.m_transformer = t;
/*      */ 
/*  970 */     if (((this.m_transformer instanceof SerializerTrace)) && (((SerializerTrace)this.m_transformer).hasTraceListeners()))
/*      */     {
/*  972 */       this.m_tracer = ((SerializerTrace)this.m_transformer);
/*      */     }
/*  974 */     else this.m_tracer = null;
/*      */   }
/*      */ 
/*      */   public Transformer getTransformer()
/*      */   {
/*  984 */     return this.m_transformer;
/*      */   }
/*      */ 
/*      */   public void characters(Node node)
/*      */     throws SAXException
/*      */   {
/*  996 */     flushPending();
/*  997 */     String data = node.getNodeValue();
/*  998 */     if (data != null)
/*      */     {
/* 1000 */       int length = data.length();
/* 1001 */       if (length > this.m_charsBuff.length)
/*      */       {
/* 1003 */         this.m_charsBuff = new char[length * 2 + 1];
/*      */       }
/* 1005 */       data.getChars(0, length, this.m_charsBuff, 0);
/* 1006 */       characters(this.m_charsBuff, 0, length);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void error(SAXParseException exc)
/*      */     throws SAXException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void fatalError(SAXParseException exc)
/*      */     throws SAXException
/*      */   {
/* 1022 */     this.m_elemContext.m_startTagOpen = false;
/*      */   }
/*      */ 
/*      */   public void warning(SAXParseException exc)
/*      */     throws SAXException
/*      */   {
/*      */   }
/*      */ 
/*      */   protected void fireStartEntity(String name)
/*      */     throws SAXException
/*      */   {
/* 1040 */     if (this.m_tracer != null)
/*      */     {
/* 1042 */       flushMyWriter();
/* 1043 */       this.m_tracer.fireGenerateEvent(9, name);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void flushMyWriter()
/*      */   {
/* 1072 */     if (this.m_writer != null)
/*      */     {
/*      */       try
/*      */       {
/* 1076 */         this.m_writer.flush();
/*      */       }
/*      */       catch (IOException ioe)
/*      */       {
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void fireCDATAEvent(char[] chars, int start, int length)
/*      */     throws SAXException
/*      */   {
/* 1093 */     if (this.m_tracer != null)
/*      */     {
/* 1095 */       flushMyWriter();
/* 1096 */       this.m_tracer.fireGenerateEvent(10, chars, start, length);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void fireCommentEvent(char[] chars, int start, int length)
/*      */     throws SAXException
/*      */   {
/* 1109 */     if (this.m_tracer != null)
/*      */     {
/* 1111 */       flushMyWriter();
/* 1112 */       this.m_tracer.fireGenerateEvent(8, new String(chars, start, length));
/*      */     }
/*      */   }
/*      */ 
/*      */   public void fireEndEntity(String name)
/*      */     throws SAXException
/*      */   {
/* 1124 */     if (this.m_tracer != null)
/* 1125 */       flushMyWriter();
/*      */   }
/*      */ 
/*      */   protected void fireStartDoc()
/*      */     throws SAXException
/*      */   {
/* 1135 */     if (this.m_tracer != null)
/*      */     {
/* 1137 */       flushMyWriter();
/* 1138 */       this.m_tracer.fireGenerateEvent(1);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void fireEndDoc()
/*      */     throws SAXException
/*      */   {
/* 1149 */     if (this.m_tracer != null)
/*      */     {
/* 1151 */       flushMyWriter();
/* 1152 */       this.m_tracer.fireGenerateEvent(2);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void fireStartElem(String elemName)
/*      */     throws SAXException
/*      */   {
/* 1166 */     if (this.m_tracer != null)
/*      */     {
/* 1168 */       flushMyWriter();
/* 1169 */       this.m_tracer.fireGenerateEvent(3, elemName, this.m_attributes);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void fireEscapingEvent(String name, String data)
/*      */     throws SAXException
/*      */   {
/* 1195 */     if (this.m_tracer != null)
/*      */     {
/* 1197 */       flushMyWriter();
/* 1198 */       this.m_tracer.fireGenerateEvent(7, name, data);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void fireEntityReference(String name)
/*      */     throws SAXException
/*      */   {
/* 1210 */     if (this.m_tracer != null)
/*      */     {
/* 1212 */       flushMyWriter();
/* 1213 */       this.m_tracer.fireGenerateEvent(9, name, (Attributes)null);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void startDocument()
/*      */     throws SAXException
/*      */   {
/* 1235 */     startDocumentInternal();
/* 1236 */     this.m_needToCallStartDocument = false;
/*      */   }
/*      */ 
/*      */   protected void startDocumentInternal()
/*      */     throws SAXException
/*      */   {
/* 1258 */     if (this.m_tracer != null)
/* 1259 */       fireStartDoc();
/*      */   }
/*      */ 
/*      */   protected void setDocumentInfo()
/*      */   {
/* 1266 */     if (this.m_locator == null)
/* 1267 */       return;
/*      */     try {
/* 1269 */       String strVersion = ((Locator2)this.m_locator).getXMLVersion();
/* 1270 */       if (strVersion != null)
/* 1271 */         setVersion(strVersion);
/*      */     }
/*      */     catch (ClassCastException e)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setSourceLocator(SourceLocator locator)
/*      */   {
/* 1288 */     this.m_sourceLocator = locator;
/*      */   }
/*      */ 
/*      */   public void setNamespaceMappings(NamespaceMappings mappings)
/*      */   {
/* 1299 */     this.m_prefixMap = mappings;
/*      */   }
/*      */ 
/*      */   public boolean reset()
/*      */   {
/* 1304 */     resetSerializerBase();
/* 1305 */     return true;
/*      */   }
/*      */ 
/*      */   private void resetSerializerBase()
/*      */   {
/* 1314 */     this.m_attributes.clear();
/* 1315 */     this.m_cdataSectionElements = null;
/* 1316 */     this.m_elemContext = new ElemContext();
/* 1317 */     this.m_doctypePublic = null;
/* 1318 */     this.m_doctypeSystem = null;
/* 1319 */     this.m_doIndent = false;
/* 1320 */     this.m_encoding = null;
/* 1321 */     this.m_indentAmount = 0;
/* 1322 */     this.m_inEntityRef = false;
/* 1323 */     this.m_inExternalDTD = false;
/* 1324 */     this.m_mediatype = null;
/* 1325 */     this.m_needToCallStartDocument = true;
/* 1326 */     this.m_needToOutputDocTypeDecl = false;
/* 1327 */     if (this.m_prefixMap != null)
/* 1328 */       this.m_prefixMap.reset();
/* 1329 */     this.m_shouldNotWriteXMLHeader = false;
/* 1330 */     this.m_sourceLocator = null;
/* 1331 */     this.m_standalone = null;
/* 1332 */     this.m_standaloneWasSpecified = false;
/* 1333 */     this.m_tracer = null;
/* 1334 */     this.m_transformer = null;
/* 1335 */     this.m_version = null;
/*      */   }
/*      */ 
/*      */   final boolean inTemporaryOutputState()
/*      */   {
/* 1354 */     return getEncoding() == null;
/*      */   }
/*      */ 
/*      */   public void addAttribute(String uri, String localName, String rawName, String type, String value)
/*      */     throws SAXException
/*      */   {
/* 1365 */     if (this.m_elemContext.m_startTagOpen)
/*      */     {
/* 1367 */       addAttributeAlways(uri, localName, rawName, type, value, false);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void notationDecl(String arg0, String arg1, String arg2)
/*      */     throws SAXException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void unparsedEntityDecl(String arg0, String arg1, String arg2, String arg3)
/*      */     throws SAXException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void setDTDEntityExpansion(boolean expand)
/*      */   {
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.serializer.SerializerBase
 * JD-Core Version:    0.6.2
 */