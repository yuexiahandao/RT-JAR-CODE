/*     */ package com.sun.xml.internal.stream.dtd;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.util.NamespaceSupport;
/*     */ import com.sun.org.apache.xerces.internal.util.SymbolTable;
/*     */ import com.sun.org.apache.xerces.internal.util.XMLChar;
/*     */ import com.sun.org.apache.xerces.internal.util.XMLSymbols;
/*     */ import com.sun.org.apache.xerces.internal.xni.Augmentations;
/*     */ import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
/*     */ import com.sun.org.apache.xerces.internal.xni.QName;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLAttributes;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLString;
/*     */ import com.sun.org.apache.xerces.internal.xni.XNIException;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
/*     */ import com.sun.xml.internal.stream.dtd.nonvalidating.DTDGrammar;
/*     */ import com.sun.xml.internal.stream.dtd.nonvalidating.XMLAttributeDecl;
/*     */ import com.sun.xml.internal.stream.dtd.nonvalidating.XMLSimpleType;
/*     */ 
/*     */ public class DTDGrammarUtil
/*     */ {
/*     */   protected static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
/*     */   protected static final String NAMESPACES = "http://xml.org/sax/features/namespaces";
/*     */   private static final boolean DEBUG_ATTRIBUTES = false;
/*     */   private static final boolean DEBUG_ELEMENT_CHILDREN = false;
/*  70 */   protected DTDGrammar fDTDGrammar = null;
/*     */   protected boolean fNamespaces;
/*  75 */   protected SymbolTable fSymbolTable = null;
/*     */ 
/*  78 */   private int fCurrentElementIndex = -1;
/*     */ 
/*  81 */   private int fCurrentContentSpecType = -1;
/*     */ 
/*  84 */   private boolean[] fElementContentState = new boolean[8];
/*     */ 
/*  87 */   private int fElementDepth = -1;
/*     */ 
/*  90 */   private boolean fInElementContent = false;
/*     */ 
/*  93 */   private XMLAttributeDecl fTempAttDecl = new XMLAttributeDecl();
/*     */ 
/*  96 */   private QName fTempQName = new QName();
/*     */ 
/*  99 */   private StringBuffer fBuffer = new StringBuffer();
/*     */ 
/* 101 */   private NamespaceContext fNamespaceContext = null;
/*     */ 
/*     */   public DTDGrammarUtil(SymbolTable symbolTable)
/*     */   {
/* 105 */     this.fSymbolTable = symbolTable;
/*     */   }
/*     */ 
/*     */   public DTDGrammarUtil(DTDGrammar grammar, SymbolTable symbolTable) {
/* 109 */     this.fDTDGrammar = grammar;
/* 110 */     this.fSymbolTable = symbolTable;
/*     */   }
/*     */ 
/*     */   public DTDGrammarUtil(DTDGrammar grammar, SymbolTable symbolTable, NamespaceContext namespaceContext)
/*     */   {
/* 115 */     this.fDTDGrammar = grammar;
/* 116 */     this.fSymbolTable = symbolTable;
/* 117 */     this.fNamespaceContext = namespaceContext;
/*     */   }
/*     */ 
/*     */   public void reset(XMLComponentManager componentManager)
/*     */     throws XMLConfigurationException
/*     */   {
/* 137 */     this.fDTDGrammar = null;
/* 138 */     this.fInElementContent = false;
/* 139 */     this.fCurrentElementIndex = -1;
/* 140 */     this.fCurrentContentSpecType = -1;
/* 141 */     this.fNamespaces = componentManager.getFeature("http://xml.org/sax/features/namespaces", true);
/* 142 */     this.fSymbolTable = ((SymbolTable)componentManager.getProperty("http://apache.org/xml/properties/internal/symbol-table"));
/*     */ 
/* 144 */     this.fElementDepth = -1;
/*     */   }
/*     */ 
/*     */   public void startElement(QName element, XMLAttributes attributes)
/*     */     throws XNIException
/*     */   {
/* 158 */     handleStartElement(element, attributes);
/*     */   }
/*     */ 
/*     */   public void endElement(QName element)
/*     */     throws XNIException
/*     */   {
/* 170 */     handleEndElement(element);
/*     */   }
/*     */ 
/*     */   public void startCDATA(Augmentations augs)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void endCDATA(Augmentations augs)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void addDTDDefaultAttrs(QName elementName, XMLAttributes attributes)
/*     */     throws XNIException
/*     */   {
/* 198 */     int elementIndex = this.fDTDGrammar.getElementDeclIndex(elementName);
/*     */ 
/* 200 */     if ((elementIndex == -1) || (this.fDTDGrammar == null)) {
/* 201 */       return;
/*     */     }
/*     */ 
/* 209 */     int attlistIndex = this.fDTDGrammar.getFirstAttributeDeclIndex(elementIndex);
/*     */ 
/* 211 */     while (attlistIndex != -1)
/*     */     {
/* 213 */       this.fDTDGrammar.getAttributeDecl(attlistIndex, this.fTempAttDecl);
/*     */ 
/* 229 */       String attPrefix = this.fTempAttDecl.name.prefix;
/* 230 */       String attLocalpart = this.fTempAttDecl.name.localpart;
/* 231 */       String attRawName = this.fTempAttDecl.name.rawname;
/* 232 */       String attType = getAttributeTypeName(this.fTempAttDecl);
/* 233 */       int attDefaultType = this.fTempAttDecl.simpleType.defaultType;
/* 234 */       String attValue = null;
/*     */ 
/* 236 */       if (this.fTempAttDecl.simpleType.defaultValue != null) {
/* 237 */         attValue = this.fTempAttDecl.simpleType.defaultValue;
/*     */       }
/* 239 */       boolean specified = false;
/* 240 */       boolean required = attDefaultType == 2;
/* 241 */       boolean cdata = attType == XMLSymbols.fCDATASymbol;
/*     */ 
/* 243 */       if ((!cdata) || (required) || (attValue != null))
/*     */       {
/* 246 */         if ((this.fNamespaceContext != null) && (attRawName.startsWith("xmlns"))) {
/* 247 */           String prefix = "";
/* 248 */           int pos = attRawName.indexOf(':');
/* 249 */           if (pos != -1)
/* 250 */             prefix = attRawName.substring(0, pos);
/*     */           else {
/* 252 */             prefix = attRawName;
/*     */           }
/* 254 */           prefix = this.fSymbolTable.addSymbol(prefix);
/* 255 */           if (!((NamespaceSupport)this.fNamespaceContext).containsPrefixInCurrentContext(prefix))
/*     */           {
/* 258 */             this.fNamespaceContext.declarePrefix(prefix, attValue);
/*     */           }
/* 260 */           specified = true;
/*     */         }
/*     */         else {
/* 263 */           int attrCount = attributes.getLength();
/* 264 */           for (int i = 0; i < attrCount; i++)
/* 265 */             if (attributes.getQName(i) == attRawName) {
/* 266 */               specified = true;
/* 267 */               break;
/*     */             }
/*     */         }
/*     */       }
/*     */       int newAttr;
/* 275 */       if ((!specified) && 
/* 276 */         (attValue != null)) {
/* 277 */         if (this.fNamespaces) {
/* 278 */           int index = attRawName.indexOf(':');
/* 279 */           if (index != -1) {
/* 280 */             attPrefix = attRawName.substring(0, index);
/* 281 */             attPrefix = this.fSymbolTable.addSymbol(attPrefix);
/* 282 */             attLocalpart = attRawName.substring(index + 1);
/* 283 */             attLocalpart = this.fSymbolTable.addSymbol(attLocalpart);
/*     */           }
/*     */         }
/* 286 */         this.fTempQName.setValues(attPrefix, attLocalpart, attRawName, this.fTempAttDecl.name.uri);
/*     */ 
/* 288 */         newAttr = attributes.addAttribute(this.fTempQName, attType, attValue);
/*     */       }
/*     */ 
/* 292 */       attlistIndex = this.fDTDGrammar.getNextAttributeDeclIndex(attlistIndex);
/*     */     }
/*     */ 
/* 299 */     int attrCount = attributes.getLength();
/* 300 */     for (int i = 0; i < attrCount; i++) {
/* 301 */       String attrRawName = attributes.getQName(i);
/* 302 */       boolean declared = false;
/* 303 */       int position = this.fDTDGrammar.getFirstAttributeDeclIndex(elementIndex);
/*     */ 
/* 305 */       while (position != -1) {
/* 306 */         this.fDTDGrammar.getAttributeDecl(position, this.fTempAttDecl);
/* 307 */         if (this.fTempAttDecl.name.rawname == attrRawName)
/*     */         {
/* 309 */           declared = true;
/* 310 */           break;
/*     */         }
/* 312 */         position = this.fDTDGrammar.getNextAttributeDeclIndex(position);
/*     */       }
/* 314 */       if (declared)
/*     */       {
/* 318 */         String type = getAttributeTypeName(this.fTempAttDecl);
/* 319 */         attributes.setType(i, type);
/*     */ 
/* 321 */         boolean changedByNormalization = false;
/* 322 */         if ((attributes.isSpecified(i)) && (type != XMLSymbols.fCDATASymbol))
/* 323 */           changedByNormalization = normalizeAttrValue(attributes, i);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean normalizeAttrValue(XMLAttributes attributes, int index)
/*     */   {
/* 339 */     boolean leadingSpace = true;
/* 340 */     boolean spaceStart = false;
/* 341 */     boolean readingNonSpace = false;
/* 342 */     int count = 0;
/* 343 */     int eaten = 0;
/* 344 */     String attrValue = attributes.getValue(index);
/* 345 */     char[] attValue = new char[attrValue.length()];
/*     */ 
/* 347 */     this.fBuffer.setLength(0);
/* 348 */     attrValue.getChars(0, attrValue.length(), attValue, 0);
/* 349 */     for (int i = 0; i < attValue.length; i++)
/*     */     {
/* 351 */       if (attValue[i] == ' ')
/*     */       {
/* 354 */         if (readingNonSpace) {
/* 355 */           spaceStart = true;
/* 356 */           readingNonSpace = false;
/*     */         }
/*     */ 
/* 359 */         if ((spaceStart) && (!leadingSpace)) {
/* 360 */           spaceStart = false;
/* 361 */           this.fBuffer.append(attValue[i]);
/* 362 */           count++;
/*     */         }
/* 364 */         else if ((leadingSpace) || (!spaceStart)) {
/* 365 */           eaten++;
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 370 */         readingNonSpace = true;
/* 371 */         spaceStart = false;
/* 372 */         leadingSpace = false;
/* 373 */         this.fBuffer.append(attValue[i]);
/* 374 */         count++;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 379 */     if ((count > 0) && (this.fBuffer.charAt(count - 1) == ' ')) {
/* 380 */       this.fBuffer.setLength(count - 1);
/*     */     }
/*     */ 
/* 383 */     String newValue = this.fBuffer.toString();
/* 384 */     attributes.setValue(index, newValue);
/* 385 */     return !attrValue.equals(newValue);
/*     */   }
/*     */ 
/*     */   private String getAttributeTypeName(XMLAttributeDecl attrDecl)
/*     */   {
/* 393 */     switch (attrDecl.simpleType.type) {
/*     */     case 1:
/* 395 */       return attrDecl.simpleType.list ? XMLSymbols.fENTITIESSymbol : XMLSymbols.fENTITYSymbol;
/*     */     case 2:
/* 399 */       StringBuffer buffer = new StringBuffer();
/* 400 */       buffer.append('(');
/* 401 */       for (int i = 0; i < attrDecl.simpleType.enumeration.length; i++) {
/* 402 */         if (i > 0) {
/* 403 */           buffer.append("|");
/*     */         }
/* 405 */         buffer.append(attrDecl.simpleType.enumeration[i]);
/*     */       }
/* 407 */       buffer.append(')');
/* 408 */       return this.fSymbolTable.addSymbol(buffer.toString());
/*     */     case 3:
/* 411 */       return XMLSymbols.fIDSymbol;
/*     */     case 4:
/* 414 */       return attrDecl.simpleType.list ? XMLSymbols.fIDREFSSymbol : XMLSymbols.fIDREFSymbol;
/*     */     case 5:
/* 418 */       return attrDecl.simpleType.list ? XMLSymbols.fNMTOKENSSymbol : XMLSymbols.fNMTOKENSymbol;
/*     */     case 6:
/* 422 */       return XMLSymbols.fNOTATIONSymbol;
/*     */     }
/*     */ 
/* 425 */     return XMLSymbols.fCDATASymbol;
/*     */   }
/*     */ 
/*     */   private void ensureStackCapacity(int newElementDepth)
/*     */   {
/* 432 */     if (newElementDepth == this.fElementContentState.length) {
/* 433 */       boolean[] newStack = new boolean[newElementDepth * 2];
/* 434 */       System.arraycopy(this.fElementContentState, 0, newStack, 0, newElementDepth);
/*     */ 
/* 436 */       this.fElementContentState = newStack;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void handleStartElement(QName element, XMLAttributes attributes)
/*     */     throws XNIException
/*     */   {
/* 447 */     if (this.fDTDGrammar == null) {
/* 448 */       this.fCurrentElementIndex = -1;
/* 449 */       this.fCurrentContentSpecType = -1;
/* 450 */       this.fInElementContent = false;
/* 451 */       return;
/*     */     }
/* 453 */     this.fCurrentElementIndex = this.fDTDGrammar.getElementDeclIndex(element);
/* 454 */     this.fCurrentContentSpecType = this.fDTDGrammar.getContentSpecType(this.fCurrentElementIndex);
/*     */ 
/* 457 */     addDTDDefaultAttrs(element, attributes);
/*     */ 
/* 460 */     this.fInElementContent = (this.fCurrentContentSpecType == 3);
/* 461 */     this.fElementDepth += 1;
/* 462 */     ensureStackCapacity(this.fElementDepth);
/* 463 */     this.fElementContentState[this.fElementDepth] = this.fInElementContent;
/*     */   }
/*     */ 
/*     */   protected void handleEndElement(QName element)
/*     */     throws XNIException
/*     */   {
/* 469 */     if (this.fDTDGrammar == null) return;
/* 470 */     this.fElementDepth -= 1;
/* 471 */     if (this.fElementDepth < -1) {
/* 472 */       throw new RuntimeException("FWK008 Element stack underflow");
/*     */     }
/* 474 */     if (this.fElementDepth < 0) {
/* 475 */       this.fCurrentElementIndex = -1;
/* 476 */       this.fCurrentContentSpecType = -1;
/* 477 */       this.fInElementContent = false;
/* 478 */       return;
/*     */     }
/* 480 */     this.fInElementContent = this.fElementContentState[this.fElementDepth];
/*     */   }
/*     */ 
/*     */   public boolean isInElementContent() {
/* 484 */     return this.fInElementContent;
/*     */   }
/*     */ 
/*     */   public boolean isIgnorableWhiteSpace(XMLString text) {
/* 488 */     if (isInElementContent()) {
/* 489 */       for (int i = text.offset; i < text.offset + text.length; i++) {
/* 490 */         if (!XMLChar.isSpace(text.ch[i])) {
/* 491 */           return false;
/*     */         }
/*     */       }
/* 494 */       return true;
/*     */     }
/* 496 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.stream.dtd.DTDGrammarUtil
 * JD-Core Version:    0.6.2
 */