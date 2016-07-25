/*     */ package com.sun.xml.internal.stream.dtd.nonvalidating;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.util.SymbolTable;
/*     */ import com.sun.org.apache.xerces.internal.util.XMLSymbols;
/*     */ import com.sun.org.apache.xerces.internal.xni.Augmentations;
/*     */ import com.sun.org.apache.xerces.internal.xni.QName;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLLocator;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLString;
/*     */ import com.sun.org.apache.xerces.internal.xni.XNIException;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLDTDContentModelSource;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLDTDSource;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Hashtable;
/*     */ import java.util.List;
/*     */ 
/*     */ public class DTDGrammar
/*     */ {
/*     */   public static final int TOP_LEVEL_SCOPE = -1;
/*     */   private static final int CHUNK_SHIFT = 8;
/*     */   private static final int CHUNK_SIZE = 256;
/*     */   private static final int CHUNK_MASK = 255;
/*     */   private static final int INITIAL_CHUNK_COUNT = 4;
/*     */   private static final short LIST_FLAG = 128;
/*     */   private static final short LIST_MASK = -129;
/*     */   private static final boolean DEBUG = false;
/*  84 */   protected XMLDTDSource fDTDSource = null;
/*  85 */   protected XMLDTDContentModelSource fDTDContentModelSource = null;
/*     */   protected int fCurrentElementIndex;
/*     */   protected int fCurrentAttributeIndex;
/*  94 */   protected boolean fReadingExternalDTD = false;
/*     */   private SymbolTable fSymbolTable;
/*  98 */   private ArrayList notationDecls = new ArrayList();
/*     */ 
/* 103 */   private int fElementDeclCount = 0;
/*     */ 
/* 106 */   private QName[][] fElementDeclName = new QName[4][];
/*     */ 
/* 112 */   private short[][] fElementDeclType = new short[4][];
/*     */ 
/* 116 */   private int[][] fElementDeclFirstAttributeDeclIndex = new int[4][];
/*     */ 
/* 119 */   private int[][] fElementDeclLastAttributeDeclIndex = new int[4][];
/*     */ 
/* 124 */   private int fAttributeDeclCount = 0;
/*     */ 
/* 127 */   private QName[][] fAttributeDeclName = new QName[4][];
/*     */ 
/* 133 */   private short[][] fAttributeDeclType = new short[4][];
/*     */ 
/* 136 */   private String[][][] fAttributeDeclEnumeration = new String[4][][];
/* 137 */   private short[][] fAttributeDeclDefaultType = new short[4][];
/* 138 */   private String[][] fAttributeDeclDefaultValue = new String[4][];
/* 139 */   private String[][] fAttributeDeclNonNormalizedDefaultValue = new String[4][];
/* 140 */   private int[][] fAttributeDeclNextAttributeDeclIndex = new int[4][];
/*     */ 
/* 143 */   private QNameHashtable fElementIndexMap = new QNameHashtable();
/*     */ 
/* 146 */   private QName fQName = new QName();
/*     */ 
/* 149 */   protected XMLAttributeDecl fAttributeDecl = new XMLAttributeDecl();
/*     */ 
/* 152 */   private XMLElementDecl fElementDecl = new XMLElementDecl();
/*     */ 
/* 155 */   private XMLSimpleType fSimpleType = new XMLSimpleType();
/*     */ 
/* 159 */   Hashtable fElementDeclTab = new Hashtable();
/*     */ 
/*     */   public DTDGrammar(SymbolTable symbolTable)
/*     */   {
/* 163 */     this.fSymbolTable = symbolTable;
/*     */   }
/*     */ 
/*     */   public int getAttributeDeclIndex(int elementDeclIndex, String attributeDeclName) {
/* 167 */     if (elementDeclIndex == -1) {
/* 168 */       return -1;
/*     */     }
/* 170 */     int attDefIndex = getFirstAttributeDeclIndex(elementDeclIndex);
/* 171 */     while (attDefIndex != -1) {
/* 172 */       getAttributeDecl(attDefIndex, this.fAttributeDecl);
/*     */ 
/* 174 */       if ((this.fAttributeDecl.name.rawname == attributeDeclName) || (attributeDeclName.equals(this.fAttributeDecl.name.rawname)))
/*     */       {
/* 176 */         return attDefIndex;
/*     */       }
/* 178 */       attDefIndex = getNextAttributeDeclIndex(attDefIndex);
/*     */     }
/* 180 */     return -1;
/*     */   }
/*     */ 
/*     */   public void startDTD(XMLLocator locator, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void elementDecl(String name, String contentModel, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/* 216 */     XMLElementDecl tmpElementDecl = (XMLElementDecl)this.fElementDeclTab.get(name);
/* 217 */     if (tmpElementDecl != null) {
/* 218 */       if (tmpElementDecl.type == -1) {
/* 219 */         this.fCurrentElementIndex = getElementDeclIndex(name);
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 227 */       this.fCurrentElementIndex = createElementDecl();
/*     */     }
/*     */ 
/* 230 */     XMLElementDecl elementDecl = new XMLElementDecl();
/* 231 */     QName elementName = new QName(null, name, name, null);
/*     */ 
/* 233 */     elementDecl.name.setValues(elementName);
/* 234 */     elementDecl.scope = -1;
/* 235 */     if (contentModel.equals("EMPTY")) {
/* 236 */       elementDecl.type = 1;
/*     */     }
/* 238 */     else if (contentModel.equals("ANY")) {
/* 239 */       elementDecl.type = 0;
/*     */     }
/* 241 */     else if (contentModel.startsWith("(")) {
/* 242 */       if (contentModel.indexOf("#PCDATA") > 0) {
/* 243 */         elementDecl.type = 2;
/*     */       }
/*     */       else {
/* 246 */         elementDecl.type = 3;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 252 */     this.fElementDeclTab.put(name, elementDecl);
/*     */ 
/* 254 */     this.fElementDecl = elementDecl;
/*     */ 
/* 262 */     setElementDecl(this.fCurrentElementIndex, this.fElementDecl);
/*     */ 
/* 264 */     int chunk = this.fCurrentElementIndex >> 8;
/* 265 */     ensureElementDeclCapacity(chunk);
/*     */   }
/*     */ 
/*     */   public void attributeDecl(String elementName, String attributeName, String type, String[] enumeration, String defaultType, XMLString defaultValue, XMLString nonNormalizedDefaultValue, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/* 298 */     if ((type != XMLSymbols.fCDATASymbol) && (defaultValue != null)) {
/* 299 */       normalizeDefaultAttrValue(defaultValue);
/*     */     }
/*     */ 
/* 302 */     if (!this.fElementDeclTab.containsKey(elementName))
/*     */     {
/* 308 */       this.fCurrentElementIndex = createElementDecl();
/*     */ 
/* 310 */       XMLElementDecl elementDecl = new XMLElementDecl();
/* 311 */       elementDecl.name.setValues(null, elementName, elementName, null);
/*     */ 
/* 313 */       elementDecl.scope = -1;
/*     */ 
/* 316 */       this.fElementDeclTab.put(elementName, elementDecl);
/*     */ 
/* 319 */       setElementDecl(this.fCurrentElementIndex, elementDecl);
/*     */     }
/*     */ 
/* 323 */     int elementIndex = getElementDeclIndex(elementName);
/*     */ 
/* 327 */     if (getAttributeDeclIndex(elementIndex, attributeName) != -1) {
/* 328 */       return;
/*     */     }
/*     */ 
/* 331 */     this.fCurrentAttributeIndex = createAttributeDecl();
/*     */ 
/* 333 */     this.fSimpleType.clear();
/* 334 */     if (defaultType != null) {
/* 335 */       if (defaultType.equals("#FIXED"))
/* 336 */         this.fSimpleType.defaultType = 1;
/* 337 */       else if (defaultType.equals("#IMPLIED"))
/* 338 */         this.fSimpleType.defaultType = 0;
/* 339 */       else if (defaultType.equals("#REQUIRED")) {
/* 340 */         this.fSimpleType.defaultType = 2;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 346 */     this.fSimpleType.defaultValue = (defaultValue != null ? defaultValue.toString() : null);
/* 347 */     this.fSimpleType.nonNormalizedDefaultValue = (nonNormalizedDefaultValue != null ? nonNormalizedDefaultValue.toString() : null);
/* 348 */     this.fSimpleType.enumeration = enumeration;
/*     */ 
/* 350 */     if (type.equals("CDATA")) {
/* 351 */       this.fSimpleType.type = 0;
/*     */     }
/* 353 */     else if (type.equals("ID")) {
/* 354 */       this.fSimpleType.type = 3;
/*     */     }
/* 356 */     else if (type.startsWith("IDREF")) {
/* 357 */       this.fSimpleType.type = 4;
/* 358 */       if (type.indexOf("S") > 0) {
/* 359 */         this.fSimpleType.list = true;
/*     */       }
/*     */     }
/* 362 */     else if (type.equals("ENTITIES")) {
/* 363 */       this.fSimpleType.type = 1;
/* 364 */       this.fSimpleType.list = true;
/*     */     }
/* 366 */     else if (type.equals("ENTITY")) {
/* 367 */       this.fSimpleType.type = 1;
/*     */     }
/* 369 */     else if (type.equals("NMTOKENS")) {
/* 370 */       this.fSimpleType.type = 5;
/* 371 */       this.fSimpleType.list = true;
/*     */     }
/* 373 */     else if (type.equals("NMTOKEN")) {
/* 374 */       this.fSimpleType.type = 5;
/*     */     }
/* 376 */     else if (type.startsWith("NOTATION")) {
/* 377 */       this.fSimpleType.type = 6;
/*     */     }
/* 379 */     else if (type.startsWith("ENUMERATION")) {
/* 380 */       this.fSimpleType.type = 2;
/*     */     }
/*     */     else
/*     */     {
/* 384 */       System.err.println("!!! unknown attribute type " + type);
/*     */     }
/*     */ 
/* 390 */     this.fQName.setValues(null, attributeName, attributeName, null);
/* 391 */     this.fAttributeDecl.setValues(this.fQName, this.fSimpleType, false);
/*     */ 
/* 393 */     setAttributeDecl(elementIndex, this.fCurrentAttributeIndex, this.fAttributeDecl);
/*     */ 
/* 395 */     int chunk = this.fCurrentAttributeIndex >> 8;
/* 396 */     ensureAttributeDeclCapacity(chunk);
/*     */   }
/*     */ 
/*     */   public SymbolTable getSymbolTable()
/*     */   {
/* 401 */     return this.fSymbolTable;
/*     */   }
/*     */ 
/*     */   public int getFirstElementDeclIndex()
/*     */   {
/* 412 */     return this.fElementDeclCount >= 0 ? 0 : -1;
/*     */   }
/*     */ 
/*     */   public int getNextElementDeclIndex(int elementDeclIndex)
/*     */   {
/* 422 */     return elementDeclIndex < this.fElementDeclCount - 1 ? elementDeclIndex + 1 : -1;
/*     */   }
/*     */ 
/*     */   public int getElementDeclIndex(String elementDeclName)
/*     */   {
/* 434 */     int mapping = this.fElementIndexMap.get(elementDeclName);
/*     */ 
/* 436 */     return mapping;
/*     */   }
/*     */ 
/*     */   public int getElementDeclIndex(QName elementDeclQName)
/*     */   {
/* 443 */     return getElementDeclIndex(elementDeclQName.rawname);
/*     */   }
/*     */ 
/*     */   public short getContentSpecType(int elementIndex)
/*     */   {
/* 451 */     if ((elementIndex < 0) || (elementIndex >= this.fElementDeclCount)) {
/* 452 */       return -1;
/*     */     }
/*     */ 
/* 455 */     int chunk = elementIndex >> 8;
/* 456 */     int index = elementIndex & 0xFF;
/*     */ 
/* 458 */     if (this.fElementDeclType[chunk][index] == -1) {
/* 459 */       return -1;
/*     */     }
/*     */ 
/* 462 */     return (short)(this.fElementDeclType[chunk][index] & 0xFFFFFF7F);
/*     */   }
/*     */ 
/*     */   public boolean getElementDecl(int elementDeclIndex, XMLElementDecl elementDecl)
/*     */   {
/* 477 */     if ((elementDeclIndex < 0) || (elementDeclIndex >= this.fElementDeclCount)) {
/* 478 */       return false;
/*     */     }
/*     */ 
/* 481 */     int chunk = elementDeclIndex >> 8;
/* 482 */     int index = elementDeclIndex & 0xFF;
/*     */ 
/* 484 */     elementDecl.name.setValues(this.fElementDeclName[chunk][index]);
/*     */ 
/* 486 */     if (this.fElementDeclType[chunk][index] == -1) {
/* 487 */       elementDecl.type = -1;
/* 488 */       elementDecl.simpleType.list = false;
/*     */     } else {
/* 490 */       elementDecl.type = ((short)(this.fElementDeclType[chunk][index] & 0xFFFFFF7F));
/* 491 */       elementDecl.simpleType.list = ((this.fElementDeclType[chunk][index] & 0x80) != 0);
/*     */     }
/*     */ 
/* 494 */     elementDecl.simpleType.defaultType = -1;
/* 495 */     elementDecl.simpleType.defaultValue = null;
/* 496 */     return true;
/*     */   }
/*     */ 
/*     */   public int getFirstAttributeDeclIndex(int elementDeclIndex)
/*     */   {
/* 510 */     int chunk = elementDeclIndex >> 8;
/* 511 */     int index = elementDeclIndex & 0xFF;
/*     */ 
/* 513 */     return this.fElementDeclFirstAttributeDeclIndex[chunk][index];
/*     */   }
/*     */ 
/*     */   public int getNextAttributeDeclIndex(int attributeDeclIndex)
/*     */   {
/* 524 */     int chunk = attributeDeclIndex >> 8;
/* 525 */     int index = attributeDeclIndex & 0xFF;
/*     */ 
/* 527 */     return this.fAttributeDeclNextAttributeDeclIndex[chunk][index];
/*     */   }
/*     */ 
/*     */   public boolean getAttributeDecl(int attributeDeclIndex, XMLAttributeDecl attributeDecl)
/*     */   {
/* 539 */     if ((attributeDeclIndex < 0) || (attributeDeclIndex >= this.fAttributeDeclCount)) {
/* 540 */       return false;
/*     */     }
/* 542 */     int chunk = attributeDeclIndex >> 8;
/* 543 */     int index = attributeDeclIndex & 0xFF;
/*     */ 
/* 545 */     attributeDecl.name.setValues(this.fAttributeDeclName[chunk][index]);
/*     */     boolean isList;
/*     */     short attributeType;
/*     */     boolean isList;
/* 550 */     if (this.fAttributeDeclType[chunk][index] == -1)
/*     */     {
/* 552 */       short attributeType = -1;
/* 553 */       isList = false;
/*     */     } else {
/* 555 */       attributeType = (short)(this.fAttributeDeclType[chunk][index] & 0xFFFFFF7F);
/* 556 */       isList = (this.fAttributeDeclType[chunk][index] & 0x80) != 0;
/*     */     }
/* 558 */     attributeDecl.simpleType.setValues(attributeType, this.fAttributeDeclName[chunk][index].localpart, this.fAttributeDeclEnumeration[chunk][index], isList, this.fAttributeDeclDefaultType[chunk][index], this.fAttributeDeclDefaultValue[chunk][index], this.fAttributeDeclNonNormalizedDefaultValue[chunk][index]);
/*     */ 
/* 563 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean isCDATAAttribute(QName elName, QName atName)
/*     */   {
/* 577 */     int elDeclIdx = getElementDeclIndex(elName);
/* 578 */     if ((getAttributeDecl(elDeclIdx, this.fAttributeDecl)) && (this.fAttributeDecl.simpleType.type != 0))
/*     */     {
/* 580 */       return false;
/*     */     }
/* 582 */     return true;
/*     */   }
/*     */ 
/*     */   public void printElements()
/*     */   {
/* 588 */     int elementDeclIndex = 0;
/* 589 */     XMLElementDecl elementDecl = new XMLElementDecl();
/* 590 */     while (getElementDecl(elementDeclIndex++, elementDecl))
/*     */     {
/* 592 */       System.out.println("element decl: " + elementDecl.name + ", " + elementDecl.name.rawname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void printAttributes(int elementDeclIndex)
/*     */   {
/* 599 */     int attributeDeclIndex = getFirstAttributeDeclIndex(elementDeclIndex);
/* 600 */     System.out.print(elementDeclIndex);
/* 601 */     System.out.print(" [");
/* 602 */     while (attributeDeclIndex != -1) {
/* 603 */       System.out.print(' ');
/* 604 */       System.out.print(attributeDeclIndex);
/* 605 */       printAttribute(attributeDeclIndex);
/* 606 */       attributeDeclIndex = getNextAttributeDeclIndex(attributeDeclIndex);
/* 607 */       if (attributeDeclIndex != -1) {
/* 608 */         System.out.print(",");
/*     */       }
/*     */     }
/* 611 */     System.out.println(" ]");
/*     */   }
/*     */ 
/*     */   protected int createElementDecl()
/*     */   {
/* 616 */     int chunk = this.fElementDeclCount >> 8;
/* 617 */     int index = this.fElementDeclCount & 0xFF;
/* 618 */     ensureElementDeclCapacity(chunk);
/* 619 */     this.fElementDeclName[chunk][index] = new QName();
/* 620 */     this.fElementDeclType[chunk][index] = -1;
/* 621 */     this.fElementDeclFirstAttributeDeclIndex[chunk][index] = -1;
/* 622 */     this.fElementDeclLastAttributeDeclIndex[chunk][index] = -1;
/* 623 */     return this.fElementDeclCount++;
/*     */   }
/*     */ 
/*     */   protected void setElementDecl(int elementDeclIndex, XMLElementDecl elementDecl) {
/* 627 */     if ((elementDeclIndex < 0) || (elementDeclIndex >= this.fElementDeclCount)) {
/* 628 */       return;
/*     */     }
/* 630 */     int chunk = elementDeclIndex >> 8;
/* 631 */     int index = elementDeclIndex & 0xFF;
/*     */ 
/* 633 */     int scope = elementDecl.scope;
/*     */ 
/* 636 */     this.fElementDeclName[chunk][index].setValues(elementDecl.name);
/* 637 */     this.fElementDeclType[chunk][index] = elementDecl.type;
/*     */ 
/* 641 */     if (elementDecl.simpleType.list == true)
/*     */     {
/*     */       int tmp79_77 = index;
/*     */       short[] tmp79_76 = this.fElementDeclType[chunk]; tmp79_76[tmp79_77] = ((short)(tmp79_76[tmp79_77] | 0x80));
/*     */     }
/*     */ 
/* 645 */     this.fElementIndexMap.put(elementDecl.name.rawname, elementDeclIndex);
/*     */   }
/*     */ 
/*     */   protected void setFirstAttributeDeclIndex(int elementDeclIndex, int newFirstAttrIndex)
/*     */   {
/* 653 */     if ((elementDeclIndex < 0) || (elementDeclIndex >= this.fElementDeclCount)) {
/* 654 */       return;
/*     */     }
/*     */ 
/* 657 */     int chunk = elementDeclIndex >> 8;
/* 658 */     int index = elementDeclIndex & 0xFF;
/*     */ 
/* 660 */     this.fElementDeclFirstAttributeDeclIndex[chunk][index] = newFirstAttrIndex;
/*     */   }
/*     */ 
/*     */   protected int createAttributeDecl()
/*     */   {
/* 665 */     int chunk = this.fAttributeDeclCount >> 8;
/* 666 */     int index = this.fAttributeDeclCount & 0xFF;
/*     */ 
/* 668 */     ensureAttributeDeclCapacity(chunk);
/* 669 */     this.fAttributeDeclName[chunk][index] = new QName();
/* 670 */     this.fAttributeDeclType[chunk][index] = -1;
/* 671 */     this.fAttributeDeclEnumeration[chunk][index] = null;
/* 672 */     this.fAttributeDeclDefaultType[chunk][index] = 0;
/* 673 */     this.fAttributeDeclDefaultValue[chunk][index] = null;
/* 674 */     this.fAttributeDeclNonNormalizedDefaultValue[chunk][index] = null;
/* 675 */     this.fAttributeDeclNextAttributeDeclIndex[chunk][index] = -1;
/* 676 */     return this.fAttributeDeclCount++;
/*     */   }
/*     */ 
/*     */   protected void setAttributeDecl(int elementDeclIndex, int attributeDeclIndex, XMLAttributeDecl attributeDecl)
/*     */   {
/* 682 */     int attrChunk = attributeDeclIndex >> 8;
/* 683 */     int attrIndex = attributeDeclIndex & 0xFF;
/* 684 */     this.fAttributeDeclName[attrChunk][attrIndex].setValues(attributeDecl.name);
/* 685 */     this.fAttributeDeclType[attrChunk][attrIndex] = attributeDecl.simpleType.type;
/*     */ 
/* 687 */     if (attributeDecl.simpleType.list)
/*     */     {
/*     */       int tmp66_64 = attrIndex;
/*     */       short[] tmp66_63 = this.fAttributeDeclType[attrChunk]; tmp66_63[tmp66_64] = ((short)(tmp66_63[tmp66_64] | 0x80));
/*     */     }
/* 690 */     this.fAttributeDeclEnumeration[attrChunk][attrIndex] = attributeDecl.simpleType.enumeration;
/* 691 */     this.fAttributeDeclDefaultType[attrChunk][attrIndex] = attributeDecl.simpleType.defaultType;
/*     */ 
/* 693 */     this.fAttributeDeclDefaultValue[attrChunk][attrIndex] = attributeDecl.simpleType.defaultValue;
/* 694 */     this.fAttributeDeclNonNormalizedDefaultValue[attrChunk][attrIndex] = attributeDecl.simpleType.nonNormalizedDefaultValue;
/*     */ 
/* 696 */     int elemChunk = elementDeclIndex >> 8;
/* 697 */     int elemIndex = elementDeclIndex & 0xFF;
/* 698 */     int index = this.fElementDeclFirstAttributeDeclIndex[elemChunk][elemIndex];
/* 699 */     while ((index != -1) && 
/* 700 */       (index != attributeDeclIndex))
/*     */     {
/* 703 */       attrChunk = index >> 8;
/* 704 */       attrIndex = index & 0xFF;
/* 705 */       index = this.fAttributeDeclNextAttributeDeclIndex[attrChunk][attrIndex];
/*     */     }
/* 707 */     if (index == -1) {
/* 708 */       if (this.fElementDeclFirstAttributeDeclIndex[elemChunk][elemIndex] == -1) {
/* 709 */         this.fElementDeclFirstAttributeDeclIndex[elemChunk][elemIndex] = attributeDeclIndex;
/*     */       } else {
/* 711 */         index = this.fElementDeclLastAttributeDeclIndex[elemChunk][elemIndex];
/* 712 */         attrChunk = index >> 8;
/* 713 */         attrIndex = index & 0xFF;
/* 714 */         this.fAttributeDeclNextAttributeDeclIndex[attrChunk][attrIndex] = attributeDeclIndex;
/*     */       }
/* 716 */       this.fElementDeclLastAttributeDeclIndex[elemChunk][elemIndex] = attributeDeclIndex;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void notationDecl(String name, XMLResourceIdentifier identifier, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/* 724 */     XMLNotationDecl notationDecl = new XMLNotationDecl();
/* 725 */     notationDecl.setValues(name, identifier.getPublicId(), identifier.getLiteralSystemId(), identifier.getBaseSystemId());
/*     */ 
/* 727 */     this.notationDecls.add(notationDecl);
/*     */   }
/*     */ 
/*     */   public List getNotationDecls() {
/* 731 */     return this.notationDecls;
/*     */   }
/*     */ 
/*     */   private void printAttribute(int attributeDeclIndex)
/*     */   {
/* 739 */     XMLAttributeDecl attributeDecl = new XMLAttributeDecl();
/* 740 */     if (getAttributeDecl(attributeDeclIndex, attributeDecl)) {
/* 741 */       System.out.print(" { ");
/* 742 */       System.out.print(attributeDecl.name.localpart);
/* 743 */       System.out.print(" }");
/*     */     }
/*     */   }
/*     */ 
/*     */   private void ensureElementDeclCapacity(int chunk)
/*     */   {
/* 751 */     if (chunk >= this.fElementDeclName.length)
/*     */     {
/* 753 */       this.fElementDeclName = resize(this.fElementDeclName, this.fElementDeclName.length * 2);
/* 754 */       this.fElementDeclType = resize(this.fElementDeclType, this.fElementDeclType.length * 2);
/* 755 */       this.fElementDeclFirstAttributeDeclIndex = resize(this.fElementDeclFirstAttributeDeclIndex, this.fElementDeclFirstAttributeDeclIndex.length * 2);
/* 756 */       this.fElementDeclLastAttributeDeclIndex = resize(this.fElementDeclLastAttributeDeclIndex, this.fElementDeclLastAttributeDeclIndex.length * 2);
/*     */     }
/* 758 */     else if (this.fElementDeclName[chunk] != null) {
/* 759 */       return;
/*     */     }
/*     */ 
/* 762 */     this.fElementDeclName[chunk] = new QName[256];
/* 763 */     this.fElementDeclType[chunk] = new short[256];
/* 764 */     this.fElementDeclFirstAttributeDeclIndex[chunk] = new int[256];
/* 765 */     this.fElementDeclLastAttributeDeclIndex[chunk] = new int[256];
/*     */   }
/*     */ 
/*     */   private void ensureAttributeDeclCapacity(int chunk)
/*     */   {
/* 771 */     if (chunk >= this.fAttributeDeclName.length) {
/* 772 */       this.fAttributeDeclName = resize(this.fAttributeDeclName, this.fAttributeDeclName.length * 2);
/* 773 */       this.fAttributeDeclType = resize(this.fAttributeDeclType, this.fAttributeDeclType.length * 2);
/* 774 */       this.fAttributeDeclEnumeration = resize(this.fAttributeDeclEnumeration, this.fAttributeDeclEnumeration.length * 2);
/* 775 */       this.fAttributeDeclDefaultType = resize(this.fAttributeDeclDefaultType, this.fAttributeDeclDefaultType.length * 2);
/* 776 */       this.fAttributeDeclDefaultValue = resize(this.fAttributeDeclDefaultValue, this.fAttributeDeclDefaultValue.length * 2);
/* 777 */       this.fAttributeDeclNonNormalizedDefaultValue = resize(this.fAttributeDeclNonNormalizedDefaultValue, this.fAttributeDeclNonNormalizedDefaultValue.length * 2);
/* 778 */       this.fAttributeDeclNextAttributeDeclIndex = resize(this.fAttributeDeclNextAttributeDeclIndex, this.fAttributeDeclNextAttributeDeclIndex.length * 2);
/*     */     }
/* 780 */     else if (this.fAttributeDeclName[chunk] != null) {
/* 781 */       return;
/*     */     }
/*     */ 
/* 784 */     this.fAttributeDeclName[chunk] = new QName[256];
/* 785 */     this.fAttributeDeclType[chunk] = new short[256];
/* 786 */     this.fAttributeDeclEnumeration[chunk] = new String[256][];
/* 787 */     this.fAttributeDeclDefaultType[chunk] = new short[256];
/* 788 */     this.fAttributeDeclDefaultValue[chunk] = new String[256];
/* 789 */     this.fAttributeDeclNonNormalizedDefaultValue[chunk] = new String[256];
/* 790 */     this.fAttributeDeclNextAttributeDeclIndex[chunk] = new int[256];
/*     */   }
/*     */ 
/*     */   private static short[][] resize(short[][] array, int newsize)
/*     */   {
/* 798 */     short[][] newarray = new short[newsize][];
/* 799 */     System.arraycopy(array, 0, newarray, 0, array.length);
/* 800 */     return newarray;
/*     */   }
/*     */ 
/*     */   private static int[][] resize(int[][] array, int newsize) {
/* 804 */     int[][] newarray = new int[newsize][];
/* 805 */     System.arraycopy(array, 0, newarray, 0, array.length);
/* 806 */     return newarray;
/*     */   }
/*     */ 
/*     */   private static QName[][] resize(QName[][] array, int newsize) {
/* 810 */     QName[][] newarray = new QName[newsize][];
/* 811 */     System.arraycopy(array, 0, newarray, 0, array.length);
/* 812 */     return newarray;
/*     */   }
/*     */ 
/*     */   private static String[][] resize(String[][] array, int newsize) {
/* 816 */     String[][] newarray = new String[newsize][];
/* 817 */     System.arraycopy(array, 0, newarray, 0, array.length);
/* 818 */     return newarray;
/*     */   }
/*     */ 
/*     */   private static String[][][] resize(String[][][] array, int newsize) {
/* 822 */     String[][][] newarray = new String[newsize][][];
/* 823 */     System.arraycopy(array, 0, newarray, 0, array.length);
/* 824 */     return newarray;
/*     */   }
/*     */ 
/*     */   private boolean normalizeDefaultAttrValue(XMLString value)
/*     */   {
/* 957 */     int oldLength = value.length;
/*     */ 
/* 959 */     boolean skipSpace = true;
/* 960 */     int current = value.offset;
/* 961 */     int end = value.offset + value.length;
/* 962 */     for (int i = value.offset; i < end; i++) {
/* 963 */       if (value.ch[i] == ' ') {
/* 964 */         if (!skipSpace)
/*     */         {
/* 966 */           value.ch[(current++)] = ' ';
/* 967 */           skipSpace = true;
/*     */         }
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 975 */         if (current != i) {
/* 976 */           value.ch[current] = value.ch[i];
/*     */         }
/* 978 */         current++;
/* 979 */         skipSpace = false;
/*     */       }
/*     */     }
/* 982 */     if (current != end) {
/* 983 */       if (skipSpace)
/*     */       {
/* 985 */         current--;
/*     */       }
/*     */ 
/* 988 */       value.length = (current - value.offset);
/* 989 */       return true;
/*     */     }
/* 991 */     return false;
/*     */   }
/*     */ 
/*     */   public void endDTD(Augmentations augs)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   protected static final class QNameHashtable
/*     */   {
/*     */     public static final boolean UNIQUE_STRINGS = true;
/*     */     private static final int INITIAL_BUCKET_SIZE = 4;
/*     */     private static final int HASHTABLE_SIZE = 101;
/* 857 */     private Object[][] fHashTable = new Object[101][];
/*     */ 
/*     */     public void put(String key, int value)
/*     */     {
/* 866 */       int hash = (hash(key) + 2) % 101;
/* 867 */       Object[] bucket = this.fHashTable[hash];
/*     */ 
/* 869 */       if (bucket == null) {
/* 870 */         bucket = new Object[9];
/* 871 */         bucket[0] = { 1 };
/* 872 */         bucket[1] = key;
/* 873 */         bucket[2] = { value };
/* 874 */         this.fHashTable[hash] = bucket;
/*     */       } else {
/* 876 */         int count = ((int[])(int[])bucket[0])[0];
/* 877 */         int offset = 1 + 2 * count;
/* 878 */         if (offset == bucket.length) {
/* 879 */           int newSize = count + 4;
/* 880 */           Object[] newBucket = new Object[1 + 2 * newSize];
/* 881 */           System.arraycopy(bucket, 0, newBucket, 0, offset);
/* 882 */           bucket = newBucket;
/* 883 */           this.fHashTable[hash] = bucket;
/*     */         }
/* 885 */         boolean found = false;
/* 886 */         int j = 1;
/* 887 */         for (int i = 0; i < count; i++) {
/* 888 */           if ((String)bucket[j] == key) {
/* 889 */             ((int[])bucket[(j + 1)])[0] = value;
/* 890 */             found = true;
/* 891 */             break;
/*     */           }
/* 893 */           j += 2;
/*     */         }
/* 895 */         if (!found) {
/* 896 */           bucket[(offset++)] = key;
/* 897 */           bucket[offset] = { value };
/* 898 */           ((int[])bucket[0])[0] = (++count);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     public int get(String key)
/*     */     {
/* 909 */       int hash = (hash(key) + 2) % 101;
/* 910 */       Object[] bucket = this.fHashTable[hash];
/*     */ 
/* 912 */       if (bucket == null) {
/* 913 */         return -1;
/*     */       }
/* 915 */       int count = ((int[])(int[])bucket[0])[0];
/*     */ 
/* 917 */       int j = 1;
/* 918 */       for (int i = 0; i < count; i++) {
/* 919 */         if ((String)bucket[j] == key) {
/* 920 */           return ((int[])(int[])bucket[(j + 1)])[0];
/*     */         }
/* 922 */         j += 2;
/*     */       }
/* 924 */       return -1;
/*     */     }
/*     */ 
/*     */     protected int hash(String symbol)
/*     */     {
/* 935 */       if (symbol == null) {
/* 936 */         return 0;
/*     */       }
/* 938 */       int code = 0;
/* 939 */       int length = symbol.length();
/* 940 */       for (int i = 0; i < length; i++) {
/* 941 */         code = code * 37 + symbol.charAt(i);
/*     */       }
/* 943 */       return code & 0x7FFFFFF;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.stream.dtd.nonvalidating.DTDGrammar
 * JD-Core Version:    0.6.2
 */