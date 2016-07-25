/*     */ package com.sun.xml.internal.ws.streaming;
/*     */ 
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ 
/*     */ public class XMLStreamReaderUtil
/*     */ {
/*     */   public static void close(XMLStreamReader reader)
/*     */   {
/*     */     try
/*     */     {
/*  47 */       reader.close();
/*     */     } catch (XMLStreamException e) {
/*  49 */       throw wrapException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void readRest(XMLStreamReader reader) {
/*     */     try {
/*  55 */       while (reader.getEventType() != 8)
/*  56 */         reader.next();
/*     */     }
/*     */     catch (XMLStreamException e) {
/*  59 */       throw wrapException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static int next(XMLStreamReader reader) {
/*     */     try {
/*  65 */       int readerEvent = reader.next();
/*     */ 
/*  67 */       while (readerEvent != 8) {
/*  68 */         switch (readerEvent) {
/*     */         case 1:
/*     */         case 2:
/*     */         case 3:
/*     */         case 4:
/*     */         case 12:
/*  74 */           return readerEvent;
/*     */         case 5:
/*     */         case 6:
/*     */         case 7:
/*     */         case 8:
/*     */         case 9:
/*     */         case 10:
/*  78 */         case 11: } readerEvent = reader.next();
/*     */       }
/*     */ 
/*  81 */       return readerEvent;
/*     */     }
/*     */     catch (XMLStreamException e) {
/*  84 */       throw wrapException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static int nextElementContent(XMLStreamReader reader) {
/*  89 */     int state = nextContent(reader);
/*  90 */     if (state == 4) {
/*  91 */       throw new XMLStreamReaderException("xmlreader.unexpectedCharacterContent", new Object[] { reader.getText() });
/*     */     }
/*     */ 
/*  94 */     return state;
/*     */   }
/*     */ 
/*     */   public static int nextContent(XMLStreamReader reader) {
/*     */     while (true) {
/*  99 */       int state = next(reader);
/* 100 */       switch (state) {
/*     */       case 1:
/*     */       case 2:
/*     */       case 8:
/* 104 */         return state;
/*     */       case 4:
/* 106 */         if (!reader.isWhiteSpace())
/* 107 */           return 4;
/*     */         break;
/*     */       case 3:
/*     */       case 5:
/*     */       case 6:
/*     */       case 7:
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void skipElement(XMLStreamReader reader) {
/* 118 */     assert (reader.getEventType() == 1);
/* 119 */     skipTags(reader, true);
/* 120 */     assert (reader.getEventType() == 2);
/*     */   }
/*     */ 
/*     */   public static void skipSiblings(XMLStreamReader reader, QName parent)
/*     */   {
/* 128 */     skipTags(reader, reader.getName().equals(parent));
/* 129 */     assert (reader.getEventType() == 2);
/*     */   }
/*     */ 
/*     */   private static void skipTags(XMLStreamReader reader, boolean exitCondition) {
/*     */     try {
/* 134 */       int tags = 0;
/*     */       int state;
/* 135 */       while ((state = reader.next()) != 8)
/* 136 */         if (state == 1) {
/* 137 */           tags++;
/*     */         }
/* 139 */         else if (state == 2) {
/* 140 */           if ((tags == 0) && (exitCondition)) return;
/* 141 */           tags--;
/*     */         }
/*     */     }
/*     */     catch (XMLStreamException e)
/*     */     {
/* 146 */       throw wrapException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static String getElementText(XMLStreamReader reader)
/*     */   {
/*     */     try
/*     */     {
/* 155 */       return reader.getElementText();
/*     */     } catch (XMLStreamException e) {
/* 157 */       throw wrapException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static QName getElementQName(XMLStreamReader reader)
/*     */   {
/*     */     try
/*     */     {
/* 168 */       String text = reader.getElementText().trim();
/* 169 */       String prefix = text.substring(0, text.indexOf(':'));
/* 170 */       String namespaceURI = reader.getNamespaceContext().getNamespaceURI(prefix);
/* 171 */       if (namespaceURI == null) {
/* 172 */         namespaceURI = "";
/*     */       }
/* 174 */       String localPart = text.substring(text.indexOf(':') + 1, text.length());
/*     */ 
/* 176 */       return new QName(namespaceURI, localPart);
/*     */     } catch (XMLStreamException e) {
/* 178 */       throw wrapException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static Attributes getAttributes(XMLStreamReader reader)
/*     */   {
/* 187 */     return (reader.getEventType() == 1) || (reader.getEventType() == 10) ? new AttributesImpl(reader) : null;
/*     */   }
/*     */ 
/*     */   public static void verifyReaderState(XMLStreamReader reader, int expectedState)
/*     */   {
/* 193 */     int state = reader.getEventType();
/* 194 */     if (state != expectedState)
/* 195 */       throw new XMLStreamReaderException("xmlreader.unexpectedState", new Object[] { getStateName(expectedState), getStateName(state) });
/*     */   }
/*     */ 
/*     */   public static void verifyTag(XMLStreamReader reader, String namespaceURI, String localName)
/*     */   {
/* 202 */     if ((!localName.equals(reader.getLocalName())) || (!namespaceURI.equals(reader.getNamespaceURI())))
/* 203 */       throw new XMLStreamReaderException("xmlreader.unexpectedState.tag", new Object[] { "{" + namespaceURI + "}" + localName, "{" + reader.getNamespaceURI() + "}" + reader.getLocalName() });
/*     */   }
/*     */ 
/*     */   public static void verifyTag(XMLStreamReader reader, QName name)
/*     */   {
/* 211 */     verifyTag(reader, name.getNamespaceURI(), name.getLocalPart());
/*     */   }
/*     */ 
/*     */   public static String getStateName(XMLStreamReader reader) {
/* 215 */     return getStateName(reader.getEventType());
/*     */   }
/*     */ 
/*     */   public static String getStateName(int state) {
/* 219 */     switch (state) {
/*     */     case 10:
/* 221 */       return "ATTRIBUTE";
/*     */     case 12:
/* 223 */       return "CDATA";
/*     */     case 4:
/* 225 */       return "CHARACTERS";
/*     */     case 5:
/* 227 */       return "COMMENT";
/*     */     case 11:
/* 229 */       return "DTD";
/*     */     case 8:
/* 231 */       return "END_DOCUMENT";
/*     */     case 2:
/* 233 */       return "END_ELEMENT";
/*     */     case 15:
/* 235 */       return "ENTITY_DECLARATION";
/*     */     case 9:
/* 237 */       return "ENTITY_REFERENCE";
/*     */     case 13:
/* 239 */       return "NAMESPACE";
/*     */     case 14:
/* 241 */       return "NOTATION_DECLARATION";
/*     */     case 3:
/* 243 */       return "PROCESSING_INSTRUCTION";
/*     */     case 6:
/* 245 */       return "SPACE";
/*     */     case 7:
/* 247 */       return "START_DOCUMENT";
/*     */     case 1:
/* 249 */       return "START_ELEMENT";
/*     */     }
/* 251 */     return "UNKNOWN";
/*     */   }
/*     */ 
/*     */   private static XMLStreamReaderException wrapException(XMLStreamException e)
/*     */   {
/* 256 */     return new XMLStreamReaderException("xmlreader.ioException", new Object[] { e });
/*     */   }
/*     */ 
/*     */   public static class AttributesImpl
/*     */     implements Attributes
/*     */   {
/*     */     static final String XMLNS_NAMESPACE_URI = "http://www.w3.org/2000/xmlns/";
/*     */     AttributeInfo[] atInfos;
/*     */ 
/*     */     public AttributesImpl(XMLStreamReader reader)
/*     */     {
/* 318 */       if (reader == null)
/*     */       {
/* 322 */         this.atInfos = new AttributeInfo[0];
/*     */       }
/*     */       else
/*     */       {
/* 326 */         int index = 0;
/* 327 */         int namespaceCount = reader.getNamespaceCount();
/* 328 */         int attributeCount = reader.getAttributeCount();
/* 329 */         this.atInfos = new AttributeInfo[namespaceCount + attributeCount];
/* 330 */         for (int i = 0; i < namespaceCount; i++) {
/* 331 */           String namespacePrefix = reader.getNamespacePrefix(i);
/*     */ 
/* 334 */           if (namespacePrefix == null) {
/* 335 */             namespacePrefix = "";
/*     */           }
/* 337 */           this.atInfos[(index++)] = new AttributeInfo(new QName("http://www.w3.org/2000/xmlns/", namespacePrefix, "xmlns"), reader.getNamespaceURI(i));
/*     */         }
/*     */ 
/* 343 */         for (int i = 0; i < attributeCount; i++)
/* 344 */           this.atInfos[(index++)] = new AttributeInfo(reader.getAttributeName(i), reader.getAttributeValue(i));
/*     */       }
/*     */     }
/*     */ 
/*     */     public int getLength()
/*     */     {
/* 352 */       return this.atInfos.length;
/*     */     }
/*     */ 
/*     */     public String getLocalName(int index) {
/* 356 */       if ((index >= 0) && (index < this.atInfos.length)) {
/* 357 */         return this.atInfos[index].getLocalName();
/*     */       }
/* 359 */       return null;
/*     */     }
/*     */ 
/*     */     public QName getName(int index) {
/* 363 */       if ((index >= 0) && (index < this.atInfos.length)) {
/* 364 */         return this.atInfos[index].getName();
/*     */       }
/* 366 */       return null;
/*     */     }
/*     */ 
/*     */     public String getPrefix(int index) {
/* 370 */       if ((index >= 0) && (index < this.atInfos.length)) {
/* 371 */         return this.atInfos[index].getName().getPrefix();
/*     */       }
/* 373 */       return null;
/*     */     }
/*     */ 
/*     */     public String getURI(int index) {
/* 377 */       if ((index >= 0) && (index < this.atInfos.length)) {
/* 378 */         return this.atInfos[index].getName().getNamespaceURI();
/*     */       }
/* 380 */       return null;
/*     */     }
/*     */ 
/*     */     public String getValue(int index) {
/* 384 */       if ((index >= 0) && (index < this.atInfos.length)) {
/* 385 */         return this.atInfos[index].getValue();
/*     */       }
/* 387 */       return null;
/*     */     }
/*     */ 
/*     */     public String getValue(QName name) {
/* 391 */       int index = getIndex(name);
/* 392 */       if (index != -1) {
/* 393 */         return this.atInfos[index].getValue();
/*     */       }
/* 395 */       return null;
/*     */     }
/*     */ 
/*     */     public String getValue(String localName) {
/* 399 */       int index = getIndex(localName);
/* 400 */       if (index != -1) {
/* 401 */         return this.atInfos[index].getValue();
/*     */       }
/* 403 */       return null;
/*     */     }
/*     */ 
/*     */     public String getValue(String uri, String localName) {
/* 407 */       int index = getIndex(uri, localName);
/* 408 */       if (index != -1) {
/* 409 */         return this.atInfos[index].getValue();
/*     */       }
/* 411 */       return null;
/*     */     }
/*     */ 
/*     */     public boolean isNamespaceDeclaration(int index) {
/* 415 */       if ((index >= 0) && (index < this.atInfos.length)) {
/* 416 */         return this.atInfos[index].isNamespaceDeclaration();
/*     */       }
/* 418 */       return false;
/*     */     }
/*     */ 
/*     */     public int getIndex(QName name) {
/* 422 */       for (int i = 0; i < this.atInfos.length; i++) {
/* 423 */         if (this.atInfos[i].getName().equals(name)) {
/* 424 */           return i;
/*     */         }
/*     */       }
/* 427 */       return -1;
/*     */     }
/*     */ 
/*     */     public int getIndex(String localName) {
/* 431 */       for (int i = 0; i < this.atInfos.length; i++) {
/* 432 */         if (this.atInfos[i].getName().getLocalPart().equals(localName)) {
/* 433 */           return i;
/*     */         }
/*     */       }
/* 436 */       return -1;
/*     */     }
/*     */ 
/*     */     public int getIndex(String uri, String localName)
/*     */     {
/* 441 */       for (int i = 0; i < this.atInfos.length; i++) {
/* 442 */         QName qName = this.atInfos[i].getName();
/* 443 */         if ((qName.getNamespaceURI().equals(uri)) && (qName.getLocalPart().equals(localName)))
/*     */         {
/* 446 */           return i;
/*     */         }
/*     */       }
/* 449 */       return -1;
/*     */     }
/*     */ 
/*     */     static class AttributeInfo
/*     */     {
/*     */       private QName name;
/*     */       private String value;
/*     */ 
/*     */       public AttributeInfo(QName name, String value)
/*     */       {
/* 275 */         this.name = name;
/* 276 */         if (value == null)
/*     */         {
/* 278 */           this.value = "";
/*     */         }
/* 280 */         else this.value = value;
/*     */       }
/*     */ 
/*     */       QName getName()
/*     */       {
/* 285 */         return this.name;
/*     */       }
/*     */ 
/*     */       String getValue() {
/* 289 */         return this.value;
/*     */       }
/*     */ 
/*     */       String getLocalName()
/*     */       {
/* 296 */         if (isNamespaceDeclaration()) {
/* 297 */           if (this.name.getLocalPart().equals("")) {
/* 298 */             return "xmlns";
/*     */           }
/* 300 */           return "xmlns:" + this.name.getLocalPart();
/*     */         }
/* 302 */         return this.name.getLocalPart();
/*     */       }
/*     */ 
/*     */       boolean isNamespaceDeclaration() {
/* 306 */         return this.name.getNamespaceURI() == "http://www.w3.org/2000/xmlns/";
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.streaming.XMLStreamReaderUtil
 * JD-Core Version:    0.6.2
 */