/*     */ package com.sun.org.apache.xerces.internal.impl.xs.traversers;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.ValidatedInfo;
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType;
/*     */ import com.sun.org.apache.xerces.internal.impl.validation.ValidationState;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.SchemaNamespaceSupport;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XSAnnotationImpl;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XSComplexTypeDecl;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XSConstraints;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XSDeclarationPool;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XSElementDecl;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XSParticleDecl;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.util.XInt;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.util.XSObjectListImpl;
/*     */ import com.sun.org.apache.xerces.internal.util.DOMUtil;
/*     */ import com.sun.org.apache.xerces.internal.util.SymbolTable;
/*     */ import com.sun.org.apache.xerces.internal.util.XMLChar;
/*     */ import com.sun.org.apache.xerces.internal.xni.QName;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSObject;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSObjectList;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;
/*     */ import java.util.Locale;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ class XSDElementTraverser extends XSDAbstractTraverser
/*     */ {
/*  76 */   protected final XSElementDecl fTempElementDecl = new XSElementDecl();
/*     */   boolean fDeferTraversingLocalElements;
/*     */ 
/*     */   XSDElementTraverser(XSDHandler handler, XSAttributeChecker gAttrCheck)
/*     */   {
/*  84 */     super(handler, gAttrCheck);
/*     */   }
/*     */ 
/*     */   XSParticleDecl traverseLocal(Element elmDecl, XSDocumentInfo schemaDoc, SchemaGrammar grammar, int allContextFlags, XSObject parent)
/*     */   {
/* 106 */     XSParticleDecl particle = null;
/* 107 */     if (this.fSchemaHandler.fDeclPool != null)
/* 108 */       particle = this.fSchemaHandler.fDeclPool.getParticleDecl();
/*     */     else {
/* 110 */       particle = new XSParticleDecl();
/*     */     }
/* 112 */     if (this.fDeferTraversingLocalElements)
/*     */     {
/* 116 */       particle.fType = 1;
/* 117 */       Attr attr = elmDecl.getAttributeNode(SchemaSymbols.ATT_MINOCCURS);
/* 118 */       if (attr != null) {
/* 119 */         String min = attr.getValue();
/*     */         try {
/* 121 */           int m = Integer.parseInt(XMLChar.trim(min));
/* 122 */           if (m >= 0)
/* 123 */             particle.fMinOccurs = m;
/*     */         }
/*     */         catch (NumberFormatException ex) {
/*     */         }
/*     */       }
/* 128 */       this.fSchemaHandler.fillInLocalElemInfo(elmDecl, schemaDoc, allContextFlags, parent, particle);
/*     */     } else {
/* 130 */       traverseLocal(particle, elmDecl, schemaDoc, grammar, allContextFlags, parent, null);
/*     */ 
/* 132 */       if (particle.fType == 0) {
/* 133 */         particle = null;
/*     */       }
/*     */     }
/* 136 */     return particle;
/*     */   }
/*     */ 
/*     */   protected void traverseLocal(XSParticleDecl particle, Element elmDecl, XSDocumentInfo schemaDoc, SchemaGrammar grammar, int allContextFlags, XSObject parent, String[] localNSDecls)
/*     */   {
/* 155 */     if (localNSDecls != null) {
/* 156 */       schemaDoc.fNamespaceSupport.setEffectiveContext(localNSDecls);
/*     */     }
/*     */ 
/* 160 */     Object[] attrValues = this.fAttrChecker.checkAttributes(elmDecl, false, schemaDoc);
/*     */ 
/* 162 */     QName refAtt = (QName)attrValues[XSAttributeChecker.ATTIDX_REF];
/* 163 */     XInt minAtt = (XInt)attrValues[XSAttributeChecker.ATTIDX_MINOCCURS];
/* 164 */     XInt maxAtt = (XInt)attrValues[XSAttributeChecker.ATTIDX_MAXOCCURS];
/*     */ 
/* 166 */     XSElementDecl element = null;
/* 167 */     XSAnnotationImpl annotation = null;
/* 168 */     if (elmDecl.getAttributeNode(SchemaSymbols.ATT_REF) != null) {
/* 169 */       if (refAtt != null) {
/* 170 */         element = (XSElementDecl)this.fSchemaHandler.getGlobalDecl(schemaDoc, 3, refAtt, elmDecl);
/*     */ 
/* 172 */         Element child = DOMUtil.getFirstChildElement(elmDecl);
/* 173 */         if ((child != null) && (DOMUtil.getLocalName(child).equals(SchemaSymbols.ELT_ANNOTATION))) {
/* 174 */           annotation = traverseAnnotationDecl(child, attrValues, false, schemaDoc);
/* 175 */           child = DOMUtil.getNextSiblingElement(child);
/*     */         }
/*     */         else {
/* 178 */           String text = DOMUtil.getSyntheticAnnotation(elmDecl);
/* 179 */           if (text != null) {
/* 180 */             annotation = traverseSyntheticAnnotation(elmDecl, text, attrValues, false, schemaDoc);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 187 */         if (child != null)
/* 188 */           reportSchemaError("src-element.2.2", new Object[] { refAtt.rawname, DOMUtil.getLocalName(child) }, child);
/*     */       }
/*     */       else {
/* 191 */         element = null;
/*     */       }
/*     */     }
/* 194 */     else element = traverseNamedElement(elmDecl, attrValues, schemaDoc, grammar, false, parent);
/*     */ 
/* 197 */     particle.fMinOccurs = minAtt.intValue();
/* 198 */     particle.fMaxOccurs = maxAtt.intValue();
/* 199 */     if (element != null) {
/* 200 */       particle.fType = 1;
/* 201 */       particle.fValue = element;
/*     */     }
/*     */     else {
/* 204 */       particle.fType = 0;
/*     */     }
/* 206 */     if (refAtt != null)
/*     */     {
/*     */       XSObjectList annotations;
/* 208 */       if (annotation != null) {
/* 209 */         XSObjectList annotations = new XSObjectListImpl();
/* 210 */         ((XSObjectListImpl)annotations).addXSObject(annotation);
/*     */       } else {
/* 212 */         annotations = XSObjectListImpl.EMPTY_LIST;
/*     */       }
/* 214 */       particle.fAnnotations = annotations;
/*     */     } else {
/* 216 */       particle.fAnnotations = (element != null ? element.fAnnotations : XSObjectListImpl.EMPTY_LIST);
/*     */     }
/*     */ 
/* 219 */     Long defaultVals = (Long)attrValues[XSAttributeChecker.ATTIDX_FROMDEFAULT];
/* 220 */     checkOccurrences(particle, SchemaSymbols.ELT_ELEMENT, (Element)elmDecl.getParentNode(), allContextFlags, defaultVals.longValue());
/*     */ 
/* 224 */     this.fAttrChecker.returnAttrArray(attrValues, schemaDoc);
/*     */   }
/*     */ 
/*     */   XSElementDecl traverseGlobal(Element elmDecl, XSDocumentInfo schemaDoc, SchemaGrammar grammar)
/*     */   {
/* 241 */     Object[] attrValues = this.fAttrChecker.checkAttributes(elmDecl, true, schemaDoc);
/* 242 */     XSElementDecl element = traverseNamedElement(elmDecl, attrValues, schemaDoc, grammar, true, null);
/* 243 */     this.fAttrChecker.returnAttrArray(attrValues, schemaDoc);
/* 244 */     return element;
/*     */   }
/*     */ 
/*     */   XSElementDecl traverseNamedElement(Element elmDecl, Object[] attrValues, XSDocumentInfo schemaDoc, SchemaGrammar grammar, boolean isGlobal, XSObject parent)
/*     */   {
/* 265 */     Boolean abstractAtt = (Boolean)attrValues[XSAttributeChecker.ATTIDX_ABSTRACT];
/* 266 */     XInt blockAtt = (XInt)attrValues[XSAttributeChecker.ATTIDX_BLOCK];
/* 267 */     String defaultAtt = (String)attrValues[XSAttributeChecker.ATTIDX_DEFAULT];
/* 268 */     XInt finalAtt = (XInt)attrValues[XSAttributeChecker.ATTIDX_FINAL];
/* 269 */     String fixedAtt = (String)attrValues[XSAttributeChecker.ATTIDX_FIXED];
/* 270 */     XInt formAtt = (XInt)attrValues[XSAttributeChecker.ATTIDX_FORM];
/* 271 */     String nameAtt = (String)attrValues[XSAttributeChecker.ATTIDX_NAME];
/* 272 */     Boolean nillableAtt = (Boolean)attrValues[XSAttributeChecker.ATTIDX_NILLABLE];
/* 273 */     QName subGroupAtt = (QName)attrValues[XSAttributeChecker.ATTIDX_SUBSGROUP];
/* 274 */     QName typeAtt = (QName)attrValues[XSAttributeChecker.ATTIDX_TYPE];
/*     */ 
/* 278 */     XSElementDecl element = null;
/* 279 */     if (this.fSchemaHandler.fDeclPool != null)
/* 280 */       element = this.fSchemaHandler.fDeclPool.getElementDecl();
/*     */     else {
/* 282 */       element = new XSElementDecl();
/*     */     }
/*     */ 
/* 285 */     if (nameAtt != null) {
/* 286 */       element.fName = this.fSymbolTable.addSymbol(nameAtt);
/*     */     }
/*     */ 
/* 289 */     if (isGlobal) {
/* 290 */       element.fTargetNamespace = schemaDoc.fTargetNamespace;
/* 291 */       element.setIsGlobal();
/*     */     }
/*     */     else {
/* 294 */       if ((parent instanceof XSComplexTypeDecl)) {
/* 295 */         element.setIsLocal((XSComplexTypeDecl)parent);
/*     */       }
/* 297 */       if (formAtt != null) {
/* 298 */         if (formAtt.intValue() == 1)
/* 299 */           element.fTargetNamespace = schemaDoc.fTargetNamespace;
/*     */         else
/* 301 */           element.fTargetNamespace = null;
/* 302 */       } else if (schemaDoc.fAreLocalElementsQualified)
/* 303 */         element.fTargetNamespace = schemaDoc.fTargetNamespace;
/*     */       else {
/* 305 */         element.fTargetNamespace = null;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 310 */     if (blockAtt == null)
/*     */     {
/* 312 */       element.fBlock = schemaDoc.fBlockDefault;
/*     */ 
/* 315 */       if (element.fBlock != 31)
/*     */       {
/*     */         XSElementDecl tmp282_280 = element; tmp282_280.fBlock = ((short)(tmp282_280.fBlock & 0x7));
/*     */       }
/*     */     }
/*     */     else {
/* 320 */       element.fBlock = blockAtt.shortValue();
/*     */ 
/* 322 */       if ((element.fBlock != 31) && ((element.fBlock | 0x7) != 7))
/*     */       {
/* 326 */         reportSchemaError("s4s-att-invalid-value", new Object[] { element.fName, "block", "must be (#all | List of (extension | restriction | substitution))" }, elmDecl);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 333 */     element.fFinal = (finalAtt == null ? schemaDoc.fFinalDefault : finalAtt.shortValue());
/*     */     XSElementDecl tmp382_380 = element; tmp382_380.fFinal = ((short)(tmp382_380.fFinal & 0x3));
/*     */ 
/* 337 */     if (nillableAtt.booleanValue())
/* 338 */       element.setIsNillable();
/* 339 */     if ((abstractAtt != null) && (abstractAtt.booleanValue())) {
/* 340 */       element.setIsAbstract();
/*     */     }
/*     */ 
/* 343 */     if (fixedAtt != null) {
/* 344 */       element.fDefault = new ValidatedInfo();
/* 345 */       element.fDefault.normalizedValue = fixedAtt;
/* 346 */       element.setConstraintType((short)2);
/* 347 */     } else if (defaultAtt != null) {
/* 348 */       element.fDefault = new ValidatedInfo();
/* 349 */       element.fDefault.normalizedValue = defaultAtt;
/* 350 */       element.setConstraintType((short)1);
/*     */     } else {
/* 352 */       element.setConstraintType((short)0);
/*     */     }
/*     */ 
/* 356 */     if (subGroupAtt != null) {
/* 357 */       element.fSubGroup = ((XSElementDecl)this.fSchemaHandler.getGlobalDecl(schemaDoc, 3, subGroupAtt, elmDecl));
/*     */     }
/*     */ 
/* 361 */     Element child = DOMUtil.getFirstChildElement(elmDecl);
/* 362 */     XSAnnotationImpl annotation = null;
/* 363 */     if ((child != null) && (DOMUtil.getLocalName(child).equals(SchemaSymbols.ELT_ANNOTATION))) {
/* 364 */       annotation = traverseAnnotationDecl(child, attrValues, false, schemaDoc);
/* 365 */       child = DOMUtil.getNextSiblingElement(child);
/*     */     }
/*     */     else {
/* 368 */       String text = DOMUtil.getSyntheticAnnotation(elmDecl);
/* 369 */       if (text != null)
/* 370 */         annotation = traverseSyntheticAnnotation(elmDecl, text, attrValues, false, schemaDoc);
/*     */     }
/*     */     XSObjectList annotations;
/* 375 */     if (annotation != null) {
/* 376 */       XSObjectList annotations = new XSObjectListImpl();
/* 377 */       ((XSObjectListImpl)annotations).addXSObject(annotation);
/*     */     } else {
/* 379 */       annotations = XSObjectListImpl.EMPTY_LIST;
/*     */     }
/* 381 */     element.fAnnotations = annotations;
/*     */ 
/* 384 */     XSTypeDefinition elementType = null;
/* 385 */     boolean haveAnonType = false;
/*     */ 
/* 388 */     if (child != null) {
/* 389 */       String childName = DOMUtil.getLocalName(child);
/*     */ 
/* 391 */       if (childName.equals(SchemaSymbols.ELT_COMPLEXTYPE)) {
/* 392 */         elementType = this.fSchemaHandler.fComplexTypeTraverser.traverseLocal(child, schemaDoc, grammar);
/* 393 */         haveAnonType = true;
/* 394 */         child = DOMUtil.getNextSiblingElement(child);
/*     */       }
/* 396 */       else if (childName.equals(SchemaSymbols.ELT_SIMPLETYPE)) {
/* 397 */         elementType = this.fSchemaHandler.fSimpleTypeTraverser.traverseLocal(child, schemaDoc, grammar);
/* 398 */         haveAnonType = true;
/* 399 */         child = DOMUtil.getNextSiblingElement(child);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 404 */     if ((elementType == null) && (typeAtt != null)) {
/* 405 */       elementType = (XSTypeDefinition)this.fSchemaHandler.getGlobalDecl(schemaDoc, 7, typeAtt, elmDecl);
/* 406 */       if (elementType == null) {
/* 407 */         element.fUnresolvedTypeName = typeAtt;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 412 */     if ((elementType == null) && (element.fSubGroup != null)) {
/* 413 */       elementType = element.fSubGroup.fType;
/*     */     }
/*     */ 
/* 416 */     if (elementType == null) {
/* 417 */       elementType = SchemaGrammar.fAnyType;
/*     */     }
/*     */ 
/* 420 */     element.fType = elementType;
/*     */ 
/* 425 */     if (child != null) {
/* 426 */       String childName = DOMUtil.getLocalName(child);
/* 427 */       while ((child != null) && ((childName.equals(SchemaSymbols.ELT_KEY)) || (childName.equals(SchemaSymbols.ELT_KEYREF)) || (childName.equals(SchemaSymbols.ELT_UNIQUE))))
/*     */       {
/* 432 */         if ((childName.equals(SchemaSymbols.ELT_KEY)) || (childName.equals(SchemaSymbols.ELT_UNIQUE)))
/*     */         {
/* 436 */           DOMUtil.setHidden(child, this.fSchemaHandler.fHiddenNodes);
/* 437 */           this.fSchemaHandler.fUniqueOrKeyTraverser.traverse(child, element, schemaDoc, grammar);
/* 438 */           if (DOMUtil.getAttrValue(child, SchemaSymbols.ATT_NAME).length() != 0) {
/* 439 */             this.fSchemaHandler.checkForDuplicateNames(schemaDoc.fTargetNamespace + "," + DOMUtil.getAttrValue(child, SchemaSymbols.ATT_NAME), 1, this.fSchemaHandler.getIDRegistry(), this.fSchemaHandler.getIDRegistry_sub(), child, schemaDoc);
/*     */           }
/*     */ 
/*     */         }
/* 445 */         else if (childName.equals(SchemaSymbols.ELT_KEYREF)) {
/* 446 */           this.fSchemaHandler.storeKeyRef(child, schemaDoc, element);
/*     */         }
/* 448 */         child = DOMUtil.getNextSiblingElement(child);
/* 449 */         if (child != null) {
/* 450 */           childName = DOMUtil.getLocalName(child);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 458 */     if (nameAtt == null) {
/* 459 */       if (isGlobal)
/* 460 */         reportSchemaError("s4s-att-must-appear", new Object[] { SchemaSymbols.ELT_ELEMENT, SchemaSymbols.ATT_NAME }, elmDecl);
/*     */       else
/* 462 */         reportSchemaError("src-element.2.1", null, elmDecl);
/* 463 */       nameAtt = "(no name)";
/*     */     }
/*     */ 
/* 467 */     if (child != null) {
/* 468 */       reportSchemaError("s4s-elt-must-match.1", new Object[] { nameAtt, "(annotation?, (simpleType | complexType)?, (unique | key | keyref)*))", DOMUtil.getLocalName(child) }, child);
/*     */     }
/*     */ 
/* 476 */     if ((defaultAtt != null) && (fixedAtt != null)) {
/* 477 */       reportSchemaError("src-element.1", new Object[] { nameAtt }, elmDecl);
/*     */     }
/*     */ 
/* 488 */     if ((haveAnonType) && (typeAtt != null)) {
/* 489 */       reportSchemaError("src-element.3", new Object[] { nameAtt }, elmDecl);
/*     */     }
/*     */ 
/* 494 */     checkNotationType(nameAtt, elementType, elmDecl);
/*     */ 
/* 499 */     if (element.fDefault != null) {
/* 500 */       this.fValidationState.setNamespaceSupport(schemaDoc.fNamespaceSupport);
/* 501 */       if (XSConstraints.ElementDefaultValidImmediate(element.fType, element.fDefault.normalizedValue, this.fValidationState, element.fDefault) == null) {
/* 502 */         reportSchemaError("e-props-correct.2", new Object[] { nameAtt, element.fDefault.normalizedValue }, elmDecl);
/* 503 */         element.fDefault = null;
/* 504 */         element.setConstraintType((short)0);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 509 */     if ((element.fSubGroup != null) && 
/* 510 */       (!XSConstraints.checkTypeDerivationOk(element.fType, element.fSubGroup.fType, element.fSubGroup.fFinal))) {
/* 511 */       reportSchemaError("e-props-correct.4", new Object[] { nameAtt, subGroupAtt.prefix + ":" + subGroupAtt.localpart }, elmDecl);
/* 512 */       element.fSubGroup = null;
/*     */     }
/*     */ 
/* 517 */     if ((element.fDefault != null) && (
/* 518 */       ((elementType.getTypeCategory() == 16) && (((XSSimpleType)elementType).isIDType())) || ((elementType.getTypeCategory() == 15) && (((XSComplexTypeDecl)elementType).containsTypeID()))))
/*     */     {
/* 522 */       reportSchemaError("e-props-correct.5", new Object[] { element.fName }, elmDecl);
/* 523 */       element.fDefault = null;
/* 524 */       element.setConstraintType((short)0);
/*     */     }
/*     */ 
/* 529 */     if (element.fName == null) {
/* 530 */       return null;
/*     */     }
/*     */ 
/* 533 */     if (isGlobal) {
/* 534 */       grammar.addGlobalElementDeclAll(element);
/*     */ 
/* 536 */       if (grammar.getGlobalElementDecl(element.fName) == null) {
/* 537 */         grammar.addGlobalElementDecl(element);
/*     */       }
/*     */ 
/* 541 */       String loc = this.fSchemaHandler.schemaDocument2SystemId(schemaDoc);
/* 542 */       XSElementDecl element2 = grammar.getGlobalElementDecl(element.fName, loc);
/* 543 */       if (element2 == null) {
/* 544 */         grammar.addGlobalElementDecl(element, loc);
/*     */       }
/*     */ 
/* 549 */       if (this.fSchemaHandler.fTolerateDuplicates) {
/* 550 */         if (element2 != null) {
/* 551 */           element = element2;
/*     */         }
/* 553 */         this.fSchemaHandler.addGlobalElementDecl(element);
/*     */       }
/*     */     }
/*     */ 
/* 557 */     return element;
/*     */   }
/*     */ 
/*     */   void reset(SymbolTable symbolTable, boolean validateAnnotations, Locale locale) {
/* 561 */     super.reset(symbolTable, validateAnnotations, locale);
/* 562 */     this.fDeferTraversingLocalElements = true;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.traversers.XSDElementTraverser
 * JD-Core Version:    0.6.2
 */