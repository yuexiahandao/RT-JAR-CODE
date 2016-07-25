/*     */ package sun.org.mozilla.javascript.internal.xmlimpl;
/*     */ 
/*     */ import org.w3c.dom.Node;
/*     */ import sun.org.mozilla.javascript.internal.Context;
/*     */ import sun.org.mozilla.javascript.internal.ScriptRuntime;
/*     */ import sun.org.mozilla.javascript.internal.Scriptable;
/*     */ import sun.org.mozilla.javascript.internal.Undefined;
/*     */ import sun.org.mozilla.javascript.internal.xml.XMLObject;
/*     */ 
/*     */ class XML extends XMLObjectImpl
/*     */ {
/*     */   static final long serialVersionUID = -630969919086449092L;
/*     */   private XmlNode node;
/*     */ 
/*     */   XML(XMLLibImpl paramXMLLibImpl, Scriptable paramScriptable, XMLObject paramXMLObject, XmlNode paramXmlNode)
/*     */   {
/*  53 */     super(paramXMLLibImpl, paramScriptable, paramXMLObject);
/*  54 */     initialize(paramXmlNode);
/*     */   }
/*     */ 
/*     */   void initialize(XmlNode paramXmlNode) {
/*  58 */     this.node = paramXmlNode;
/*  59 */     this.node.setXml(this);
/*     */   }
/*     */ 
/*     */   final XML getXML()
/*     */   {
/*  64 */     return this;
/*     */   }
/*     */ 
/*     */   void replaceWith(XML paramXML)
/*     */   {
/*  71 */     if (this.node.parent() != null)
/*  72 */       this.node.replaceWith(paramXML.node);
/*     */     else
/*  74 */       initialize(paramXML.node);
/*     */   }
/*     */ 
/*     */   XML makeXmlFromString(XMLName paramXMLName, String paramString)
/*     */   {
/*     */     try
/*     */     {
/*  81 */       return newTextElementXML(this.node, paramXMLName.toQname(), paramString);
/*     */     } catch (Exception localException) {
/*  83 */       throw ScriptRuntime.typeError(localException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   XmlNode getAnnotation()
/*     */   {
/*  89 */     return this.node;
/*     */   }
/*     */ 
/*     */   public Object get(int paramInt, Scriptable paramScriptable)
/*     */   {
/* 100 */     if (paramInt == 0) {
/* 101 */       return this;
/*     */     }
/* 103 */     return Scriptable.NOT_FOUND;
/*     */   }
/*     */ 
/*     */   public boolean has(int paramInt, Scriptable paramScriptable)
/*     */   {
/* 109 */     return paramInt == 0;
/*     */   }
/*     */ 
/*     */   public void put(int paramInt, Scriptable paramScriptable, Object paramObject)
/*     */   {
/* 116 */     throw ScriptRuntime.typeError("Assignment to indexed XML is not allowed");
/*     */   }
/*     */ 
/*     */   public Object[] getIds()
/*     */   {
/* 121 */     if (isPrototype()) {
/* 122 */       return new Object[0];
/*     */     }
/* 124 */     return new Object[] { Integer.valueOf(0) };
/*     */   }
/*     */ 
/*     */   public void delete(int paramInt)
/*     */   {
/* 131 */     if (paramInt == 0)
/* 132 */       remove();
/*     */   }
/*     */ 
/*     */   boolean hasXMLProperty(XMLName paramXMLName)
/*     */   {
/* 142 */     if (isPrototype()) {
/* 143 */       return getMethod(paramXMLName.localName()) != NOT_FOUND;
/*     */     }
/* 145 */     return (getPropertyList(paramXMLName).length() > 0) || (getMethod(paramXMLName.localName()) != NOT_FOUND);
/*     */   }
/*     */ 
/*     */   Object getXMLProperty(XMLName paramXMLName)
/*     */   {
/* 151 */     if (isPrototype()) {
/* 152 */       return getMethod(paramXMLName.localName());
/*     */     }
/* 154 */     return getPropertyList(paramXMLName);
/*     */   }
/*     */ 
/*     */   XmlNode.QName getNodeQname()
/*     */   {
/* 165 */     return this.node.getQname();
/*     */   }
/*     */ 
/*     */   XML[] getChildren() {
/* 169 */     if (!isElement()) return null;
/* 170 */     XmlNode[] arrayOfXmlNode = this.node.getMatchingChildren(XmlNode.Filter.TRUE);
/* 171 */     XML[] arrayOfXML = new XML[arrayOfXmlNode.length];
/* 172 */     for (int i = 0; i < arrayOfXML.length; i++) {
/* 173 */       arrayOfXML[i] = toXML(arrayOfXmlNode[i]);
/*     */     }
/* 175 */     return arrayOfXML;
/*     */   }
/*     */ 
/*     */   XML[] getAttributes() {
/* 179 */     XmlNode[] arrayOfXmlNode = this.node.getAttributes();
/* 180 */     XML[] arrayOfXML = new XML[arrayOfXmlNode.length];
/* 181 */     for (int i = 0; i < arrayOfXML.length; i++) {
/* 182 */       arrayOfXML[i] = toXML(arrayOfXmlNode[i]);
/*     */     }
/* 184 */     return arrayOfXML;
/*     */   }
/*     */ 
/*     */   XMLList getPropertyList(XMLName paramXMLName)
/*     */   {
/* 189 */     return paramXMLName.getMyValueOn(this);
/*     */   }
/*     */ 
/*     */   void deleteXMLProperty(XMLName paramXMLName)
/*     */   {
/* 194 */     XMLList localXMLList = getPropertyList(paramXMLName);
/* 195 */     for (int i = 0; i < localXMLList.length(); i++)
/* 196 */       localXMLList.item(i).node.deleteMe();
/*     */   }
/*     */ 
/*     */   void putXMLProperty(XMLName paramXMLName, Object paramObject)
/*     */   {
/* 202 */     if (!isPrototype())
/*     */     {
/* 205 */       paramXMLName.setMyValueOn(this, paramObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   boolean hasOwnProperty(XMLName paramXMLName)
/*     */   {
/* 211 */     boolean bool = false;
/*     */ 
/* 213 */     if (isPrototype()) {
/* 214 */       String str = paramXMLName.localName();
/* 215 */       bool = 0 != findPrototypeId(str);
/*     */     } else {
/* 217 */       bool = getPropertyList(paramXMLName).length() > 0;
/*     */     }
/*     */ 
/* 220 */     return bool;
/*     */   }
/*     */ 
/*     */   protected Object jsConstructor(Context paramContext, boolean paramBoolean, Object[] paramArrayOfObject)
/*     */   {
/* 225 */     if ((paramArrayOfObject.length == 0) || (paramArrayOfObject[0] == null) || (paramArrayOfObject[0] == Undefined.instance)) {
/* 226 */       paramArrayOfObject = new Object[] { "" };
/*     */     }
/*     */ 
/* 229 */     XML localXML = ecmaToXml(paramArrayOfObject[0]);
/* 230 */     if (paramBoolean) {
/* 231 */       return localXML.copy();
/*     */     }
/* 233 */     return localXML;
/*     */   }
/*     */ 
/*     */   public Scriptable getExtraMethodSource(Context paramContext)
/*     */   {
/* 240 */     if (hasSimpleContent()) {
/* 241 */       String str = toString();
/* 242 */       return ScriptRuntime.toObjectOrNull(paramContext, str);
/*     */     }
/* 244 */     return null;
/*     */   }
/*     */ 
/*     */   void removeChild(int paramInt)
/*     */   {
/* 252 */     this.node.removeChild(paramInt);
/*     */   }
/*     */ 
/*     */   void normalize()
/*     */   {
/* 257 */     this.node.normalize();
/*     */   }
/*     */ 
/*     */   private XML toXML(XmlNode paramXmlNode) {
/* 261 */     if (paramXmlNode.getXml() == null) {
/* 262 */       paramXmlNode.setXml(newXML(paramXmlNode));
/*     */     }
/* 264 */     return paramXmlNode.getXml();
/*     */   }
/*     */ 
/*     */   void setAttribute(XMLName paramXMLName, Object paramObject) {
/* 268 */     if (!isElement()) throw new IllegalStateException("Can only set attributes on elements.");
/*     */ 
/* 270 */     if ((paramXMLName.uri() == null) && (paramXMLName.localName().equals("*"))) {
/* 271 */       throw ScriptRuntime.typeError("@* assignment not supported.");
/*     */     }
/* 273 */     this.node.setAttribute(paramXMLName.toQname(), ScriptRuntime.toString(paramObject));
/*     */   }
/*     */ 
/*     */   void remove() {
/* 277 */     this.node.deleteMe();
/*     */   }
/*     */ 
/*     */   void addMatches(XMLList paramXMLList, XMLName paramXMLName)
/*     */   {
/* 282 */     paramXMLName.addMatches(paramXMLList, this);
/*     */   }
/*     */ 
/*     */   XMLList elements(XMLName paramXMLName)
/*     */   {
/* 287 */     XMLList localXMLList = newXMLList();
/* 288 */     localXMLList.setTargets(this, paramXMLName.toQname());
/*     */ 
/* 290 */     XmlNode[] arrayOfXmlNode = this.node.getMatchingChildren(XmlNode.Filter.ELEMENT);
/* 291 */     for (int i = 0; i < arrayOfXmlNode.length; i++) {
/* 292 */       if (paramXMLName.matches(toXML(arrayOfXmlNode[i]))) {
/* 293 */         localXMLList.addToList(toXML(arrayOfXmlNode[i]));
/*     */       }
/*     */     }
/* 296 */     return localXMLList;
/*     */   }
/*     */ 
/*     */   XMLList child(XMLName paramXMLName)
/*     */   {
/* 303 */     XMLList localXMLList = newXMLList();
/*     */ 
/* 308 */     XmlNode[] arrayOfXmlNode = this.node.getMatchingChildren(XmlNode.Filter.ELEMENT);
/* 309 */     for (int i = 0; i < arrayOfXmlNode.length; i++) {
/* 310 */       if (paramXMLName.matchesElement(arrayOfXmlNode[i].getQname())) {
/* 311 */         localXMLList.addToList(toXML(arrayOfXmlNode[i]));
/*     */       }
/*     */     }
/* 314 */     localXMLList.setTargets(this, paramXMLName.toQname());
/* 315 */     return localXMLList;
/*     */   }
/*     */ 
/*     */   XML replace(XMLName paramXMLName, Object paramObject) {
/* 319 */     putXMLProperty(paramXMLName, paramObject);
/* 320 */     return this;
/*     */   }
/*     */ 
/*     */   XMLList children()
/*     */   {
/* 325 */     XMLList localXMLList = newXMLList();
/* 326 */     XMLName localXMLName = XMLName.formStar();
/* 327 */     localXMLList.setTargets(this, localXMLName.toQname());
/* 328 */     XmlNode[] arrayOfXmlNode = this.node.getMatchingChildren(XmlNode.Filter.TRUE);
/* 329 */     for (int i = 0; i < arrayOfXmlNode.length; i++) {
/* 330 */       localXMLList.addToList(toXML(arrayOfXmlNode[i]));
/*     */     }
/* 332 */     return localXMLList;
/*     */   }
/*     */ 
/*     */   XMLList child(int paramInt)
/*     */   {
/* 338 */     XMLList localXMLList = newXMLList();
/* 339 */     localXMLList.setTargets(this, null);
/* 340 */     if ((paramInt >= 0) && (paramInt < this.node.getChildCount())) {
/* 341 */       localXMLList.addToList(getXmlChild(paramInt));
/*     */     }
/* 343 */     return localXMLList;
/*     */   }
/*     */ 
/*     */   XML getXmlChild(int paramInt) {
/* 347 */     XmlNode localXmlNode = this.node.getChild(paramInt);
/* 348 */     if (localXmlNode.getXml() == null) {
/* 349 */       localXmlNode.setXml(newXML(localXmlNode));
/*     */     }
/* 351 */     return localXmlNode.getXml();
/*     */   }
/*     */ 
/*     */   int childIndex() {
/* 355 */     return this.node.getChildIndex();
/*     */   }
/*     */ 
/*     */   boolean contains(Object paramObject)
/*     */   {
/* 360 */     if ((paramObject instanceof XML)) {
/* 361 */       return equivalentXml(paramObject);
/*     */     }
/* 363 */     return false;
/*     */   }
/*     */ 
/*     */   boolean equivalentXml(Object paramObject)
/*     */   {
/* 370 */     boolean bool = false;
/*     */ 
/* 372 */     if ((paramObject instanceof XML))
/*     */     {
/* 374 */       return this.node.toXmlString(getProcessor()).equals(((XML)paramObject).node.toXmlString(getProcessor()));
/*     */     }
/*     */     Object localObject;
/* 375 */     if ((paramObject instanceof XMLList))
/*     */     {
/* 377 */       localObject = (XMLList)paramObject;
/*     */ 
/* 379 */       if (((XMLList)localObject).length() == 1)
/* 380 */         bool = equivalentXml(((XMLList)localObject).getXML());
/*     */     }
/* 382 */     else if (hasSimpleContent()) {
/* 383 */       localObject = ScriptRuntime.toString(paramObject);
/*     */ 
/* 385 */       bool = toString().equals(localObject);
/*     */     }
/*     */ 
/* 388 */     return bool;
/*     */   }
/*     */ 
/*     */   XMLObjectImpl copy()
/*     */   {
/* 393 */     return newXML(this.node.copy());
/*     */   }
/*     */ 
/*     */   boolean hasSimpleContent()
/*     */   {
/* 398 */     if ((isComment()) || (isProcessingInstruction())) return false;
/* 399 */     if ((isText()) || (this.node.isAttributeType())) return true;
/* 400 */     return !this.node.hasChildElement();
/*     */   }
/*     */ 
/*     */   boolean hasComplexContent()
/*     */   {
/* 405 */     return !hasSimpleContent();
/*     */   }
/*     */ 
/*     */   int length()
/*     */   {
/* 412 */     return 1;
/*     */   }
/*     */ 
/*     */   boolean is(XML paramXML)
/*     */   {
/* 417 */     return this.node.isSameNode(paramXML.node);
/*     */   }
/*     */ 
/*     */   Object nodeKind() {
/* 421 */     return ecmaClass();
/*     */   }
/*     */ 
/*     */   Object parent()
/*     */   {
/* 426 */     XmlNode localXmlNode = this.node.parent();
/* 427 */     if (localXmlNode == null) return null;
/* 428 */     return newXML(this.node.parent());
/*     */   }
/*     */ 
/*     */   boolean propertyIsEnumerable(Object paramObject)
/*     */   {
/*     */     boolean bool;
/* 435 */     if ((paramObject instanceof Integer)) {
/* 436 */       bool = ((Integer)paramObject).intValue() == 0;
/* 437 */     } else if ((paramObject instanceof Number)) {
/* 438 */       double d = ((Number)paramObject).doubleValue();
/*     */ 
/* 440 */       bool = (d == 0.0D) && (1.0D / d > 0.0D);
/*     */     } else {
/* 442 */       bool = ScriptRuntime.toString(paramObject).equals("0");
/*     */     }
/* 444 */     return bool;
/*     */   }
/*     */ 
/*     */   Object valueOf()
/*     */   {
/* 449 */     return this;
/*     */   }
/*     */ 
/*     */   XMLList comments()
/*     */   {
/* 458 */     XMLList localXMLList = newXMLList();
/* 459 */     this.node.addMatchingChildren(localXMLList, XmlNode.Filter.COMMENT);
/* 460 */     return localXMLList;
/*     */   }
/*     */ 
/*     */   XMLList text()
/*     */   {
/* 465 */     XMLList localXMLList = newXMLList();
/* 466 */     this.node.addMatchingChildren(localXMLList, XmlNode.Filter.TEXT);
/* 467 */     return localXMLList;
/*     */   }
/*     */ 
/*     */   XMLList processingInstructions(XMLName paramXMLName)
/*     */   {
/* 472 */     XMLList localXMLList = newXMLList();
/* 473 */     this.node.addMatchingChildren(localXMLList, XmlNode.Filter.PROCESSING_INSTRUCTION(paramXMLName));
/* 474 */     return localXMLList;
/*     */   }
/*     */ 
/*     */   private XmlNode[] getNodesForInsert(Object paramObject)
/*     */   {
/* 488 */     if ((paramObject instanceof XML))
/* 489 */       return new XmlNode[] { ((XML)paramObject).node };
/* 490 */     if ((paramObject instanceof XMLList)) {
/* 491 */       XMLList localXMLList = (XMLList)paramObject;
/* 492 */       XmlNode[] arrayOfXmlNode = new XmlNode[localXMLList.length()];
/* 493 */       for (int i = 0; i < localXMLList.length(); i++) {
/* 494 */         arrayOfXmlNode[i] = localXMLList.item(i).node;
/*     */       }
/* 496 */       return arrayOfXmlNode;
/*     */     }
/* 498 */     return new XmlNode[] { XmlNode.createText(getProcessor(), ScriptRuntime.toString(paramObject)) };
/*     */   }
/*     */ 
/*     */   XML replace(int paramInt, Object paramObject)
/*     */   {
/* 505 */     XMLList localXMLList = child(paramInt);
/* 506 */     if (localXMLList.length() > 0)
/*     */     {
/* 508 */       XML localXML = localXMLList.item(0);
/* 509 */       insertChildAfter(localXML, paramObject);
/* 510 */       removeChild(paramInt);
/*     */     }
/* 512 */     return this;
/*     */   }
/*     */ 
/*     */   XML prependChild(Object paramObject) {
/* 516 */     if (this.node.isParentType()) {
/* 517 */       this.node.insertChildrenAt(0, getNodesForInsert(paramObject));
/*     */     }
/* 519 */     return this;
/*     */   }
/*     */ 
/*     */   XML appendChild(Object paramObject) {
/* 523 */     if (this.node.isParentType()) {
/* 524 */       XmlNode[] arrayOfXmlNode = getNodesForInsert(paramObject);
/* 525 */       this.node.insertChildrenAt(this.node.getChildCount(), arrayOfXmlNode);
/*     */     }
/* 527 */     return this;
/*     */   }
/*     */ 
/*     */   private int getChildIndexOf(XML paramXML) {
/* 531 */     for (int i = 0; i < this.node.getChildCount(); i++) {
/* 532 */       if (this.node.getChild(i).isSameNode(paramXML.node)) {
/* 533 */         return i;
/*     */       }
/*     */     }
/* 536 */     return -1;
/*     */   }
/*     */ 
/*     */   XML insertChildBefore(XML paramXML, Object paramObject) {
/* 540 */     if (paramXML == null)
/*     */     {
/* 542 */       appendChild(paramObject);
/*     */     } else {
/* 544 */       XmlNode[] arrayOfXmlNode = getNodesForInsert(paramObject);
/* 545 */       int i = getChildIndexOf(paramXML);
/* 546 */       if (i != -1) {
/* 547 */         this.node.insertChildrenAt(i, arrayOfXmlNode);
/*     */       }
/*     */     }
/*     */ 
/* 551 */     return this;
/*     */   }
/*     */ 
/*     */   XML insertChildAfter(XML paramXML, Object paramObject) {
/* 555 */     if (paramXML == null)
/*     */     {
/* 557 */       prependChild(paramObject);
/*     */     } else {
/* 559 */       XmlNode[] arrayOfXmlNode = getNodesForInsert(paramObject);
/* 560 */       int i = getChildIndexOf(paramXML);
/* 561 */       if (i != -1) {
/* 562 */         this.node.insertChildrenAt(i + 1, arrayOfXmlNode);
/*     */       }
/*     */     }
/*     */ 
/* 566 */     return this;
/*     */   }
/*     */ 
/*     */   XML setChildren(Object paramObject)
/*     */   {
/* 571 */     if (!isElement()) return this;
/*     */ 
/* 573 */     while (this.node.getChildCount() > 0) {
/* 574 */       this.node.removeChild(0);
/*     */     }
/* 576 */     XmlNode[] arrayOfXmlNode = getNodesForInsert(paramObject);
/*     */ 
/* 578 */     this.node.insertChildrenAt(0, arrayOfXmlNode);
/*     */ 
/* 580 */     return this;
/*     */   }
/*     */ 
/*     */   private void addInScopeNamespace(Namespace paramNamespace)
/*     */   {
/* 588 */     if (!isElement()) {
/* 589 */       return;
/*     */     }
/*     */ 
/* 593 */     if (paramNamespace.prefix() != null) {
/* 594 */       if ((paramNamespace.prefix().length() == 0) && (paramNamespace.uri().length() == 0)) {
/* 595 */         return;
/*     */       }
/* 597 */       if (this.node.getQname().getNamespace().getPrefix().equals(paramNamespace.prefix())) {
/* 598 */         this.node.invalidateNamespacePrefix();
/*     */       }
/* 600 */       this.node.declareNamespace(paramNamespace.prefix(), paramNamespace.uri());
/*     */     }
/*     */     else;
/*     */   }
/*     */ 
/*     */   Namespace[] inScopeNamespaces()
/*     */   {
/* 607 */     XmlNode.Namespace[] arrayOfNamespace = this.node.getInScopeNamespaces();
/* 608 */     return createNamespaces(arrayOfNamespace);
/*     */   }
/*     */ 
/*     */   private XmlNode.Namespace adapt(Namespace paramNamespace) {
/* 612 */     if (paramNamespace.prefix() == null) {
/* 613 */       return XmlNode.Namespace.create(paramNamespace.uri());
/*     */     }
/* 615 */     return XmlNode.Namespace.create(paramNamespace.prefix(), paramNamespace.uri());
/*     */   }
/*     */ 
/*     */   XML removeNamespace(Namespace paramNamespace)
/*     */   {
/* 620 */     if (!isElement()) return this;
/* 621 */     this.node.removeNamespace(adapt(paramNamespace));
/* 622 */     return this;
/*     */   }
/*     */ 
/*     */   XML addNamespace(Namespace paramNamespace) {
/* 626 */     addInScopeNamespace(paramNamespace);
/* 627 */     return this;
/*     */   }
/*     */ 
/*     */   QName name() {
/* 631 */     if ((isText()) || (isComment())) return null;
/* 632 */     if (isProcessingInstruction()) return newQName("", this.node.getQname().getLocalName(), null);
/* 633 */     return newQName(this.node.getQname());
/*     */   }
/*     */ 
/*     */   Namespace[] namespaceDeclarations() {
/* 637 */     XmlNode.Namespace[] arrayOfNamespace = this.node.getNamespaceDeclarations();
/* 638 */     return createNamespaces(arrayOfNamespace);
/*     */   }
/*     */ 
/*     */   Namespace namespace(String paramString) {
/* 642 */     if (paramString == null) {
/* 643 */       return createNamespace(this.node.getNamespaceDeclaration());
/*     */     }
/* 645 */     return createNamespace(this.node.getNamespaceDeclaration(paramString));
/*     */   }
/*     */ 
/*     */   String localName()
/*     */   {
/* 650 */     if (name() == null) return null;
/* 651 */     return name().localName();
/*     */   }
/*     */ 
/*     */   void setLocalName(String paramString)
/*     */   {
/* 656 */     if ((isText()) || (isComment())) return;
/* 657 */     this.node.setLocalName(paramString);
/*     */   }
/*     */ 
/*     */   void setName(QName paramQName)
/*     */   {
/* 662 */     if ((isText()) || (isComment())) return;
/* 663 */     if (isProcessingInstruction())
/*     */     {
/* 666 */       this.node.setLocalName(paramQName.localName());
/* 667 */       return;
/*     */     }
/* 669 */     this.node.renameNode(paramQName.getDelegate());
/*     */   }
/*     */ 
/*     */   void setNamespace(Namespace paramNamespace)
/*     */   {
/* 674 */     if ((isText()) || (isComment()) || (isProcessingInstruction())) return;
/* 675 */     setName(newQName(paramNamespace.uri(), localName(), paramNamespace.prefix()));
/*     */   }
/*     */ 
/*     */   final String ecmaClass()
/*     */   {
/* 683 */     if (this.node.isTextType())
/* 684 */       return "text";
/* 685 */     if (this.node.isAttributeType())
/* 686 */       return "attribute";
/* 687 */     if (this.node.isCommentType())
/* 688 */       return "comment";
/* 689 */     if (this.node.isProcessingInstructionType())
/* 690 */       return "processing-instruction";
/* 691 */     if (this.node.isElementType()) {
/* 692 */       return "element";
/*     */     }
/* 694 */     throw new RuntimeException("Unrecognized type: " + this.node);
/*     */   }
/*     */ 
/*     */   public String getClassName()
/*     */   {
/* 703 */     return "XML";
/*     */   }
/*     */ 
/*     */   private String ecmaValue() {
/* 707 */     return this.node.ecmaValue();
/*     */   }
/*     */ 
/*     */   private String ecmaToString()
/*     */   {
/* 712 */     if ((isAttribute()) || (isText())) {
/* 713 */       return ecmaValue();
/*     */     }
/* 715 */     if (hasSimpleContent()) {
/* 716 */       StringBuffer localStringBuffer = new StringBuffer();
/* 717 */       for (int i = 0; i < this.node.getChildCount(); i++) {
/* 718 */         XmlNode localXmlNode = this.node.getChild(i);
/* 719 */         if ((!localXmlNode.isProcessingInstructionType()) && (!localXmlNode.isCommentType()))
/*     */         {
/* 724 */           XML localXML = new XML(getLib(), getParentScope(), (XMLObject)getPrototype(), localXmlNode);
/*     */ 
/* 726 */           localStringBuffer.append(localXML.toString());
/*     */         }
/*     */       }
/* 729 */       return localStringBuffer.toString();
/*     */     }
/* 731 */     return toXMLString();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 736 */     return ecmaToString();
/*     */   }
/*     */ 
/*     */   String toSource(int paramInt)
/*     */   {
/* 741 */     return toXMLString();
/*     */   }
/*     */ 
/*     */   String toXMLString()
/*     */   {
/* 746 */     return this.node.ecmaToXMLString(getProcessor());
/*     */   }
/*     */ 
/*     */   final boolean isAttribute() {
/* 750 */     return this.node.isAttributeType();
/*     */   }
/*     */ 
/*     */   final boolean isComment() {
/* 754 */     return this.node.isCommentType();
/*     */   }
/*     */ 
/*     */   final boolean isText() {
/* 758 */     return this.node.isTextType();
/*     */   }
/*     */ 
/*     */   final boolean isElement() {
/* 762 */     return this.node.isElementType();
/*     */   }
/*     */ 
/*     */   final boolean isProcessingInstruction() {
/* 766 */     return this.node.isProcessingInstructionType();
/*     */   }
/*     */ 
/*     */   Node toDomNode()
/*     */   {
/* 771 */     return this.node.toDomNode();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.xmlimpl.XML
 * JD-Core Version:    0.6.2
 */