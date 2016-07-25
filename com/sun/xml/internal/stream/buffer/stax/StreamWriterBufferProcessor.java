/*     */ package com.sun.xml.internal.stream.buffer.stax;
/*     */ 
/*     */ import com.sun.xml.internal.org.jvnet.staxex.XMLStreamWriterEx;
/*     */ import com.sun.xml.internal.stream.buffer.AbstractProcessor;
/*     */ import com.sun.xml.internal.stream.buffer.XMLStreamBuffer;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ 
/*     */ public class StreamWriterBufferProcessor extends AbstractProcessor
/*     */ {
/*     */   public StreamWriterBufferProcessor()
/*     */   {
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public StreamWriterBufferProcessor(XMLStreamBuffer buffer)
/*     */   {
/*  60 */     setXMLStreamBuffer(buffer, buffer.isFragment());
/*     */   }
/*     */ 
/*     */   public StreamWriterBufferProcessor(XMLStreamBuffer buffer, boolean produceFragmentEvent)
/*     */   {
/*  69 */     setXMLStreamBuffer(buffer, produceFragmentEvent);
/*     */   }
/*     */ 
/*     */   public final void process(XMLStreamBuffer buffer, XMLStreamWriter writer) throws XMLStreamException {
/*  73 */     setXMLStreamBuffer(buffer, buffer.isFragment());
/*  74 */     process(writer);
/*     */   }
/*     */ 
/*     */   public void process(XMLStreamWriter writer) throws XMLStreamException {
/*  78 */     if (this._fragmentMode)
/*  79 */       writeFragment(writer);
/*     */     else
/*  81 */       write(writer);
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public void setXMLStreamBuffer(XMLStreamBuffer buffer)
/*     */   {
/*  90 */     setBuffer(buffer);
/*     */   }
/*     */ 
/*     */   public void setXMLStreamBuffer(XMLStreamBuffer buffer, boolean produceFragmentEvent)
/*     */   {
/*  99 */     setBuffer(buffer, produceFragmentEvent);
/*     */   }
/*     */ 
/*     */   public void write(XMLStreamWriter writer)
/*     */     throws XMLStreamException
/*     */   {
/* 110 */     if (!this._fragmentMode) {
/* 111 */       if (this._treeCount > 1)
/* 112 */         throw new IllegalStateException("forest cannot be written as a full infoset");
/* 113 */       writer.writeStartDocument();
/*     */     }
/*     */     while (true)
/*     */     {
/* 117 */       int item = getEIIState(peekStructure());
/* 118 */       writer.flush();
/*     */ 
/* 120 */       switch (item) {
/*     */       case 1:
/* 122 */         readStructure();
/* 123 */         break;
/*     */       case 3:
/*     */       case 4:
/*     */       case 5:
/*     */       case 6:
/* 128 */         writeFragment(writer);
/* 129 */         break;
/*     */       case 12:
/* 131 */         readStructure();
/* 132 */         int length = readStructure();
/* 133 */         int start = readContentCharactersBuffer(length);
/* 134 */         String comment = new String(this._contentCharactersBuffer, start, length);
/* 135 */         writer.writeComment(comment);
/* 136 */         break;
/*     */       case 13:
/* 139 */         readStructure();
/* 140 */         int length = readStructure16();
/* 141 */         int start = readContentCharactersBuffer(length);
/* 142 */         String comment = new String(this._contentCharactersBuffer, start, length);
/* 143 */         writer.writeComment(comment);
/* 144 */         break;
/*     */       case 14:
/* 147 */         readStructure();
/* 148 */         char[] ch = readContentCharactersCopy();
/* 149 */         writer.writeComment(new String(ch));
/* 150 */         break;
/*     */       case 16:
/* 153 */         readStructure();
/* 154 */         writer.writeProcessingInstruction(readStructureString(), readStructureString());
/* 155 */         break;
/*     */       case 17:
/* 157 */         readStructure();
/* 158 */         writer.writeEndDocument();
/* 159 */         return;
/*     */       case 2:
/*     */       case 7:
/*     */       case 8:
/*     */       case 9:
/*     */       case 10:
/*     */       case 11:
/*     */       case 15:
/*     */       default:
/* 161 */         throw new XMLStreamException("Invalid State " + item);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writeFragment(XMLStreamWriter writer)
/*     */     throws XMLStreamException
/*     */   {
/* 176 */     if ((writer instanceof XMLStreamWriterEx))
/* 177 */       writeFragmentEx((XMLStreamWriterEx)writer);
/*     */     else
/* 179 */       writeFragmentNoEx(writer);
/*     */   }
/*     */ 
/*     */   public void writeFragmentEx(XMLStreamWriterEx writer) throws XMLStreamException
/*     */   {
/* 184 */     int depth = 0;
/*     */ 
/* 186 */     int item = getEIIState(peekStructure());
/* 187 */     if (item == 1) {
/* 188 */       readStructure();
/*     */     }
/*     */     do
/*     */     {
/* 192 */       item = readEiiState();
/*     */ 
/* 194 */       switch (item) {
/*     */       case 1:
/* 196 */         throw new AssertionError();
/*     */       case 3:
/* 198 */         depth++;
/* 199 */         String uri = readStructureString();
/* 200 */         String localName = readStructureString();
/* 201 */         String prefix = getPrefixFromQName(readStructureString());
/* 202 */         writer.writeStartElement(prefix, localName, uri);
/* 203 */         writeAttributes(writer, isInscope(depth));
/* 204 */         break;
/*     */       case 4:
/* 207 */         depth++;
/* 208 */         String prefix = readStructureString();
/* 209 */         String uri = readStructureString();
/* 210 */         String localName = readStructureString();
/* 211 */         writer.writeStartElement(prefix, localName, uri);
/* 212 */         writeAttributes(writer, isInscope(depth));
/* 213 */         break;
/*     */       case 5:
/* 216 */         depth++;
/* 217 */         String uri = readStructureString();
/* 218 */         String localName = readStructureString();
/* 219 */         writer.writeStartElement("", localName, uri);
/* 220 */         writeAttributes(writer, isInscope(depth));
/* 221 */         break;
/*     */       case 6:
/* 224 */         depth++;
/* 225 */         String localName = readStructureString();
/* 226 */         writer.writeStartElement(localName);
/* 227 */         writeAttributes(writer, isInscope(depth));
/* 228 */         break;
/*     */       case 7:
/* 231 */         int length = readStructure();
/* 232 */         int start = readContentCharactersBuffer(length);
/* 233 */         writer.writeCharacters(this._contentCharactersBuffer, start, length);
/* 234 */         break;
/*     */       case 8:
/* 237 */         int length = readStructure16();
/* 238 */         int start = readContentCharactersBuffer(length);
/* 239 */         writer.writeCharacters(this._contentCharactersBuffer, start, length);
/* 240 */         break;
/*     */       case 9:
/* 243 */         char[] c = readContentCharactersCopy();
/* 244 */         writer.writeCharacters(c, 0, c.length);
/* 245 */         break;
/*     */       case 10:
/* 248 */         String s = readContentString();
/* 249 */         writer.writeCharacters(s);
/* 250 */         break;
/*     */       case 11:
/* 253 */         CharSequence c = (CharSequence)readContentObject();
/* 254 */         writer.writePCDATA(c);
/* 255 */         break;
/*     */       case 12:
/* 258 */         int length = readStructure();
/* 259 */         int start = readContentCharactersBuffer(length);
/* 260 */         String comment = new String(this._contentCharactersBuffer, start, length);
/* 261 */         writer.writeComment(comment);
/* 262 */         break;
/*     */       case 13:
/* 265 */         int length = readStructure16();
/* 266 */         int start = readContentCharactersBuffer(length);
/* 267 */         String comment = new String(this._contentCharactersBuffer, start, length);
/* 268 */         writer.writeComment(comment);
/* 269 */         break;
/*     */       case 14:
/* 272 */         char[] ch = readContentCharactersCopy();
/* 273 */         writer.writeComment(new String(ch));
/* 274 */         break;
/*     */       case 16:
/* 277 */         writer.writeProcessingInstruction(readStructureString(), readStructureString());
/* 278 */         break;
/*     */       case 17:
/* 280 */         writer.writeEndElement();
/* 281 */         depth--;
/* 282 */         if (depth == 0)
/* 283 */           this._treeCount -= 1; break;
/*     */       case 2:
/*     */       case 15:
/*     */       default:
/* 286 */         throw new XMLStreamException("Invalid State " + item);
/*     */       }
/*     */     }
/* 288 */     while ((depth > 0) || (this._treeCount > 0));
/*     */   }
/*     */ 
/*     */   public void writeFragmentNoEx(XMLStreamWriter writer) throws XMLStreamException
/*     */   {
/* 293 */     int depth = 0;
/*     */ 
/* 295 */     int item = getEIIState(peekStructure());
/* 296 */     if (item == 1)
/* 297 */       readStructure();
/*     */     do
/*     */     {
/* 300 */       item = readEiiState();
/*     */ 
/* 302 */       switch (item) {
/*     */       case 1:
/* 304 */         throw new AssertionError();
/*     */       case 3:
/* 306 */         depth++;
/* 307 */         String uri = readStructureString();
/* 308 */         String localName = readStructureString();
/* 309 */         String prefix = getPrefixFromQName(readStructureString());
/* 310 */         writer.writeStartElement(prefix, localName, uri);
/* 311 */         writeAttributes(writer, isInscope(depth));
/* 312 */         break;
/*     */       case 4:
/* 315 */         depth++;
/* 316 */         String prefix = readStructureString();
/* 317 */         String uri = readStructureString();
/* 318 */         String localName = readStructureString();
/* 319 */         writer.writeStartElement(prefix, localName, uri);
/* 320 */         writeAttributes(writer, isInscope(depth));
/* 321 */         break;
/*     */       case 5:
/* 324 */         depth++;
/* 325 */         String uri = readStructureString();
/* 326 */         String localName = readStructureString();
/* 327 */         writer.writeStartElement("", localName, uri);
/* 328 */         writeAttributes(writer, isInscope(depth));
/* 329 */         break;
/*     */       case 6:
/* 332 */         depth++;
/* 333 */         String localName = readStructureString();
/* 334 */         writer.writeStartElement(localName);
/* 335 */         writeAttributes(writer, isInscope(depth));
/* 336 */         break;
/*     */       case 7:
/* 339 */         int length = readStructure();
/* 340 */         int start = readContentCharactersBuffer(length);
/* 341 */         writer.writeCharacters(this._contentCharactersBuffer, start, length);
/* 342 */         break;
/*     */       case 8:
/* 345 */         int length = readStructure16();
/* 346 */         int start = readContentCharactersBuffer(length);
/* 347 */         writer.writeCharacters(this._contentCharactersBuffer, start, length);
/* 348 */         break;
/*     */       case 9:
/* 351 */         char[] c = readContentCharactersCopy();
/* 352 */         writer.writeCharacters(c, 0, c.length);
/* 353 */         break;
/*     */       case 10:
/* 356 */         String s = readContentString();
/* 357 */         writer.writeCharacters(s);
/* 358 */         break;
/*     */       case 11:
/* 361 */         CharSequence c = (CharSequence)readContentObject();
/* 362 */         writer.writeCharacters(c.toString());
/* 363 */         break;
/*     */       case 12:
/* 366 */         int length = readStructure();
/* 367 */         int start = readContentCharactersBuffer(length);
/* 368 */         String comment = new String(this._contentCharactersBuffer, start, length);
/* 369 */         writer.writeComment(comment);
/* 370 */         break;
/*     */       case 13:
/* 373 */         int length = readStructure16();
/* 374 */         int start = readContentCharactersBuffer(length);
/* 375 */         String comment = new String(this._contentCharactersBuffer, start, length);
/* 376 */         writer.writeComment(comment);
/* 377 */         break;
/*     */       case 14:
/* 380 */         char[] ch = readContentCharactersCopy();
/* 381 */         writer.writeComment(new String(ch));
/* 382 */         break;
/*     */       case 16:
/* 385 */         writer.writeProcessingInstruction(readStructureString(), readStructureString());
/* 386 */         break;
/*     */       case 17:
/* 388 */         writer.writeEndElement();
/* 389 */         depth--;
/* 390 */         if (depth == 0)
/* 391 */           this._treeCount -= 1; break;
/*     */       case 2:
/*     */       case 15:
/*     */       default:
/* 394 */         throw new XMLStreamException("Invalid State " + item);
/*     */       }
/*     */     }
/* 396 */     while ((depth > 0) || (this._treeCount > 0));
/*     */   }
/*     */ 
/*     */   private boolean isInscope(int depth)
/*     */   {
/* 401 */     return (this._buffer.getInscopeNamespaces().size() > 0) && (depth == 1);
/*     */   }
/*     */ 
/*     */   private void writeAttributes(XMLStreamWriter writer, boolean inscope)
/*     */     throws XMLStreamException
/*     */   {
/* 409 */     Set prefixSet = inscope ? new HashSet() : Collections.emptySet();
/* 410 */     int item = peekStructure();
/* 411 */     if ((item & 0xF0) == 64)
/*     */     {
/* 414 */       item = writeNamespaceAttributes(item, writer, inscope, prefixSet);
/*     */     }
/* 416 */     if (inscope) {
/* 417 */       writeInscopeNamespaces(writer, prefixSet);
/*     */     }
/* 419 */     if ((item & 0xF0) == 48)
/* 420 */       writeAttributes(item, writer);
/*     */   }
/*     */ 
/*     */   private static String fixNull(String s)
/*     */   {
/* 425 */     if (s == null) return "";
/* 426 */     return s;
/*     */   }
/*     */ 
/*     */   private void writeInscopeNamespaces(XMLStreamWriter writer, Set<String> prefixSet)
/*     */     throws XMLStreamException
/*     */   {
/* 433 */     for (Map.Entry e : this._buffer.getInscopeNamespaces().entrySet()) {
/* 434 */       String key = fixNull((String)e.getKey());
/*     */ 
/* 436 */       if (!prefixSet.contains(key))
/* 437 */         writer.writeNamespace(key, (String)e.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   private int writeNamespaceAttributes(int item, XMLStreamWriter writer, boolean collectPrefixes, Set<String> prefixSet)
/*     */     throws XMLStreamException
/*     */   {
/*     */     do
/*     */     {
/*     */       String prefix;
/* 444 */       switch (getNIIState(item))
/*     */       {
/*     */       case 1:
/* 447 */         writer.writeDefaultNamespace("");
/* 448 */         if (collectPrefixes)
/* 449 */           prefixSet.add(""); break;
/*     */       case 2:
/* 455 */         prefix = readStructureString();
/* 456 */         writer.writeNamespace(prefix, "");
/* 457 */         if (collectPrefixes)
/* 458 */           prefixSet.add(prefix); break;
/*     */       case 3:
/* 463 */         prefix = readStructureString();
/* 464 */         writer.writeNamespace(prefix, readStructureString());
/* 465 */         if (collectPrefixes)
/* 466 */           prefixSet.add(prefix); break;
/*     */       case 4:
/* 471 */         writer.writeDefaultNamespace(readStructureString());
/* 472 */         if (collectPrefixes) {
/* 473 */           prefixSet.add("");
/*     */         }
/*     */         break;
/*     */       }
/* 477 */       readStructure();
/*     */ 
/* 479 */       item = peekStructure();
/* 480 */     }while ((item & 0xF0) == 64);
/*     */ 
/* 482 */     return item;
/*     */   }
/*     */ 
/*     */   private void writeAttributes(int item, XMLStreamWriter writer) throws XMLStreamException {
/*     */     do {
/* 487 */       switch (getAIIState(item)) {
/*     */       case 1:
/* 489 */         String uri = readStructureString();
/* 490 */         String localName = readStructureString();
/* 491 */         String prefix = getPrefixFromQName(readStructureString());
/* 492 */         writer.writeAttribute(prefix, uri, localName, readContentString());
/* 493 */         break;
/*     */       case 2:
/* 496 */         writer.writeAttribute(readStructureString(), readStructureString(), readStructureString(), readContentString());
/*     */ 
/* 498 */         break;
/*     */       case 3:
/* 500 */         writer.writeAttribute(readStructureString(), readStructureString(), readContentString());
/* 501 */         break;
/*     */       case 4:
/* 503 */         writer.writeAttribute(readStructureString(), readContentString());
/*     */       }
/*     */ 
/* 507 */       readStructureString();
/*     */ 
/* 509 */       readStructure();
/*     */ 
/* 511 */       item = peekStructure();
/* 512 */     }while ((item & 0xF0) == 48);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.stream.buffer.stax.StreamWriterBufferProcessor
 * JD-Core Version:    0.6.2
 */