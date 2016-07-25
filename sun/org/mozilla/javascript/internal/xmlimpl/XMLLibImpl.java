/*     */ package sun.org.mozilla.javascript.internal.xmlimpl;
/*     */ 
/*     */ import org.w3c.dom.Node;
/*     */ import org.xml.sax.SAXException;
/*     */ import sun.org.mozilla.javascript.internal.Context;
/*     */ import sun.org.mozilla.javascript.internal.Kit;
/*     */ import sun.org.mozilla.javascript.internal.Ref;
/*     */ import sun.org.mozilla.javascript.internal.ScriptRuntime;
/*     */ import sun.org.mozilla.javascript.internal.Scriptable;
/*     */ import sun.org.mozilla.javascript.internal.Undefined;
/*     */ import sun.org.mozilla.javascript.internal.Wrapper;
/*     */ import sun.org.mozilla.javascript.internal.xml.XMLLib;
/*     */ import sun.org.mozilla.javascript.internal.xml.XMLObject;
/*     */ 
/*     */ public final class XMLLibImpl extends XMLLib
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private Scriptable globalScope;
/*     */   private XML xmlPrototype;
/*     */   private XMLList xmlListPrototype;
/*     */   private Namespace namespacePrototype;
/*     */   private QName qnamePrototype;
/* 122 */   private XmlProcessor options = new XmlProcessor();
/*     */ 
/*     */   public static Node toDomNode(Object paramObject)
/*     */   {
/*  58 */     if ((paramObject instanceof XML)) {
/*  59 */       return ((XML)paramObject).toDomNode();
/*     */     }
/*  61 */     throw new IllegalArgumentException("xmlObject is not an XML object in JavaScript.");
/*     */   }
/*     */ 
/*     */   public static void init(Context paramContext, Scriptable paramScriptable, boolean paramBoolean)
/*     */   {
/*  67 */     XMLLibImpl localXMLLibImpl = new XMLLibImpl(paramScriptable);
/*  68 */     XMLLib localXMLLib = localXMLLibImpl.bindToScope(paramScriptable);
/*  69 */     if (localXMLLib == localXMLLibImpl)
/*  70 */       localXMLLibImpl.exportToScope(paramBoolean);
/*     */   }
/*     */ 
/*     */   public void setIgnoreComments(boolean paramBoolean)
/*     */   {
/*  75 */     this.options.setIgnoreComments(paramBoolean);
/*     */   }
/*     */ 
/*     */   public void setIgnoreWhitespace(boolean paramBoolean) {
/*  79 */     this.options.setIgnoreWhitespace(paramBoolean);
/*     */   }
/*     */ 
/*     */   public void setIgnoreProcessingInstructions(boolean paramBoolean) {
/*  83 */     this.options.setIgnoreProcessingInstructions(paramBoolean);
/*     */   }
/*     */ 
/*     */   public void setPrettyPrinting(boolean paramBoolean) {
/*  87 */     this.options.setPrettyPrinting(paramBoolean);
/*     */   }
/*     */ 
/*     */   public void setPrettyIndent(int paramInt) {
/*  91 */     this.options.setPrettyIndent(paramInt);
/*     */   }
/*     */ 
/*     */   public boolean isIgnoreComments() {
/*  95 */     return this.options.isIgnoreComments();
/*     */   }
/*     */ 
/*     */   public boolean isIgnoreProcessingInstructions() {
/*  99 */     return this.options.isIgnoreProcessingInstructions();
/*     */   }
/*     */ 
/*     */   public boolean isIgnoreWhitespace() {
/* 103 */     return this.options.isIgnoreWhitespace();
/*     */   }
/*     */ 
/*     */   public boolean isPrettyPrinting() {
/* 107 */     return this.options.isPrettyPrinting();
/*     */   }
/*     */ 
/*     */   public int getPrettyIndent() {
/* 111 */     return this.options.getPrettyIndent();
/*     */   }
/*     */ 
/*     */   private XMLLibImpl(Scriptable paramScriptable)
/*     */   {
/* 125 */     this.globalScope = paramScriptable;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   QName qnamePrototype() {
/* 130 */     return this.qnamePrototype;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   Scriptable globalScope() {
/* 135 */     return this.globalScope;
/*     */   }
/*     */ 
/*     */   XmlProcessor getProcessor() {
/* 139 */     return this.options;
/*     */   }
/*     */ 
/*     */   private void exportToScope(boolean paramBoolean) {
/* 143 */     this.xmlPrototype = newXML(XmlNode.createText(this.options, ""));
/* 144 */     this.xmlListPrototype = newXMLList();
/* 145 */     this.namespacePrototype = Namespace.create(this.globalScope, null, XmlNode.Namespace.GLOBAL);
/* 146 */     this.qnamePrototype = QName.create(this, this.globalScope, null, XmlNode.QName.create(XmlNode.Namespace.create(""), ""));
/*     */ 
/* 148 */     this.xmlPrototype.exportAsJSClass(paramBoolean);
/* 149 */     this.xmlListPrototype.exportAsJSClass(paramBoolean);
/* 150 */     this.namespacePrototype.exportAsJSClass(paramBoolean);
/* 151 */     this.qnamePrototype.exportAsJSClass(paramBoolean);
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   XMLName toAttributeName(Context paramContext, Object paramObject) {
/* 156 */     if ((paramObject instanceof XMLName))
/*     */     {
/* 158 */       return (XMLName)paramObject;
/* 159 */     }if ((paramObject instanceof QName))
/* 160 */       return XMLName.create(((QName)paramObject).getDelegate(), true, false);
/* 161 */     if (((paramObject instanceof Boolean)) || ((paramObject instanceof Number)) || (paramObject == Undefined.instance) || (paramObject == null))
/*     */     {
/* 165 */       throw badXMLName(paramObject);
/*     */     }
/*     */ 
/* 168 */     String str = null;
/* 169 */     if ((paramObject instanceof String))
/* 170 */       str = (String)paramObject;
/*     */     else {
/* 172 */       str = ScriptRuntime.toString(paramObject);
/*     */     }
/* 174 */     if ((str != null) && (str.equals("*"))) str = null;
/* 175 */     return XMLName.create(XmlNode.QName.create(XmlNode.Namespace.create(""), str), true, false);
/*     */   }
/*     */ 
/*     */   private static RuntimeException badXMLName(Object paramObject)
/*     */   {
/*     */     String str;
/* 182 */     if ((paramObject instanceof Number))
/* 183 */       str = "Can not construct XML name from number: ";
/* 184 */     else if ((paramObject instanceof Boolean))
/* 185 */       str = "Can not construct XML name from boolean: ";
/* 186 */     else if ((paramObject == Undefined.instance) || (paramObject == null))
/* 187 */       str = "Can not construct XML name from ";
/*     */     else {
/* 189 */       throw new IllegalArgumentException(paramObject.toString());
/*     */     }
/* 191 */     return ScriptRuntime.typeError(str + ScriptRuntime.toString(paramObject));
/*     */   }
/*     */ 
/*     */   XMLName toXMLNameFromString(Context paramContext, String paramString) {
/* 195 */     return XMLName.create(getDefaultNamespaceURI(paramContext), paramString);
/*     */   }
/*     */ 
/*     */   XMLName toXMLName(Context paramContext, Object paramObject)
/*     */   {
/*     */     XMLName localXMLName;
/* 202 */     if ((paramObject instanceof XMLName)) {
/* 203 */       localXMLName = (XMLName)paramObject;
/*     */     }
/*     */     else
/*     */     {
/*     */       Object localObject;
/* 204 */       if ((paramObject instanceof QName)) {
/* 205 */         localObject = (QName)paramObject;
/* 206 */         localXMLName = XMLName.formProperty(((QName)localObject).uri(), ((QName)localObject).localName());
/* 207 */       } else if ((paramObject instanceof String)) {
/* 208 */         localXMLName = toXMLNameFromString(paramContext, (String)paramObject); } else {
/* 209 */         if (((paramObject instanceof Boolean)) || ((paramObject instanceof Number)) || (paramObject == Undefined.instance) || (paramObject == null))
/*     */         {
/* 213 */           throw badXMLName(paramObject);
/*     */         }
/* 215 */         localObject = ScriptRuntime.toString(paramObject);
/* 216 */         localXMLName = toXMLNameFromString(paramContext, (String)localObject);
/*     */       }
/*     */     }
/* 219 */     return localXMLName;
/*     */   }
/*     */ 
/*     */   XMLName toXMLNameOrIndex(Context paramContext, Object paramObject)
/*     */   {
/*     */     XMLName localXMLName;
/* 231 */     if ((paramObject instanceof XMLName)) {
/* 232 */       localXMLName = (XMLName)paramObject;
/* 233 */     } else if ((paramObject instanceof String)) {
/* 234 */       String str1 = (String)paramObject;
/* 235 */       long l1 = ScriptRuntime.testUint32String(str1);
/* 236 */       if (l1 >= 0L) {
/* 237 */         ScriptRuntime.storeUint32Result(paramContext, l1);
/* 238 */         localXMLName = null;
/*     */       } else {
/* 240 */         localXMLName = toXMLNameFromString(paramContext, str1);
/*     */       }
/* 242 */     } else if ((paramObject instanceof Number)) {
/* 243 */       double d = ((Number)paramObject).doubleValue();
/* 244 */       long l3 = ()d;
/* 245 */       if ((l3 == d) && (0L <= l3) && (l3 <= 4294967295L)) {
/* 246 */         ScriptRuntime.storeUint32Result(paramContext, l3);
/* 247 */         localXMLName = null;
/*     */       } else {
/* 249 */         throw badXMLName(paramObject);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/*     */       Object localObject;
/* 251 */       if ((paramObject instanceof QName)) {
/* 252 */         localObject = (QName)paramObject;
/* 253 */         String str2 = ((QName)localObject).uri();
/* 254 */         int i = 0;
/* 255 */         localXMLName = null;
/* 256 */         if ((str2 != null) && (str2.length() == 0))
/*     */         {
/* 258 */           long l4 = ScriptRuntime.testUint32String(str2);
/* 259 */           if (l4 >= 0L) {
/* 260 */             ScriptRuntime.storeUint32Result(paramContext, l4);
/* 261 */             i = 1;
/*     */           }
/*     */         }
/* 264 */         if (i == 0)
/* 265 */           localXMLName = XMLName.formProperty(str2, ((QName)localObject).localName());
/*     */       } else {
/* 267 */         if (((paramObject instanceof Boolean)) || (paramObject == Undefined.instance) || (paramObject == null))
/*     */         {
/* 271 */           throw badXMLName(paramObject);
/*     */         }
/* 273 */         localObject = ScriptRuntime.toString(paramObject);
/* 274 */         long l2 = ScriptRuntime.testUint32String((String)localObject);
/* 275 */         if (l2 >= 0L) {
/* 276 */           ScriptRuntime.storeUint32Result(paramContext, l2);
/* 277 */           localXMLName = null;
/*     */         } else {
/* 279 */           localXMLName = toXMLNameFromString(paramContext, (String)localObject);
/*     */         }
/*     */       }
/*     */     }
/* 283 */     return localXMLName;
/*     */   }
/*     */ 
/*     */   Object addXMLObjects(Context paramContext, XMLObject paramXMLObject1, XMLObject paramXMLObject2)
/*     */   {
/* 288 */     XMLList localXMLList1 = newXMLList();
/*     */     XMLList localXMLList2;
/* 290 */     if ((paramXMLObject1 instanceof XMLList)) {
/* 291 */       localXMLList2 = (XMLList)paramXMLObject1;
/* 292 */       if (localXMLList2.length() == 1) {
/* 293 */         localXMLList1.addToList(localXMLList2.item(0));
/*     */       }
/*     */       else
/*     */       {
/* 298 */         localXMLList1 = newXMLListFrom(paramXMLObject1);
/*     */       }
/*     */     } else {
/* 301 */       localXMLList1.addToList(paramXMLObject1);
/*     */     }
/*     */ 
/* 304 */     if ((paramXMLObject2 instanceof XMLList)) {
/* 305 */       localXMLList2 = (XMLList)paramXMLObject2;
/* 306 */       for (int i = 0; i < localXMLList2.length(); i++)
/* 307 */         localXMLList1.addToList(localXMLList2.item(i));
/*     */     }
/* 309 */     else if ((paramXMLObject2 instanceof XML)) {
/* 310 */       localXMLList1.addToList(paramXMLObject2);
/*     */     }
/*     */ 
/* 313 */     return localXMLList1;
/*     */   }
/*     */ 
/*     */   private Ref xmlPrimaryReference(Context paramContext, XMLName paramXMLName, Scriptable paramScriptable)
/*     */   {
/* 318 */     Object localObject2 = null;
/*     */     do
/*     */     {
/* 322 */       if ((paramScriptable instanceof XMLWithScope)) {
/* 323 */         localObject1 = (XMLObjectImpl)paramScriptable.getPrototype();
/* 324 */         if (((XMLObjectImpl)localObject1).hasXMLProperty(paramXMLName)) {
/*     */           break;
/*     */         }
/* 327 */         if (localObject2 == null) {
/* 328 */           localObject2 = localObject1;
/*     */         }
/*     */       }
/* 331 */       paramScriptable = paramScriptable.getParentScope();
/* 332 */     }while (paramScriptable != null);
/* 333 */     Object localObject1 = localObject2;
/*     */ 
/* 340 */     if (localObject1 != null) {
/* 341 */       paramXMLName.initXMLObject((XMLObjectImpl)localObject1);
/*     */     }
/* 343 */     return paramXMLName;
/*     */   }
/*     */ 
/*     */   Namespace castToNamespace(Context paramContext, Object paramObject) {
/* 347 */     return this.namespacePrototype.castToNamespace(paramObject);
/*     */   }
/*     */ 
/*     */   private String getDefaultNamespaceURI(Context paramContext) {
/* 351 */     return getDefaultNamespace(paramContext).uri();
/*     */   }
/*     */ 
/*     */   Namespace newNamespace(String paramString) {
/* 355 */     return this.namespacePrototype.newNamespace(paramString);
/*     */   }
/*     */ 
/*     */   Namespace getDefaultNamespace(Context paramContext) {
/* 359 */     if (paramContext == null) {
/* 360 */       paramContext = Context.getCurrentContext();
/* 361 */       if (paramContext == null) {
/* 362 */         return this.namespacePrototype;
/*     */       }
/*     */     }
/*     */ 
/* 366 */     Object localObject = ScriptRuntime.searchDefaultNamespace(paramContext);
/* 367 */     if (localObject == null) {
/* 368 */       return this.namespacePrototype;
/*     */     }
/* 370 */     if ((localObject instanceof Namespace)) {
/* 371 */       return (Namespace)localObject;
/*     */     }
/*     */ 
/* 376 */     return this.namespacePrototype;
/*     */   }
/*     */ 
/*     */   Namespace[] createNamespaces(XmlNode.Namespace[] paramArrayOfNamespace)
/*     */   {
/* 382 */     Namespace[] arrayOfNamespace = new Namespace[paramArrayOfNamespace.length];
/* 383 */     for (int i = 0; i < paramArrayOfNamespace.length; i++) {
/* 384 */       arrayOfNamespace[i] = this.namespacePrototype.newNamespace(paramArrayOfNamespace[i].getPrefix(), paramArrayOfNamespace[i].getUri());
/*     */     }
/* 386 */     return arrayOfNamespace;
/*     */   }
/*     */ 
/*     */   QName constructQName(Context paramContext, Object paramObject1, Object paramObject2)
/*     */   {
/* 391 */     return this.qnamePrototype.constructQName(this, paramContext, paramObject1, paramObject2);
/*     */   }
/*     */ 
/*     */   QName newQName(String paramString1, String paramString2, String paramString3) {
/* 395 */     return this.qnamePrototype.newQName(this, paramString1, paramString2, paramString3);
/*     */   }
/*     */ 
/*     */   QName constructQName(Context paramContext, Object paramObject)
/*     */   {
/* 400 */     return this.qnamePrototype.constructQName(this, paramContext, paramObject);
/*     */   }
/*     */ 
/*     */   QName castToQName(Context paramContext, Object paramObject) {
/* 404 */     return this.qnamePrototype.castToQName(this, paramContext, paramObject);
/*     */   }
/*     */ 
/*     */   QName newQName(XmlNode.QName paramQName) {
/* 408 */     return QName.create(this, this.globalScope, this.qnamePrototype, paramQName);
/*     */   }
/*     */ 
/*     */   XML newXML(XmlNode paramXmlNode) {
/* 412 */     return new XML(this, this.globalScope, this.xmlPrototype, paramXmlNode);
/*     */   }
/*     */ 
/*     */   final XML newXMLFromJs(Object paramObject)
/*     */   {
/*     */     String str;
/* 420 */     if ((paramObject == null) || (paramObject == Undefined.instance))
/* 421 */       str = "";
/* 422 */     else if ((paramObject instanceof XMLObjectImpl))
/*     */     {
/* 424 */       str = ((XMLObjectImpl)paramObject).toXMLString();
/*     */     }
/* 426 */     else str = ScriptRuntime.toString(paramObject);
/*     */ 
/* 429 */     if (str.trim().startsWith("<>")) {
/* 430 */       throw ScriptRuntime.typeError("Invalid use of XML object anonymous tags <></>.");
/*     */     }
/*     */ 
/* 433 */     if (str.indexOf("<") == -1)
/*     */     {
/* 435 */       return newXML(XmlNode.createText(this.options, str));
/*     */     }
/* 437 */     return parse(str);
/*     */   }
/*     */ 
/*     */   private XML parse(String paramString) {
/*     */     try {
/* 442 */       return newXML(XmlNode.createElement(this.options, getDefaultNamespaceURI(Context.getCurrentContext()), paramString));
/*     */     } catch (SAXException localSAXException) {
/* 444 */       throw ScriptRuntime.typeError("Cannot parse XML: " + localSAXException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   final XML ecmaToXml(Object paramObject)
/*     */   {
/* 450 */     if ((paramObject == null) || (paramObject == Undefined.instance)) throw ScriptRuntime.typeError("Cannot convert " + paramObject + " to XML");
/* 451 */     if ((paramObject instanceof XML)) return (XML)paramObject;
/* 452 */     if ((paramObject instanceof XMLList)) {
/* 453 */       localObject = (XMLList)paramObject;
/* 454 */       if (((XMLList)localObject).getXML() != null) {
/* 455 */         return ((XMLList)localObject).getXML();
/*     */       }
/* 457 */       throw ScriptRuntime.typeError("Cannot convert list of >1 element to XML");
/*     */     }
/*     */ 
/* 464 */     if ((paramObject instanceof Wrapper)) {
/* 465 */       paramObject = ((Wrapper)paramObject).unwrap();
/*     */     }
/* 467 */     if ((paramObject instanceof Node)) {
/* 468 */       localObject = (Node)paramObject;
/* 469 */       return newXML(XmlNode.createElementFromNode((Node)localObject));
/*     */     }
/*     */ 
/* 472 */     Object localObject = ScriptRuntime.toString(paramObject);
/*     */ 
/* 474 */     if ((((String)localObject).length() > 0) && (((String)localObject).charAt(0) == '<')) {
/* 475 */       return parse((String)localObject);
/*     */     }
/* 477 */     return newXML(XmlNode.createText(this.options, (String)localObject));
/*     */   }
/*     */ 
/*     */   final XML newTextElementXML(XmlNode paramXmlNode, XmlNode.QName paramQName, String paramString)
/*     */   {
/* 482 */     return newXML(XmlNode.newElementWithText(this.options, paramXmlNode, paramQName, paramString));
/*     */   }
/*     */ 
/*     */   XMLList newXMLList() {
/* 486 */     return new XMLList(this, this.globalScope, this.xmlListPrototype);
/*     */   }
/*     */ 
/*     */   final XMLList newXMLListFrom(Object paramObject) {
/* 490 */     XMLList localXMLList1 = newXMLList();
/*     */ 
/* 492 */     if ((paramObject == null) || ((paramObject instanceof Undefined)))
/* 493 */       return localXMLList1;
/* 494 */     if ((paramObject instanceof XML)) {
/* 495 */       localObject = (XML)paramObject;
/* 496 */       localXMLList1.getNodeList().add((XML)localObject);
/* 497 */       return localXMLList1;
/* 498 */     }if ((paramObject instanceof XMLList)) {
/* 499 */       localObject = (XMLList)paramObject;
/* 500 */       localXMLList1.getNodeList().add(((XMLList)localObject).getNodeList());
/* 501 */       return localXMLList1;
/*     */     }
/* 503 */     Object localObject = ScriptRuntime.toString(paramObject).trim();
/*     */ 
/* 505 */     if (!((String)localObject).startsWith("<>")) {
/* 506 */       localObject = "<>" + (String)localObject + "</>";
/*     */     }
/*     */ 
/* 509 */     localObject = "<fragment>" + ((String)localObject).substring(2);
/* 510 */     if (!((String)localObject).endsWith("</>")) {
/* 511 */       throw ScriptRuntime.typeError("XML with anonymous tag missing end anonymous tag");
/*     */     }
/*     */ 
/* 514 */     localObject = ((String)localObject).substring(0, ((String)localObject).length() - 3) + "</fragment>";
/*     */ 
/* 516 */     XML localXML = newXMLFromJs(localObject);
/*     */ 
/* 519 */     XMLList localXMLList2 = localXML.children();
/*     */ 
/* 521 */     for (int i = 0; i < localXMLList2.getNodeList().length(); i++)
/*     */     {
/* 523 */       localXMLList1.getNodeList().add((XML)localXMLList2.item(i).copy());
/*     */     }
/* 525 */     return localXMLList1;
/*     */   }
/*     */ 
/*     */   XmlNode.QName toNodeQName(Context paramContext, Object paramObject1, Object paramObject2)
/*     */   {
/*     */     Object localObject;
/*     */     String str;
/* 535 */     if ((paramObject2 instanceof QName)) {
/* 536 */       localObject = (QName)paramObject2;
/* 537 */       str = ((QName)localObject).localName();
/*     */     } else {
/* 539 */       str = ScriptRuntime.toString(paramObject2);
/*     */     }
/*     */ 
/* 543 */     if (paramObject1 == Undefined.instance) {
/* 544 */       if ("*".equals(str))
/* 545 */         localObject = null;
/*     */       else
/* 547 */         localObject = getDefaultNamespace(paramContext).getDelegate();
/*     */     }
/* 549 */     else if (paramObject1 == null)
/* 550 */       localObject = null;
/* 551 */     else if ((paramObject1 instanceof Namespace))
/* 552 */       localObject = ((Namespace)paramObject1).getDelegate();
/*     */     else {
/* 554 */       localObject = this.namespacePrototype.constructNamespace(paramObject1).getDelegate();
/*     */     }
/*     */ 
/* 557 */     if ((str != null) && (str.equals("*"))) str = null;
/* 558 */     return XmlNode.QName.create((XmlNode.Namespace)localObject, str);
/*     */   }
/*     */ 
/*     */   XmlNode.QName toNodeQName(Context paramContext, String paramString, boolean paramBoolean) {
/* 562 */     XmlNode.Namespace localNamespace = getDefaultNamespace(paramContext).getDelegate();
/* 563 */     if ((paramString != null) && (paramString.equals("*"))) {
/* 564 */       return XmlNode.QName.create(null, null);
/*     */     }
/* 566 */     if (paramBoolean) {
/* 567 */       return XmlNode.QName.create(XmlNode.Namespace.GLOBAL, paramString);
/*     */     }
/* 569 */     return XmlNode.QName.create(localNamespace, paramString);
/*     */   }
/*     */ 
/*     */   XmlNode.QName toNodeQName(Context paramContext, Object paramObject, boolean paramBoolean)
/*     */   {
/* 579 */     if ((paramObject instanceof XMLName))
/* 580 */       return ((XMLName)paramObject).toQname();
/* 581 */     if ((paramObject instanceof QName)) {
/* 582 */       localObject = (QName)paramObject;
/* 583 */       return ((QName)localObject).getDelegate();
/* 584 */     }if (((paramObject instanceof Boolean)) || ((paramObject instanceof Number)) || (paramObject == Undefined.instance) || (paramObject == null))
/*     */     {
/* 590 */       throw badXMLName(paramObject);
/*     */     }
/* 592 */     Object localObject = null;
/* 593 */     if ((paramObject instanceof String))
/* 594 */       localObject = (String)paramObject;
/*     */     else {
/* 596 */       localObject = ScriptRuntime.toString(paramObject);
/*     */     }
/* 598 */     return toNodeQName(paramContext, (String)localObject, paramBoolean);
/*     */   }
/*     */ 
/*     */   public boolean isXMLName(Context paramContext, Object paramObject)
/*     */   {
/* 608 */     return XMLName.accept(paramObject);
/*     */   }
/*     */ 
/*     */   public Object toDefaultXmlNamespace(Context paramContext, Object paramObject)
/*     */   {
/* 613 */     return this.namespacePrototype.constructNamespace(paramObject);
/*     */   }
/*     */ 
/*     */   public String escapeTextValue(Object paramObject)
/*     */   {
/* 618 */     return this.options.escapeTextValue(paramObject);
/*     */   }
/*     */ 
/*     */   public String escapeAttributeValue(Object paramObject)
/*     */   {
/* 623 */     return this.options.escapeAttributeValue(paramObject);
/*     */   }
/*     */ 
/*     */   public Ref nameRef(Context paramContext, Object paramObject, Scriptable paramScriptable, int paramInt)
/*     */   {
/* 628 */     if ((paramInt & 0x2) == 0)
/*     */     {
/* 630 */       throw Kit.codeBug();
/*     */     }
/* 632 */     XMLName localXMLName = toAttributeName(paramContext, paramObject);
/* 633 */     return xmlPrimaryReference(paramContext, localXMLName, paramScriptable);
/*     */   }
/*     */ 
/*     */   public Ref nameRef(Context paramContext, Object paramObject1, Object paramObject2, Scriptable paramScriptable, int paramInt)
/*     */   {
/* 638 */     XMLName localXMLName = XMLName.create(toNodeQName(paramContext, paramObject1, paramObject2), false, false);
/*     */ 
/* 641 */     if (((paramInt & 0x2) != 0) && 
/* 642 */       (!localXMLName.isAttributeName())) {
/* 643 */       localXMLName.setAttributeName();
/*     */     }
/*     */ 
/* 647 */     return xmlPrimaryReference(paramContext, localXMLName, paramScriptable);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.xmlimpl.XMLLibImpl
 * JD-Core Version:    0.6.2
 */