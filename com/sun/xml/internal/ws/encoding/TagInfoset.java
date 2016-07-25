/*     */ package com.sun.xml.internal.ws.encoding;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.helpers.AttributesImpl;
/*     */ 
/*     */ public final class TagInfoset
/*     */ {
/*     */ 
/*     */   @NotNull
/*     */   public final String[] ns;
/*     */ 
/*     */   @NotNull
/*     */   public final AttributesImpl atts;
/*     */ 
/*     */   @Nullable
/*     */   public final String prefix;
/*     */ 
/*     */   @Nullable
/*     */   public final String nsUri;
/*     */ 
/*     */   @NotNull
/*     */   public final String localName;
/*     */ 
/*     */   @Nullable
/*     */   private String qname;
/* 222 */   private static final String[] EMPTY_ARRAY = new String[0];
/* 223 */   private static final AttributesImpl EMPTY_ATTRIBUTES = new AttributesImpl();
/*     */ 
/*     */   public TagInfoset(String nsUri, String localName, String prefix, AttributesImpl atts, String[] ns)
/*     */   {
/*  93 */     this.nsUri = nsUri;
/*  94 */     this.prefix = prefix;
/*  95 */     this.localName = localName;
/*  96 */     this.atts = atts;
/*  97 */     this.ns = ns;
/*     */   }
/*     */ 
/*     */   public TagInfoset(XMLStreamReader reader)
/*     */   {
/* 105 */     this.prefix = reader.getPrefix();
/* 106 */     this.nsUri = reader.getNamespaceURI();
/* 107 */     this.localName = reader.getLocalName();
/*     */ 
/* 109 */     int nsc = reader.getNamespaceCount();
/* 110 */     if (nsc > 0) {
/* 111 */       this.ns = new String[nsc * 2];
/* 112 */       for (int i = 0; i < nsc; i++) {
/* 113 */         this.ns[(i * 2)] = fixNull(reader.getNamespacePrefix(i));
/* 114 */         this.ns[(i * 2 + 1)] = fixNull(reader.getNamespaceURI(i));
/*     */       }
/*     */     } else {
/* 117 */       this.ns = EMPTY_ARRAY;
/*     */     }
/*     */ 
/* 120 */     int ac = reader.getAttributeCount();
/* 121 */     if (ac > 0) {
/* 122 */       this.atts = new AttributesImpl();
/* 123 */       StringBuilder sb = new StringBuilder();
/* 124 */       for (int i = 0; i < ac; i++) {
/* 125 */         sb.setLength(0);
/* 126 */         String prefix = reader.getAttributePrefix(i);
/* 127 */         String localName = reader.getAttributeLocalName(i);
/*     */         String qname;
/*     */         String qname;
/* 130 */         if ((prefix != null) && (prefix.length() != 0)) {
/* 131 */           sb.append(prefix);
/* 132 */           sb.append(":");
/* 133 */           sb.append(localName);
/* 134 */           qname = sb.toString();
/*     */         } else {
/* 136 */           qname = localName;
/*     */         }
/*     */ 
/* 139 */         this.atts.addAttribute(fixNull(reader.getAttributeNamespace(i)), localName, qname, reader.getAttributeType(i), reader.getAttributeValue(i));
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 147 */       this.atts = EMPTY_ATTRIBUTES;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writeStart(ContentHandler contentHandler)
/*     */     throws SAXException
/*     */   {
/* 155 */     for (int i = 0; i < this.ns.length; i += 2)
/* 156 */       contentHandler.startPrefixMapping(fixNull(this.ns[i]), fixNull(this.ns[(i + 1)]));
/* 157 */     contentHandler.startElement(fixNull(this.nsUri), this.localName, getQName(), this.atts);
/*     */   }
/*     */ 
/*     */   public void writeEnd(ContentHandler contentHandler)
/*     */     throws SAXException
/*     */   {
/* 164 */     contentHandler.endElement(fixNull(this.nsUri), this.localName, getQName());
/* 165 */     for (int i = this.ns.length - 2; i >= 0; i -= 2)
/* 166 */       contentHandler.endPrefixMapping(fixNull(this.ns[i]));
/*     */   }
/*     */ 
/*     */   public void writeStart(XMLStreamWriter w)
/*     */     throws XMLStreamException
/*     */   {
/* 175 */     if (this.prefix == null) {
/* 176 */       if (this.nsUri == null) {
/* 177 */         w.writeStartElement(this.localName);
/*     */       }
/*     */       else
/*     */       {
/* 181 */         w.writeStartElement("", this.localName, this.nsUri);
/*     */       }
/*     */     }
/* 184 */     else w.writeStartElement(this.prefix, this.localName, this.nsUri);
/*     */ 
/* 187 */     for (int i = 0; i < this.ns.length; i += 2) {
/* 188 */       w.writeNamespace(this.ns[i], this.ns[(i + 1)]);
/*     */     }
/*     */ 
/* 191 */     for (int i = 0; i < this.atts.getLength(); i++) {
/* 192 */       String nsUri = this.atts.getURI(i);
/* 193 */       if ((nsUri == null) || (nsUri.length() == 0)) {
/* 194 */         w.writeAttribute(this.atts.getLocalName(i), this.atts.getValue(i));
/*     */       } else {
/* 196 */         String rawName = this.atts.getQName(i);
/* 197 */         String prefix = rawName.substring(0, rawName.indexOf(':'));
/* 198 */         w.writeAttribute(prefix, nsUri, this.atts.getLocalName(i), this.atts.getValue(i));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private String getQName() {
/* 204 */     if (this.qname != null) return this.qname;
/*     */ 
/* 206 */     StringBuilder sb = new StringBuilder();
/* 207 */     if (this.prefix != null) {
/* 208 */       sb.append(this.prefix);
/* 209 */       sb.append(':');
/* 210 */       sb.append(this.localName);
/* 211 */       this.qname = sb.toString();
/*     */     } else {
/* 213 */       this.qname = this.localName;
/*     */     }
/* 215 */     return this.qname;
/*     */   }
/*     */   private static String fixNull(String s) {
/* 218 */     if (s == null) return "";
/* 219 */     return s;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.encoding.TagInfoset
 * JD-Core Version:    0.6.2
 */