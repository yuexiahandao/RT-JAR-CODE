/*     */ package com.sun.xml.internal.stream.buffer.stax;
/*     */ 
/*     */ import com.sun.xml.internal.org.jvnet.staxex.Base64Data;
/*     */ import com.sun.xml.internal.org.jvnet.staxex.XMLStreamReaderEx;
/*     */ import com.sun.xml.internal.stream.buffer.MutableXMLStreamBuffer;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ 
/*     */ public class StreamReaderBufferCreator extends StreamBufferCreator
/*     */ {
/*     */   private int _eventType;
/*     */   private boolean _storeInScopeNamespacesOnElementFragment;
/*     */   private Map<String, Integer> _inScopePrefixes;
/*     */ 
/*     */   public StreamReaderBufferCreator()
/*     */   {
/*     */   }
/*     */ 
/*     */   public StreamReaderBufferCreator(MutableXMLStreamBuffer buffer)
/*     */   {
/*  63 */     setBuffer(buffer);
/*     */   }
/*     */ 
/*     */   public MutableXMLStreamBuffer create(XMLStreamReader reader)
/*     */     throws XMLStreamException
/*     */   {
/*  85 */     if (this._buffer == null) {
/*  86 */       createBuffer();
/*     */     }
/*  88 */     store(reader);
/*     */ 
/*  90 */     return getXMLStreamBuffer();
/*     */   }
/*     */ 
/*     */   public MutableXMLStreamBuffer createElementFragment(XMLStreamReader reader, boolean storeInScopeNamespaces)
/*     */     throws XMLStreamException
/*     */   {
/* 111 */     if (this._buffer == null) {
/* 112 */       createBuffer();
/*     */     }
/*     */ 
/* 115 */     if (!reader.hasNext()) {
/* 116 */       return this._buffer;
/*     */     }
/*     */ 
/* 119 */     this._storeInScopeNamespacesOnElementFragment = storeInScopeNamespaces;
/*     */ 
/* 121 */     this._eventType = reader.getEventType();
/* 122 */     if (this._eventType != 1) {
/*     */       do
/* 124 */         this._eventType = reader.next();
/* 125 */       while ((this._eventType != 1) && (this._eventType != 8));
/*     */     }
/*     */ 
/* 128 */     if (storeInScopeNamespaces) {
/* 129 */       this._inScopePrefixes = new HashMap();
/*     */     }
/*     */ 
/* 132 */     storeElementAndChildren(reader);
/*     */ 
/* 134 */     return getXMLStreamBuffer();
/*     */   }
/*     */ 
/*     */   private void store(XMLStreamReader reader) throws XMLStreamException {
/* 138 */     if (!reader.hasNext()) {
/* 139 */       return;
/*     */     }
/*     */ 
/* 142 */     this._eventType = reader.getEventType();
/* 143 */     switch (this._eventType) {
/*     */     case 7:
/* 145 */       storeDocumentAndChildren(reader);
/* 146 */       break;
/*     */     case 1:
/* 148 */       storeElementAndChildren(reader);
/* 149 */       break;
/*     */     default:
/* 151 */       throw new XMLStreamException("XMLStreamReader not positioned at a document or element");
/*     */     }
/*     */ 
/* 154 */     increaseTreeCount();
/*     */   }
/*     */ 
/*     */   private void storeDocumentAndChildren(XMLStreamReader reader) throws XMLStreamException {
/* 158 */     storeStructure(16);
/*     */ 
/* 160 */     this._eventType = reader.next();
/* 161 */     while (this._eventType != 8) {
/* 162 */       switch (this._eventType) {
/*     */       case 1:
/* 164 */         storeElementAndChildren(reader);
/* 165 */         break;
/*     */       case 5:
/* 167 */         storeComment(reader);
/* 168 */         break;
/*     */       case 3:
/* 170 */         storeProcessingInstruction(reader);
/*     */       case 2:
/*     */       case 4:
/*     */       default:
/* 173 */         this._eventType = reader.next();
/*     */       }
/*     */     }
/* 176 */     storeStructure(144);
/*     */   }
/*     */ 
/*     */   private void storeElementAndChildren(XMLStreamReader reader) throws XMLStreamException {
/* 180 */     if ((reader instanceof XMLStreamReaderEx))
/* 181 */       storeElementAndChildrenEx((XMLStreamReaderEx)reader);
/*     */     else
/* 183 */       storeElementAndChildrenNoEx(reader);
/*     */   }
/*     */ 
/*     */   private void storeElementAndChildrenEx(XMLStreamReaderEx reader) throws XMLStreamException
/*     */   {
/* 188 */     int depth = 1;
/* 189 */     if (this._storeInScopeNamespacesOnElementFragment)
/* 190 */       storeElementWithInScopeNamespaces(reader);
/*     */     else {
/* 192 */       storeElement(reader);
/*     */     }
/*     */ 
/* 195 */     while (depth > 0) {
/* 196 */       this._eventType = reader.next();
/* 197 */       switch (this._eventType) {
/*     */       case 1:
/* 199 */         depth++;
/* 200 */         storeElement(reader);
/* 201 */         break;
/*     */       case 2:
/* 203 */         depth--;
/* 204 */         storeStructure(144);
/* 205 */         break;
/*     */       case 13:
/* 207 */         storeNamespaceAttributes(reader);
/* 208 */         break;
/*     */       case 10:
/* 210 */         storeAttributes(reader);
/* 211 */         break;
/*     */       case 4:
/*     */       case 6:
/*     */       case 12:
/* 215 */         CharSequence c = reader.getPCDATA();
/* 216 */         if ((c instanceof Base64Data)) {
/* 217 */           storeStructure(92);
/* 218 */           storeContentObject(((Base64Data)c).clone());
/*     */         } else {
/* 220 */           storeContentCharacters(80, reader.getTextCharacters(), reader.getTextStart(), reader.getTextLength());
/*     */         }
/*     */ 
/* 224 */         break;
/*     */       case 5:
/* 227 */         storeComment(reader);
/* 228 */         break;
/*     */       case 3:
/* 230 */         storeProcessingInstruction(reader);
/*     */       case 7:
/*     */       case 8:
/*     */       case 9:
/*     */       case 11:
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 239 */     this._eventType = reader.next();
/*     */   }
/*     */ 
/*     */   private void storeElementAndChildrenNoEx(XMLStreamReader reader) throws XMLStreamException {
/* 243 */     int depth = 1;
/* 244 */     if (this._storeInScopeNamespacesOnElementFragment)
/* 245 */       storeElementWithInScopeNamespaces(reader);
/*     */     else {
/* 247 */       storeElement(reader);
/*     */     }
/*     */ 
/* 250 */     while (depth > 0) {
/* 251 */       this._eventType = reader.next();
/* 252 */       switch (this._eventType) {
/*     */       case 1:
/* 254 */         depth++;
/* 255 */         storeElement(reader);
/* 256 */         break;
/*     */       case 2:
/* 258 */         depth--;
/* 259 */         storeStructure(144);
/* 260 */         break;
/*     */       case 13:
/* 262 */         storeNamespaceAttributes(reader);
/* 263 */         break;
/*     */       case 10:
/* 265 */         storeAttributes(reader);
/* 266 */         break;
/*     */       case 4:
/*     */       case 6:
/*     */       case 12:
/* 270 */         storeContentCharacters(80, reader.getTextCharacters(), reader.getTextStart(), reader.getTextLength());
/*     */ 
/* 273 */         break;
/*     */       case 5:
/* 276 */         storeComment(reader);
/* 277 */         break;
/*     */       case 3:
/* 279 */         storeProcessingInstruction(reader);
/*     */       case 7:
/*     */       case 8:
/*     */       case 9:
/*     */       case 11:
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 288 */     this._eventType = reader.next();
/*     */   }
/*     */ 
/*     */   private void storeElementWithInScopeNamespaces(XMLStreamReader reader) {
/* 292 */     storeQualifiedName(32, reader.getPrefix(), reader.getNamespaceURI(), reader.getLocalName());
/*     */ 
/* 295 */     if (reader.getNamespaceCount() > 0) {
/* 296 */       storeNamespaceAttributes(reader);
/*     */     }
/*     */ 
/* 299 */     if (reader.getAttributeCount() > 0)
/* 300 */       storeAttributes(reader);
/*     */   }
/*     */ 
/*     */   private void storeElement(XMLStreamReader reader)
/*     */   {
/* 305 */     storeQualifiedName(32, reader.getPrefix(), reader.getNamespaceURI(), reader.getLocalName());
/*     */ 
/* 308 */     if (reader.getNamespaceCount() > 0) {
/* 309 */       storeNamespaceAttributes(reader);
/*     */     }
/*     */ 
/* 312 */     if (reader.getAttributeCount() > 0)
/* 313 */       storeAttributes(reader);
/*     */   }
/*     */ 
/*     */   public void storeElement(String nsURI, String localName, String prefix, String[] ns)
/*     */   {
/* 335 */     storeQualifiedName(32, prefix, nsURI, localName);
/* 336 */     storeNamespaceAttributes(ns);
/*     */   }
/*     */ 
/*     */   public void storeEndElement()
/*     */   {
/* 347 */     storeStructure(144);
/*     */   }
/*     */ 
/*     */   private void storeNamespaceAttributes(XMLStreamReader reader) {
/* 351 */     int count = reader.getNamespaceCount();
/* 352 */     for (int i = 0; i < count; i++)
/* 353 */       storeNamespaceAttribute(reader.getNamespacePrefix(i), reader.getNamespaceURI(i));
/*     */   }
/*     */ 
/*     */   private void storeNamespaceAttributes(String[] ns)
/*     */   {
/* 361 */     for (int i = 0; i < ns.length; i += 2)
/* 362 */       storeNamespaceAttribute(ns[i], ns[(i + 1)]);
/*     */   }
/*     */ 
/*     */   private void storeAttributes(XMLStreamReader reader)
/*     */   {
/* 367 */     int count = reader.getAttributeCount();
/* 368 */     for (int i = 0; i < count; i++)
/* 369 */       storeAttribute(reader.getAttributePrefix(i), reader.getAttributeNamespace(i), reader.getAttributeLocalName(i), reader.getAttributeType(i), reader.getAttributeValue(i));
/*     */   }
/*     */ 
/*     */   private void storeComment(XMLStreamReader reader)
/*     */   {
/* 375 */     storeContentCharacters(96, reader.getTextCharacters(), reader.getTextStart(), reader.getTextLength());
/*     */   }
/*     */ 
/*     */   private void storeProcessingInstruction(XMLStreamReader reader)
/*     */   {
/* 380 */     storeProcessingInstruction(reader.getPITarget(), reader.getPIData());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.stream.buffer.stax.StreamReaderBufferCreator
 * JD-Core Version:    0.6.2
 */