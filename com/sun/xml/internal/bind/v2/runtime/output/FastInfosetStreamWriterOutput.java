/*     */ package com.sun.xml.internal.bind.v2.runtime.output;
/*     */ 
/*     */ import com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl;
/*     */ import com.sun.xml.internal.bind.v2.runtime.Name;
/*     */ import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Base64Data;
/*     */ import com.sun.xml.internal.fastinfoset.stax.StAXDocumentSerializer;
/*     */ import com.sun.xml.internal.org.jvnet.fastinfoset.VocabularyApplicationData;
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.WeakHashMap;
/*     */ import javax.xml.bind.JAXBContext;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public final class FastInfosetStreamWriterOutput extends XMLStreamWriterOutput
/*     */ {
/*     */   private final StAXDocumentSerializer fiout;
/*     */   private final Encoded[] localNames;
/*     */   private final TablesPerJAXBContext tables;
/*     */ 
/*     */   public FastInfosetStreamWriterOutput(StAXDocumentSerializer out, JAXBContextImpl context)
/*     */   {
/* 223 */     super(out);
/*     */ 
/* 225 */     this.fiout = out;
/* 226 */     this.localNames = context.getUTF8NameTable();
/*     */ 
/* 228 */     VocabularyApplicationData vocabAppData = this.fiout.getVocabularyApplicationData();
/* 229 */     AppData appData = null;
/* 230 */     if ((vocabAppData == null) || (!(vocabAppData instanceof AppData))) {
/* 231 */       appData = new AppData();
/* 232 */       this.fiout.setVocabularyApplicationData(appData);
/*     */     } else {
/* 234 */       appData = (AppData)vocabAppData;
/*     */     }
/*     */ 
/* 237 */     TablesPerJAXBContext tablesPerContext = (TablesPerJAXBContext)appData.contexts.get(context);
/* 238 */     if (tablesPerContext != null) {
/* 239 */       this.tables = tablesPerContext;
/*     */ 
/* 244 */       this.tables.clearOrResetTables(out.getLocalNameIndex());
/*     */     } else {
/* 246 */       this.tables = new TablesPerJAXBContext(context, out.getLocalNameIndex());
/* 247 */       appData.contexts.put(context, this.tables);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void startDocument(XMLSerializer serializer, boolean fragment, int[] nsUriIndex2prefixIndex, NamespaceContextImpl nsContext)
/*     */     throws IOException, SAXException, XMLStreamException
/*     */   {
/* 255 */     super.startDocument(serializer, fragment, nsUriIndex2prefixIndex, nsContext);
/*     */ 
/* 257 */     if (fragment)
/* 258 */       this.fiout.initiateLowLevelWriting();
/*     */   }
/*     */ 
/*     */   public void endDocument(boolean fragment) throws IOException, SAXException, XMLStreamException
/*     */   {
/* 263 */     super.endDocument(fragment);
/*     */   }
/*     */ 
/*     */   public void beginStartTag(Name name) throws IOException
/*     */   {
/* 268 */     this.fiout.writeLowLevelTerminationAndMark();
/*     */ 
/* 270 */     if (this.nsContext.getCurrent().count() == 0) {
/* 271 */       int qNameIndex = this.tables.elementIndexes[name.qNameIndex] - this.tables.indexOffset;
/* 272 */       int prefixIndex = this.nsUriIndex2prefixIndex[name.nsUriIndex];
/*     */ 
/* 274 */       if ((qNameIndex >= 0) && (this.tables.elementIndexPrefixes[name.qNameIndex] == prefixIndex))
/*     */       {
/* 276 */         this.fiout.writeLowLevelStartElementIndexed(0, qNameIndex);
/*     */       } else {
/* 278 */         this.tables.elementIndexes[name.qNameIndex] = (this.fiout.getNextElementIndex() + this.tables.indexOffset);
/* 279 */         this.tables.elementIndexPrefixes[name.qNameIndex] = prefixIndex;
/* 280 */         writeLiteral(60, name, this.nsContext.getPrefix(prefixIndex), this.nsContext.getNamespaceURI(prefixIndex));
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 286 */       beginStartTagWithNamespaces(name);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void beginStartTagWithNamespaces(Name name) throws IOException {
/* 291 */     NamespaceContextImpl.Element nse = this.nsContext.getCurrent();
/*     */ 
/* 293 */     this.fiout.writeLowLevelStartNamespaces();
/* 294 */     for (int i = nse.count() - 1; i >= 0; i--) {
/* 295 */       String uri = nse.getNsUri(i);
/* 296 */       if ((uri.length() != 0) || (nse.getBase() != 1))
/*     */       {
/* 298 */         this.fiout.writeLowLevelNamespace(nse.getPrefix(i), uri);
/*     */       }
/*     */     }
/* 300 */     this.fiout.writeLowLevelEndNamespaces();
/*     */ 
/* 302 */     int qNameIndex = this.tables.elementIndexes[name.qNameIndex] - this.tables.indexOffset;
/* 303 */     int prefixIndex = this.nsUriIndex2prefixIndex[name.nsUriIndex];
/*     */ 
/* 305 */     if ((qNameIndex >= 0) && (this.tables.elementIndexPrefixes[name.qNameIndex] == prefixIndex))
/*     */     {
/* 307 */       this.fiout.writeLowLevelStartElementIndexed(0, qNameIndex);
/*     */     } else {
/* 309 */       this.tables.elementIndexes[name.qNameIndex] = (this.fiout.getNextElementIndex() + this.tables.indexOffset);
/* 310 */       this.tables.elementIndexPrefixes[name.qNameIndex] = prefixIndex;
/* 311 */       writeLiteral(60, name, this.nsContext.getPrefix(prefixIndex), this.nsContext.getNamespaceURI(prefixIndex));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void attribute(Name name, String value)
/*     */     throws IOException
/*     */   {
/* 320 */     this.fiout.writeLowLevelStartAttributes();
/*     */ 
/* 322 */     int qNameIndex = this.tables.attributeIndexes[name.qNameIndex] - this.tables.indexOffset;
/* 323 */     if (qNameIndex >= 0) {
/* 324 */       this.fiout.writeLowLevelAttributeIndexed(qNameIndex);
/*     */     } else {
/* 326 */       this.tables.attributeIndexes[name.qNameIndex] = (this.fiout.getNextAttributeIndex() + this.tables.indexOffset);
/*     */ 
/* 328 */       int namespaceURIId = name.nsUriIndex;
/* 329 */       if (namespaceURIId == -1) {
/* 330 */         writeLiteral(120, name, "", "");
/*     */       }
/*     */       else
/*     */       {
/* 335 */         int prefix = this.nsUriIndex2prefixIndex[namespaceURIId];
/* 336 */         writeLiteral(120, name, this.nsContext.getPrefix(prefix), this.nsContext.getNamespaceURI(prefix));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 343 */     this.fiout.writeLowLevelAttributeValue(value);
/*     */   }
/*     */ 
/*     */   private void writeLiteral(int type, Name name, String prefix, String namespaceURI) throws IOException {
/* 347 */     int localNameIndex = this.tables.localNameIndexes[name.localNameIndex] - this.tables.indexOffset;
/*     */ 
/* 349 */     if (localNameIndex < 0) {
/* 350 */       this.tables.localNameIndexes[name.localNameIndex] = (this.fiout.getNextLocalNameIndex() + this.tables.indexOffset);
/*     */ 
/* 352 */       this.fiout.writeLowLevelStartNameLiteral(type, prefix, this.localNames[name.localNameIndex].buf, namespaceURI);
/*     */     }
/*     */     else
/*     */     {
/* 358 */       this.fiout.writeLowLevelStartNameLiteral(type, prefix, localNameIndex, namespaceURI);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void endStartTag()
/*     */     throws IOException
/*     */   {
/* 368 */     this.fiout.writeLowLevelEndStartElement();
/*     */   }
/*     */ 
/*     */   public void endTag(Name name) throws IOException
/*     */   {
/* 373 */     this.fiout.writeLowLevelEndElement();
/*     */   }
/*     */ 
/*     */   public void endTag(int prefix, String localName) throws IOException
/*     */   {
/* 378 */     this.fiout.writeLowLevelEndElement();
/*     */   }
/*     */ 
/*     */   public void text(Pcdata value, boolean needsSeparatingWhitespace)
/*     */     throws IOException
/*     */   {
/* 384 */     if (needsSeparatingWhitespace) {
/* 385 */       this.fiout.writeLowLevelText(" ");
/*     */     }
/*     */ 
/* 390 */     if (!(value instanceof Base64Data)) {
/* 391 */       int len = value.length();
/* 392 */       if (len < this.buf.length) {
/* 393 */         value.writeTo(this.buf, 0);
/* 394 */         this.fiout.writeLowLevelText(this.buf, len);
/*     */       } else {
/* 396 */         this.fiout.writeLowLevelText(value.toString());
/*     */       }
/*     */     } else {
/* 399 */       Base64Data dataValue = (Base64Data)value;
/*     */ 
/* 401 */       this.fiout.writeLowLevelOctets(dataValue.get(), dataValue.getDataLen());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void text(String value, boolean needsSeparatingWhitespace)
/*     */     throws IOException
/*     */   {
/* 408 */     if (needsSeparatingWhitespace) {
/* 409 */       this.fiout.writeLowLevelText(" ");
/*     */     }
/* 411 */     this.fiout.writeLowLevelText(value);
/*     */   }
/*     */ 
/*     */   public void beginStartTag(int prefix, String localName)
/*     */     throws IOException
/*     */   {
/* 417 */     this.fiout.writeLowLevelTerminationAndMark();
/*     */ 
/* 419 */     int type = 0;
/* 420 */     if (this.nsContext.getCurrent().count() > 0) {
/* 421 */       NamespaceContextImpl.Element nse = this.nsContext.getCurrent();
/*     */ 
/* 423 */       this.fiout.writeLowLevelStartNamespaces();
/* 424 */       for (int i = nse.count() - 1; i >= 0; i--) {
/* 425 */         String uri = nse.getNsUri(i);
/* 426 */         if ((uri.length() != 0) || (nse.getBase() != 1))
/*     */         {
/* 428 */           this.fiout.writeLowLevelNamespace(nse.getPrefix(i), uri);
/*     */         }
/*     */       }
/* 430 */       this.fiout.writeLowLevelEndNamespaces();
/*     */ 
/* 432 */       type = 0;
/*     */     }
/*     */ 
/* 435 */     boolean isIndexed = this.fiout.writeLowLevelStartElement(type, this.nsContext.getPrefix(prefix), localName, this.nsContext.getNamespaceURI(prefix));
/*     */ 
/* 441 */     if (!isIndexed)
/* 442 */       this.tables.incrementMaxIndexValue();
/*     */   }
/*     */ 
/*     */   public void attribute(int prefix, String localName, String value) throws IOException
/*     */   {
/* 447 */     this.fiout.writeLowLevelStartAttributes();
/*     */     boolean isIndexed;
/*     */     boolean isIndexed;
/* 450 */     if (prefix == -1)
/* 451 */       isIndexed = this.fiout.writeLowLevelAttribute("", "", localName);
/*     */     else {
/* 453 */       isIndexed = this.fiout.writeLowLevelAttribute(this.nsContext.getPrefix(prefix), this.nsContext.getNamespaceURI(prefix), localName);
/*     */     }
/*     */ 
/* 458 */     if (!isIndexed) {
/* 459 */       this.tables.incrementMaxIndexValue();
/*     */     }
/* 461 */     this.fiout.writeLowLevelAttributeValue(value);
/*     */   }
/*     */ 
/*     */   static final class AppData
/*     */     implements VocabularyApplicationData
/*     */   {
/* 208 */     final Map<JAXBContext, FastInfosetStreamWriterOutput.TablesPerJAXBContext> contexts = new WeakHashMap();
/*     */ 
/* 210 */     final Collection<FastInfosetStreamWriterOutput.TablesPerJAXBContext> collectionOfContexts = this.contexts.values();
/*     */ 
/*     */     public void clear()
/*     */     {
/* 216 */       for (FastInfosetStreamWriterOutput.TablesPerJAXBContext c : this.collectionOfContexts)
/* 217 */         c.requireClearTables();
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class TablesPerJAXBContext
/*     */   {
/*     */     final int[] elementIndexes;
/*     */     final int[] elementIndexPrefixes;
/*     */     final int[] attributeIndexes;
/*     */     final int[] localNameIndexes;
/*     */     int indexOffset;
/*     */     int maxIndex;
/*     */     boolean requiresClear;
/*     */ 
/*     */     TablesPerJAXBContext(JAXBContextImpl context, int initialIndexOffset)
/*     */     {
/* 108 */       this.elementIndexes = new int[context.getNumberOfElementNames()];
/* 109 */       this.elementIndexPrefixes = new int[context.getNumberOfElementNames()];
/* 110 */       this.attributeIndexes = new int[context.getNumberOfAttributeNames()];
/* 111 */       this.localNameIndexes = new int[context.getNumberOfLocalNames()];
/*     */ 
/* 113 */       this.indexOffset = 1;
/* 114 */       this.maxIndex = (initialIndexOffset + this.elementIndexes.length + this.attributeIndexes.length);
/*     */     }
/*     */ 
/*     */     public void requireClearTables()
/*     */     {
/* 121 */       this.requiresClear = true;
/*     */     }
/*     */ 
/*     */     public void clearOrResetTables(int intialIndexOffset)
/*     */     {
/* 131 */       if (this.requiresClear) {
/* 132 */         this.requiresClear = false;
/*     */ 
/* 135 */         this.indexOffset += this.maxIndex;
/*     */ 
/* 137 */         this.maxIndex = (intialIndexOffset + this.elementIndexes.length + this.attributeIndexes.length);
/*     */ 
/* 140 */         if (this.indexOffset + this.maxIndex < 0)
/* 141 */           clearAll();
/*     */       }
/*     */       else
/*     */       {
/* 145 */         this.maxIndex = (intialIndexOffset + this.elementIndexes.length + this.attributeIndexes.length);
/*     */ 
/* 148 */         if (this.indexOffset + this.maxIndex < 0)
/* 149 */           resetAll();
/*     */       }
/*     */     }
/*     */ 
/*     */     private void clearAll()
/*     */     {
/* 155 */       clear(this.elementIndexes);
/* 156 */       clear(this.attributeIndexes);
/* 157 */       clear(this.localNameIndexes);
/* 158 */       this.indexOffset = 1;
/*     */     }
/*     */ 
/*     */     private void clear(int[] array) {
/* 162 */       for (int i = 0; i < array.length; i++)
/* 163 */         array[i] = 0;
/*     */     }
/*     */ 
/*     */     public void incrementMaxIndexValue()
/*     */     {
/* 174 */       this.maxIndex += 1;
/*     */ 
/* 177 */       if (this.indexOffset + this.maxIndex < 0)
/* 178 */         resetAll();
/*     */     }
/*     */ 
/*     */     private void resetAll()
/*     */     {
/* 183 */       clear(this.elementIndexes);
/* 184 */       clear(this.attributeIndexes);
/* 185 */       clear(this.localNameIndexes);
/* 186 */       this.indexOffset = 1;
/*     */     }
/*     */ 
/*     */     private void reset(int[] array) {
/* 190 */       for (int i = 0; i < array.length; i++)
/* 191 */         if (array[i] > this.indexOffset)
/* 192 */           array[i] = (array[i] - this.indexOffset + 1);
/*     */         else
/* 194 */           array[i] = 0;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.output.FastInfosetStreamWriterOutput
 * JD-Core Version:    0.6.2
 */