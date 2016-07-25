/*     */ package com.sun.xml.internal.fastinfoset.dom;
/*     */ 
/*     */ import com.sun.xml.internal.fastinfoset.Encoder;
/*     */ import com.sun.xml.internal.fastinfoset.QualifiedName;
/*     */ import com.sun.xml.internal.fastinfoset.util.LocalNameQualifiedNamesMap;
/*     */ import com.sun.xml.internal.fastinfoset.util.LocalNameQualifiedNamesMap.Entry;
/*     */ import com.sun.xml.internal.fastinfoset.util.NamespaceContextImplementation;
/*     */ import com.sun.xml.internal.fastinfoset.vocab.SerializerVocabulary;
/*     */ import com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetException;
/*     */ import java.io.IOException;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ 
/*     */ public class DOMDocumentSerializer extends Encoder
/*     */ {
/* 115 */   protected NamespaceContextImplementation _namespaceScopeContext = new NamespaceContextImplementation();
/* 116 */   protected Node[] _attributes = new Node[32];
/*     */ 
/*     */   public final void serialize(Node n)
/*     */     throws IOException
/*     */   {
/*  57 */     switch (n.getNodeType()) {
/*     */     case 9:
/*  59 */       serialize((Document)n);
/*  60 */       break;
/*     */     case 1:
/*  62 */       serializeElementAsDocument(n);
/*  63 */       break;
/*     */     case 8:
/*  65 */       serializeComment(n);
/*  66 */       break;
/*     */     case 7:
/*  68 */       serializeProcessingInstruction(n);
/*     */     case 2:
/*     */     case 3:
/*     */     case 4:
/*     */     case 5:
/*     */     case 6:
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void serialize(Document d) throws IOException
/*     */   {
/*  79 */     reset();
/*  80 */     encodeHeader(false);
/*  81 */     encodeInitialVocabulary();
/*     */ 
/*  83 */     NodeList nl = d.getChildNodes();
/*  84 */     for (int i = 0; i < nl.getLength(); i++) {
/*  85 */       Node n = nl.item(i);
/*  86 */       switch (n.getNodeType()) {
/*     */       case 1:
/*  88 */         serializeElement(n);
/*  89 */         break;
/*     */       case 8:
/*  91 */         serializeComment(n);
/*  92 */         break;
/*     */       case 7:
/*  94 */         serializeProcessingInstruction(n);
/*     */       }
/*     */     }
/*     */ 
/*  98 */     encodeDocumentTermination();
/*     */   }
/*     */ 
/*     */   protected final void serializeElementAsDocument(Node e) throws IOException {
/* 102 */     reset();
/* 103 */     encodeHeader(false);
/* 104 */     encodeInitialVocabulary();
/*     */ 
/* 106 */     serializeElement(e);
/*     */ 
/* 108 */     encodeDocumentTermination();
/*     */   }
/*     */ 
/*     */   protected final void serializeElement(Node e)
/*     */     throws IOException
/*     */   {
/* 119 */     encodeTermination();
/*     */ 
/* 121 */     int attributesSize = 0;
/*     */ 
/* 123 */     this._namespaceScopeContext.pushContext();
/*     */ 
/* 125 */     if (e.hasAttributes())
/*     */     {
/* 130 */       NamedNodeMap nnm = e.getAttributes();
/* 131 */       for (int i = 0; i < nnm.getLength(); i++) {
/* 132 */         Node a = nnm.item(i);
/* 133 */         String namespaceURI = a.getNamespaceURI();
/* 134 */         if ((namespaceURI != null) && (namespaceURI.equals("http://www.w3.org/2000/xmlns/"))) {
/* 135 */           String attrPrefix = a.getLocalName();
/* 136 */           String attrNamespace = a.getNodeValue();
/* 137 */           if ((attrPrefix == "xmlns") || (attrPrefix.equals("xmlns"))) {
/* 138 */             attrPrefix = "";
/*     */           }
/* 140 */           this._namespaceScopeContext.declarePrefix(attrPrefix, attrNamespace);
/*     */         } else {
/* 142 */           if (attributesSize == this._attributes.length) {
/* 143 */             Node[] attributes = new Node[attributesSize * 3 / 2 + 1];
/* 144 */             System.arraycopy(this._attributes, 0, attributes, 0, attributesSize);
/* 145 */             this._attributes = attributes;
/*     */           }
/* 147 */           this._attributes[(attributesSize++)] = a;
/*     */ 
/* 149 */           String attrNamespaceURI = a.getNamespaceURI();
/* 150 */           String attrPrefix = a.getPrefix();
/* 151 */           if ((attrPrefix != null) && (!this._namespaceScopeContext.getNamespaceURI(attrPrefix).equals(attrNamespaceURI))) {
/* 152 */             this._namespaceScopeContext.declarePrefix(attrPrefix, attrNamespaceURI);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 158 */     String elementNamespaceURI = e.getNamespaceURI();
/* 159 */     String elementPrefix = e.getPrefix();
/* 160 */     if (elementPrefix == null) elementPrefix = "";
/* 161 */     if ((elementNamespaceURI != null) && (!this._namespaceScopeContext.getNamespaceURI(elementPrefix).equals(elementNamespaceURI)))
/*     */     {
/* 163 */       this._namespaceScopeContext.declarePrefix(elementPrefix, elementNamespaceURI);
/*     */     }
/*     */ 
/* 166 */     if (!this._namespaceScopeContext.isCurrentContextEmpty()) {
/* 167 */       if (attributesSize > 0) {
/* 168 */         write(120);
/*     */       }
/*     */       else {
/* 171 */         write(56);
/*     */       }
/*     */ 
/* 174 */       for (int i = this._namespaceScopeContext.getCurrentContextStartIndex(); 
/* 175 */         i < this._namespaceScopeContext.getCurrentContextEndIndex(); i++)
/*     */       {
/* 177 */         String prefix = this._namespaceScopeContext.getPrefix(i);
/* 178 */         String uri = this._namespaceScopeContext.getNamespaceURI(i);
/* 179 */         encodeNamespaceAttribute(prefix, uri);
/*     */       }
/* 181 */       write(240);
/* 182 */       this._b = 0;
/*     */     } else {
/* 184 */       this._b = (attributesSize > 0 ? 64 : 0);
/*     */     }
/*     */ 
/* 188 */     String namespaceURI = elementNamespaceURI;
/*     */ 
/* 190 */     namespaceURI = namespaceURI == null ? "" : namespaceURI;
/* 191 */     encodeElement(namespaceURI, e.getNodeName(), e.getLocalName());
/*     */ 
/* 193 */     if (attributesSize > 0)
/*     */     {
/* 195 */       for (int i = 0; i < attributesSize; i++) {
/* 196 */         Node a = this._attributes[i];
/* 197 */         this._attributes[i] = null;
/* 198 */         namespaceURI = a.getNamespaceURI();
/* 199 */         namespaceURI = namespaceURI == null ? "" : namespaceURI;
/* 200 */         encodeAttribute(namespaceURI, a.getNodeName(), a.getLocalName());
/*     */ 
/* 202 */         String value = a.getNodeValue();
/* 203 */         boolean addToTable = isAttributeValueLengthMatchesLimit(value.length());
/* 204 */         encodeNonIdentifyingStringOnFirstBit(value, this._v.attributeValue, addToTable, false);
/*     */       }
/*     */ 
/* 207 */       this._b = 240;
/* 208 */       this._terminate = true;
/*     */     }
/*     */ 
/* 211 */     if (e.hasChildNodes())
/*     */     {
/* 213 */       NodeList nl = e.getChildNodes();
/* 214 */       for (int i = 0; i < nl.getLength(); i++) {
/* 215 */         Node n = nl.item(i);
/* 216 */         switch (n.getNodeType()) {
/*     */         case 1:
/* 218 */           serializeElement(n);
/* 219 */           break;
/*     */         case 3:
/* 221 */           serializeText(n);
/* 222 */           break;
/*     */         case 4:
/* 224 */           serializeCDATA(n);
/* 225 */           break;
/*     */         case 8:
/* 227 */           serializeComment(n);
/* 228 */           break;
/*     */         case 7:
/* 230 */           serializeProcessingInstruction(n);
/*     */         case 2:
/*     */         case 5:
/*     */         case 6:
/*     */         }
/*     */       }
/*     */     }
/* 235 */     encodeElementTermination();
/* 236 */     this._namespaceScopeContext.popContext();
/*     */   }
/*     */ 
/*     */   protected final void serializeText(Node t) throws IOException
/*     */   {
/* 241 */     String text = t.getNodeValue();
/*     */ 
/* 243 */     int length = text != null ? text.length() : 0;
/* 244 */     if (length == 0)
/* 245 */       return;
/* 246 */     if (length < this._charBuffer.length) {
/* 247 */       text.getChars(0, length, this._charBuffer, 0);
/* 248 */       if ((getIgnoreWhiteSpaceTextContent()) && (isWhiteSpace(this._charBuffer, 0, length))) {
/* 249 */         return;
/*     */       }
/* 251 */       encodeTermination();
/* 252 */       encodeCharacters(this._charBuffer, 0, length);
/*     */     } else {
/* 254 */       char[] ch = text.toCharArray();
/* 255 */       if ((getIgnoreWhiteSpaceTextContent()) && (isWhiteSpace(ch, 0, length))) {
/* 256 */         return;
/*     */       }
/* 258 */       encodeTermination();
/* 259 */       encodeCharactersNoClone(ch, 0, length);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final void serializeCDATA(Node t) throws IOException {
/* 264 */     String text = t.getNodeValue();
/*     */ 
/* 266 */     int length = text != null ? text.length() : 0;
/* 267 */     if (length == 0) {
/* 268 */       return;
/*     */     }
/* 270 */     char[] ch = text.toCharArray();
/* 271 */     if ((getIgnoreWhiteSpaceTextContent()) && (isWhiteSpace(ch, 0, length))) {
/* 272 */       return;
/*     */     }
/* 274 */     encodeTermination();
/*     */     try {
/* 276 */       encodeCIIBuiltInAlgorithmDataAsCDATA(ch, 0, length);
/*     */     } catch (FastInfosetException e) {
/* 278 */       throw new IOException("");
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final void serializeComment(Node c) throws IOException
/*     */   {
/* 284 */     if (getIgnoreComments()) return;
/*     */ 
/* 286 */     encodeTermination();
/*     */ 
/* 288 */     String comment = c.getNodeValue();
/*     */ 
/* 290 */     int length = comment != null ? comment.length() : 0;
/* 291 */     if (length == 0) {
/* 292 */       encodeComment(this._charBuffer, 0, 0);
/* 293 */     } else if (length < this._charBuffer.length) {
/* 294 */       comment.getChars(0, length, this._charBuffer, 0);
/* 295 */       encodeComment(this._charBuffer, 0, length);
/*     */     } else {
/* 297 */       char[] ch = comment.toCharArray();
/* 298 */       encodeCommentNoClone(ch, 0, length);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final void serializeProcessingInstruction(Node pi) throws IOException {
/* 303 */     if (getIgnoreProcesingInstructions()) return;
/*     */ 
/* 305 */     encodeTermination();
/*     */ 
/* 307 */     String target = pi.getNodeName();
/* 308 */     String data = pi.getNodeValue();
/* 309 */     encodeProcessingInstruction(target, data);
/*     */   }
/*     */ 
/*     */   protected final void encodeElement(String namespaceURI, String qName, String localName) throws IOException {
/* 313 */     LocalNameQualifiedNamesMap.Entry entry = this._v.elementName.obtainEntry(qName);
/* 314 */     if (entry._valueIndex > 0) {
/* 315 */       QualifiedName[] names = entry._value;
/* 316 */       for (int i = 0; i < entry._valueIndex; i++) {
/* 317 */         if ((namespaceURI == names[i].namespaceName) || (namespaceURI.equals(names[i].namespaceName))) {
/* 318 */           encodeNonZeroIntegerOnThirdBit(names[i].index);
/* 319 */           return;
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 325 */     if (localName != null) {
/* 326 */       encodeLiteralElementQualifiedNameOnThirdBit(namespaceURI, getPrefixFromQualifiedName(qName), localName, entry);
/*     */     }
/*     */     else
/* 329 */       encodeLiteralElementQualifiedNameOnThirdBit(namespaceURI, "", qName, entry);
/*     */   }
/*     */ 
/*     */   protected final void encodeAttribute(String namespaceURI, String qName, String localName) throws IOException
/*     */   {
/* 334 */     LocalNameQualifiedNamesMap.Entry entry = this._v.attributeName.obtainEntry(qName);
/* 335 */     if (entry._valueIndex > 0) {
/* 336 */       QualifiedName[] names = entry._value;
/* 337 */       for (int i = 0; i < entry._valueIndex; i++) {
/* 338 */         if ((namespaceURI == names[i].namespaceName) || (namespaceURI.equals(names[i].namespaceName))) {
/* 339 */           encodeNonZeroIntegerOnSecondBitFirstBitZero(names[i].index);
/* 340 */           return;
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 346 */     if (localName != null) {
/* 347 */       encodeLiteralAttributeQualifiedNameOnSecondBit(namespaceURI, getPrefixFromQualifiedName(qName), localName, entry);
/*     */     }
/*     */     else
/* 350 */       encodeLiteralAttributeQualifiedNameOnSecondBit(namespaceURI, "", qName, entry);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.dom.DOMDocumentSerializer
 * JD-Core Version:    0.6.2
 */