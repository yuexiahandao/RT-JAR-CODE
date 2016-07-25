/*      */ package org.xml.sax.helpers;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Vector;
/*      */ import org.xml.sax.AttributeList;
/*      */ import org.xml.sax.Attributes;
/*      */ import org.xml.sax.ContentHandler;
/*      */ import org.xml.sax.DTDHandler;
/*      */ import org.xml.sax.DocumentHandler;
/*      */ import org.xml.sax.EntityResolver;
/*      */ import org.xml.sax.ErrorHandler;
/*      */ import org.xml.sax.InputSource;
/*      */ import org.xml.sax.Locator;
/*      */ import org.xml.sax.Parser;
/*      */ import org.xml.sax.SAXException;
/*      */ import org.xml.sax.SAXNotRecognizedException;
/*      */ import org.xml.sax.SAXNotSupportedException;
/*      */ import org.xml.sax.SAXParseException;
/*      */ import org.xml.sax.XMLReader;
/*      */ 
/*      */ public class ParserAdapter
/*      */   implements XMLReader, DocumentHandler
/*      */ {
/*   84 */   private static SecuritySupport ss = new SecuritySupport();
/*      */   private static final String FEATURES = "http://xml.org/sax/features/";
/*      */   private static final String NAMESPACES = "http://xml.org/sax/features/namespaces";
/*      */   private static final String NAMESPACE_PREFIXES = "http://xml.org/sax/features/namespace-prefixes";
/*      */   private static final String XMLNS_URIs = "http://xml.org/sax/features/xmlns-uris";
/*      */   private NamespaceSupport nsSupport;
/*      */   private AttributeListAdapter attAdapter;
/*  846 */   private boolean parsing = false;
/*  847 */   private String[] nameParts = new String[3];
/*      */ 
/*  849 */   private Parser parser = null;
/*      */ 
/*  851 */   private AttributesImpl atts = null;
/*      */ 
/*  854 */   private boolean namespaces = true;
/*  855 */   private boolean prefixes = false;
/*  856 */   private boolean uris = false;
/*      */   Locator locator;
/*  863 */   EntityResolver entityResolver = null;
/*  864 */   DTDHandler dtdHandler = null;
/*  865 */   ContentHandler contentHandler = null;
/*  866 */   ErrorHandler errorHandler = null;
/*      */ 
/*      */   public ParserAdapter()
/*      */     throws SAXException
/*      */   {
/*  106 */     String driver = ss.getSystemProperty("org.xml.sax.parser");
/*      */     try
/*      */     {
/*  109 */       setup(ParserFactory.makeParser());
/*      */     } catch (ClassNotFoundException e1) {
/*  111 */       throw new SAXException("Cannot find SAX1 driver class " + driver, e1);
/*      */     }
/*      */     catch (IllegalAccessException e2)
/*      */     {
/*  115 */       throw new SAXException("SAX1 driver class " + driver + " found but cannot be loaded", e2);
/*      */     }
/*      */     catch (InstantiationException e3)
/*      */     {
/*  120 */       throw new SAXException("SAX1 driver class " + driver + " loaded but cannot be instantiated", e3);
/*      */     }
/*      */     catch (ClassCastException e4)
/*      */     {
/*  125 */       throw new SAXException("SAX1 driver class " + driver + " does not implement org.xml.sax.Parser");
/*      */     }
/*      */     catch (NullPointerException e5)
/*      */     {
/*  130 */       throw new SAXException("System property org.xml.sax.parser not specified");
/*      */     }
/*      */   }
/*      */ 
/*      */   public ParserAdapter(Parser parser)
/*      */   {
/*  150 */     setup(parser);
/*      */   }
/*      */ 
/*      */   private void setup(Parser parser)
/*      */   {
/*  163 */     if (parser == null) {
/*  164 */       throw new NullPointerException("Parser argument must not be null");
/*      */     }
/*      */ 
/*  167 */     this.parser = parser;
/*  168 */     this.atts = new AttributesImpl();
/*  169 */     this.nsSupport = new NamespaceSupport();
/*  170 */     this.attAdapter = new AttributeListAdapter();
/*      */   }
/*      */ 
/*      */   public void setFeature(String name, boolean value)
/*      */     throws SAXNotRecognizedException, SAXNotSupportedException
/*      */   {
/*  206 */     if (name.equals("http://xml.org/sax/features/namespaces")) {
/*  207 */       checkNotParsing("feature", name);
/*  208 */       this.namespaces = value;
/*  209 */       if ((!this.namespaces) && (!this.prefixes))
/*  210 */         this.prefixes = true;
/*      */     }
/*  212 */     else if (name.equals("http://xml.org/sax/features/namespace-prefixes")) {
/*  213 */       checkNotParsing("feature", name);
/*  214 */       this.prefixes = value;
/*  215 */       if ((!this.prefixes) && (!this.namespaces))
/*  216 */         this.namespaces = true;
/*      */     }
/*  218 */     else if (name.equals("http://xml.org/sax/features/xmlns-uris")) {
/*  219 */       checkNotParsing("feature", name);
/*  220 */       this.uris = value;
/*      */     } else {
/*  222 */       throw new SAXNotRecognizedException("Feature: " + name);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean getFeature(String name)
/*      */     throws SAXNotRecognizedException, SAXNotSupportedException
/*      */   {
/*  244 */     if (name.equals("http://xml.org/sax/features/namespaces"))
/*  245 */       return this.namespaces;
/*  246 */     if (name.equals("http://xml.org/sax/features/namespace-prefixes"))
/*  247 */       return this.prefixes;
/*  248 */     if (name.equals("http://xml.org/sax/features/xmlns-uris")) {
/*  249 */       return this.uris;
/*      */     }
/*  251 */     throw new SAXNotRecognizedException("Feature: " + name);
/*      */   }
/*      */ 
/*      */   public void setProperty(String name, Object value)
/*      */     throws SAXNotRecognizedException, SAXNotSupportedException
/*      */   {
/*  272 */     throw new SAXNotRecognizedException("Property: " + name);
/*      */   }
/*      */ 
/*      */   public Object getProperty(String name)
/*      */     throws SAXNotRecognizedException, SAXNotSupportedException
/*      */   {
/*  292 */     throw new SAXNotRecognizedException("Property: " + name);
/*      */   }
/*      */ 
/*      */   public void setEntityResolver(EntityResolver resolver)
/*      */   {
/*  304 */     this.entityResolver = resolver;
/*      */   }
/*      */ 
/*      */   public EntityResolver getEntityResolver()
/*      */   {
/*  316 */     return this.entityResolver;
/*      */   }
/*      */ 
/*      */   public void setDTDHandler(DTDHandler handler)
/*      */   {
/*  328 */     this.dtdHandler = handler;
/*      */   }
/*      */ 
/*      */   public DTDHandler getDTDHandler()
/*      */   {
/*  340 */     return this.dtdHandler;
/*      */   }
/*      */ 
/*      */   public void setContentHandler(ContentHandler handler)
/*      */   {
/*  352 */     this.contentHandler = handler;
/*      */   }
/*      */ 
/*      */   public ContentHandler getContentHandler()
/*      */   {
/*  364 */     return this.contentHandler;
/*      */   }
/*      */ 
/*      */   public void setErrorHandler(ErrorHandler handler)
/*      */   {
/*  376 */     this.errorHandler = handler;
/*      */   }
/*      */ 
/*      */   public ErrorHandler getErrorHandler()
/*      */   {
/*  388 */     return this.errorHandler;
/*      */   }
/*      */ 
/*      */   public void parse(String systemId)
/*      */     throws IOException, SAXException
/*      */   {
/*  406 */     parse(new InputSource(systemId));
/*      */   }
/*      */ 
/*      */   public void parse(InputSource input)
/*      */     throws IOException, SAXException
/*      */   {
/*  424 */     if (this.parsing) {
/*  425 */       throw new SAXException("Parser is already in use");
/*      */     }
/*  427 */     setupParser();
/*  428 */     this.parsing = true;
/*      */     try {
/*  430 */       this.parser.parse(input);
/*      */     } finally {
/*  432 */       this.parsing = false;
/*      */     }
/*  434 */     this.parsing = false;
/*      */   }
/*      */ 
/*      */   public void setDocumentLocator(Locator locator)
/*      */   {
/*  453 */     this.locator = locator;
/*  454 */     if (this.contentHandler != null)
/*  455 */       this.contentHandler.setDocumentLocator(locator);
/*      */   }
/*      */ 
/*      */   public void startDocument()
/*      */     throws SAXException
/*      */   {
/*  471 */     if (this.contentHandler != null)
/*  472 */       this.contentHandler.startDocument();
/*      */   }
/*      */ 
/*      */   public void endDocument()
/*      */     throws SAXException
/*      */   {
/*  488 */     if (this.contentHandler != null)
/*  489 */       this.contentHandler.endDocument();
/*      */   }
/*      */ 
/*      */   public void startElement(String qName, AttributeList qAtts)
/*      */     throws SAXException
/*      */   {
/*  512 */     Vector exceptions = null;
/*      */ 
/*  516 */     if (!this.namespaces) {
/*  517 */       if (this.contentHandler != null) {
/*  518 */         this.attAdapter.setAttributeList(qAtts);
/*  519 */         this.contentHandler.startElement("", "", qName.intern(), this.attAdapter);
/*      */       }
/*      */ 
/*  522 */       return;
/*      */     }
/*      */ 
/*  527 */     this.nsSupport.pushContext();
/*  528 */     int length = qAtts.getLength();
/*      */ 
/*  531 */     for (int i = 0; i < length; i++) {
/*  532 */       String attQName = qAtts.getName(i);
/*      */ 
/*  534 */       if (attQName.startsWith("xmlns"))
/*      */       {
/*  538 */         int n = attQName.indexOf(':');
/*      */         String prefix;
/*      */         String prefix;
/*  541 */         if ((n == -1) && (attQName.length() == 5)) {
/*  542 */           prefix = ""; } else {
/*  543 */           if (n != 5)
/*      */           {
/*      */             continue;
/*      */           }
/*      */ 
/*  548 */           prefix = attQName.substring(n + 1);
/*      */         }
/*  550 */         String value = qAtts.getValue(i);
/*  551 */         if (!this.nsSupport.declarePrefix(prefix, value)) {
/*  552 */           reportError("Illegal Namespace prefix: " + prefix);
/*      */         }
/*  555 */         else if (this.contentHandler != null) {
/*  556 */           this.contentHandler.startPrefixMapping(prefix, value);
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  562 */     this.atts.clear();
/*  563 */     for (int i = 0; i < length; i++) {
/*  564 */       String attQName = qAtts.getName(i);
/*  565 */       String type = qAtts.getType(i);
/*  566 */       String value = qAtts.getValue(i);
/*      */ 
/*  569 */       if (attQName.startsWith("xmlns"))
/*      */       {
/*  571 */         int n = attQName.indexOf(':');
/*      */         String prefix;
/*      */         String prefix;
/*  573 */         if ((n == -1) && (attQName.length() == 5)) {
/*  574 */           prefix = "";
/*      */         }
/*      */         else
/*      */         {
/*      */           String prefix;
/*  575 */           if (n != 5)
/*      */           {
/*  578 */             prefix = null;
/*      */           }
/*  580 */           else prefix = attQName.substring(6);
/*      */         }
/*      */ 
/*  583 */         if (prefix != null) {
/*  584 */           if (!this.prefixes) continue;
/*  585 */           if (this.uris)
/*      */           {
/*  589 */             this.atts.addAttribute("http://www.w3.org/XML/1998/namespace", prefix, attQName.intern(), type, value); continue;
/*      */           }
/*      */ 
/*  592 */           this.atts.addAttribute("", "", attQName.intern(), type, value); continue;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */       try
/*      */       {
/*  601 */         String[] attName = processName(attQName, true, true);
/*  602 */         this.atts.addAttribute(attName[0], attName[1], attName[2], type, value);
/*      */       }
/*      */       catch (SAXException e) {
/*  605 */         if (exceptions == null)
/*  606 */           exceptions = new Vector();
/*  607 */         exceptions.addElement(e);
/*  608 */         this.atts.addAttribute("", attQName, attQName, type, value);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  613 */     if ((exceptions != null) && (this.errorHandler != null)) {
/*  614 */       for (int i = 0; i < exceptions.size(); i++) {
/*  615 */         this.errorHandler.error((SAXParseException)exceptions.elementAt(i));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  620 */     if (this.contentHandler != null) {
/*  621 */       String[] name = processName(qName, false, false);
/*  622 */       this.contentHandler.startElement(name[0], name[1], name[2], this.atts);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void endElement(String qName)
/*      */     throws SAXException
/*      */   {
/*  641 */     if (!this.namespaces) {
/*  642 */       if (this.contentHandler != null) {
/*  643 */         this.contentHandler.endElement("", "", qName.intern());
/*      */       }
/*  645 */       return;
/*      */     }
/*      */ 
/*  649 */     String[] names = processName(qName, false, false);
/*  650 */     if (this.contentHandler != null) {
/*  651 */       this.contentHandler.endElement(names[0], names[1], names[2]);
/*  652 */       Enumeration prefixes = this.nsSupport.getDeclaredPrefixes();
/*  653 */       while (prefixes.hasMoreElements()) {
/*  654 */         String prefix = (String)prefixes.nextElement();
/*  655 */         this.contentHandler.endPrefixMapping(prefix);
/*      */       }
/*      */     }
/*  658 */     this.nsSupport.popContext();
/*      */   }
/*      */ 
/*      */   public void characters(char[] ch, int start, int length)
/*      */     throws SAXException
/*      */   {
/*  676 */     if (this.contentHandler != null)
/*  677 */       this.contentHandler.characters(ch, start, length);
/*      */   }
/*      */ 
/*      */   public void ignorableWhitespace(char[] ch, int start, int length)
/*      */     throws SAXException
/*      */   {
/*  696 */     if (this.contentHandler != null)
/*  697 */       this.contentHandler.ignorableWhitespace(ch, start, length);
/*      */   }
/*      */ 
/*      */   public void processingInstruction(String target, String data)
/*      */     throws SAXException
/*      */   {
/*  715 */     if (this.contentHandler != null)
/*  716 */       this.contentHandler.processingInstruction(target, data);
/*      */   }
/*      */ 
/*      */   private void setupParser()
/*      */   {
/*  733 */     if ((!this.prefixes) && (!this.namespaces)) {
/*  734 */       throw new IllegalStateException();
/*      */     }
/*  736 */     this.nsSupport.reset();
/*  737 */     if (this.uris) {
/*  738 */       this.nsSupport.setNamespaceDeclUris(true);
/*      */     }
/*  740 */     if (this.entityResolver != null) {
/*  741 */       this.parser.setEntityResolver(this.entityResolver);
/*      */     }
/*  743 */     if (this.dtdHandler != null) {
/*  744 */       this.parser.setDTDHandler(this.dtdHandler);
/*      */     }
/*  746 */     if (this.errorHandler != null) {
/*  747 */       this.parser.setErrorHandler(this.errorHandler);
/*      */     }
/*  749 */     this.parser.setDocumentHandler(this);
/*  750 */     this.locator = null;
/*      */   }
/*      */ 
/*      */   private String[] processName(String qName, boolean isAttribute, boolean useException)
/*      */     throws SAXException
/*      */   {
/*  771 */     String[] parts = this.nsSupport.processName(qName, this.nameParts, isAttribute);
/*      */ 
/*  773 */     if (parts == null) {
/*  774 */       if (useException)
/*  775 */         throw makeException("Undeclared prefix: " + qName);
/*  776 */       reportError("Undeclared prefix: " + qName);
/*  777 */       parts = new String[3];
/*      */       String tmp85_83 = ""; parts[1] = tmp85_83; parts[0] = tmp85_83;
/*  779 */       parts[2] = qName.intern();
/*      */     }
/*  781 */     return parts;
/*      */   }
/*      */ 
/*      */   void reportError(String message)
/*      */     throws SAXException
/*      */   {
/*  795 */     if (this.errorHandler != null)
/*  796 */       this.errorHandler.error(makeException(message));
/*      */   }
/*      */ 
/*      */   private SAXParseException makeException(String message)
/*      */   {
/*  807 */     if (this.locator != null) {
/*  808 */       return new SAXParseException(message, this.locator);
/*      */     }
/*  810 */     return new SAXParseException(message, null, null, -1, -1);
/*      */   }
/*      */ 
/*      */   private void checkNotParsing(String type, String name)
/*      */     throws SAXNotSupportedException
/*      */   {
/*  829 */     if (this.parsing)
/*  830 */       throw new SAXNotSupportedException("Cannot change " + type + ' ' + name + " while parsing");
/*      */   }
/*      */ 
/*      */   final class AttributeListAdapter
/*      */     implements Attributes
/*      */   {
/*      */     private AttributeList qAtts;
/*      */ 
/*      */     AttributeListAdapter()
/*      */     {
/*      */     }
/*      */ 
/*      */     void setAttributeList(AttributeList qAtts)
/*      */     {
/*  907 */       this.qAtts = qAtts;
/*      */     }
/*      */ 
/*      */     public int getLength()
/*      */     {
/*  919 */       return this.qAtts.getLength();
/*      */     }
/*      */ 
/*      */     public String getURI(int i)
/*      */     {
/*  932 */       return "";
/*      */     }
/*      */ 
/*      */     public String getLocalName(int i)
/*      */     {
/*  945 */       return "";
/*      */     }
/*      */ 
/*      */     public String getQName(int i)
/*      */     {
/*  957 */       return this.qAtts.getName(i).intern();
/*      */     }
/*      */ 
/*      */     public String getType(int i)
/*      */     {
/*  969 */       return this.qAtts.getType(i).intern();
/*      */     }
/*      */ 
/*      */     public String getValue(int i)
/*      */     {
/*  981 */       return this.qAtts.getValue(i);
/*      */     }
/*      */ 
/*      */     public int getIndex(String uri, String localName)
/*      */     {
/*  995 */       return -1;
/*      */     }
/*      */ 
/*      */     public int getIndex(String qName)
/*      */     {
/* 1008 */       int max = ParserAdapter.this.atts.getLength();
/* 1009 */       for (int i = 0; i < max; i++) {
/* 1010 */         if (this.qAtts.getName(i).equals(qName)) {
/* 1011 */           return i;
/*      */         }
/*      */       }
/* 1014 */       return -1;
/*      */     }
/*      */ 
/*      */     public String getType(String uri, String localName)
/*      */     {
/* 1027 */       return null;
/*      */     }
/*      */ 
/*      */     public String getType(String qName)
/*      */     {
/* 1039 */       return this.qAtts.getType(qName).intern();
/*      */     }
/*      */ 
/*      */     public String getValue(String uri, String localName)
/*      */     {
/* 1052 */       return null;
/*      */     }
/*      */ 
/*      */     public String getValue(String qName)
/*      */     {
/* 1064 */       return this.qAtts.getValue(qName);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.xml.sax.helpers.ParserAdapter
 * JD-Core Version:    0.6.2
 */