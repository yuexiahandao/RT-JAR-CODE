/*     */ package com.sun.xml.internal.bind.v2.runtime.output;
/*     */ 
/*     */ import com.sun.xml.internal.bind.util.AttributesImpl;
/*     */ import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;
/*     */ import java.io.IOException;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.helpers.LocatorImpl;
/*     */ 
/*     */ public class SAXOutput extends XmlOutputAbstractImpl
/*     */ {
/*     */   protected final ContentHandler out;
/*     */   private String elementNsUri;
/*     */   private String elementLocalName;
/*     */   private String elementQName;
/*  54 */   private char[] buf = new char[256];
/*     */ 
/*  56 */   private final AttributesImpl atts = new AttributesImpl();
/*     */ 
/*     */   public SAXOutput(ContentHandler out)
/*     */   {
/*  48 */     this.out = out;
/*  49 */     out.setDocumentLocator(new LocatorImpl());
/*     */   }
/*     */ 
/*     */   public void startDocument(XMLSerializer serializer, boolean fragment, int[] nsUriIndex2prefixIndex, NamespaceContextImpl nsContext)
/*     */     throws SAXException, IOException, XMLStreamException
/*     */   {
/*  62 */     super.startDocument(serializer, fragment, nsUriIndex2prefixIndex, nsContext);
/*  63 */     if (!fragment)
/*  64 */       this.out.startDocument();
/*     */   }
/*     */ 
/*     */   public void endDocument(boolean fragment) throws SAXException, IOException, XMLStreamException {
/*  68 */     if (!fragment)
/*  69 */       this.out.endDocument();
/*  70 */     super.endDocument(fragment);
/*     */   }
/*     */ 
/*     */   public void beginStartTag(int prefix, String localName) {
/*  74 */     this.elementNsUri = this.nsContext.getNamespaceURI(prefix);
/*  75 */     this.elementLocalName = localName;
/*  76 */     this.elementQName = getQName(prefix, localName);
/*  77 */     this.atts.clear();
/*     */   }
/*     */ 
/*     */   public void attribute(int prefix, String localName, String value)
/*     */   {
/*     */     String qname;
/*     */     String nsUri;
/*     */     String qname;
/*  83 */     if (prefix == -1) {
/*  84 */       String nsUri = "";
/*  85 */       qname = localName;
/*     */     } else {
/*  87 */       nsUri = this.nsContext.getNamespaceURI(prefix);
/*  88 */       String p = this.nsContext.getPrefix(prefix);
/*     */       String qname;
/*  89 */       if (p.length() == 0)
/*     */       {
/*  94 */         qname = localName;
/*     */       }
/*  96 */       else qname = p + ':' + localName;
/*     */     }
/*  98 */     this.atts.addAttribute(nsUri, localName, qname, "CDATA", value);
/*     */   }
/*     */ 
/*     */   public void endStartTag() throws SAXException {
/* 102 */     NamespaceContextImpl.Element ns = this.nsContext.getCurrent();
/* 103 */     if (ns != null) {
/* 104 */       int sz = ns.count();
/* 105 */       for (int i = 0; i < sz; i++) {
/* 106 */         String p = ns.getPrefix(i);
/* 107 */         String uri = ns.getNsUri(i);
/* 108 */         if ((uri.length() != 0) || (ns.getBase() != 1))
/*     */         {
/* 110 */           this.out.startPrefixMapping(p, uri);
/*     */         }
/*     */       }
/*     */     }
/* 113 */     this.out.startElement(this.elementNsUri, this.elementLocalName, this.elementQName, this.atts);
/*     */   }
/*     */ 
/*     */   public void endTag(int prefix, String localName) throws SAXException {
/* 117 */     this.out.endElement(this.nsContext.getNamespaceURI(prefix), localName, getQName(prefix, localName));
/*     */ 
/* 123 */     NamespaceContextImpl.Element ns = this.nsContext.getCurrent();
/* 124 */     if (ns != null) {
/* 125 */       int sz = ns.count();
/* 126 */       for (int i = sz - 1; i >= 0; i--) {
/* 127 */         String p = ns.getPrefix(i);
/* 128 */         String uri = ns.getNsUri(i);
/* 129 */         if ((uri.length() != 0) || (ns.getBase() != 1))
/*     */         {
/* 131 */           this.out.endPrefixMapping(p);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private String getQName(int prefix, String localName) {
/* 138 */     String p = this.nsContext.getPrefix(prefix);
/*     */     String qname;
/*     */     String qname;
/* 139 */     if (p.length() == 0)
/* 140 */       qname = localName;
/*     */     else
/* 142 */       qname = p + ':' + localName;
/* 143 */     return qname;
/*     */   }
/*     */ 
/*     */   public void text(String value, boolean needsSP) throws IOException, SAXException, XMLStreamException {
/* 147 */     int vlen = value.length();
/* 148 */     if (this.buf.length <= vlen) {
/* 149 */       this.buf = new char[Math.max(this.buf.length * 2, vlen + 1)];
/*     */     }
/* 151 */     if (needsSP) {
/* 152 */       value.getChars(0, vlen, this.buf, 1);
/* 153 */       this.buf[0] = ' ';
/*     */     } else {
/* 155 */       value.getChars(0, vlen, this.buf, 0);
/*     */     }
/* 157 */     this.out.characters(this.buf, 0, vlen + (needsSP ? 1 : 0));
/*     */   }
/*     */ 
/*     */   public void text(Pcdata value, boolean needsSP) throws IOException, SAXException, XMLStreamException {
/* 161 */     int vlen = value.length();
/* 162 */     if (this.buf.length <= vlen) {
/* 163 */       this.buf = new char[Math.max(this.buf.length * 2, vlen + 1)];
/*     */     }
/* 165 */     if (needsSP) {
/* 166 */       value.writeTo(this.buf, 1);
/* 167 */       this.buf[0] = ' ';
/*     */     } else {
/* 169 */       value.writeTo(this.buf, 0);
/*     */     }
/* 171 */     this.out.characters(this.buf, 0, vlen + (needsSP ? 1 : 0));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.output.SAXOutput
 * JD-Core Version:    0.6.2
 */