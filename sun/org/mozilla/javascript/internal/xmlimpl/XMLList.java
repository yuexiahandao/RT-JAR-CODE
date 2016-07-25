/*     */ package sun.org.mozilla.javascript.internal.xmlimpl;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import sun.org.mozilla.javascript.internal.Callable;
/*     */ import sun.org.mozilla.javascript.internal.Context;
/*     */ import sun.org.mozilla.javascript.internal.Function;
/*     */ import sun.org.mozilla.javascript.internal.ScriptRuntime;
/*     */ import sun.org.mozilla.javascript.internal.Scriptable;
/*     */ import sun.org.mozilla.javascript.internal.Undefined;
/*     */ import sun.org.mozilla.javascript.internal.xml.XMLObject;
/*     */ 
/*     */ class XMLList extends XMLObjectImpl
/*     */   implements Function
/*     */ {
/*     */   static final long serialVersionUID = -4543618751670781135L;
/*     */   private XmlNode.InternalList _annos;
/*  52 */   private XMLObjectImpl targetObject = null;
/*  53 */   private XmlNode.QName targetProperty = null;
/*     */ 
/*     */   XMLList(XMLLibImpl paramXMLLibImpl, Scriptable paramScriptable, XMLObject paramXMLObject) {
/*  56 */     super(paramXMLLibImpl, paramScriptable, paramXMLObject);
/*  57 */     this._annos = new XmlNode.InternalList();
/*     */   }
/*     */ 
/*     */   XmlNode.InternalList getNodeList()
/*     */   {
/*  62 */     return this._annos;
/*     */   }
/*     */ 
/*     */   void setTargets(XMLObjectImpl paramXMLObjectImpl, XmlNode.QName paramQName)
/*     */   {
/*  67 */     this.targetObject = paramXMLObjectImpl;
/*  68 */     this.targetProperty = paramQName;
/*     */   }
/*     */ 
/*     */   private XML getXmlFromAnnotation(int paramInt)
/*     */   {
/*  73 */     return getXML(this._annos, paramInt);
/*     */   }
/*     */ 
/*     */   XML getXML()
/*     */   {
/*  78 */     if (length() == 1) return getXmlFromAnnotation(0);
/*  79 */     return null;
/*     */   }
/*     */ 
/*     */   private void internalRemoveFromList(int paramInt) {
/*  83 */     this._annos.remove(paramInt);
/*     */   }
/*     */ 
/*     */   void replace(int paramInt, XML paramXML) {
/*  87 */     if (paramInt < length()) {
/*  88 */       XmlNode.InternalList localInternalList = new XmlNode.InternalList();
/*  89 */       localInternalList.add(this._annos, 0, paramInt);
/*  90 */       localInternalList.add(paramXML);
/*  91 */       localInternalList.add(this._annos, paramInt + 1, length());
/*  92 */       this._annos = localInternalList;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void insert(int paramInt, XML paramXML) {
/*  97 */     if (paramInt < length()) {
/*  98 */       XmlNode.InternalList localInternalList = new XmlNode.InternalList();
/*  99 */       localInternalList.add(this._annos, 0, paramInt);
/* 100 */       localInternalList.add(paramXML);
/* 101 */       localInternalList.add(this._annos, paramInt, length());
/* 102 */       this._annos = localInternalList;
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getClassName()
/*     */   {
/* 114 */     return "XMLList";
/*     */   }
/*     */ 
/*     */   public Object get(int paramInt, Scriptable paramScriptable)
/*     */   {
/* 127 */     if ((paramInt >= 0) && (paramInt < length())) {
/* 128 */       return getXmlFromAnnotation(paramInt);
/*     */     }
/* 130 */     return Scriptable.NOT_FOUND;
/*     */   }
/*     */ 
/*     */   boolean hasXMLProperty(XMLName paramXMLName)
/*     */   {
/* 136 */     boolean bool = false;
/*     */ 
/* 140 */     String str = paramXMLName.localName();
/* 141 */     if ((getPropertyList(paramXMLName).length() > 0) || (getMethod(str) != NOT_FOUND))
/*     */     {
/* 143 */       bool = true;
/*     */     }
/*     */ 
/* 146 */     return bool;
/*     */   }
/*     */ 
/*     */   public boolean has(int paramInt, Scriptable paramScriptable)
/*     */   {
/* 151 */     return (0 <= paramInt) && (paramInt < length());
/*     */   }
/*     */ 
/*     */   void putXMLProperty(XMLName paramXMLName, Object paramObject)
/*     */   {
/* 159 */     if (paramObject == null)
/* 160 */       paramObject = "null";
/* 161 */     else if ((paramObject instanceof Undefined)) {
/* 162 */       paramObject = "undefined";
/*     */     }
/*     */ 
/* 165 */     if (length() > 1)
/* 166 */       throw ScriptRuntime.typeError("Assignment to lists with more than one item is not supported");
/*     */     XML localXML;
/*     */     Object localObject;
/* 168 */     if (length() == 0)
/*     */     {
/* 171 */       if ((this.targetObject != null) && (this.targetProperty != null) && (this.targetProperty.getLocalName() != null) && (this.targetProperty.getLocalName().length() > 0))
/*     */       {
/* 177 */         localXML = newTextElementXML(null, this.targetProperty, null);
/* 178 */         addToList(localXML);
/*     */ 
/* 180 */         if (paramXMLName.isAttributeName()) {
/* 181 */           setAttribute(paramXMLName, paramObject);
/*     */         } else {
/* 183 */           localObject = item(0);
/* 184 */           ((XML)localObject).putXMLProperty(paramXMLName, paramObject);
/*     */ 
/* 187 */           replace(0, item(0));
/*     */         }
/*     */ 
/* 191 */         localObject = XMLName.formProperty(this.targetProperty.getNamespace().getUri(), this.targetProperty.getLocalName());
/*     */ 
/* 194 */         this.targetObject.putXMLProperty((XMLName)localObject, this);
/*     */       } else {
/* 196 */         throw ScriptRuntime.typeError("Assignment to empty XMLList without targets not supported");
/*     */       }
/*     */     }
/* 199 */     else if (paramXMLName.isAttributeName()) {
/* 200 */       setAttribute(paramXMLName, paramObject);
/*     */     } else {
/* 202 */       localXML = item(0);
/* 203 */       localXML.putXMLProperty(paramXMLName, paramObject);
/*     */ 
/* 206 */       replace(0, item(0));
/*     */ 
/* 208 */       if ((this.targetObject != null) && (this.targetProperty != null) && (this.targetProperty.getLocalName() != null))
/*     */       {
/* 212 */         localObject = XMLName.formProperty(this.targetProperty.getNamespace().getUri(), this.targetProperty.getLocalName());
/*     */ 
/* 215 */         this.targetObject.putXMLProperty((XMLName)localObject, this);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   Object getXMLProperty(XMLName paramXMLName)
/*     */   {
/* 222 */     return getPropertyList(paramXMLName);
/*     */   }
/*     */ 
/*     */   private void replaceNode(XML paramXML1, XML paramXML2) {
/* 226 */     paramXML1.replaceWith(paramXML2);
/*     */   }
/*     */ 
/*     */   public void put(int paramInt, Scriptable paramScriptable, Object paramObject)
/*     */   {
/* 231 */     Object localObject1 = Undefined.instance;
/*     */ 
/* 236 */     if (paramObject == null)
/* 237 */       paramObject = "null";
/* 238 */     else if ((paramObject instanceof Undefined))
/* 239 */       paramObject = "undefined";
/*     */     Object localObject2;
/*     */     XML localXML;
/* 242 */     if ((paramObject instanceof XMLObject)) {
/* 243 */       localObject2 = (XMLObject)paramObject;
/*     */     }
/* 245 */     else if (this.targetProperty == null) {
/* 246 */       localObject2 = newXMLFromJs(paramObject.toString());
/*     */     }
/*     */     else
/*     */     {
/* 252 */       localObject2 = item(paramInt);
/* 253 */       if (localObject2 == null) {
/* 254 */         localXML = item(0);
/* 255 */         localObject2 = localXML == null ? newTextElementXML(null, this.targetProperty, null) : localXML.copy();
/*     */       }
/*     */ 
/* 259 */       ((XML)localObject2).setChildren(paramObject);
/*     */     }
/*     */ 
/* 264 */     if (paramInt < length())
/* 265 */       localObject1 = item(paramInt).parent();
/* 266 */     else if (length() == 0) {
/* 267 */       localObject1 = this.targetObject != null ? this.targetObject.getXML() : parent();
/*     */     }
/*     */     else
/* 270 */       localObject1 = parent();
/*     */     Object localObject3;
/* 273 */     if ((localObject1 instanceof XML))
/*     */     {
/* 275 */       localXML = (XML)localObject1;
/*     */ 
/* 277 */       if (paramInt < length())
/*     */       {
/* 279 */         localObject3 = getXmlFromAnnotation(paramInt);
/*     */ 
/* 281 */         if ((localObject2 instanceof XML)) {
/* 282 */           replaceNode((XML)localObject3, (XML)localObject2);
/* 283 */           replace(paramInt, (XML)localObject3);
/* 284 */         } else if ((localObject2 instanceof XMLList))
/*     */         {
/* 286 */           XMLList localXMLList = (XMLList)localObject2;
/*     */ 
/* 288 */           if (localXMLList.length() > 0) {
/* 289 */             int j = ((XML)localObject3).childIndex();
/* 290 */             replaceNode((XML)localObject3, localXMLList.item(0));
/* 291 */             replace(paramInt, localXMLList.item(0));
/*     */ 
/* 293 */             for (int k = 1; k < localXMLList.length(); k++) {
/* 294 */               localXML.insertChildAfter(localXML.getXmlChild(j), localXMLList.item(k));
/* 295 */               j++;
/* 296 */               insert(paramInt + k, localXMLList.item(k));
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       else {
/* 302 */         localXML.appendChild(localObject2);
/* 303 */         addToList(localXML.getXmlChild(paramInt));
/*     */       }
/*     */ 
/*     */     }
/* 307 */     else if (paramInt < length()) {
/* 308 */       localXML = getXML(this._annos, paramInt);
/*     */ 
/* 310 */       if ((localObject2 instanceof XML)) {
/* 311 */         replaceNode(localXML, (XML)localObject2);
/* 312 */         replace(paramInt, localXML);
/* 313 */       } else if ((localObject2 instanceof XMLList))
/*     */       {
/* 315 */         localObject3 = (XMLList)localObject2;
/*     */ 
/* 317 */         if (((XMLList)localObject3).length() > 0) {
/* 318 */           replaceNode(localXML, ((XMLList)localObject3).item(0));
/* 319 */           replace(paramInt, ((XMLList)localObject3).item(0));
/*     */ 
/* 321 */           for (int i = 1; i < ((XMLList)localObject3).length(); i++)
/* 322 */             insert(paramInt + i, ((XMLList)localObject3).item(i));
/*     */         }
/*     */       }
/*     */     }
/*     */     else {
/* 327 */       addToList(localObject2);
/*     */     }
/*     */   }
/*     */ 
/*     */   private XML getXML(XmlNode.InternalList paramInternalList, int paramInt)
/*     */   {
/* 333 */     if ((paramInt >= 0) && (paramInt < length())) {
/* 334 */       return xmlFromNode(paramInternalList.item(paramInt));
/*     */     }
/* 336 */     return null;
/*     */   }
/*     */ 
/*     */   void deleteXMLProperty(XMLName paramXMLName)
/*     */   {
/* 342 */     for (int i = 0; i < length(); i++) {
/* 343 */       XML localXML = getXmlFromAnnotation(i);
/*     */ 
/* 345 */       if (localXML.isElement())
/* 346 */         localXML.deleteXMLProperty(paramXMLName);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void delete(int paramInt)
/*     */   {
/* 353 */     if ((paramInt >= 0) && (paramInt < length())) {
/* 354 */       XML localXML = getXmlFromAnnotation(paramInt);
/*     */ 
/* 356 */       localXML.remove();
/*     */ 
/* 358 */       internalRemoveFromList(paramInt);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object[] getIds()
/*     */   {
/*     */     Object[] arrayOfObject;
/* 366 */     if (isPrototype()) {
/* 367 */       arrayOfObject = new Object[0];
/*     */     } else {
/* 369 */       arrayOfObject = new Object[length()];
/*     */ 
/* 371 */       for (int i = 0; i < arrayOfObject.length; i++) {
/* 372 */         arrayOfObject[i] = Integer.valueOf(i);
/*     */       }
/*     */     }
/*     */ 
/* 376 */     return arrayOfObject;
/*     */   }
/*     */ 
/*     */   public Object[] getIdsForDebug() {
/* 380 */     return getIds();
/*     */   }
/*     */ 
/*     */   void remove()
/*     */   {
/* 386 */     int i = length();
/* 387 */     for (int j = i - 1; j >= 0; j--) {
/* 388 */       XML localXML = getXmlFromAnnotation(j);
/* 389 */       if (localXML != null) {
/* 390 */         localXML.remove();
/* 391 */         internalRemoveFromList(j);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   XML item(int paramInt) {
/* 397 */     return this._annos != null ? getXmlFromAnnotation(paramInt) : createEmptyXML();
/*     */   }
/*     */ 
/*     */   private void setAttribute(XMLName paramXMLName, Object paramObject)
/*     */   {
/* 402 */     for (int i = 0; i < length(); i++) {
/* 403 */       XML localXML = getXmlFromAnnotation(i);
/* 404 */       localXML.setAttribute(paramXMLName, paramObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   void addToList(Object paramObject) {
/* 409 */     this._annos.addToList(paramObject);
/*     */   }
/*     */ 
/*     */   XMLList child(int paramInt)
/*     */   {
/* 420 */     XMLList localXMLList = newXMLList();
/*     */ 
/* 422 */     for (int i = 0; i < length(); i++) {
/* 423 */       localXMLList.addToList(getXmlFromAnnotation(i).child(paramInt));
/*     */     }
/*     */ 
/* 426 */     return localXMLList;
/*     */   }
/*     */ 
/*     */   XMLList child(XMLName paramXMLName)
/*     */   {
/* 431 */     XMLList localXMLList = newXMLList();
/*     */ 
/* 433 */     for (int i = 0; i < length(); i++) {
/* 434 */       localXMLList.addToList(getXmlFromAnnotation(i).child(paramXMLName));
/*     */     }
/*     */ 
/* 437 */     return localXMLList;
/*     */   }
/*     */ 
/*     */   void addMatches(XMLList paramXMLList, XMLName paramXMLName)
/*     */   {
/* 442 */     for (int i = 0; i < length(); i++)
/* 443 */       getXmlFromAnnotation(i).addMatches(paramXMLList, paramXMLName);
/*     */   }
/*     */ 
/*     */   XMLList children()
/*     */   {
/* 449 */     ArrayList localArrayList = new ArrayList();
/*     */ 
/* 451 */     for (int i = 0; i < length(); i++) {
/* 452 */       XML localXML = getXmlFromAnnotation(i);
/*     */ 
/* 454 */       if (localXML != null) {
/* 455 */         XMLList localXMLList2 = localXML.children();
/*     */ 
/* 457 */         int m = localXMLList2.length();
/* 458 */         for (int n = 0; n < m; n++) {
/* 459 */           localArrayList.add(localXMLList2.item(n));
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 464 */     XMLList localXMLList1 = newXMLList();
/* 465 */     int j = localArrayList.size();
/*     */ 
/* 467 */     for (int k = 0; k < j; k++) {
/* 468 */       localXMLList1.addToList(localArrayList.get(k));
/*     */     }
/*     */ 
/* 471 */     return localXMLList1;
/*     */   }
/*     */ 
/*     */   XMLList comments()
/*     */   {
/* 476 */     XMLList localXMLList = newXMLList();
/*     */ 
/* 478 */     for (int i = 0; i < length(); i++) {
/* 479 */       XML localXML = getXmlFromAnnotation(i);
/* 480 */       localXMLList.addToList(localXML.comments());
/*     */     }
/*     */ 
/* 483 */     return localXMLList;
/*     */   }
/*     */ 
/*     */   XMLList elements(XMLName paramXMLName)
/*     */   {
/* 488 */     XMLList localXMLList = newXMLList();
/* 489 */     for (int i = 0; i < length(); i++) {
/* 490 */       XML localXML = getXmlFromAnnotation(i);
/* 491 */       localXMLList.addToList(localXML.elements(paramXMLName));
/*     */     }
/* 493 */     return localXMLList;
/*     */   }
/*     */ 
/*     */   boolean contains(Object paramObject)
/*     */   {
/* 498 */     boolean bool = false;
/*     */ 
/* 500 */     for (int i = 0; i < length(); i++) {
/* 501 */       XML localXML = getXmlFromAnnotation(i);
/*     */ 
/* 503 */       if (localXML.equivalentXml(paramObject)) {
/* 504 */         bool = true;
/* 505 */         break;
/*     */       }
/*     */     }
/*     */ 
/* 509 */     return bool;
/*     */   }
/*     */ 
/*     */   XMLObjectImpl copy()
/*     */   {
/* 514 */     XMLList localXMLList = newXMLList();
/*     */ 
/* 516 */     for (int i = 0; i < length(); i++) {
/* 517 */       XML localXML = getXmlFromAnnotation(i);
/* 518 */       localXMLList.addToList(localXML.copy());
/*     */     }
/*     */ 
/* 521 */     return localXMLList;
/*     */   }
/*     */ 
/*     */   boolean hasOwnProperty(XMLName paramXMLName)
/*     */   {
/* 526 */     if (isPrototype()) {
/* 527 */       String str = paramXMLName.localName();
/* 528 */       return findPrototypeId(str) != 0;
/*     */     }
/* 530 */     return getPropertyList(paramXMLName).length() > 0;
/*     */   }
/*     */ 
/*     */   boolean hasComplexContent()
/*     */   {
/* 537 */     int i = length();
/*     */     boolean bool;
/* 539 */     if (i == 0) {
/* 540 */       bool = false;
/* 541 */     } else if (i == 1) {
/* 542 */       bool = getXmlFromAnnotation(0).hasComplexContent();
/*     */     } else {
/* 544 */       bool = false;
/*     */ 
/* 546 */       for (int j = 0; j < i; j++) {
/* 547 */         XML localXML = getXmlFromAnnotation(j);
/* 548 */         if (localXML.isElement()) {
/* 549 */           bool = true;
/* 550 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 555 */     return bool;
/*     */   }
/*     */ 
/*     */   boolean hasSimpleContent()
/*     */   {
/* 560 */     if (length() == 0)
/* 561 */       return true;
/* 562 */     if (length() == 1) {
/* 563 */       return getXmlFromAnnotation(0).hasSimpleContent();
/*     */     }
/* 565 */     for (int i = 0; i < length(); i++) {
/* 566 */       XML localXML = getXmlFromAnnotation(i);
/* 567 */       if (localXML.isElement()) {
/* 568 */         return false;
/*     */       }
/*     */     }
/* 571 */     return true;
/*     */   }
/*     */ 
/*     */   int length()
/*     */   {
/* 577 */     int i = 0;
/*     */ 
/* 579 */     if (this._annos != null) {
/* 580 */       i = this._annos.length();
/*     */     }
/*     */ 
/* 583 */     return i;
/*     */   }
/*     */ 
/*     */   void normalize()
/*     */   {
/* 588 */     for (int i = 0; i < length(); i++)
/* 589 */       getXmlFromAnnotation(i).normalize();
/*     */   }
/*     */ 
/*     */   Object parent()
/*     */   {
/* 599 */     if (length() == 0) return Undefined.instance;
/*     */ 
/* 601 */     Object localObject1 = null;
/*     */ 
/* 603 */     for (int i = 0; i < length(); i++) {
/* 604 */       Object localObject2 = getXmlFromAnnotation(i).parent();
/* 605 */       if (!(localObject2 instanceof XML)) return Undefined.instance;
/* 606 */       XML localXML = (XML)localObject2;
/* 607 */       if (i == 0)
/*     */       {
/* 609 */         localObject1 = localXML;
/*     */       }
/* 611 */       else if (!localObject1.is(localXML))
/*     */       {
/* 614 */         return Undefined.instance;
/*     */       }
/*     */     }
/*     */ 
/* 618 */     return localObject1;
/*     */   }
/*     */ 
/*     */   XMLList processingInstructions(XMLName paramXMLName)
/*     */   {
/* 623 */     XMLList localXMLList = newXMLList();
/*     */ 
/* 625 */     for (int i = 0; i < length(); i++) {
/* 626 */       XML localXML = getXmlFromAnnotation(i);
/*     */ 
/* 628 */       localXMLList.addToList(localXML.processingInstructions(paramXMLName));
/*     */     }
/*     */ 
/* 631 */     return localXMLList;
/*     */   }
/*     */ 
/*     */   boolean propertyIsEnumerable(Object paramObject)
/*     */   {
/*     */     long l;
/* 637 */     if ((paramObject instanceof Integer)) {
/* 638 */       l = ((Integer)paramObject).intValue();
/* 639 */     } else if ((paramObject instanceof Number)) {
/* 640 */       double d = ((Number)paramObject).doubleValue();
/* 641 */       l = ()d;
/* 642 */       if (l != d) {
/* 643 */         return false;
/*     */       }
/* 645 */       if ((l == 0L) && (1.0D / d < 0.0D))
/*     */       {
/* 647 */         return false;
/*     */       }
/*     */     } else {
/* 650 */       String str = ScriptRuntime.toString(paramObject);
/* 651 */       l = ScriptRuntime.testUint32String(str);
/*     */     }
/* 653 */     return (0L <= l) && (l < length());
/*     */   }
/*     */ 
/*     */   XMLList text()
/*     */   {
/* 658 */     XMLList localXMLList = newXMLList();
/*     */ 
/* 660 */     for (int i = 0; i < length(); i++) {
/* 661 */       localXMLList.addToList(getXmlFromAnnotation(i).text());
/*     */     }
/*     */ 
/* 664 */     return localXMLList;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 670 */     if (hasSimpleContent()) {
/* 671 */       StringBuffer localStringBuffer = new StringBuffer();
/*     */ 
/* 673 */       for (int i = 0; i < length(); i++) {
/* 674 */         XML localXML = getXmlFromAnnotation(i);
/* 675 */         if ((!localXML.isComment()) && (!localXML.isProcessingInstruction()))
/*     */         {
/* 678 */           localStringBuffer.append(localXML.toString());
/*     */         }
/*     */       }
/*     */ 
/* 682 */       return localStringBuffer.toString();
/*     */     }
/* 684 */     return toXMLString();
/*     */   }
/*     */ 
/*     */   String toSource(int paramInt)
/*     */   {
/* 690 */     return toXMLString();
/*     */   }
/*     */ 
/*     */   String toXMLString()
/*     */   {
/* 696 */     StringBuffer localStringBuffer = new StringBuffer();
/*     */ 
/* 698 */     for (int i = 0; i < length(); i++) {
/* 699 */       if ((getProcessor().isPrettyPrinting()) && (i != 0)) {
/* 700 */         localStringBuffer.append('\n');
/*     */       }
/* 702 */       localStringBuffer.append(getXmlFromAnnotation(i).toXMLString());
/*     */     }
/* 704 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   Object valueOf()
/*     */   {
/* 709 */     return this;
/*     */   }
/*     */ 
/*     */   boolean equivalentXml(Object paramObject)
/*     */   {
/* 718 */     boolean bool = false;
/*     */ 
/* 721 */     if (((paramObject instanceof Undefined)) && (length() == 0)) {
/* 722 */       bool = true;
/* 723 */     } else if (length() == 1) {
/* 724 */       bool = getXmlFromAnnotation(0).equivalentXml(paramObject);
/* 725 */     } else if ((paramObject instanceof XMLList)) {
/* 726 */       XMLList localXMLList = (XMLList)paramObject;
/*     */ 
/* 728 */       if (localXMLList.length() == length()) {
/* 729 */         bool = true;
/*     */ 
/* 731 */         for (int i = 0; i < length(); i++) {
/* 732 */           if (!getXmlFromAnnotation(i).equivalentXml(localXMLList.getXmlFromAnnotation(i))) {
/* 733 */             bool = false;
/* 734 */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 740 */     return bool;
/*     */   }
/*     */ 
/*     */   private XMLList getPropertyList(XMLName paramXMLName) {
/* 744 */     XMLList localXMLList = newXMLList();
/* 745 */     XmlNode.QName localQName = null;
/*     */ 
/* 747 */     if ((!paramXMLName.isDescendants()) && (!paramXMLName.isAttributeName()))
/*     */     {
/* 750 */       localQName = paramXMLName.toQname();
/*     */     }
/*     */ 
/* 753 */     localXMLList.setTargets(this, localQName);
/*     */ 
/* 755 */     for (int i = 0; i < length(); i++) {
/* 756 */       localXMLList.addToList(getXmlFromAnnotation(i).getPropertyList(paramXMLName));
/*     */     }
/*     */ 
/* 760 */     return localXMLList;
/*     */   }
/*     */ 
/*     */   private Object applyOrCall(boolean paramBoolean, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject)
/*     */   {
/* 766 */     String str = paramBoolean ? "apply" : "call";
/* 767 */     if ((!(paramScriptable2 instanceof XMLList)) || (((XMLList)paramScriptable2).targetProperty == null))
/*     */     {
/* 769 */       throw ScriptRuntime.typeError1("msg.isnt.function", str);
/*     */     }
/*     */ 
/* 772 */     return ScriptRuntime.applyOrCall(paramBoolean, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   protected Object jsConstructor(Context paramContext, boolean paramBoolean, Object[] paramArrayOfObject)
/*     */   {
/* 779 */     if (paramArrayOfObject.length == 0) {
/* 780 */       return newXMLList();
/*     */     }
/* 782 */     Object localObject = paramArrayOfObject[0];
/* 783 */     if ((!paramBoolean) && ((localObject instanceof XMLList)))
/*     */     {
/* 785 */       return localObject;
/*     */     }
/* 787 */     return newXMLListFrom(localObject);
/*     */   }
/*     */ 
/*     */   public Scriptable getExtraMethodSource(Context paramContext)
/*     */   {
/* 796 */     if (length() == 1) {
/* 797 */       return getXmlFromAnnotation(0);
/*     */     }
/* 799 */     return null;
/*     */   }
/*     */ 
/*     */   public Object call(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject)
/*     */   {
/* 806 */     if (this.targetProperty == null) {
/* 807 */       throw ScriptRuntime.notFunctionError(this);
/*     */     }
/* 809 */     String str = this.targetProperty.getLocalName();
/*     */ 
/* 811 */     boolean bool = str.equals("apply");
/* 812 */     if ((bool) || (str.equals("call"))) {
/* 813 */       return applyOrCall(bool, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject);
/*     */     }
/* 815 */     Callable localCallable = ScriptRuntime.getElemFunctionAndThis(this, str, paramContext);
/*     */ 
/* 820 */     ScriptRuntime.lastStoredScriptable(paramContext);
/* 821 */     return localCallable.call(paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   public Scriptable construct(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject) {
/* 825 */     throw ScriptRuntime.typeError1("msg.not.ctor", "XMLList");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.xmlimpl.XMLList
 * JD-Core Version:    0.6.2
 */