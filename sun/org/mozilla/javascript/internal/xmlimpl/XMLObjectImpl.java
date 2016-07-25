/*     */ package sun.org.mozilla.javascript.internal.xmlimpl;
/*     */ 
/*     */ import sun.org.mozilla.javascript.internal.Context;
/*     */ import sun.org.mozilla.javascript.internal.IdFunctionObject;
/*     */ import sun.org.mozilla.javascript.internal.Kit;
/*     */ import sun.org.mozilla.javascript.internal.NativeWith;
/*     */ import sun.org.mozilla.javascript.internal.Ref;
/*     */ import sun.org.mozilla.javascript.internal.ScriptRuntime;
/*     */ import sun.org.mozilla.javascript.internal.Scriptable;
/*     */ import sun.org.mozilla.javascript.internal.Undefined;
/*     */ import sun.org.mozilla.javascript.internal.xml.XMLObject;
/*     */ 
/*     */ abstract class XMLObjectImpl extends XMLObject
/*     */ {
/*  55 */   private static final Object XMLOBJECT_TAG = "XMLObject";
/*     */   private XMLLibImpl lib;
/*     */   private boolean prototypeFlag;
/*     */   private static final int Id_constructor = 1;
/*     */   private static final int Id_addNamespace = 2;
/*     */   private static final int Id_appendChild = 3;
/*     */   private static final int Id_attribute = 4;
/*     */   private static final int Id_attributes = 5;
/*     */   private static final int Id_child = 6;
/*     */   private static final int Id_childIndex = 7;
/*     */   private static final int Id_children = 8;
/*     */   private static final int Id_comments = 9;
/*     */   private static final int Id_contains = 10;
/*     */   private static final int Id_copy = 11;
/*     */   private static final int Id_descendants = 12;
/*     */   private static final int Id_elements = 13;
/*     */   private static final int Id_inScopeNamespaces = 14;
/*     */   private static final int Id_insertChildAfter = 15;
/*     */   private static final int Id_insertChildBefore = 16;
/*     */   private static final int Id_hasOwnProperty = 17;
/*     */   private static final int Id_hasComplexContent = 18;
/*     */   private static final int Id_hasSimpleContent = 19;
/*     */   private static final int Id_length = 20;
/*     */   private static final int Id_localName = 21;
/*     */   private static final int Id_name = 22;
/*     */   private static final int Id_namespace = 23;
/*     */   private static final int Id_namespaceDeclarations = 24;
/*     */   private static final int Id_nodeKind = 25;
/*     */   private static final int Id_normalize = 26;
/*     */   private static final int Id_parent = 27;
/*     */   private static final int Id_prependChild = 28;
/*     */   private static final int Id_processingInstructions = 29;
/*     */   private static final int Id_propertyIsEnumerable = 30;
/*     */   private static final int Id_removeNamespace = 31;
/*     */   private static final int Id_replace = 32;
/*     */   private static final int Id_setChildren = 33;
/*     */   private static final int Id_setLocalName = 34;
/*     */   private static final int Id_setName = 35;
/*     */   private static final int Id_setNamespace = 36;
/*     */   private static final int Id_text = 37;
/*     */   private static final int Id_toString = 38;
/*     */   private static final int Id_toSource = 39;
/*     */   private static final int Id_toXMLString = 40;
/*     */   private static final int Id_valueOf = 41;
/*     */   private static final int MAX_PROTOTYPE_ID = 41;
/*     */ 
/*     */   protected XMLObjectImpl(XMLLibImpl paramXMLLibImpl, Scriptable paramScriptable, XMLObject paramXMLObject)
/*     */   {
/*  62 */     initialize(paramXMLLibImpl, paramScriptable, paramXMLObject);
/*     */   }
/*     */ 
/*     */   final void initialize(XMLLibImpl paramXMLLibImpl, Scriptable paramScriptable, XMLObject paramXMLObject)
/*     */   {
/*  68 */     setParentScope(paramScriptable);
/*  69 */     setPrototype(paramXMLObject);
/*  70 */     this.prototypeFlag = (paramXMLObject == null);
/*  71 */     this.lib = paramXMLLibImpl;
/*     */   }
/*     */ 
/*     */   final boolean isPrototype() {
/*  75 */     return this.prototypeFlag;
/*     */   }
/*     */ 
/*     */   XMLLibImpl getLib() {
/*  79 */     return this.lib;
/*     */   }
/*     */ 
/*     */   final XML newXML(XmlNode paramXmlNode) {
/*  83 */     return this.lib.newXML(paramXmlNode);
/*     */   }
/*     */ 
/*     */   XML xmlFromNode(XmlNode paramXmlNode) {
/*  87 */     if (paramXmlNode.getXml() == null) {
/*  88 */       paramXmlNode.setXml(newXML(paramXmlNode));
/*     */     }
/*  90 */     return paramXmlNode.getXml();
/*     */   }
/*     */ 
/*     */   final XMLList newXMLList() {
/*  94 */     return this.lib.newXMLList();
/*     */   }
/*     */ 
/*     */   final XMLList newXMLListFrom(Object paramObject) {
/*  98 */     return this.lib.newXMLListFrom(paramObject);
/*     */   }
/*     */ 
/*     */   final XmlProcessor getProcessor() {
/* 102 */     return this.lib.getProcessor();
/*     */   }
/*     */ 
/*     */   final QName newQName(String paramString1, String paramString2, String paramString3) {
/* 106 */     return this.lib.newQName(paramString1, paramString2, paramString3);
/*     */   }
/*     */ 
/*     */   final QName newQName(XmlNode.QName paramQName) {
/* 110 */     return this.lib.newQName(paramQName);
/*     */   }
/*     */ 
/*     */   final Namespace createNamespace(XmlNode.Namespace paramNamespace) {
/* 114 */     if (paramNamespace == null) return null;
/* 115 */     return this.lib.createNamespaces(new XmlNode.Namespace[] { paramNamespace })[0];
/*     */   }
/*     */ 
/*     */   final Namespace[] createNamespaces(XmlNode.Namespace[] paramArrayOfNamespace) {
/* 119 */     return this.lib.createNamespaces(paramArrayOfNamespace);
/*     */   }
/*     */ 
/*     */   public final Object get(String paramString, Scriptable paramScriptable)
/*     */   {
/* 128 */     return super.get(paramString, paramScriptable);
/*     */   }
/*     */ 
/*     */   public final boolean has(String paramString, Scriptable paramScriptable)
/*     */   {
/* 133 */     return super.has(paramString, paramScriptable);
/*     */   }
/*     */ 
/*     */   public final void put(String paramString, Scriptable paramScriptable, Object paramObject)
/*     */   {
/* 138 */     super.put(paramString, paramScriptable, paramObject);
/*     */   }
/*     */ 
/*     */   public final void delete(String paramString)
/*     */   {
/* 144 */     throw new IllegalArgumentException("String: [" + paramString + "]");
/*     */   }
/*     */ 
/*     */   public final Scriptable getPrototype()
/*     */   {
/* 149 */     return super.getPrototype();
/*     */   }
/*     */ 
/*     */   public final void setPrototype(Scriptable paramScriptable)
/*     */   {
/* 154 */     super.setPrototype(paramScriptable);
/*     */   }
/*     */ 
/*     */   public final Scriptable getParentScope()
/*     */   {
/* 159 */     return super.getParentScope();
/*     */   }
/*     */ 
/*     */   public final void setParentScope(Scriptable paramScriptable)
/*     */   {
/* 164 */     super.setParentScope(paramScriptable);
/*     */   }
/*     */ 
/*     */   public final Object getDefaultValue(Class<?> paramClass)
/*     */   {
/* 169 */     return toString();
/*     */   }
/*     */ 
/*     */   public final boolean hasInstance(Scriptable paramScriptable)
/*     */   {
/* 174 */     return super.hasInstance(paramScriptable);
/*     */   }
/*     */ 
/*     */   abstract boolean hasXMLProperty(XMLName paramXMLName);
/*     */ 
/*     */   abstract Object getXMLProperty(XMLName paramXMLName);
/*     */ 
/*     */   abstract void putXMLProperty(XMLName paramXMLName, Object paramObject);
/*     */ 
/*     */   abstract void deleteXMLProperty(XMLName paramXMLName);
/*     */ 
/*     */   abstract boolean equivalentXml(Object paramObject);
/*     */ 
/*     */   abstract void addMatches(XMLList paramXMLList, XMLName paramXMLName);
/*     */ 
/*     */   private XMLList getMatches(XMLName paramXMLName)
/*     */   {
/* 209 */     XMLList localXMLList = newXMLList();
/* 210 */     addMatches(localXMLList, paramXMLName);
/* 211 */     return localXMLList; } 
/*     */   abstract XML getXML();
/*     */ 
/*     */   abstract XMLList child(int paramInt);
/*     */ 
/*     */   abstract XMLList child(XMLName paramXMLName);
/*     */ 
/*     */   abstract XMLList children();
/*     */ 
/*     */   abstract XMLList comments();
/*     */ 
/*     */   abstract boolean contains(Object paramObject);
/*     */ 
/*     */   abstract XMLObjectImpl copy();
/*     */ 
/*     */   abstract XMLList elements(XMLName paramXMLName);
/*     */ 
/*     */   abstract boolean hasOwnProperty(XMLName paramXMLName);
/*     */ 
/*     */   abstract boolean hasComplexContent();
/*     */ 
/*     */   abstract boolean hasSimpleContent();
/*     */ 
/*     */   abstract int length();
/*     */ 
/*     */   abstract void normalize();
/*     */ 
/*     */   abstract Object parent();
/*     */ 
/*     */   abstract XMLList processingInstructions(XMLName paramXMLName);
/*     */ 
/*     */   abstract boolean propertyIsEnumerable(Object paramObject);
/*     */ 
/*     */   abstract XMLList text();
/*     */ 
/*     */   public abstract String toString();
/*     */ 
/*     */   abstract String toSource(int paramInt);
/*     */ 
/*     */   abstract String toXMLString();
/*     */ 
/*     */   abstract Object valueOf();
/*     */ 
/*     */   protected abstract Object jsConstructor(Context paramContext, boolean paramBoolean, Object[] paramArrayOfObject);
/*     */ 
/* 242 */   final Object getMethod(String paramString) { return super.get(paramString, this); }
/*     */ 
/*     */ 
/*     */   protected final Object equivalentValues(Object paramObject)
/*     */   {
/* 258 */     boolean bool = equivalentXml(paramObject);
/* 259 */     return bool ? Boolean.TRUE : Boolean.FALSE;
/*     */   }
/*     */ 
/*     */   public final boolean ecmaHas(Context paramContext, Object paramObject)
/*     */   {
/* 273 */     if (paramContext == null) paramContext = Context.getCurrentContext();
/* 274 */     XMLName localXMLName = this.lib.toXMLNameOrIndex(paramContext, paramObject);
/* 275 */     if (localXMLName == null) {
/* 276 */       long l = ScriptRuntime.lastUint32Result(paramContext);
/*     */ 
/* 278 */       return has((int)l, this);
/*     */     }
/* 280 */     return hasXMLProperty(localXMLName);
/*     */   }
/*     */ 
/*     */   public final Object ecmaGet(Context paramContext, Object paramObject)
/*     */   {
/* 288 */     if (paramContext == null) paramContext = Context.getCurrentContext();
/* 289 */     XMLName localXMLName = this.lib.toXMLNameOrIndex(paramContext, paramObject);
/* 290 */     if (localXMLName == null) {
/* 291 */       long l = ScriptRuntime.lastUint32Result(paramContext);
/*     */ 
/* 293 */       Object localObject = get((int)l, this);
/* 294 */       if (localObject == Scriptable.NOT_FOUND) {
/* 295 */         localObject = Undefined.instance;
/*     */       }
/* 297 */       return localObject;
/*     */     }
/* 299 */     return getXMLProperty(localXMLName);
/*     */   }
/*     */ 
/*     */   public final void ecmaPut(Context paramContext, Object paramObject1, Object paramObject2)
/*     */   {
/* 307 */     if (paramContext == null) paramContext = Context.getCurrentContext();
/* 308 */     XMLName localXMLName = this.lib.toXMLNameOrIndex(paramContext, paramObject1);
/* 309 */     if (localXMLName == null) {
/* 310 */       long l = ScriptRuntime.lastUint32Result(paramContext);
/*     */ 
/* 312 */       put((int)l, this, paramObject2);
/* 313 */       return;
/*     */     }
/* 315 */     putXMLProperty(localXMLName, paramObject2);
/*     */   }
/*     */ 
/*     */   public final boolean ecmaDelete(Context paramContext, Object paramObject)
/*     */   {
/* 323 */     if (paramContext == null) paramContext = Context.getCurrentContext();
/* 324 */     XMLName localXMLName = this.lib.toXMLNameOrIndex(paramContext, paramObject);
/* 325 */     if (localXMLName == null) {
/* 326 */       long l = ScriptRuntime.lastUint32Result(paramContext);
/*     */ 
/* 328 */       delete((int)l);
/* 329 */       return true;
/*     */     }
/* 331 */     deleteXMLProperty(localXMLName);
/* 332 */     return true;
/*     */   }
/*     */ 
/*     */   public Ref memberRef(Context paramContext, Object paramObject, int paramInt)
/*     */   {
/* 338 */     boolean bool1 = (paramInt & 0x2) != 0;
/* 339 */     boolean bool2 = (paramInt & 0x4) != 0;
/* 340 */     if ((!bool1) && (!bool2))
/*     */     {
/* 344 */       throw Kit.codeBug();
/*     */     }
/* 346 */     XmlNode.QName localQName = this.lib.toNodeQName(paramContext, paramObject, bool1);
/* 347 */     XMLName localXMLName = XMLName.create(localQName, bool1, bool2);
/* 348 */     localXMLName.initXMLObject(this);
/* 349 */     return localXMLName;
/*     */   }
/*     */ 
/*     */   public Ref memberRef(Context paramContext, Object paramObject1, Object paramObject2, int paramInt)
/*     */   {
/* 359 */     boolean bool1 = (paramInt & 0x2) != 0;
/* 360 */     boolean bool2 = (paramInt & 0x4) != 0;
/* 361 */     XMLName localXMLName = XMLName.create(this.lib.toNodeQName(paramContext, paramObject1, paramObject2), bool1, bool2);
/*     */ 
/* 363 */     localXMLName.initXMLObject(this);
/* 364 */     return localXMLName;
/*     */   }
/*     */ 
/*     */   public NativeWith enterWith(Scriptable paramScriptable)
/*     */   {
/* 369 */     return new XMLWithScope(this.lib, paramScriptable, this);
/*     */   }
/*     */ 
/*     */   public NativeWith enterDotQuery(Scriptable paramScriptable)
/*     */   {
/* 374 */     XMLWithScope localXMLWithScope = new XMLWithScope(this.lib, paramScriptable, this);
/* 375 */     localXMLWithScope.initAsDotQuery();
/* 376 */     return localXMLWithScope;
/*     */   }
/*     */ 
/*     */   public final Object addValues(Context paramContext, boolean paramBoolean, Object paramObject)
/*     */   {
/* 382 */     if ((paramObject instanceof XMLObject))
/*     */     {
/*     */       Object localObject1;
/*     */       Object localObject2;
/* 384 */       if (paramBoolean) {
/* 385 */         localObject1 = this;
/* 386 */         localObject2 = (XMLObject)paramObject;
/*     */       } else {
/* 388 */         localObject1 = (XMLObject)paramObject;
/* 389 */         localObject2 = this;
/*     */       }
/* 391 */       return this.lib.addXMLObjects(paramContext, (XMLObject)localObject1, (XMLObject)localObject2);
/*     */     }
/* 393 */     if (paramObject == Undefined.instance)
/*     */     {
/* 395 */       return ScriptRuntime.toString(this);
/*     */     }
/*     */ 
/* 398 */     return super.addValues(paramContext, paramBoolean, paramObject);
/*     */   }
/*     */ 
/*     */   final void exportAsJSClass(boolean paramBoolean)
/*     */   {
/* 408 */     this.prototypeFlag = true;
/* 409 */     exportAsJSClass(41, getParentScope(), paramBoolean);
/*     */   }
/*     */ 
/*     */   protected int findPrototypeId(String paramString)
/*     */   {
/* 463 */     int i = 0; String str = null;
/*     */     int j;
/* 464 */     switch (paramString.length()) { case 4:
/* 465 */       j = paramString.charAt(0);
/* 466 */       if (j == 99) { str = "copy"; i = 11;
/* 467 */       } else if (j == 110) { str = "name"; i = 22;
/* 468 */       } else if (j == 116) { str = "text"; i = 37; } break;
/*     */     case 5:
/* 470 */       str = "child"; i = 6; break;
/*     */     case 6:
/* 471 */       j = paramString.charAt(0);
/* 472 */       if (j == 108) { str = "length"; i = 20;
/* 473 */       } else if (j == 112) { str = "parent"; i = 27; } break;
/*     */     case 7:
/* 475 */       j = paramString.charAt(0);
/* 476 */       if (j == 114) { str = "replace"; i = 32;
/* 477 */       } else if (j == 115) { str = "setName"; i = 35;
/* 478 */       } else if (j == 118) { str = "valueOf"; i = 41; } break;
/*     */     case 8:
/* 480 */       switch (paramString.charAt(2)) { case 'S':
/* 481 */         j = paramString.charAt(7);
/* 482 */         if (j == 101) { str = "toSource"; i = 39;
/* 483 */         } else if (j == 103) { str = "toString"; i = 38; } break;
/*     */       case 'd':
/* 485 */         str = "nodeKind"; i = 25; break;
/*     */       case 'e':
/* 486 */         str = "elements"; i = 13; break;
/*     */       case 'i':
/* 487 */         str = "children"; i = 8; break;
/*     */       case 'm':
/* 488 */         str = "comments"; i = 9; break;
/*     */       case 'n':
/* 489 */         str = "contains"; i = 10; }
/* 490 */       break;
/*     */     case 9:
/* 491 */       switch (paramString.charAt(2)) { case 'c':
/* 492 */         str = "localName"; i = 21; break;
/*     */       case 'm':
/* 493 */         str = "namespace"; i = 23; break;
/*     */       case 'r':
/* 494 */         str = "normalize"; i = 26; break;
/*     */       case 't':
/* 495 */         str = "attribute"; i = 4; }
/* 496 */       break;
/*     */     case 10:
/* 497 */       j = paramString.charAt(0);
/* 498 */       if (j == 97) { str = "attributes"; i = 5;
/* 499 */       } else if (j == 99) { str = "childIndex"; i = 7; } break;
/*     */     case 11:
/* 501 */       switch (paramString.charAt(0)) { case 'a':
/* 502 */         str = "appendChild"; i = 3; break;
/*     */       case 'c':
/* 503 */         str = "constructor"; i = 1; break;
/*     */       case 'd':
/* 504 */         str = "descendants"; i = 12; break;
/*     */       case 's':
/* 505 */         str = "setChildren"; i = 33; break;
/*     */       case 't':
/* 506 */         str = "toXMLString"; i = 40; }
/* 507 */       break;
/*     */     case 12:
/* 508 */       j = paramString.charAt(0);
/* 509 */       if (j == 97) { str = "addNamespace"; i = 2;
/* 510 */       } else if (j == 112) { str = "prependChild"; i = 28;
/* 511 */       } else if (j == 115) {
/* 512 */         j = paramString.charAt(3);
/* 513 */         if (j == 76) { str = "setLocalName"; i = 34;
/* 514 */         } else if (j == 78) { str = "setNamespace"; i = 36; }  } break;
/*     */     case 14:
/* 517 */       str = "hasOwnProperty"; i = 17; break;
/*     */     case 15:
/* 518 */       str = "removeNamespace"; i = 31; break;
/*     */     case 16:
/* 519 */       j = paramString.charAt(0);
/* 520 */       if (j == 104) { str = "hasSimpleContent"; i = 19;
/* 521 */       } else if (j == 105) { str = "insertChildAfter"; i = 15; } break;
/*     */     case 17:
/* 523 */       j = paramString.charAt(3);
/* 524 */       if (j == 67) { str = "hasComplexContent"; i = 18;
/* 525 */       } else if (j == 99) { str = "inScopeNamespaces"; i = 14;
/* 526 */       } else if (j == 101) { str = "insertChildBefore"; i = 16; } break;
/*     */     case 20:
/* 528 */       str = "propertyIsEnumerable"; i = 30; break;
/*     */     case 21:
/* 529 */       str = "namespaceDeclarations"; i = 24; break;
/*     */     case 22:
/* 530 */       str = "processingInstructions"; i = 29; break;
/*     */     case 13:
/*     */     case 18:
/* 532 */     case 19: } if ((str != null) && (str != paramString) && (!str.equals(paramString))) i = 0;
/*     */ 
/* 536 */     return i;
/*     */   }
/*     */ 
/*     */   protected void initPrototypeId(int paramInt)
/*     */   {
/*     */     int i;
/*     */     String str;
/* 544 */     switch (paramInt)
/*     */     {
/*     */     case 1:
/*     */       Object localObject;
/* 547 */       if ((this instanceof XML))
/* 548 */         localObject = new XMLCtor((XML)this, XMLOBJECT_TAG, paramInt, 1);
/*     */       else {
/* 550 */         localObject = new IdFunctionObject(this, XMLOBJECT_TAG, paramInt, 1);
/*     */       }
/* 552 */       initPrototypeConstructor((IdFunctionObject)localObject);
/* 553 */       return;
/*     */     case 2:
/* 556 */       i = 1; str = "addNamespace"; break;
/*     */     case 3:
/* 557 */       i = 1; str = "appendChild"; break;
/*     */     case 4:
/* 558 */       i = 1; str = "attribute"; break;
/*     */     case 5:
/* 559 */       i = 0; str = "attributes"; break;
/*     */     case 6:
/* 560 */       i = 1; str = "child"; break;
/*     */     case 7:
/* 561 */       i = 0; str = "childIndex"; break;
/*     */     case 8:
/* 562 */       i = 0; str = "children"; break;
/*     */     case 9:
/* 563 */       i = 0; str = "comments"; break;
/*     */     case 10:
/* 564 */       i = 1; str = "contains"; break;
/*     */     case 11:
/* 565 */       i = 0; str = "copy"; break;
/*     */     case 12:
/* 566 */       i = 1; str = "descendants"; break;
/*     */     case 13:
/* 567 */       i = 1; str = "elements"; break;
/*     */     case 18:
/* 568 */       i = 0; str = "hasComplexContent"; break;
/*     */     case 17:
/* 569 */       i = 1; str = "hasOwnProperty"; break;
/*     */     case 19:
/* 570 */       i = 0; str = "hasSimpleContent"; break;
/*     */     case 14:
/* 571 */       i = 0; str = "inScopeNamespaces"; break;
/*     */     case 15:
/* 572 */       i = 2; str = "insertChildAfter"; break;
/*     */     case 16:
/* 573 */       i = 2; str = "insertChildBefore"; break;
/*     */     case 20:
/* 574 */       i = 0; str = "length"; break;
/*     */     case 21:
/* 575 */       i = 0; str = "localName"; break;
/*     */     case 22:
/* 576 */       i = 0; str = "name"; break;
/*     */     case 23:
/* 577 */       i = 1; str = "namespace"; break;
/*     */     case 24:
/* 579 */       i = 0; str = "namespaceDeclarations"; break;
/*     */     case 25:
/* 580 */       i = 0; str = "nodeKind"; break;
/*     */     case 26:
/* 581 */       i = 0; str = "normalize"; break;
/*     */     case 27:
/* 582 */       i = 0; str = "parent"; break;
/*     */     case 28:
/* 583 */       i = 1; str = "prependChild"; break;
/*     */     case 29:
/* 585 */       i = 1; str = "processingInstructions"; break;
/*     */     case 30:
/* 587 */       i = 1; str = "propertyIsEnumerable"; break;
/*     */     case 31:
/* 588 */       i = 1; str = "removeNamespace"; break;
/*     */     case 32:
/* 589 */       i = 2; str = "replace"; break;
/*     */     case 33:
/* 590 */       i = 1; str = "setChildren"; break;
/*     */     case 34:
/* 591 */       i = 1; str = "setLocalName"; break;
/*     */     case 35:
/* 592 */       i = 1; str = "setName"; break;
/*     */     case 36:
/* 593 */       i = 1; str = "setNamespace"; break;
/*     */     case 37:
/* 594 */       i = 0; str = "text"; break;
/*     */     case 38:
/* 595 */       i = 0; str = "toString"; break;
/*     */     case 39:
/* 596 */       i = 1; str = "toSource"; break;
/*     */     case 40:
/* 597 */       i = 1; str = "toXMLString"; break;
/*     */     case 41:
/* 598 */       i = 0; str = "valueOf"; break;
/*     */     default:
/* 600 */       throw new IllegalArgumentException(String.valueOf(paramInt));
/*     */     }
/* 602 */     initPrototypeMethod(XMLOBJECT_TAG, paramInt, str, i);
/*     */   }
/*     */ 
/*     */   private Object[] toObjectArray(Object[] paramArrayOfObject) {
/* 606 */     Object[] arrayOfObject = new Object[paramArrayOfObject.length];
/* 607 */     for (int i = 0; i < arrayOfObject.length; i++) {
/* 608 */       arrayOfObject[i] = paramArrayOfObject[i];
/*     */     }
/* 610 */     return arrayOfObject;
/*     */   }
/*     */ 
/*     */   private void xmlMethodNotFound(Object paramObject, String paramString) {
/* 614 */     throw ScriptRuntime.notFunctionError(paramObject, paramString);
/*     */   }
/*     */ 
/*     */   public Object execIdCall(IdFunctionObject paramIdFunctionObject, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject)
/*     */   {
/* 621 */     if (!paramIdFunctionObject.hasTag(XMLOBJECT_TAG)) {
/* 622 */       return super.execIdCall(paramIdFunctionObject, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject);
/*     */     }
/* 624 */     int i = paramIdFunctionObject.methodId();
/* 625 */     if (i == 1) {
/* 626 */       return jsConstructor(paramContext, paramScriptable2 == null, paramArrayOfObject);
/*     */     }
/*     */ 
/* 630 */     if (!(paramScriptable2 instanceof XMLObjectImpl))
/* 631 */       throw incompatibleCallError(paramIdFunctionObject);
/* 632 */     XMLObjectImpl localXMLObjectImpl = (XMLObjectImpl)paramScriptable2;
/*     */ 
/* 634 */     XML localXML = localXMLObjectImpl.getXML();
/*     */     Object localObject1;
/*     */     Object localObject2;
/* 635 */     switch (i) {
/*     */     case 3:
/* 637 */       if (localXML == null) xmlMethodNotFound(localXMLObjectImpl, "appendChild");
/* 638 */       return localXML.appendChild(arg(paramArrayOfObject, 0));
/*     */     case 2:
/* 641 */       if (localXML == null) xmlMethodNotFound(localXMLObjectImpl, "addNamespace");
/* 642 */       localObject1 = this.lib.castToNamespace(paramContext, arg(paramArrayOfObject, 0));
/* 643 */       return localXML.addNamespace((Namespace)localObject1);
/*     */     case 7:
/* 646 */       if (localXML == null) xmlMethodNotFound(localXMLObjectImpl, "childIndex");
/* 647 */       return ScriptRuntime.wrapInt(localXML.childIndex());
/*     */     case 14:
/* 650 */       if (localXML == null) xmlMethodNotFound(localXMLObjectImpl, "inScopeNamespaces");
/* 651 */       return paramContext.newArray(paramScriptable1, toObjectArray(localXML.inScopeNamespaces()));
/*     */     case 15:
/* 654 */       if (localXML == null) xmlMethodNotFound(localXMLObjectImpl, "insertChildAfter");
/* 655 */       localObject1 = arg(paramArrayOfObject, 0);
/* 656 */       if ((localObject1 == null) || ((localObject1 instanceof XML))) {
/* 657 */         return localXML.insertChildAfter((XML)localObject1, arg(paramArrayOfObject, 1));
/*     */       }
/* 659 */       return Undefined.instance;
/*     */     case 16:
/* 662 */       if (localXML == null) xmlMethodNotFound(localXMLObjectImpl, "insertChildBefore");
/* 663 */       localObject1 = arg(paramArrayOfObject, 0);
/* 664 */       if ((localObject1 == null) || ((localObject1 instanceof XML))) {
/* 665 */         return localXML.insertChildBefore((XML)localObject1, arg(paramArrayOfObject, 1));
/*     */       }
/* 667 */       return Undefined.instance;
/*     */     case 21:
/* 670 */       if (localXML == null) xmlMethodNotFound(localXMLObjectImpl, "localName");
/* 671 */       return localXML.localName();
/*     */     case 22:
/* 674 */       if (localXML == null) xmlMethodNotFound(localXMLObjectImpl, "name");
/* 675 */       return localXML.name();
/*     */     case 23:
/* 678 */       if (localXML == null) xmlMethodNotFound(localXMLObjectImpl, "namespace");
/* 679 */       localObject1 = paramArrayOfObject.length > 0 ? ScriptRuntime.toString(paramArrayOfObject[0]) : null;
/* 680 */       localObject2 = localXML.namespace((String)localObject1);
/* 681 */       if (localObject2 == null) {
/* 682 */         return Undefined.instance;
/*     */       }
/* 684 */       return localObject2;
/*     */     case 24:
/* 688 */       if (localXML == null) xmlMethodNotFound(localXMLObjectImpl, "namespaceDeclarations");
/* 689 */       localObject1 = localXML.namespaceDeclarations();
/* 690 */       return paramContext.newArray(paramScriptable1, toObjectArray((Object[])localObject1));
/*     */     case 25:
/* 693 */       if (localXML == null) xmlMethodNotFound(localXMLObjectImpl, "nodeKind");
/* 694 */       return localXML.nodeKind();
/*     */     case 28:
/* 697 */       if (localXML == null) xmlMethodNotFound(localXMLObjectImpl, "prependChild");
/* 698 */       return localXML.prependChild(arg(paramArrayOfObject, 0));
/*     */     case 31:
/* 701 */       if (localXML == null) xmlMethodNotFound(localXMLObjectImpl, "removeNamespace");
/* 702 */       localObject1 = this.lib.castToNamespace(paramContext, arg(paramArrayOfObject, 0));
/* 703 */       return localXML.removeNamespace((Namespace)localObject1);
/*     */     case 32:
/* 706 */       if (localXML == null) xmlMethodNotFound(localXMLObjectImpl, "replace");
/* 707 */       localObject1 = this.lib.toXMLNameOrIndex(paramContext, arg(paramArrayOfObject, 0));
/* 708 */       localObject2 = arg(paramArrayOfObject, 1);
/* 709 */       if (localObject1 == null)
/*     */       {
/* 711 */         int m = (int)ScriptRuntime.lastUint32Result(paramContext);
/* 712 */         return localXML.replace(m, localObject2);
/*     */       }
/* 714 */       return localXML.replace((XMLName)localObject1, localObject2);
/*     */     case 33:
/* 718 */       if (localXML == null) xmlMethodNotFound(localXMLObjectImpl, "setChildren");
/* 719 */       return localXML.setChildren(arg(paramArrayOfObject, 0));
/*     */     case 34:
/* 722 */       if (localXML == null) xmlMethodNotFound(localXMLObjectImpl, "setLocalName");
/*     */ 
/* 724 */       localObject2 = arg(paramArrayOfObject, 0);
/* 725 */       if ((localObject2 instanceof QName))
/* 726 */         localObject1 = ((QName)localObject2).localName();
/*     */       else {
/* 728 */         localObject1 = ScriptRuntime.toString(localObject2);
/*     */       }
/* 730 */       localXML.setLocalName((String)localObject1);
/* 731 */       return Undefined.instance;
/*     */     case 35:
/* 734 */       if (localXML == null) xmlMethodNotFound(localXMLObjectImpl, "setName");
/* 735 */       localObject1 = paramArrayOfObject.length != 0 ? paramArrayOfObject[0] : Undefined.instance;
/* 736 */       localObject2 = this.lib.constructQName(paramContext, localObject1);
/* 737 */       localXML.setName((QName)localObject2);
/* 738 */       return Undefined.instance;
/*     */     case 36:
/* 741 */       if (localXML == null) xmlMethodNotFound(localXMLObjectImpl, "setNamespace");
/* 742 */       localObject1 = this.lib.castToNamespace(paramContext, arg(paramArrayOfObject, 0));
/* 743 */       localXML.setNamespace((Namespace)localObject1);
/* 744 */       return Undefined.instance;
/*     */     case 4:
/* 748 */       localObject1 = XMLName.create(this.lib.toNodeQName(paramContext, arg(paramArrayOfObject, 0), true), true, false);
/* 749 */       return localXMLObjectImpl.getMatches((XMLName)localObject1);
/*     */     case 5:
/* 752 */       return localXMLObjectImpl.getMatches(XMLName.create(XmlNode.QName.create(null, null), true, false));
/*     */     case 6:
/* 754 */       localObject1 = this.lib.toXMLNameOrIndex(paramContext, arg(paramArrayOfObject, 0));
/* 755 */       if (localObject1 == null)
/*     */       {
/* 757 */         int k = (int)ScriptRuntime.lastUint32Result(paramContext);
/* 758 */         return localXMLObjectImpl.child(k);
/*     */       }
/* 760 */       return localXMLObjectImpl.child((XMLName)localObject1);
/*     */     case 8:
/* 764 */       return localXMLObjectImpl.children();
/*     */     case 9:
/* 766 */       return localXMLObjectImpl.comments();
/*     */     case 10:
/* 768 */       return ScriptRuntime.wrapBoolean(localXMLObjectImpl.contains(arg(paramArrayOfObject, 0)));
/*     */     case 11:
/* 771 */       return localXMLObjectImpl.copy();
/*     */     case 12:
/* 773 */       localObject1 = paramArrayOfObject.length == 0 ? XmlNode.QName.create(null, null) : this.lib.toNodeQName(paramContext, paramArrayOfObject[0], false);
/* 774 */       return localXMLObjectImpl.getMatches(XMLName.create((XmlNode.QName)localObject1, false, true));
/*     */     case 13:
/* 777 */       localObject1 = paramArrayOfObject.length == 0 ? XMLName.formStar() : this.lib.toXMLName(paramContext, paramArrayOfObject[0]);
/*     */ 
/* 780 */       return localXMLObjectImpl.elements((XMLName)localObject1);
/*     */     case 17:
/* 783 */       localObject1 = this.lib.toXMLName(paramContext, arg(paramArrayOfObject, 0));
/* 784 */       return ScriptRuntime.wrapBoolean(localXMLObjectImpl.hasOwnProperty((XMLName)localObject1));
/*     */     case 18:
/* 788 */       return ScriptRuntime.wrapBoolean(localXMLObjectImpl.hasComplexContent());
/*     */     case 19:
/* 790 */       return ScriptRuntime.wrapBoolean(localXMLObjectImpl.hasSimpleContent());
/*     */     case 20:
/* 792 */       return ScriptRuntime.wrapInt(localXMLObjectImpl.length());
/*     */     case 26:
/* 794 */       localXMLObjectImpl.normalize();
/* 795 */       return Undefined.instance;
/*     */     case 27:
/* 797 */       return localXMLObjectImpl.parent();
/*     */     case 29:
/* 799 */       localObject1 = paramArrayOfObject.length > 0 ? this.lib.toXMLName(paramContext, paramArrayOfObject[0]) : XMLName.formStar();
/*     */ 
/* 802 */       return localXMLObjectImpl.processingInstructions((XMLName)localObject1);
/*     */     case 30:
/* 805 */       return ScriptRuntime.wrapBoolean(localXMLObjectImpl.propertyIsEnumerable(arg(paramArrayOfObject, 0)));
/*     */     case 37:
/* 809 */       return localXMLObjectImpl.text();
/*     */     case 38:
/* 811 */       return localXMLObjectImpl.toString();
/*     */     case 39:
/* 813 */       int j = ScriptRuntime.toInt32(paramArrayOfObject, 0);
/* 814 */       return localXMLObjectImpl.toSource(j);
/*     */     case 40:
/* 816 */       return localXMLObjectImpl.toXMLString();
/*     */     case 41:
/* 819 */       return localXMLObjectImpl.valueOf();
/*     */     }
/* 821 */     throw new IllegalArgumentException(String.valueOf(i));
/*     */   }
/*     */ 
/*     */   private static Object arg(Object[] paramArrayOfObject, int paramInt) {
/* 825 */     return paramInt < paramArrayOfObject.length ? paramArrayOfObject[paramInt] : Undefined.instance;
/*     */   }
/*     */ 
/*     */   final XML newTextElementXML(XmlNode paramXmlNode, XmlNode.QName paramQName, String paramString) {
/* 829 */     return this.lib.newTextElementXML(paramXmlNode, paramQName, paramString);
/*     */   }
/*     */ 
/*     */   final XML newXMLFromJs(Object paramObject)
/*     */   {
/* 834 */     return this.lib.newXMLFromJs(paramObject);
/*     */   }
/*     */ 
/*     */   final XML ecmaToXml(Object paramObject) {
/* 838 */     return this.lib.ecmaToXml(paramObject);
/*     */   }
/*     */ 
/*     */   final String ecmaEscapeAttributeValue(String paramString)
/*     */   {
/* 843 */     String str = this.lib.escapeAttributeValue(paramString);
/* 844 */     return str.substring(1, str.length() - 1);
/*     */   }
/*     */ 
/*     */   final XML createEmptyXML() {
/* 848 */     return newXML(XmlNode.createEmpty(getProcessor()));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.xmlimpl.XMLObjectImpl
 * JD-Core Version:    0.6.2
 */