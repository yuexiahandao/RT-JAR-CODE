/*     */ package com.sun.org.apache.xerces.internal.impl.xs.traversers;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeFacetException;
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.SchemaDVFactory;
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType;
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.xs.XSSimpleTypeDecl;
/*     */ import com.sun.org.apache.xerces.internal.impl.validation.ValidationState;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar.BuiltinSchemaGrammar;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XSAnnotationImpl;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.util.XInt;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.util.XSObjectListImpl;
/*     */ import com.sun.org.apache.xerces.internal.util.DOMUtil;
/*     */ import com.sun.org.apache.xerces.internal.xni.QName;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSObjectList;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Vector;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ class XSDSimpleTypeTraverser extends XSDAbstractTraverser
/*     */ {
/*  86 */   private boolean fIsBuiltIn = false;
/*     */ 
/*     */   XSDSimpleTypeTraverser(XSDHandler handler, XSAttributeChecker gAttrCheck)
/*     */   {
/*  90 */     super(handler, gAttrCheck);
/*     */   }
/*     */ 
/*     */   XSSimpleType traverseGlobal(Element elmNode, XSDocumentInfo schemaDoc, SchemaGrammar grammar)
/*     */   {
/*  99 */     Object[] attrValues = this.fAttrChecker.checkAttributes(elmNode, true, schemaDoc);
/* 100 */     String nameAtt = (String)attrValues[XSAttributeChecker.ATTIDX_NAME];
/* 101 */     if (nameAtt == null) {
/* 102 */       attrValues[XSAttributeChecker.ATTIDX_NAME] = "(no name)";
/*     */     }
/* 104 */     XSSimpleType type = traverseSimpleTypeDecl(elmNode, attrValues, schemaDoc, grammar);
/* 105 */     this.fAttrChecker.returnAttrArray(attrValues, schemaDoc);
/*     */ 
/* 108 */     if (nameAtt == null) {
/* 109 */       reportSchemaError("s4s-att-must-appear", new Object[] { SchemaSymbols.ELT_SIMPLETYPE, SchemaSymbols.ATT_NAME }, elmNode);
/* 110 */       type = null;
/*     */     }
/*     */ 
/* 114 */     if (type != null) {
/* 115 */       if (grammar.getGlobalTypeDecl(type.getName()) == null) {
/* 116 */         grammar.addGlobalSimpleTypeDecl(type);
/*     */       }
/*     */ 
/* 120 */       String loc = this.fSchemaHandler.schemaDocument2SystemId(schemaDoc);
/* 121 */       XSTypeDefinition type2 = grammar.getGlobalTypeDecl(type.getName(), loc);
/* 122 */       if (type2 == null) {
/* 123 */         grammar.addGlobalSimpleTypeDecl(type, loc);
/*     */       }
/*     */ 
/* 127 */       if (this.fSchemaHandler.fTolerateDuplicates) {
/* 128 */         if ((type2 != null) && 
/* 129 */           ((type2 instanceof XSSimpleType))) {
/* 130 */           type = (XSSimpleType)type2;
/*     */         }
/*     */ 
/* 133 */         this.fSchemaHandler.addGlobalTypeDecl(type);
/*     */       }
/*     */     }
/*     */ 
/* 137 */     return type;
/*     */   }
/*     */ 
/*     */   XSSimpleType traverseLocal(Element elmNode, XSDocumentInfo schemaDoc, SchemaGrammar grammar)
/*     */   {
/* 145 */     Object[] attrValues = this.fAttrChecker.checkAttributes(elmNode, false, schemaDoc);
/* 146 */     String name = genAnonTypeName(elmNode);
/* 147 */     XSSimpleType type = getSimpleType(name, elmNode, attrValues, schemaDoc, grammar);
/* 148 */     if ((type instanceof XSSimpleTypeDecl)) {
/* 149 */       ((XSSimpleTypeDecl)type).setAnonymous(true);
/*     */     }
/* 151 */     this.fAttrChecker.returnAttrArray(attrValues, schemaDoc);
/*     */ 
/* 153 */     return type;
/*     */   }
/*     */ 
/*     */   private XSSimpleType traverseSimpleTypeDecl(Element simpleTypeDecl, Object[] attrValues, XSDocumentInfo schemaDoc, SchemaGrammar grammar)
/*     */   {
/* 162 */     String name = (String)attrValues[XSAttributeChecker.ATTIDX_NAME];
/* 163 */     return getSimpleType(name, simpleTypeDecl, attrValues, schemaDoc, grammar);
/*     */   }
/*     */ 
/*     */   private String genAnonTypeName(Element simpleTypeDecl)
/*     */   {
/* 175 */     StringBuffer typeName = new StringBuffer("#AnonType_");
/* 176 */     Element node = DOMUtil.getParent(simpleTypeDecl);
/* 177 */     while ((node != null) && (node != DOMUtil.getRoot(DOMUtil.getDocument(node)))) {
/* 178 */       typeName.append(node.getAttribute(SchemaSymbols.ATT_NAME));
/* 179 */       node = DOMUtil.getParent(node);
/*     */     }
/* 181 */     return typeName.toString();
/*     */   }
/*     */ 
/*     */   private XSSimpleType getSimpleType(String name, Element simpleTypeDecl, Object[] attrValues, XSDocumentInfo schemaDoc, SchemaGrammar grammar)
/*     */   {
/* 193 */     XInt finalAttr = (XInt)attrValues[XSAttributeChecker.ATTIDX_FINAL];
/* 194 */     int finalProperty = finalAttr == null ? schemaDoc.fFinalDefault : finalAttr.intValue();
/*     */ 
/* 196 */     Element child = DOMUtil.getFirstChildElement(simpleTypeDecl);
/* 197 */     XSAnnotationImpl[] annotations = null;
/* 198 */     if ((child != null) && (DOMUtil.getLocalName(child).equals(SchemaSymbols.ELT_ANNOTATION))) {
/* 199 */       XSAnnotationImpl annotation = traverseAnnotationDecl(child, attrValues, false, schemaDoc);
/* 200 */       if (annotation != null)
/* 201 */         annotations = new XSAnnotationImpl[] { annotation };
/* 202 */       child = DOMUtil.getNextSiblingElement(child);
/*     */     }
/*     */     else {
/* 205 */       String text = DOMUtil.getSyntheticAnnotation(simpleTypeDecl);
/* 206 */       if (text != null) {
/* 207 */         XSAnnotationImpl annotation = traverseSyntheticAnnotation(simpleTypeDecl, text, attrValues, false, schemaDoc);
/* 208 */         annotations = new XSAnnotationImpl[] { annotation };
/*     */       }
/*     */     }
/*     */ 
/* 212 */     if (child == null) {
/* 213 */       reportSchemaError("s4s-elt-must-match.2", new Object[] { SchemaSymbols.ELT_SIMPLETYPE, "(annotation?, (restriction | list | union))" }, simpleTypeDecl);
/* 214 */       return errorType(name, schemaDoc.fTargetNamespace, (short)2);
/*     */     }
/*     */ 
/* 217 */     String varietyProperty = DOMUtil.getLocalName(child);
/* 218 */     short refType = 2;
/* 219 */     boolean restriction = false; boolean list = false; boolean union = false;
/* 220 */     if (varietyProperty.equals(SchemaSymbols.ELT_RESTRICTION)) {
/* 221 */       refType = 2;
/* 222 */       restriction = true;
/*     */     }
/* 224 */     else if (varietyProperty.equals(SchemaSymbols.ELT_LIST)) {
/* 225 */       refType = 16;
/* 226 */       list = true;
/*     */     }
/* 228 */     else if (varietyProperty.equals(SchemaSymbols.ELT_UNION)) {
/* 229 */       refType = 8;
/* 230 */       union = true;
/*     */     }
/*     */     else {
/* 233 */       reportSchemaError("s4s-elt-must-match.1", new Object[] { SchemaSymbols.ELT_SIMPLETYPE, "(annotation?, (restriction | list | union))", varietyProperty }, simpleTypeDecl);
/* 234 */       return errorType(name, schemaDoc.fTargetNamespace, (short)2);
/*     */     }
/*     */ 
/* 237 */     Element nextChild = DOMUtil.getNextSiblingElement(child);
/* 238 */     if (nextChild != null) {
/* 239 */       reportSchemaError("s4s-elt-must-match.1", new Object[] { SchemaSymbols.ELT_SIMPLETYPE, "(annotation?, (restriction | list | union))", DOMUtil.getLocalName(nextChild) }, nextChild);
/*     */     }
/*     */ 
/* 242 */     Object[] contentAttrs = this.fAttrChecker.checkAttributes(child, false, schemaDoc);
/* 243 */     QName baseTypeName = (QName)contentAttrs[XSAttributeChecker.ATTIDX_ITEMTYPE];
/*     */ 
/* 246 */     Vector memberTypes = (Vector)contentAttrs[XSAttributeChecker.ATTIDX_MEMBERTYPES];
/*     */ 
/* 248 */     Element content = DOMUtil.getFirstChildElement(child);
/*     */ 
/* 250 */     if ((content != null) && (DOMUtil.getLocalName(content).equals(SchemaSymbols.ELT_ANNOTATION))) {
/* 251 */       XSAnnotationImpl annotation = traverseAnnotationDecl(content, contentAttrs, false, schemaDoc);
/* 252 */       if (annotation != null) {
/* 253 */         if (annotations == null) {
/* 254 */           annotations = new XSAnnotationImpl[] { annotation };
/*     */         }
/*     */         else {
/* 257 */           XSAnnotationImpl[] tempArray = new XSAnnotationImpl[2];
/* 258 */           tempArray[0] = annotations[0];
/* 259 */           annotations = tempArray;
/* 260 */           annotations[1] = annotation;
/*     */         }
/*     */       }
/* 263 */       content = DOMUtil.getNextSiblingElement(content);
/*     */     }
/*     */     else {
/* 266 */       String text = DOMUtil.getSyntheticAnnotation(child);
/* 267 */       if (text != null) {
/* 268 */         XSAnnotationImpl annotation = traverseSyntheticAnnotation(child, text, contentAttrs, false, schemaDoc);
/* 269 */         if (annotations == null) {
/* 270 */           annotations = new XSAnnotationImpl[] { annotation };
/*     */         }
/*     */         else {
/* 273 */           XSAnnotationImpl[] tempArray = new XSAnnotationImpl[2];
/* 274 */           tempArray[0] = annotations[0];
/* 275 */           annotations = tempArray;
/* 276 */           annotations[1] = annotation;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 281 */     XSSimpleType baseValidator = null;
/* 282 */     if (((restriction) || (list)) && (baseTypeName != null)) {
/* 283 */       baseValidator = findDTValidator(child, name, baseTypeName, refType, schemaDoc);
/*     */ 
/* 285 */       if ((baseValidator == null) && (this.fIsBuiltIn)) {
/* 286 */         this.fIsBuiltIn = false;
/* 287 */         return null;
/*     */       }
/*     */     }
/*     */ 
/* 291 */     ArrayList dTValidators = null;
/* 292 */     XSSimpleType dv = null;
/*     */ 
/* 294 */     if ((union) && (memberTypes != null) && (memberTypes.size() > 0)) {
/* 295 */       int size = memberTypes.size();
/* 296 */       dTValidators = new ArrayList(size);
/*     */ 
/* 298 */       for (int i = 0; i < size; i++)
/*     */       {
/* 300 */         dv = findDTValidator(child, name, (QName)memberTypes.elementAt(i), (short)8, schemaDoc);
/*     */ 
/* 302 */         if (dv != null)
/*     */         {
/* 304 */           if (dv.getVariety() == 3) {
/* 305 */             XSObjectList dvs = dv.getMemberTypes();
/* 306 */             for (int j = 0; j < dvs.getLength(); j++)
/* 307 */               dTValidators.add(dvs.item(j));
/*     */           } else {
/* 309 */             dTValidators.add(dv);
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 316 */     if ((content != null) && (DOMUtil.getLocalName(content).equals(SchemaSymbols.ELT_SIMPLETYPE))) {
/* 317 */       if ((restriction) || (list))
/*     */       {
/* 319 */         if (baseTypeName != null) {
/* 320 */           reportSchemaError(list ? "src-simple-type.3.a" : "src-simple-type.2.a", null, content);
/*     */         }
/* 322 */         if (baseValidator == null)
/*     */         {
/* 324 */           baseValidator = traverseLocal(content, schemaDoc, grammar);
/*     */         }
/*     */ 
/* 327 */         content = DOMUtil.getNextSiblingElement(content);
/*     */       }
/* 329 */       else if (union) {
/* 330 */         if (dTValidators == null) {
/* 331 */           dTValidators = new ArrayList(2);
/*     */         }
/*     */         do
/*     */         {
/* 335 */           dv = traverseLocal(content, schemaDoc, grammar);
/* 336 */           if (dv != null)
/*     */           {
/* 338 */             if (dv.getVariety() == 3) {
/* 339 */               XSObjectList dvs = dv.getMemberTypes();
/* 340 */               for (int j = 0; j < dvs.getLength(); j++)
/* 341 */                 dTValidators.add(dvs.item(j));
/*     */             }
/*     */             else
/*     */             {
/* 345 */               dTValidators.add(dv);
/*     */             }
/*     */           }
/*     */ 
/* 349 */           content = DOMUtil.getNextSiblingElement(content);
/* 350 */           if (content == null) break;  } while (DOMUtil.getLocalName(content).equals(SchemaSymbols.ELT_SIMPLETYPE));
/*     */       }
/*     */     }
/* 353 */     else if (((restriction) || (list)) && (baseTypeName == null))
/*     */     {
/* 355 */       reportSchemaError(list ? "src-simple-type.3.b" : "src-simple-type.2.b", null, child);
/*     */     }
/* 357 */     else if ((union) && ((memberTypes == null) || (memberTypes.size() == 0)))
/*     */     {
/* 359 */       reportSchemaError("src-union-memberTypes-or-simpleTypes", null, child);
/*     */     }
/*     */ 
/* 363 */     if (((restriction) || (list)) && (baseValidator == null)) {
/* 364 */       this.fAttrChecker.returnAttrArray(contentAttrs, schemaDoc);
/* 365 */       return errorType(name, schemaDoc.fTargetNamespace, (short)(restriction ? 2 : 16));
/*     */     }
/*     */ 
/* 370 */     if ((union) && ((dTValidators == null) || (dTValidators.size() == 0))) {
/* 371 */       this.fAttrChecker.returnAttrArray(contentAttrs, schemaDoc);
/* 372 */       return errorType(name, schemaDoc.fTargetNamespace, (short)8);
/*     */     }
/*     */ 
/* 376 */     if ((list) && (isListDatatype(baseValidator))) {
/* 377 */       reportSchemaError("cos-st-restricts.2.1", new Object[] { name, baseValidator.getName() }, child);
/* 378 */       this.fAttrChecker.returnAttrArray(contentAttrs, schemaDoc);
/* 379 */       return errorType(name, schemaDoc.fTargetNamespace, (short)16);
/*     */     }
/*     */ 
/* 383 */     XSSimpleType newDecl = null;
/* 384 */     if (restriction) {
/* 385 */       newDecl = this.fSchemaHandler.fDVFactory.createTypeRestriction(name, schemaDoc.fTargetNamespace, (short)finalProperty, baseValidator, annotations == null ? null : new XSObjectListImpl(annotations, annotations.length));
/*     */     }
/* 388 */     else if (list) {
/* 389 */       newDecl = this.fSchemaHandler.fDVFactory.createTypeList(name, schemaDoc.fTargetNamespace, (short)finalProperty, baseValidator, annotations == null ? null : new XSObjectListImpl(annotations, annotations.length));
/*     */     }
/* 392 */     else if (union) {
/* 393 */       XSSimpleType[] memberDecls = (XSSimpleType[])dTValidators.toArray(new XSSimpleType[dTValidators.size()]);
/* 394 */       newDecl = this.fSchemaHandler.fDVFactory.createTypeUnion(name, schemaDoc.fTargetNamespace, (short)finalProperty, memberDecls, annotations == null ? null : new XSObjectListImpl(annotations, annotations.length));
/*     */     }
/*     */ 
/* 398 */     if ((restriction) && (content != null)) {
/* 399 */       XSDAbstractTraverser.FacetInfo fi = traverseFacets(content, baseValidator, schemaDoc);
/* 400 */       content = fi.nodeAfterFacets;
/*     */       try
/*     */       {
/* 403 */         this.fValidationState.setNamespaceSupport(schemaDoc.fNamespaceSupport);
/* 404 */         newDecl.applyFacets(fi.facetdata, fi.fPresentFacets, fi.fFixedFacets, this.fValidationState);
/*     */       } catch (InvalidDatatypeFacetException ex) {
/* 406 */         reportSchemaError(ex.getKey(), ex.getArgs(), child);
/*     */ 
/* 408 */         newDecl = this.fSchemaHandler.fDVFactory.createTypeRestriction(name, schemaDoc.fTargetNamespace, (short)finalProperty, baseValidator, annotations == null ? null : new XSObjectListImpl(annotations, annotations.length));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 413 */     if (content != null) {
/* 414 */       if (restriction) {
/* 415 */         reportSchemaError("s4s-elt-must-match.1", new Object[] { SchemaSymbols.ELT_RESTRICTION, "(annotation?, (simpleType?, (minExclusive | minInclusive | maxExclusive | maxInclusive | totalDigits | fractionDigits | length | minLength | maxLength | enumeration | whiteSpace | pattern)*))", DOMUtil.getLocalName(content) }, content);
/*     */       }
/* 417 */       else if (list) {
/* 418 */         reportSchemaError("s4s-elt-must-match.1", new Object[] { SchemaSymbols.ELT_LIST, "(annotation?, (simpleType?))", DOMUtil.getLocalName(content) }, content);
/*     */       }
/* 420 */       else if (union) {
/* 421 */         reportSchemaError("s4s-elt-must-match.1", new Object[] { SchemaSymbols.ELT_UNION, "(annotation?, (simpleType*))", DOMUtil.getLocalName(content) }, content);
/*     */       }
/*     */     }
/* 424 */     this.fAttrChecker.returnAttrArray(contentAttrs, schemaDoc);
/*     */ 
/* 426 */     return newDecl;
/*     */   }
/*     */ 
/*     */   private XSSimpleType findDTValidator(Element elm, String refName, QName baseTypeStr, short baseRefContext, XSDocumentInfo schemaDoc)
/*     */   {
/* 438 */     if (baseTypeStr == null) {
/* 439 */       return null;
/*     */     }
/* 441 */     XSTypeDefinition baseType = (XSTypeDefinition)this.fSchemaHandler.getGlobalDecl(schemaDoc, 7, baseTypeStr, elm);
/* 442 */     if (baseType == null) {
/* 443 */       return null;
/*     */     }
/* 445 */     if (baseType.getTypeCategory() != 16) {
/* 446 */       reportSchemaError("cos-st-restricts.1.1", new Object[] { baseTypeStr.rawname, refName }, elm);
/* 447 */       return null;
/*     */     }
/*     */ 
/* 451 */     if ((baseType == SchemaGrammar.fAnySimpleType) && (baseRefContext == 2))
/*     */     {
/* 455 */       if (checkBuiltIn(refName, schemaDoc.fTargetNamespace)) {
/* 456 */         return null;
/*     */       }
/* 458 */       reportSchemaError("cos-st-restricts.1.1", new Object[] { baseTypeStr.rawname, refName }, elm);
/* 459 */       return null;
/*     */     }
/*     */ 
/* 462 */     if ((baseType.getFinal() & baseRefContext) != 0) {
/* 463 */       if (baseRefContext == 2) {
/* 464 */         reportSchemaError("st-props-correct.3", new Object[] { refName, baseTypeStr.rawname }, elm);
/*     */       }
/* 466 */       else if (baseRefContext == 16) {
/* 467 */         reportSchemaError("cos-st-restricts.2.3.1.1", new Object[] { baseTypeStr.rawname, refName }, elm);
/*     */       }
/* 469 */       else if (baseRefContext == 8) {
/* 470 */         reportSchemaError("cos-st-restricts.3.3.1.1", new Object[] { baseTypeStr.rawname, refName }, elm);
/*     */       }
/* 472 */       return null;
/*     */     }
/*     */ 
/* 475 */     return (XSSimpleType)baseType;
/*     */   }
/*     */ 
/*     */   private final boolean checkBuiltIn(String name, String namespace)
/*     */   {
/* 481 */     if (namespace != SchemaSymbols.URI_SCHEMAFORSCHEMA)
/* 482 */       return false;
/* 483 */     if (SchemaGrammar.SG_SchemaNS.getGlobalTypeDecl(name) != null)
/* 484 */       this.fIsBuiltIn = true;
/* 485 */     return this.fIsBuiltIn;
/*     */   }
/*     */ 
/*     */   private boolean isListDatatype(XSSimpleType validator)
/*     */   {
/* 490 */     if (validator.getVariety() == 2) {
/* 491 */       return true;
/*     */     }
/* 493 */     if (validator.getVariety() == 3) {
/* 494 */       XSObjectList temp = validator.getMemberTypes();
/* 495 */       for (int i = 0; i < temp.getLength(); i++) {
/* 496 */         if (((XSSimpleType)temp.item(i)).getVariety() == 2) {
/* 497 */           return true;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 502 */     return false;
/*     */   }
/*     */ 
/*     */   private XSSimpleType errorType(String name, String namespace, short refType) {
/* 506 */     XSSimpleType stringType = (XSSimpleType)SchemaGrammar.SG_SchemaNS.getTypeDefinition("string");
/* 507 */     switch (refType) {
/*     */     case 2:
/* 509 */       return this.fSchemaHandler.fDVFactory.createTypeRestriction(name, namespace, (short)0, stringType, null);
/*     */     case 16:
/* 512 */       return this.fSchemaHandler.fDVFactory.createTypeList(name, namespace, (short)0, stringType, null);
/*     */     case 8:
/* 515 */       return this.fSchemaHandler.fDVFactory.createTypeUnion(name, namespace, (short)0, new XSSimpleType[] { stringType }, null);
/*     */     }
/*     */ 
/* 519 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.traversers.XSDSimpleTypeTraverser
 * JD-Core Version:    0.6.2
 */