/*     */ package com.sun.org.apache.xalan.internal.xsltc.trax;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.xsltc.dom.SAXImpl;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary;
/*     */ import java.io.IOException;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Stack;
/*     */ import java.util.Vector;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.DTDHandler;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXNotRecognizedException;
/*     */ import org.xml.sax.SAXNotSupportedException;
/*     */ import org.xml.sax.XMLReader;
/*     */ import org.xml.sax.ext.LexicalHandler;
/*     */ import org.xml.sax.helpers.AttributesImpl;
/*     */ 
/*     */ public class DOM2SAX
/*     */   implements XMLReader, Locator
/*     */ {
/*     */   private static final String EMPTYSTRING = "";
/*     */   private static final String XMLNS_PREFIX = "xmlns";
/*  58 */   private Node _dom = null;
/*  59 */   private ContentHandler _sax = null;
/*  60 */   private LexicalHandler _lex = null;
/*  61 */   private SAXImpl _saxImpl = null;
/*  62 */   private Hashtable _nsPrefixes = new Hashtable();
/*     */ 
/*     */   public DOM2SAX(Node root) {
/*  65 */     this._dom = root;
/*     */   }
/*     */ 
/*     */   public ContentHandler getContentHandler() {
/*  69 */     return this._sax;
/*     */   }
/*     */ 
/*     */   public void setContentHandler(ContentHandler handler)
/*     */     throws NullPointerException
/*     */   {
/*  75 */     this._sax = handler;
/*  76 */     if ((handler instanceof LexicalHandler)) {
/*  77 */       this._lex = ((LexicalHandler)handler);
/*     */     }
/*     */ 
/*  80 */     if ((handler instanceof SAXImpl))
/*  81 */       this._saxImpl = ((SAXImpl)handler);
/*     */   }
/*     */ 
/*     */   private boolean startPrefixMapping(String prefix, String uri)
/*     */     throws SAXException
/*     */   {
/*  93 */     boolean pushed = true;
/*  94 */     Stack uriStack = (Stack)this._nsPrefixes.get(prefix);
/*     */ 
/*  96 */     if (uriStack != null) {
/*  97 */       if (uriStack.isEmpty()) {
/*  98 */         this._sax.startPrefixMapping(prefix, uri);
/*  99 */         uriStack.push(uri);
/*     */       }
/*     */       else {
/* 102 */         String lastUri = (String)uriStack.peek();
/* 103 */         if (!lastUri.equals(uri)) {
/* 104 */           this._sax.startPrefixMapping(prefix, uri);
/* 105 */           uriStack.push(uri);
/*     */         }
/*     */         else {
/* 108 */           pushed = false;
/*     */         }
/*     */       }
/*     */     }
/*     */     else {
/* 113 */       this._sax.startPrefixMapping(prefix, uri);
/* 114 */       this._nsPrefixes.put(prefix, uriStack = new Stack());
/* 115 */       uriStack.push(uri);
/*     */     }
/* 117 */     return pushed;
/*     */   }
/*     */ 
/*     */   private void endPrefixMapping(String prefix)
/*     */     throws SAXException
/*     */   {
/* 127 */     Stack uriStack = (Stack)this._nsPrefixes.get(prefix);
/*     */ 
/* 129 */     if (uriStack != null) {
/* 130 */       this._sax.endPrefixMapping(prefix);
/* 131 */       uriStack.pop();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static String getLocalName(Node node)
/*     */   {
/* 141 */     String localName = node.getLocalName();
/*     */ 
/* 143 */     if (localName == null) {
/* 144 */       String qname = node.getNodeName();
/* 145 */       int col = qname.lastIndexOf(':');
/* 146 */       return col > 0 ? qname.substring(col + 1) : qname;
/*     */     }
/* 148 */     return localName;
/*     */   }
/*     */ 
/*     */   public void parse(InputSource unused) throws IOException, SAXException {
/* 152 */     parse(this._dom);
/*     */   }
/*     */ 
/*     */   public void parse() throws IOException, SAXException {
/* 156 */     if (this._dom != null) {
/* 157 */       boolean isIncomplete = this._dom.getNodeType() != 9;
/*     */ 
/* 160 */       if (isIncomplete) {
/* 161 */         this._sax.startDocument();
/* 162 */         parse(this._dom);
/* 163 */         this._sax.endDocument();
/*     */       }
/*     */       else {
/* 166 */         parse(this._dom);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void parse(Node node)
/*     */     throws IOException, SAXException
/*     */   {
/* 177 */     Node first = null;
/* 178 */     if (node == null)
/*     */       return;
/*     */     Node next;
/* 180 */     switch (node.getNodeType())
/*     */     {
/*     */     case 2:
/*     */     case 5:
/*     */     case 6:
/*     */     case 10:
/*     */     case 11:
/*     */     case 12:
/* 188 */       break;
/*     */     case 4:
/* 190 */       String cdata = node.getNodeValue();
/* 191 */       if (this._lex != null) {
/* 192 */         this._lex.startCDATA();
/* 193 */         this._sax.characters(cdata.toCharArray(), 0, cdata.length());
/* 194 */         this._lex.endCDATA();
/*     */       }
/*     */       else
/*     */       {
/* 199 */         this._sax.characters(cdata.toCharArray(), 0, cdata.length());
/*     */       }
/* 201 */       break;
/*     */     case 8:
/* 204 */       if (this._lex != null) {
/* 205 */         String value = node.getNodeValue();
/* 206 */         this._lex.comment(value.toCharArray(), 0, value.length());
/* 207 */       }break;
/*     */     case 9:
/* 210 */       this._sax.setDocumentLocator(this);
/*     */ 
/* 212 */       this._sax.startDocument();
/* 213 */       next = node.getFirstChild();
/* 214 */       while (next != null) {
/* 215 */         parse(next);
/* 216 */         next = next.getNextSibling();
/*     */       }
/* 218 */       this._sax.endDocument();
/* 219 */       break;
/*     */     case 1:
/* 223 */       Vector pushedPrefixes = new Vector();
/* 224 */       AttributesImpl attrs = new AttributesImpl();
/* 225 */       NamedNodeMap map = node.getAttributes();
/* 226 */       int length = map.getLength();
/*     */ 
/* 229 */       for (int i = 0; i < length; i++) {
/* 230 */         Node attr = map.item(i);
/* 231 */         String qnameAttr = attr.getNodeName();
/*     */ 
/* 234 */         if (qnameAttr.startsWith("xmlns")) {
/* 235 */           String uriAttr = attr.getNodeValue();
/* 236 */           int colon = qnameAttr.lastIndexOf(':');
/* 237 */           String prefix = colon > 0 ? qnameAttr.substring(colon + 1) : "";
/* 238 */           if (startPrefixMapping(prefix, uriAttr)) {
/* 239 */             pushedPrefixes.addElement(prefix);
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 245 */       for (int i = 0; i < length; i++) {
/* 246 */         Node attr = map.item(i);
/* 247 */         String qnameAttr = attr.getNodeName();
/*     */ 
/* 250 */         if (!qnameAttr.startsWith("xmlns")) {
/* 251 */           String uriAttr = attr.getNamespaceURI();
/* 252 */           String localNameAttr = getLocalName(attr);
/*     */ 
/* 255 */           if (uriAttr != null) {
/* 256 */             int colon = qnameAttr.lastIndexOf(':');
/*     */             String prefix;
/*     */             String prefix;
/* 257 */             if (colon > 0) {
/* 258 */               prefix = qnameAttr.substring(0, colon);
/*     */             }
/*     */             else
/*     */             {
/* 263 */               prefix = BasisLibrary.generatePrefix();
/* 264 */               qnameAttr = prefix + ':' + qnameAttr;
/*     */             }
/* 266 */             if (startPrefixMapping(prefix, uriAttr)) {
/* 267 */               pushedPrefixes.addElement(prefix);
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/* 272 */           attrs.addAttribute(attr.getNamespaceURI(), getLocalName(attr), qnameAttr, "CDATA", attr.getNodeValue());
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 278 */       String qname = node.getNodeName();
/* 279 */       String uri = node.getNamespaceURI();
/* 280 */       String localName = getLocalName(node);
/*     */ 
/* 283 */       if (uri != null) {
/* 284 */         int colon = qname.lastIndexOf(':');
/* 285 */         String prefix = colon > 0 ? qname.substring(0, colon) : "";
/* 286 */         if (startPrefixMapping(prefix, uri)) {
/* 287 */           pushedPrefixes.addElement(prefix);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 292 */       if (this._saxImpl != null) {
/* 293 */         this._saxImpl.startElement(uri, localName, qname, attrs, node);
/*     */       }
/*     */       else {
/* 296 */         this._sax.startElement(uri, localName, qname, attrs);
/*     */       }
/*     */ 
/* 300 */       next = node.getFirstChild();
/* 301 */       while (next != null) {
/* 302 */         parse(next);
/* 303 */         next = next.getNextSibling();
/*     */       }
/*     */ 
/* 307 */       this._sax.endElement(uri, localName, qname);
/*     */ 
/* 310 */       int nPushedPrefixes = pushedPrefixes.size();
/* 311 */       for (int i = 0; i < nPushedPrefixes; i++) {
/* 312 */         endPrefixMapping((String)pushedPrefixes.elementAt(i));
/*     */       }
/* 314 */       break;
/*     */     case 7:
/* 317 */       this._sax.processingInstruction(node.getNodeName(), node.getNodeValue());
/*     */ 
/* 319 */       break;
/*     */     case 3:
/* 322 */       String data = node.getNodeValue();
/* 323 */       this._sax.characters(data.toCharArray(), 0, data.length());
/*     */     }
/*     */   }
/*     */ 
/*     */   public DTDHandler getDTDHandler()
/*     */   {
/* 333 */     return null;
/*     */   }
/*     */ 
/*     */   public ErrorHandler getErrorHandler()
/*     */   {
/* 341 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean getFeature(String name)
/*     */     throws SAXNotRecognizedException, SAXNotSupportedException
/*     */   {
/* 351 */     return false;
/*     */   }
/*     */ 
/*     */   public void setFeature(String name, boolean value)
/*     */     throws SAXNotRecognizedException, SAXNotSupportedException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void parse(String sysId)
/*     */     throws IOException, SAXException
/*     */   {
/* 368 */     throw new IOException("This method is not yet implemented.");
/*     */   }
/*     */ 
/*     */   public void setDTDHandler(DTDHandler handler)
/*     */     throws NullPointerException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setEntityResolver(EntityResolver resolver)
/*     */     throws NullPointerException
/*     */   {
/*     */   }
/*     */ 
/*     */   public EntityResolver getEntityResolver()
/*     */   {
/* 392 */     return null;
/*     */   }
/*     */ 
/*     */   public void setErrorHandler(ErrorHandler handler)
/*     */     throws NullPointerException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setProperty(String name, Object value)
/*     */     throws SAXNotRecognizedException, SAXNotSupportedException
/*     */   {
/*     */   }
/*     */ 
/*     */   public Object getProperty(String name)
/*     */     throws SAXNotRecognizedException, SAXNotSupportedException
/*     */   {
/* 419 */     return null;
/*     */   }
/*     */ 
/*     */   public int getColumnNumber()
/*     */   {
/* 427 */     return 0;
/*     */   }
/*     */ 
/*     */   public int getLineNumber()
/*     */   {
/* 435 */     return 0;
/*     */   }
/*     */ 
/*     */   public String getPublicId()
/*     */   {
/* 443 */     return null;
/*     */   }
/*     */ 
/*     */   public String getSystemId()
/*     */   {
/* 451 */     return null;
/*     */   }
/*     */ 
/*     */   private String getNodeTypeFromCode(short code)
/*     */   {
/* 456 */     String retval = null;
/* 457 */     switch (code) {
/*     */     case 2:
/* 459 */       retval = "ATTRIBUTE_NODE"; break;
/*     */     case 4:
/* 461 */       retval = "CDATA_SECTION_NODE"; break;
/*     */     case 8:
/* 463 */       retval = "COMMENT_NODE"; break;
/*     */     case 11:
/* 465 */       retval = "DOCUMENT_FRAGMENT_NODE"; break;
/*     */     case 9:
/* 467 */       retval = "DOCUMENT_NODE"; break;
/*     */     case 10:
/* 469 */       retval = "DOCUMENT_TYPE_NODE"; break;
/*     */     case 1:
/* 471 */       retval = "ELEMENT_NODE"; break;
/*     */     case 6:
/* 473 */       retval = "ENTITY_NODE"; break;
/*     */     case 5:
/* 475 */       retval = "ENTITY_REFERENCE_NODE"; break;
/*     */     case 12:
/* 477 */       retval = "NOTATION_NODE"; break;
/*     */     case 7:
/* 479 */       retval = "PROCESSING_INSTRUCTION_NODE"; break;
/*     */     case 3:
/* 481 */       retval = "TEXT_NODE";
/*     */     }
/* 483 */     return retval;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.trax.DOM2SAX
 * JD-Core Version:    0.6.2
 */