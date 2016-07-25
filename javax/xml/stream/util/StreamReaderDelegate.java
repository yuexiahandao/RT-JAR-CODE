/*     */ package javax.xml.stream.util;
/*     */ 
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.Location;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ 
/*     */ public class StreamReaderDelegate
/*     */   implements XMLStreamReader
/*     */ {
/*     */   private XMLStreamReader reader;
/*     */ 
/*     */   public StreamReaderDelegate()
/*     */   {
/*     */   }
/*     */ 
/*     */   public StreamReaderDelegate(XMLStreamReader reader)
/*     */   {
/*  66 */     this.reader = reader;
/*     */   }
/*     */ 
/*     */   public void setParent(XMLStreamReader reader)
/*     */   {
/*  74 */     this.reader = reader;
/*     */   }
/*     */ 
/*     */   public XMLStreamReader getParent()
/*     */   {
/*  82 */     return this.reader;
/*     */   }
/*     */ 
/*     */   public int next()
/*     */     throws XMLStreamException
/*     */   {
/*  88 */     return this.reader.next();
/*     */   }
/*     */ 
/*     */   public int nextTag()
/*     */     throws XMLStreamException
/*     */   {
/*  94 */     return this.reader.nextTag();
/*     */   }
/*     */ 
/*     */   public String getElementText()
/*     */     throws XMLStreamException
/*     */   {
/* 100 */     return this.reader.getElementText();
/*     */   }
/*     */ 
/*     */   public void require(int type, String namespaceURI, String localName)
/*     */     throws XMLStreamException
/*     */   {
/* 106 */     this.reader.require(type, namespaceURI, localName);
/*     */   }
/*     */ 
/*     */   public boolean hasNext()
/*     */     throws XMLStreamException
/*     */   {
/* 112 */     return this.reader.hasNext();
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws XMLStreamException
/*     */   {
/* 118 */     this.reader.close();
/*     */   }
/*     */ 
/*     */   public String getNamespaceURI(String prefix)
/*     */   {
/* 123 */     return this.reader.getNamespaceURI(prefix);
/*     */   }
/*     */ 
/*     */   public NamespaceContext getNamespaceContext() {
/* 127 */     return this.reader.getNamespaceContext();
/*     */   }
/*     */ 
/*     */   public boolean isStartElement() {
/* 131 */     return this.reader.isStartElement();
/*     */   }
/*     */ 
/*     */   public boolean isEndElement() {
/* 135 */     return this.reader.isEndElement();
/*     */   }
/*     */ 
/*     */   public boolean isCharacters() {
/* 139 */     return this.reader.isCharacters();
/*     */   }
/*     */ 
/*     */   public boolean isWhiteSpace() {
/* 143 */     return this.reader.isWhiteSpace();
/*     */   }
/*     */ 
/*     */   public String getAttributeValue(String namespaceUri, String localName)
/*     */   {
/* 149 */     return this.reader.getAttributeValue(namespaceUri, localName);
/*     */   }
/*     */ 
/*     */   public int getAttributeCount() {
/* 153 */     return this.reader.getAttributeCount();
/*     */   }
/*     */ 
/*     */   public QName getAttributeName(int index) {
/* 157 */     return this.reader.getAttributeName(index);
/*     */   }
/*     */ 
/*     */   public String getAttributePrefix(int index) {
/* 161 */     return this.reader.getAttributePrefix(index);
/*     */   }
/*     */ 
/*     */   public String getAttributeNamespace(int index) {
/* 165 */     return this.reader.getAttributeNamespace(index);
/*     */   }
/*     */ 
/*     */   public String getAttributeLocalName(int index) {
/* 169 */     return this.reader.getAttributeLocalName(index);
/*     */   }
/*     */ 
/*     */   public String getAttributeType(int index) {
/* 173 */     return this.reader.getAttributeType(index);
/*     */   }
/*     */ 
/*     */   public String getAttributeValue(int index) {
/* 177 */     return this.reader.getAttributeValue(index);
/*     */   }
/*     */ 
/*     */   public boolean isAttributeSpecified(int index) {
/* 181 */     return this.reader.isAttributeSpecified(index);
/*     */   }
/*     */ 
/*     */   public int getNamespaceCount() {
/* 185 */     return this.reader.getNamespaceCount();
/*     */   }
/*     */ 
/*     */   public String getNamespacePrefix(int index) {
/* 189 */     return this.reader.getNamespacePrefix(index);
/*     */   }
/*     */ 
/*     */   public String getNamespaceURI(int index) {
/* 193 */     return this.reader.getNamespaceURI(index);
/*     */   }
/*     */ 
/*     */   public int getEventType() {
/* 197 */     return this.reader.getEventType();
/*     */   }
/*     */ 
/*     */   public String getText() {
/* 201 */     return this.reader.getText();
/*     */   }
/*     */ 
/*     */   public int getTextCharacters(int sourceStart, char[] target, int targetStart, int length)
/*     */     throws XMLStreamException
/*     */   {
/* 209 */     return this.reader.getTextCharacters(sourceStart, target, targetStart, length);
/*     */   }
/*     */ 
/*     */   public char[] getTextCharacters()
/*     */   {
/* 217 */     return this.reader.getTextCharacters();
/*     */   }
/*     */ 
/*     */   public int getTextStart() {
/* 221 */     return this.reader.getTextStart();
/*     */   }
/*     */ 
/*     */   public int getTextLength() {
/* 225 */     return this.reader.getTextLength();
/*     */   }
/*     */ 
/*     */   public String getEncoding() {
/* 229 */     return this.reader.getEncoding();
/*     */   }
/*     */ 
/*     */   public boolean hasText() {
/* 233 */     return this.reader.hasText();
/*     */   }
/*     */ 
/*     */   public Location getLocation() {
/* 237 */     return this.reader.getLocation();
/*     */   }
/*     */ 
/*     */   public QName getName() {
/* 241 */     return this.reader.getName();
/*     */   }
/*     */ 
/*     */   public String getLocalName() {
/* 245 */     return this.reader.getLocalName();
/*     */   }
/*     */ 
/*     */   public boolean hasName() {
/* 249 */     return this.reader.hasName();
/*     */   }
/*     */ 
/*     */   public String getNamespaceURI() {
/* 253 */     return this.reader.getNamespaceURI();
/*     */   }
/*     */ 
/*     */   public String getPrefix() {
/* 257 */     return this.reader.getPrefix();
/*     */   }
/*     */ 
/*     */   public String getVersion() {
/* 261 */     return this.reader.getVersion();
/*     */   }
/*     */ 
/*     */   public boolean isStandalone() {
/* 265 */     return this.reader.isStandalone();
/*     */   }
/*     */ 
/*     */   public boolean standaloneSet() {
/* 269 */     return this.reader.standaloneSet();
/*     */   }
/*     */ 
/*     */   public String getCharacterEncodingScheme() {
/* 273 */     return this.reader.getCharacterEncodingScheme();
/*     */   }
/*     */ 
/*     */   public String getPITarget() {
/* 277 */     return this.reader.getPITarget();
/*     */   }
/*     */ 
/*     */   public String getPIData() {
/* 281 */     return this.reader.getPIData();
/*     */   }
/*     */ 
/*     */   public Object getProperty(String name) {
/* 285 */     return this.reader.getProperty(name);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.stream.util.StreamReaderDelegate
 * JD-Core Version:    0.6.2
 */