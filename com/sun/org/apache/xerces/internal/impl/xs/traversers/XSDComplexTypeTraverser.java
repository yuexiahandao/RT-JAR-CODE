/*      */ package com.sun.org.apache.xerces.internal.impl.xs.traversers;
/*      */ 
/*      */ import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeFacetException;
/*      */ import com.sun.org.apache.xerces.internal.impl.dv.SchemaDVFactory;
/*      */ import com.sun.org.apache.xerces.internal.impl.dv.XSFacets;
/*      */ import com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType;
/*      */ import com.sun.org.apache.xerces.internal.impl.dv.xs.XSSimpleTypeDecl;
/*      */ import com.sun.org.apache.xerces.internal.impl.validation.ValidationState;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.XSAnnotationImpl;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.XSAttributeDecl;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.XSAttributeGroupDecl;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.XSAttributeUseImpl;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.XSComplexTypeDecl;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.XSConstraints;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.XSModelGroupImpl;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.XSParticleDecl;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.XSWildcardDecl;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.util.XInt;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.util.XSObjectListImpl;
/*      */ import com.sun.org.apache.xerces.internal.util.DOMUtil;
/*      */ import com.sun.org.apache.xerces.internal.xni.QName;
/*      */ import com.sun.org.apache.xerces.internal.xs.XSAttributeUse;
/*      */ import com.sun.org.apache.xerces.internal.xs.XSObjectList;
/*      */ import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;
/*      */ import org.w3c.dom.Element;
/*      */ 
/*      */ class XSDComplexTypeTraverser extends XSDAbstractParticleTraverser
/*      */ {
/*      */   private static final int GLOBAL_NUM = 11;
/*   73 */   private static XSParticleDecl fErrorContent = null;
/*   74 */   private static XSWildcardDecl fErrorWildcard = null;
/*      */ 
/*  104 */   private String fName = null;
/*  105 */   private String fTargetNamespace = null;
/*  106 */   private short fDerivedBy = 2;
/*  107 */   private short fFinal = 0;
/*  108 */   private short fBlock = 0;
/*  109 */   private short fContentType = 0;
/*  110 */   private XSTypeDefinition fBaseType = null;
/*  111 */   private XSAttributeGroupDecl fAttrGrp = null;
/*  112 */   private XSSimpleType fXSSimpleType = null;
/*  113 */   private XSParticleDecl fParticle = null;
/*  114 */   private boolean fIsAbstract = false;
/*  115 */   private XSComplexTypeDecl fComplexTypeDecl = null;
/*  116 */   private XSAnnotationImpl[] fAnnotations = null;
/*      */ 
/*  119 */   private Object[] fGlobalStore = null;
/*  120 */   private int fGlobalStorePos = 0;
/*      */   private static final boolean DEBUG = false;
/*      */ 
/*      */   private static XSParticleDecl getErrorContent()
/*      */   {
/*   76 */     if (fErrorContent == null) {
/*   77 */       XSParticleDecl particle = new XSParticleDecl();
/*   78 */       particle.fType = 2;
/*   79 */       particle.fValue = getErrorWildcard();
/*   80 */       particle.fMinOccurs = 0;
/*   81 */       particle.fMaxOccurs = -1;
/*   82 */       XSModelGroupImpl group = new XSModelGroupImpl();
/*   83 */       group.fCompositor = 102;
/*   84 */       group.fParticleCount = 1;
/*   85 */       group.fParticles = new XSParticleDecl[1];
/*   86 */       group.fParticles[0] = particle;
/*   87 */       XSParticleDecl errorContent = new XSParticleDecl();
/*   88 */       errorContent.fType = 3;
/*   89 */       errorContent.fValue = group;
/*   90 */       fErrorContent = errorContent;
/*      */     }
/*   92 */     return fErrorContent;
/*      */   }
/*      */   private static XSWildcardDecl getErrorWildcard() {
/*   95 */     if (fErrorWildcard == null) {
/*   96 */       XSWildcardDecl wildcard = new XSWildcardDecl();
/*   97 */       wildcard.fProcessContents = 2;
/*   98 */       fErrorWildcard = wildcard;
/*      */     }
/*  100 */     return fErrorWildcard;
/*      */   }
/*      */ 
/*      */   XSDComplexTypeTraverser(XSDHandler handler, XSAttributeChecker gAttrCheck)
/*      */   {
/*  124 */     super(handler, gAttrCheck);
/*      */   }
/*      */ 
/*      */   XSComplexTypeDecl traverseLocal(Element complexTypeNode, XSDocumentInfo schemaDoc, SchemaGrammar grammar)
/*      */   {
/*  160 */     Object[] attrValues = this.fAttrChecker.checkAttributes(complexTypeNode, false, schemaDoc);
/*      */ 
/*  162 */     String complexTypeName = genAnonTypeName(complexTypeNode);
/*  163 */     contentBackup();
/*  164 */     XSComplexTypeDecl type = traverseComplexTypeDecl(complexTypeNode, complexTypeName, attrValues, schemaDoc, grammar);
/*      */ 
/*  166 */     contentRestore();
/*      */ 
/*  168 */     grammar.addComplexTypeDecl(type, this.fSchemaHandler.element2Locator(complexTypeNode));
/*  169 */     type.setIsAnonymous();
/*  170 */     this.fAttrChecker.returnAttrArray(attrValues, schemaDoc);
/*      */ 
/*  172 */     return type;
/*      */   }
/*      */ 
/*      */   XSComplexTypeDecl traverseGlobal(Element complexTypeNode, XSDocumentInfo schemaDoc, SchemaGrammar grammar)
/*      */   {
/*  187 */     Object[] attrValues = this.fAttrChecker.checkAttributes(complexTypeNode, true, schemaDoc);
/*      */ 
/*  189 */     String complexTypeName = (String)attrValues[XSAttributeChecker.ATTIDX_NAME];
/*  190 */     contentBackup();
/*  191 */     XSComplexTypeDecl type = traverseComplexTypeDecl(complexTypeNode, complexTypeName, attrValues, schemaDoc, grammar);
/*      */ 
/*  193 */     contentRestore();
/*      */ 
/*  195 */     grammar.addComplexTypeDecl(type, this.fSchemaHandler.element2Locator(complexTypeNode));
/*      */ 
/*  197 */     if (complexTypeName == null) {
/*  198 */       reportSchemaError("s4s-att-must-appear", new Object[] { SchemaSymbols.ELT_COMPLEXTYPE, SchemaSymbols.ATT_NAME }, complexTypeNode);
/*  199 */       type = null;
/*      */     } else {
/*  201 */       if (grammar.getGlobalTypeDecl(type.getName()) == null) {
/*  202 */         grammar.addGlobalComplexTypeDecl(type);
/*      */       }
/*      */ 
/*  206 */       String loc = this.fSchemaHandler.schemaDocument2SystemId(schemaDoc);
/*  207 */       XSTypeDefinition type2 = grammar.getGlobalTypeDecl(type.getName(), loc);
/*  208 */       if (type2 == null) {
/*  209 */         grammar.addGlobalComplexTypeDecl(type, loc);
/*      */       }
/*      */ 
/*  213 */       if (this.fSchemaHandler.fTolerateDuplicates) {
/*  214 */         if ((type2 != null) && 
/*  215 */           ((type2 instanceof XSComplexTypeDecl))) {
/*  216 */           type = (XSComplexTypeDecl)type2;
/*      */         }
/*      */ 
/*  219 */         this.fSchemaHandler.addGlobalTypeDecl(type);
/*      */       }
/*      */     }
/*      */ 
/*  223 */     this.fAttrChecker.returnAttrArray(attrValues, schemaDoc);
/*      */ 
/*  225 */     return type;
/*      */   }
/*      */ 
/*      */   private XSComplexTypeDecl traverseComplexTypeDecl(Element complexTypeDecl, String complexTypeName, Object[] attrValues, XSDocumentInfo schemaDoc, SchemaGrammar grammar)
/*      */   {
/*  235 */     this.fComplexTypeDecl = new XSComplexTypeDecl();
/*  236 */     this.fAttrGrp = new XSAttributeGroupDecl();
/*  237 */     Boolean abstractAtt = (Boolean)attrValues[XSAttributeChecker.ATTIDX_ABSTRACT];
/*  238 */     XInt blockAtt = (XInt)attrValues[XSAttributeChecker.ATTIDX_BLOCK];
/*  239 */     Boolean mixedAtt = (Boolean)attrValues[XSAttributeChecker.ATTIDX_MIXED];
/*  240 */     XInt finalAtt = (XInt)attrValues[XSAttributeChecker.ATTIDX_FINAL];
/*      */ 
/*  242 */     this.fName = complexTypeName;
/*  243 */     this.fComplexTypeDecl.setName(this.fName);
/*  244 */     this.fTargetNamespace = schemaDoc.fTargetNamespace;
/*      */ 
/*  246 */     this.fBlock = (blockAtt == null ? schemaDoc.fBlockDefault : blockAtt.shortValue());
/*  247 */     this.fFinal = (finalAtt == null ? schemaDoc.fFinalDefault : finalAtt.shortValue());
/*      */ 
/*  249 */     this.fBlock = ((short)(this.fBlock & 0x3));
/*  250 */     this.fFinal = ((short)(this.fFinal & 0x3));
/*      */ 
/*  252 */     this.fIsAbstract = ((abstractAtt != null) && (abstractAtt.booleanValue()));
/*  253 */     this.fAnnotations = null;
/*      */ 
/*  255 */     Element child = null;
/*      */     try
/*      */     {
/*  261 */       child = DOMUtil.getFirstChildElement(complexTypeDecl);
/*  262 */       if (child != null) {
/*  263 */         if (DOMUtil.getLocalName(child).equals(SchemaSymbols.ELT_ANNOTATION)) {
/*  264 */           addAnnotation(traverseAnnotationDecl(child, attrValues, false, schemaDoc));
/*  265 */           child = DOMUtil.getNextSiblingElement(child);
/*      */         }
/*      */         else {
/*  268 */           String text = DOMUtil.getSyntheticAnnotation(complexTypeDecl);
/*  269 */           if (text != null) {
/*  270 */             addAnnotation(traverseSyntheticAnnotation(complexTypeDecl, text, attrValues, false, schemaDoc));
/*      */           }
/*      */         }
/*  273 */         if ((child != null) && (DOMUtil.getLocalName(child).equals(SchemaSymbols.ELT_ANNOTATION))) {
/*  274 */           throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[] { this.fName, SchemaSymbols.ELT_ANNOTATION }, child);
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*  280 */         String text = DOMUtil.getSyntheticAnnotation(complexTypeDecl);
/*  281 */         if (text != null) {
/*  282 */           addAnnotation(traverseSyntheticAnnotation(complexTypeDecl, text, attrValues, false, schemaDoc));
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  288 */       if (child == null)
/*      */       {
/*  294 */         this.fBaseType = SchemaGrammar.fAnyType;
/*  295 */         this.fDerivedBy = 2;
/*  296 */         processComplexContent(child, mixedAtt.booleanValue(), false, schemaDoc, grammar);
/*      */       }
/*  299 */       else if (DOMUtil.getLocalName(child).equals(SchemaSymbols.ELT_SIMPLECONTENT))
/*      */       {
/*  304 */         traverseSimpleContent(child, schemaDoc, grammar);
/*  305 */         Element elemTmp = DOMUtil.getNextSiblingElement(child);
/*  306 */         if (elemTmp != null) {
/*  307 */           String siblingName = DOMUtil.getLocalName(elemTmp);
/*  308 */           throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[] { this.fName, siblingName }, elemTmp);
/*      */         }
/*      */ 
/*      */       }
/*  313 */       else if (DOMUtil.getLocalName(child).equals(SchemaSymbols.ELT_COMPLEXCONTENT))
/*      */       {
/*  315 */         traverseComplexContent(child, mixedAtt.booleanValue(), schemaDoc, grammar);
/*      */ 
/*  317 */         Element elemTmp = DOMUtil.getNextSiblingElement(child);
/*  318 */         if (elemTmp != null) {
/*  319 */           String siblingName = DOMUtil.getLocalName(elemTmp);
/*  320 */           throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[] { this.fName, siblingName }, elemTmp);
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*  333 */         this.fBaseType = SchemaGrammar.fAnyType;
/*  334 */         this.fDerivedBy = 2;
/*  335 */         processComplexContent(child, mixedAtt.booleanValue(), false, schemaDoc, grammar);
/*      */       }
/*      */ 
/*      */     }
/*      */     catch (ComplexTypeRecoverableError e)
/*      */     {
/*  341 */       handleComplexTypeError(e.getMessage(), e.errorSubstText, e.errorElem);
/*      */     }
/*      */ 
/*  348 */     this.fComplexTypeDecl.setValues(this.fName, this.fTargetNamespace, this.fBaseType, this.fDerivedBy, this.fFinal, this.fBlock, this.fContentType, this.fIsAbstract, this.fAttrGrp, this.fXSSimpleType, this.fParticle, new XSObjectListImpl(this.fAnnotations, this.fAnnotations == null ? 0 : this.fAnnotations.length));
/*      */ 
/*  352 */     return this.fComplexTypeDecl;
/*      */   }
/*      */ 
/*      */   private void traverseSimpleContent(Element simpleContentElement, XSDocumentInfo schemaDoc, SchemaGrammar grammar)
/*      */     throws XSDComplexTypeTraverser.ComplexTypeRecoverableError
/*      */   {
/*  362 */     Object[] simpleContentAttrValues = this.fAttrChecker.checkAttributes(simpleContentElement, false, schemaDoc);
/*      */ 
/*  368 */     this.fContentType = 1;
/*  369 */     this.fParticle = null;
/*      */ 
/*  371 */     Element simpleContent = DOMUtil.getFirstChildElement(simpleContentElement);
/*  372 */     if ((simpleContent != null) && (DOMUtil.getLocalName(simpleContent).equals(SchemaSymbols.ELT_ANNOTATION))) {
/*  373 */       addAnnotation(traverseAnnotationDecl(simpleContent, simpleContentAttrValues, false, schemaDoc));
/*  374 */       simpleContent = DOMUtil.getNextSiblingElement(simpleContent);
/*      */     }
/*      */     else {
/*  377 */       String text = DOMUtil.getSyntheticAnnotation(simpleContentElement);
/*  378 */       if (text != null) {
/*  379 */         addAnnotation(traverseSyntheticAnnotation(simpleContentElement, text, simpleContentAttrValues, false, schemaDoc));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  384 */     if (simpleContent == null) {
/*  385 */       this.fAttrChecker.returnAttrArray(simpleContentAttrValues, schemaDoc);
/*  386 */       throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.2", new Object[] { this.fName, SchemaSymbols.ELT_SIMPLECONTENT }, simpleContentElement);
/*      */     }
/*      */ 
/*  394 */     String simpleContentName = DOMUtil.getLocalName(simpleContent);
/*  395 */     if (simpleContentName.equals(SchemaSymbols.ELT_RESTRICTION)) {
/*  396 */       this.fDerivedBy = 2;
/*  397 */     } else if (simpleContentName.equals(SchemaSymbols.ELT_EXTENSION)) {
/*  398 */       this.fDerivedBy = 1;
/*      */     } else {
/*  400 */       this.fAttrChecker.returnAttrArray(simpleContentAttrValues, schemaDoc);
/*  401 */       throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[] { this.fName, simpleContentName }, simpleContent);
/*      */     }
/*      */ 
/*  405 */     Element elemTmp = DOMUtil.getNextSiblingElement(simpleContent);
/*  406 */     if (elemTmp != null) {
/*  407 */       this.fAttrChecker.returnAttrArray(simpleContentAttrValues, schemaDoc);
/*  408 */       String siblingName = DOMUtil.getLocalName(elemTmp);
/*  409 */       throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[] { this.fName, siblingName }, elemTmp);
/*      */     }
/*      */ 
/*  414 */     Object[] derivationTypeAttrValues = this.fAttrChecker.checkAttributes(simpleContent, false, schemaDoc);
/*      */ 
/*  416 */     QName baseTypeName = (QName)derivationTypeAttrValues[XSAttributeChecker.ATTIDX_BASE];
/*      */ 
/*  422 */     if (baseTypeName == null) {
/*  423 */       this.fAttrChecker.returnAttrArray(simpleContentAttrValues, schemaDoc);
/*  424 */       this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
/*  425 */       throw new ComplexTypeRecoverableError("s4s-att-must-appear", new Object[] { simpleContentName, "base" }, simpleContent);
/*      */     }
/*      */ 
/*  429 */     XSTypeDefinition type = (XSTypeDefinition)this.fSchemaHandler.getGlobalDecl(schemaDoc, 7, baseTypeName, simpleContent);
/*      */ 
/*  432 */     if (type == null) {
/*  433 */       this.fAttrChecker.returnAttrArray(simpleContentAttrValues, schemaDoc);
/*  434 */       this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
/*  435 */       throw new ComplexTypeRecoverableError();
/*      */     }
/*      */ 
/*  438 */     this.fBaseType = type;
/*      */ 
/*  440 */     XSSimpleType baseValidator = null;
/*  441 */     XSComplexTypeDecl baseComplexType = null;
/*  442 */     int baseFinalSet = 0;
/*      */ 
/*  445 */     if (type.getTypeCategory() == 15)
/*      */     {
/*  447 */       baseComplexType = (XSComplexTypeDecl)type;
/*  448 */       baseFinalSet = baseComplexType.getFinal();
/*      */ 
/*  450 */       if (baseComplexType.getContentType() == 1) {
/*  451 */         baseValidator = (XSSimpleType)baseComplexType.getSimpleType();
/*      */       }
/*  454 */       else if ((this.fDerivedBy != 2) || (baseComplexType.getContentType() != 3) || (!((XSParticleDecl)baseComplexType.getParticle()).emptiable()))
/*      */       {
/*  459 */         this.fAttrChecker.returnAttrArray(simpleContentAttrValues, schemaDoc);
/*  460 */         this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
/*  461 */         throw new ComplexTypeRecoverableError("src-ct.2.1", new Object[] { this.fName, baseComplexType.getName() }, simpleContent);
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  466 */       baseValidator = (XSSimpleType)type;
/*      */ 
/*  468 */       if (this.fDerivedBy == 2) {
/*  469 */         this.fAttrChecker.returnAttrArray(simpleContentAttrValues, schemaDoc);
/*  470 */         this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
/*  471 */         throw new ComplexTypeRecoverableError("src-ct.2.1", new Object[] { this.fName, baseValidator.getName() }, simpleContent);
/*      */       }
/*      */ 
/*  474 */       baseFinalSet = baseValidator.getFinal();
/*      */     }
/*      */ 
/*  480 */     if ((baseFinalSet & this.fDerivedBy) != 0) {
/*  481 */       this.fAttrChecker.returnAttrArray(simpleContentAttrValues, schemaDoc);
/*  482 */       this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
/*  483 */       String errorKey = this.fDerivedBy == 1 ? "cos-ct-extends.1.1" : "derivation-ok-restriction.1";
/*      */ 
/*  485 */       throw new ComplexTypeRecoverableError(errorKey, new Object[] { this.fName, this.fBaseType.getName() }, simpleContent);
/*      */     }
/*      */ 
/*  492 */     Element scElement = simpleContent;
/*  493 */     simpleContent = DOMUtil.getFirstChildElement(simpleContent);
/*  494 */     if (simpleContent != null)
/*      */     {
/*  497 */       if (DOMUtil.getLocalName(simpleContent).equals(SchemaSymbols.ELT_ANNOTATION)) {
/*  498 */         addAnnotation(traverseAnnotationDecl(simpleContent, derivationTypeAttrValues, false, schemaDoc));
/*  499 */         simpleContent = DOMUtil.getNextSiblingElement(simpleContent);
/*      */       }
/*      */       else {
/*  502 */         String text = DOMUtil.getSyntheticAnnotation(scElement);
/*  503 */         if (text != null) {
/*  504 */           addAnnotation(traverseSyntheticAnnotation(scElement, text, derivationTypeAttrValues, false, schemaDoc));
/*      */         }
/*      */       }
/*      */ 
/*  508 */       if ((simpleContent != null) && (DOMUtil.getLocalName(simpleContent).equals(SchemaSymbols.ELT_ANNOTATION)))
/*      */       {
/*  510 */         this.fAttrChecker.returnAttrArray(simpleContentAttrValues, schemaDoc);
/*  511 */         this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
/*  512 */         throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[] { this.fName, SchemaSymbols.ELT_ANNOTATION }, simpleContent);
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*  518 */       String text = DOMUtil.getSyntheticAnnotation(scElement);
/*  519 */       if (text != null) {
/*  520 */         addAnnotation(traverseSyntheticAnnotation(scElement, text, derivationTypeAttrValues, false, schemaDoc));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  527 */     if (this.fDerivedBy == 2)
/*      */     {
/*  533 */       if ((simpleContent != null) && (DOMUtil.getLocalName(simpleContent).equals(SchemaSymbols.ELT_SIMPLETYPE)))
/*      */       {
/*  536 */         XSSimpleType dv = this.fSchemaHandler.fSimpleTypeTraverser.traverseLocal(simpleContent, schemaDoc, grammar);
/*      */ 
/*  538 */         if (dv == null) {
/*  539 */           this.fAttrChecker.returnAttrArray(simpleContentAttrValues, schemaDoc);
/*  540 */           this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
/*  541 */           throw new ComplexTypeRecoverableError();
/*      */         }
/*      */ 
/*  546 */         if ((baseValidator != null) && (!XSConstraints.checkSimpleDerivationOk(dv, baseValidator, baseValidator.getFinal())))
/*      */         {
/*  549 */           this.fAttrChecker.returnAttrArray(simpleContentAttrValues, schemaDoc);
/*  550 */           this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
/*  551 */           throw new ComplexTypeRecoverableError("derivation-ok-restriction.5.2.2.1", new Object[] { this.fName, dv.getName(), baseValidator.getName() }, simpleContent);
/*      */         }
/*      */ 
/*  555 */         baseValidator = dv;
/*  556 */         simpleContent = DOMUtil.getNextSiblingElement(simpleContent);
/*      */       }
/*      */ 
/*  561 */       if (baseValidator == null) {
/*  562 */         this.fAttrChecker.returnAttrArray(simpleContentAttrValues, schemaDoc);
/*  563 */         this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
/*  564 */         throw new ComplexTypeRecoverableError("src-ct.2.2", new Object[] { this.fName }, simpleContent);
/*      */       }
/*      */ 
/*  571 */       Element attrNode = null;
/*  572 */       XSFacets facetData = null;
/*  573 */       short presentFacets = 0;
/*  574 */       short fixedFacets = 0;
/*      */ 
/*  576 */       if (simpleContent != null) {
/*  577 */         XSDAbstractTraverser.FacetInfo fi = traverseFacets(simpleContent, baseValidator, schemaDoc);
/*  578 */         attrNode = fi.nodeAfterFacets;
/*  579 */         facetData = fi.facetdata;
/*  580 */         presentFacets = fi.fPresentFacets;
/*  581 */         fixedFacets = fi.fFixedFacets;
/*      */       }
/*      */ 
/*  584 */       String name = genAnonTypeName(simpleContentElement);
/*  585 */       this.fXSSimpleType = this.fSchemaHandler.fDVFactory.createTypeRestriction(name, schemaDoc.fTargetNamespace, (short)0, baseValidator, null);
/*      */       try {
/*  587 */         this.fValidationState.setNamespaceSupport(schemaDoc.fNamespaceSupport);
/*  588 */         this.fXSSimpleType.applyFacets(facetData, presentFacets, fixedFacets, this.fValidationState);
/*      */       } catch (InvalidDatatypeFacetException ex) {
/*  590 */         reportSchemaError(ex.getKey(), ex.getArgs(), simpleContent);
/*      */ 
/*  592 */         this.fXSSimpleType = this.fSchemaHandler.fDVFactory.createTypeRestriction(name, schemaDoc.fTargetNamespace, (short)0, baseValidator, null);
/*      */       }
/*  594 */       if ((this.fXSSimpleType instanceof XSSimpleTypeDecl)) {
/*  595 */         ((XSSimpleTypeDecl)this.fXSSimpleType).setAnonymous(true);
/*      */       }
/*      */ 
/*  601 */       if (attrNode != null) {
/*  602 */         if (!isAttrOrAttrGroup(attrNode)) {
/*  603 */           this.fAttrChecker.returnAttrArray(simpleContentAttrValues, schemaDoc);
/*  604 */           this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
/*  605 */           throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[] { this.fName, DOMUtil.getLocalName(attrNode) }, attrNode);
/*      */         }
/*      */ 
/*  609 */         Element node = traverseAttrsAndAttrGrps(attrNode, this.fAttrGrp, schemaDoc, grammar, this.fComplexTypeDecl);
/*      */ 
/*  611 */         if (node != null) {
/*  612 */           this.fAttrChecker.returnAttrArray(simpleContentAttrValues, schemaDoc);
/*  613 */           this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
/*  614 */           throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[] { this.fName, DOMUtil.getLocalName(node) }, node);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */       try
/*      */       {
/*  621 */         mergeAttributes(baseComplexType.getAttrGrp(), this.fAttrGrp, this.fName, false, simpleContentElement);
/*      */       } catch (ComplexTypeRecoverableError e) {
/*  623 */         this.fAttrChecker.returnAttrArray(simpleContentAttrValues, schemaDoc);
/*  624 */         this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
/*  625 */         throw e;
/*      */       }
/*      */ 
/*  628 */       this.fAttrGrp.removeProhibitedAttrs();
/*      */ 
/*  630 */       Object[] errArgs = this.fAttrGrp.validRestrictionOf(this.fName, baseComplexType.getAttrGrp());
/*  631 */       if (errArgs != null) {
/*  632 */         this.fAttrChecker.returnAttrArray(simpleContentAttrValues, schemaDoc);
/*  633 */         this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
/*  634 */         throw new ComplexTypeRecoverableError((String)errArgs[(errArgs.length - 1)], errArgs, attrNode);
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*  643 */       this.fXSSimpleType = baseValidator;
/*  644 */       if (simpleContent != null)
/*      */       {
/*  648 */         Element attrNode = simpleContent;
/*  649 */         if (!isAttrOrAttrGroup(attrNode)) {
/*  650 */           this.fAttrChecker.returnAttrArray(simpleContentAttrValues, schemaDoc);
/*  651 */           this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
/*  652 */           throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[] { this.fName, DOMUtil.getLocalName(attrNode) }, attrNode);
/*      */         }
/*      */ 
/*  656 */         Element node = traverseAttrsAndAttrGrps(attrNode, this.fAttrGrp, schemaDoc, grammar, this.fComplexTypeDecl);
/*      */ 
/*  659 */         if (node != null) {
/*  660 */           this.fAttrChecker.returnAttrArray(simpleContentAttrValues, schemaDoc);
/*  661 */           this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
/*  662 */           throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[] { this.fName, DOMUtil.getLocalName(node) }, node);
/*      */         }
/*      */ 
/*  667 */         this.fAttrGrp.removeProhibitedAttrs();
/*      */       }
/*      */ 
/*  670 */       if (baseComplexType != null) {
/*      */         try {
/*  672 */           mergeAttributes(baseComplexType.getAttrGrp(), this.fAttrGrp, this.fName, true, simpleContentElement);
/*      */         } catch (ComplexTypeRecoverableError e) {
/*  674 */           this.fAttrChecker.returnAttrArray(simpleContentAttrValues, schemaDoc);
/*  675 */           this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
/*  676 */           throw e;
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  682 */     this.fAttrChecker.returnAttrArray(simpleContentAttrValues, schemaDoc);
/*  683 */     this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
/*      */   }
/*      */ 
/*      */   private void traverseComplexContent(Element complexContentElement, boolean mixedOnType, XSDocumentInfo schemaDoc, SchemaGrammar grammar)
/*      */     throws XSDComplexTypeTraverser.ComplexTypeRecoverableError
/*      */   {
/*  692 */     Object[] complexContentAttrValues = this.fAttrChecker.checkAttributes(complexContentElement, false, schemaDoc);
/*      */ 
/*  699 */     boolean mixedContent = mixedOnType;
/*  700 */     Boolean mixedAtt = (Boolean)complexContentAttrValues[XSAttributeChecker.ATTIDX_MIXED];
/*  701 */     if (mixedAtt != null) {
/*  702 */       mixedContent = mixedAtt.booleanValue();
/*      */     }
/*      */ 
/*  710 */     this.fXSSimpleType = null;
/*      */ 
/*  712 */     Element complexContent = DOMUtil.getFirstChildElement(complexContentElement);
/*  713 */     if ((complexContent != null) && (DOMUtil.getLocalName(complexContent).equals(SchemaSymbols.ELT_ANNOTATION))) {
/*  714 */       addAnnotation(traverseAnnotationDecl(complexContent, complexContentAttrValues, false, schemaDoc));
/*  715 */       complexContent = DOMUtil.getNextSiblingElement(complexContent);
/*      */     }
/*      */     else {
/*  718 */       String text = DOMUtil.getSyntheticAnnotation(complexContentElement);
/*  719 */       if (text != null) {
/*  720 */         addAnnotation(traverseSyntheticAnnotation(complexContentElement, text, complexContentAttrValues, false, schemaDoc));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  725 */     if (complexContent == null) {
/*  726 */       this.fAttrChecker.returnAttrArray(complexContentAttrValues, schemaDoc);
/*  727 */       throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.2", new Object[] { this.fName, SchemaSymbols.ELT_COMPLEXCONTENT }, complexContentElement);
/*      */     }
/*      */ 
/*  735 */     String complexContentName = DOMUtil.getLocalName(complexContent);
/*  736 */     if (complexContentName.equals(SchemaSymbols.ELT_RESTRICTION)) {
/*  737 */       this.fDerivedBy = 2;
/*  738 */     } else if (complexContentName.equals(SchemaSymbols.ELT_EXTENSION)) {
/*  739 */       this.fDerivedBy = 1;
/*      */     } else {
/*  741 */       this.fAttrChecker.returnAttrArray(complexContentAttrValues, schemaDoc);
/*  742 */       throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[] { this.fName, complexContentName }, complexContent);
/*      */     }
/*      */ 
/*  745 */     Element elemTmp = DOMUtil.getNextSiblingElement(complexContent);
/*  746 */     if (elemTmp != null) {
/*  747 */       this.fAttrChecker.returnAttrArray(complexContentAttrValues, schemaDoc);
/*  748 */       String siblingName = DOMUtil.getLocalName(elemTmp);
/*  749 */       throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[] { this.fName, siblingName }, elemTmp);
/*      */     }
/*      */ 
/*  753 */     Object[] derivationTypeAttrValues = this.fAttrChecker.checkAttributes(complexContent, false, schemaDoc);
/*      */ 
/*  755 */     QName baseTypeName = (QName)derivationTypeAttrValues[XSAttributeChecker.ATTIDX_BASE];
/*      */ 
/*  761 */     if (baseTypeName == null) {
/*  762 */       this.fAttrChecker.returnAttrArray(complexContentAttrValues, schemaDoc);
/*  763 */       this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
/*  764 */       throw new ComplexTypeRecoverableError("s4s-att-must-appear", new Object[] { complexContentName, "base" }, complexContent);
/*      */     }
/*      */ 
/*  768 */     XSTypeDefinition type = (XSTypeDefinition)this.fSchemaHandler.getGlobalDecl(schemaDoc, 7, baseTypeName, complexContent);
/*      */ 
/*  773 */     if (type == null) {
/*  774 */       this.fAttrChecker.returnAttrArray(complexContentAttrValues, schemaDoc);
/*  775 */       this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
/*  776 */       throw new ComplexTypeRecoverableError();
/*      */     }
/*      */ 
/*  779 */     if (!(type instanceof XSComplexTypeDecl)) {
/*  780 */       this.fAttrChecker.returnAttrArray(complexContentAttrValues, schemaDoc);
/*  781 */       this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
/*  782 */       throw new ComplexTypeRecoverableError("src-ct.1", new Object[] { this.fName, type.getName() }, complexContent);
/*      */     }
/*      */ 
/*  785 */     XSComplexTypeDecl baseType = (XSComplexTypeDecl)type;
/*  786 */     this.fBaseType = baseType;
/*      */ 
/*  791 */     if ((baseType.getFinal() & this.fDerivedBy) != 0) {
/*  792 */       this.fAttrChecker.returnAttrArray(complexContentAttrValues, schemaDoc);
/*  793 */       this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
/*  794 */       String errorKey = this.fDerivedBy == 1 ? "cos-ct-extends.1.1" : "derivation-ok-restriction.1";
/*      */ 
/*  796 */       throw new ComplexTypeRecoverableError(errorKey, new Object[] { this.fName, this.fBaseType.getName() }, complexContent);
/*      */     }
/*      */ 
/*  803 */     complexContent = DOMUtil.getFirstChildElement(complexContent);
/*      */ 
/*  805 */     if (complexContent != null)
/*      */     {
/*  807 */       if (DOMUtil.getLocalName(complexContent).equals(SchemaSymbols.ELT_ANNOTATION)) {
/*  808 */         addAnnotation(traverseAnnotationDecl(complexContent, derivationTypeAttrValues, false, schemaDoc));
/*  809 */         complexContent = DOMUtil.getNextSiblingElement(complexContent);
/*      */       }
/*      */       else {
/*  812 */         String text = DOMUtil.getSyntheticAnnotation(complexContent);
/*  813 */         if (text != null) {
/*  814 */           addAnnotation(traverseSyntheticAnnotation(complexContent, text, derivationTypeAttrValues, false, schemaDoc));
/*      */         }
/*      */       }
/*  817 */       if ((complexContent != null) && (DOMUtil.getLocalName(complexContent).equals(SchemaSymbols.ELT_ANNOTATION)))
/*      */       {
/*  819 */         this.fAttrChecker.returnAttrArray(complexContentAttrValues, schemaDoc);
/*  820 */         this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
/*  821 */         throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[] { this.fName, SchemaSymbols.ELT_ANNOTATION }, complexContent);
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  826 */       String text = DOMUtil.getSyntheticAnnotation(complexContent);
/*  827 */       if (text != null) {
/*  828 */         addAnnotation(traverseSyntheticAnnotation(complexContent, text, derivationTypeAttrValues, false, schemaDoc));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  836 */       processComplexContent(complexContent, mixedContent, true, schemaDoc, grammar);
/*      */     }
/*      */     catch (ComplexTypeRecoverableError e) {
/*  839 */       this.fAttrChecker.returnAttrArray(complexContentAttrValues, schemaDoc);
/*  840 */       this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
/*  841 */       throw e;
/*      */     }
/*      */ 
/*  847 */     XSParticleDecl baseContent = (XSParticleDecl)baseType.getParticle();
/*  848 */     if (this.fDerivedBy == 2)
/*      */     {
/*  857 */       if ((this.fContentType == 3) && (baseType.getContentType() != 3))
/*      */       {
/*  859 */         this.fAttrChecker.returnAttrArray(complexContentAttrValues, schemaDoc);
/*  860 */         this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
/*  861 */         throw new ComplexTypeRecoverableError("derivation-ok-restriction.5.4.1.2", new Object[] { this.fName, baseType.getName() }, complexContent);
/*      */       }
/*      */ 
/*      */       try
/*      */       {
/*  867 */         mergeAttributes(baseType.getAttrGrp(), this.fAttrGrp, this.fName, false, complexContent);
/*      */       } catch (ComplexTypeRecoverableError e) {
/*  869 */         this.fAttrChecker.returnAttrArray(complexContentAttrValues, schemaDoc);
/*  870 */         this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
/*  871 */         throw e;
/*      */       }
/*      */ 
/*  874 */       this.fAttrGrp.removeProhibitedAttrs();
/*      */ 
/*  876 */       if (baseType != SchemaGrammar.fAnyType) {
/*  877 */         Object[] errArgs = this.fAttrGrp.validRestrictionOf(this.fName, baseType.getAttrGrp());
/*  878 */         if (errArgs != null) {
/*  879 */           this.fAttrChecker.returnAttrArray(complexContentAttrValues, schemaDoc);
/*  880 */           this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
/*  881 */           throw new ComplexTypeRecoverableError((String)errArgs[(errArgs.length - 1)], errArgs, complexContent);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*  891 */       if (this.fParticle == null) {
/*  892 */         this.fContentType = baseType.getContentType();
/*  893 */         this.fXSSimpleType = ((XSSimpleType)baseType.getSimpleType());
/*  894 */         this.fParticle = baseContent;
/*      */       }
/*  896 */       else if (baseType.getContentType() != 0)
/*      */       {
/*  902 */         if ((this.fContentType == 2) && (baseType.getContentType() != 2))
/*      */         {
/*  904 */           this.fAttrChecker.returnAttrArray(complexContentAttrValues, schemaDoc);
/*  905 */           this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
/*  906 */           throw new ComplexTypeRecoverableError("cos-ct-extends.1.4.3.2.2.1.a", new Object[] { this.fName }, complexContent);
/*      */         }
/*      */ 
/*  909 */         if ((this.fContentType == 3) && (baseType.getContentType() != 3))
/*      */         {
/*  911 */           this.fAttrChecker.returnAttrArray(complexContentAttrValues, schemaDoc);
/*  912 */           this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
/*  913 */           throw new ComplexTypeRecoverableError("cos-ct-extends.1.4.3.2.2.1.b", new Object[] { this.fName }, complexContent);
/*      */         }
/*      */ 
/*  918 */         if (((this.fParticle.fType == 3) && (((XSModelGroupImpl)this.fParticle.fValue).fCompositor == 103)) || ((((XSParticleDecl)baseType.getParticle()).fType == 3) && (((XSModelGroupImpl)((XSParticleDecl)baseType.getParticle()).fValue).fCompositor == 103)))
/*      */         {
/*  922 */           this.fAttrChecker.returnAttrArray(complexContentAttrValues, schemaDoc);
/*  923 */           this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
/*  924 */           throw new ComplexTypeRecoverableError("cos-all-limited.1.2", new Object[0], complexContent);
/*      */         }
/*      */ 
/*  928 */         XSModelGroupImpl group = new XSModelGroupImpl();
/*  929 */         group.fCompositor = 102;
/*  930 */         group.fParticleCount = 2;
/*  931 */         group.fParticles = new XSParticleDecl[2];
/*  932 */         group.fParticles[0] = ((XSParticleDecl)baseType.getParticle());
/*  933 */         group.fParticles[1] = this.fParticle;
/*  934 */         group.fAnnotations = XSObjectListImpl.EMPTY_LIST;
/*      */ 
/*  936 */         XSParticleDecl particle = new XSParticleDecl();
/*  937 */         particle.fType = 3;
/*  938 */         particle.fValue = group;
/*  939 */         particle.fAnnotations = XSObjectListImpl.EMPTY_LIST;
/*      */ 
/*  941 */         this.fParticle = particle;
/*      */       }
/*      */ 
/*  945 */       this.fAttrGrp.removeProhibitedAttrs();
/*      */       try {
/*  947 */         mergeAttributes(baseType.getAttrGrp(), this.fAttrGrp, this.fName, true, complexContent);
/*      */       } catch (ComplexTypeRecoverableError e) {
/*  949 */         this.fAttrChecker.returnAttrArray(complexContentAttrValues, schemaDoc);
/*  950 */         this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
/*  951 */         throw e;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  957 */     this.fAttrChecker.returnAttrArray(complexContentAttrValues, schemaDoc);
/*  958 */     this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
/*      */   }
/*      */ 
/*      */   private void mergeAttributes(XSAttributeGroupDecl fromAttrGrp, XSAttributeGroupDecl toAttrGrp, String typeName, boolean extension, Element elem)
/*      */     throws XSDComplexTypeTraverser.ComplexTypeRecoverableError
/*      */   {
/*  972 */     XSObjectList attrUseS = fromAttrGrp.getAttributeUses();
/*  973 */     XSAttributeUseImpl oneAttrUse = null;
/*  974 */     int attrCount = attrUseS.getLength();
/*  975 */     for (int i = 0; i < attrCount; i++) {
/*  976 */       oneAttrUse = (XSAttributeUseImpl)attrUseS.item(i);
/*  977 */       XSAttributeUse existingAttrUse = toAttrGrp.getAttributeUse(oneAttrUse.fAttrDecl.getNamespace(), oneAttrUse.fAttrDecl.getName());
/*      */ 
/*  979 */       if (existingAttrUse == null)
/*      */       {
/*  981 */         String idName = toAttrGrp.addAttributeUse(oneAttrUse);
/*  982 */         if (idName != null) {
/*  983 */           throw new ComplexTypeRecoverableError("ct-props-correct.5", new Object[] { typeName, idName, oneAttrUse.fAttrDecl.getName() }, elem);
/*      */         }
/*      */ 
/*      */       }
/*  988 */       else if ((existingAttrUse != oneAttrUse) && 
/*  989 */         (extension)) {
/*  990 */         reportSchemaError("ct-props-correct.4", new Object[] { typeName, oneAttrUse.fAttrDecl.getName() }, elem);
/*      */ 
/*  995 */         toAttrGrp.replaceAttributeUse(existingAttrUse, oneAttrUse);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1000 */     if (extension)
/* 1001 */       if (toAttrGrp.fAttributeWC == null) {
/* 1002 */         toAttrGrp.fAttributeWC = fromAttrGrp.fAttributeWC;
/*      */       }
/* 1004 */       else if (fromAttrGrp.fAttributeWC != null) {
/* 1005 */         toAttrGrp.fAttributeWC = toAttrGrp.fAttributeWC.performUnionWith(fromAttrGrp.fAttributeWC, toAttrGrp.fAttributeWC.fProcessContents);
/* 1006 */         if (toAttrGrp.fAttributeWC == null)
/*      */         {
/* 1010 */           throw new ComplexTypeRecoverableError("src-ct.5", new Object[] { typeName }, elem);
/*      */         }
/*      */       }
/*      */   }
/*      */ 
/*      */   private void processComplexContent(Element complexContentChild, boolean isMixed, boolean isDerivation, XSDocumentInfo schemaDoc, SchemaGrammar grammar)
/*      */     throws XSDComplexTypeTraverser.ComplexTypeRecoverableError
/*      */   {
/* 1022 */     Element attrNode = null;
/* 1023 */     XSParticleDecl particle = null;
/*      */ 
/* 1026 */     boolean emptyParticle = false;
/* 1027 */     if (complexContentChild != null)
/*      */     {
/* 1034 */       String childName = DOMUtil.getLocalName(complexContentChild);
/*      */ 
/* 1036 */       if (childName.equals(SchemaSymbols.ELT_GROUP))
/*      */       {
/* 1038 */         particle = this.fSchemaHandler.fGroupTraverser.traverseLocal(complexContentChild, schemaDoc, grammar);
/*      */ 
/* 1040 */         attrNode = DOMUtil.getNextSiblingElement(complexContentChild);
/*      */       }
/* 1042 */       else if (childName.equals(SchemaSymbols.ELT_SEQUENCE)) {
/* 1043 */         particle = traverseSequence(complexContentChild, schemaDoc, grammar, 0, this.fComplexTypeDecl);
/*      */ 
/* 1045 */         if (particle != null) {
/* 1046 */           XSModelGroupImpl group = (XSModelGroupImpl)particle.fValue;
/* 1047 */           if (group.fParticleCount == 0)
/* 1048 */             emptyParticle = true;
/*      */         }
/* 1050 */         attrNode = DOMUtil.getNextSiblingElement(complexContentChild);
/*      */       }
/* 1052 */       else if (childName.equals(SchemaSymbols.ELT_CHOICE)) {
/* 1053 */         particle = traverseChoice(complexContentChild, schemaDoc, grammar, 0, this.fComplexTypeDecl);
/*      */ 
/* 1055 */         if ((particle != null) && (particle.fMinOccurs == 0)) {
/* 1056 */           XSModelGroupImpl group = (XSModelGroupImpl)particle.fValue;
/* 1057 */           if (group.fParticleCount == 0)
/* 1058 */             emptyParticle = true;
/*      */         }
/* 1060 */         attrNode = DOMUtil.getNextSiblingElement(complexContentChild);
/*      */       }
/* 1062 */       else if (childName.equals(SchemaSymbols.ELT_ALL)) {
/* 1063 */         particle = traverseAll(complexContentChild, schemaDoc, grammar, 8, this.fComplexTypeDecl);
/*      */ 
/* 1065 */         if (particle != null) {
/* 1066 */           XSModelGroupImpl group = (XSModelGroupImpl)particle.fValue;
/* 1067 */           if (group.fParticleCount == 0)
/* 1068 */             emptyParticle = true;
/*      */         }
/* 1070 */         attrNode = DOMUtil.getNextSiblingElement(complexContentChild);
/*      */       }
/*      */       else
/*      */       {
/* 1074 */         attrNode = complexContentChild;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1081 */     if (emptyParticle)
/*      */     {
/* 1083 */       Element child = DOMUtil.getFirstChildElement(complexContentChild);
/*      */ 
/* 1085 */       if ((child != null) && 
/* 1086 */         (DOMUtil.getLocalName(child).equals(SchemaSymbols.ELT_ANNOTATION))) {
/* 1087 */         child = DOMUtil.getNextSiblingElement(child);
/*      */       }
/*      */ 
/* 1091 */       if (child == null) {
/* 1092 */         particle = null;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1097 */     if ((particle == null) && (isMixed)) {
/* 1098 */       particle = XSConstraints.getEmptySequence();
/*      */     }
/* 1100 */     this.fParticle = particle;
/*      */ 
/* 1105 */     if (this.fParticle == null)
/* 1106 */       this.fContentType = 0;
/* 1107 */     else if (isMixed)
/* 1108 */       this.fContentType = 3;
/*      */     else {
/* 1110 */       this.fContentType = 2;
/*      */     }
/*      */ 
/* 1116 */     if (attrNode != null) {
/* 1117 */       if (!isAttrOrAttrGroup(attrNode)) {
/* 1118 */         throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[] { this.fName, DOMUtil.getLocalName(attrNode) }, attrNode);
/*      */       }
/*      */ 
/* 1122 */       Element node = traverseAttrsAndAttrGrps(attrNode, this.fAttrGrp, schemaDoc, grammar, this.fComplexTypeDecl);
/*      */ 
/* 1124 */       if (node != null) {
/* 1125 */         throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[] { this.fName, DOMUtil.getLocalName(node) }, node);
/*      */       }
/*      */ 
/* 1131 */       if (!isDerivation)
/* 1132 */         this.fAttrGrp.removeProhibitedAttrs();
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean isAttrOrAttrGroup(Element e)
/*      */   {
/* 1142 */     String elementName = DOMUtil.getLocalName(e);
/*      */ 
/* 1144 */     if ((elementName.equals(SchemaSymbols.ELT_ATTRIBUTE)) || (elementName.equals(SchemaSymbols.ELT_ATTRIBUTEGROUP)) || (elementName.equals(SchemaSymbols.ELT_ANYATTRIBUTE)))
/*      */     {
/* 1147 */       return true;
/*      */     }
/* 1149 */     return false;
/*      */   }
/*      */ 
/*      */   private void traverseSimpleContentDecl(Element simpleContentDecl)
/*      */   {
/*      */   }
/*      */ 
/*      */   private void traverseComplexContentDecl(Element complexContentDecl, boolean mixedOnComplexTypeDecl)
/*      */   {
/*      */   }
/*      */ 
/*      */   private String genAnonTypeName(Element complexTypeDecl)
/*      */   {
/* 1168 */     StringBuffer typeName = new StringBuffer("#AnonType_");
/* 1169 */     Element node = DOMUtil.getParent(complexTypeDecl);
/* 1170 */     while ((node != null) && (node != DOMUtil.getRoot(DOMUtil.getDocument(node)))) {
/* 1171 */       typeName.append(node.getAttribute(SchemaSymbols.ATT_NAME));
/* 1172 */       node = DOMUtil.getParent(node);
/*      */     }
/* 1174 */     return typeName.toString();
/*      */   }
/*      */ 
/*      */   private void handleComplexTypeError(String messageId, Object[] args, Element e)
/*      */   {
/* 1181 */     if (messageId != null) {
/* 1182 */       reportSchemaError(messageId, args, e);
/*      */     }
/*      */ 
/* 1189 */     this.fBaseType = SchemaGrammar.fAnyType;
/* 1190 */     this.fContentType = 3;
/* 1191 */     this.fXSSimpleType = null;
/* 1192 */     this.fParticle = getErrorContent();
/*      */ 
/* 1195 */     this.fAttrGrp.fAttributeWC = getErrorWildcard();
/*      */   }
/*      */ 
/*      */   private void contentBackup()
/*      */   {
/* 1202 */     if (this.fGlobalStore == null) {
/* 1203 */       this.fGlobalStore = new Object[11];
/* 1204 */       this.fGlobalStorePos = 0;
/*      */     }
/* 1206 */     if (this.fGlobalStorePos == this.fGlobalStore.length) {
/* 1207 */       Object[] newArray = new Object[this.fGlobalStorePos + 11];
/* 1208 */       System.arraycopy(this.fGlobalStore, 0, newArray, 0, this.fGlobalStorePos);
/* 1209 */       this.fGlobalStore = newArray;
/*      */     }
/* 1211 */     this.fGlobalStore[(this.fGlobalStorePos++)] = this.fComplexTypeDecl;
/* 1212 */     this.fGlobalStore[(this.fGlobalStorePos++)] = (this.fIsAbstract ? Boolean.TRUE : Boolean.FALSE);
/* 1213 */     this.fGlobalStore[(this.fGlobalStorePos++)] = this.fName;
/* 1214 */     this.fGlobalStore[(this.fGlobalStorePos++)] = this.fTargetNamespace;
/*      */ 
/* 1216 */     this.fGlobalStore[(this.fGlobalStorePos++)] = new Integer((this.fDerivedBy << 16) + this.fFinal);
/* 1217 */     this.fGlobalStore[(this.fGlobalStorePos++)] = new Integer((this.fBlock << 16) + this.fContentType);
/* 1218 */     this.fGlobalStore[(this.fGlobalStorePos++)] = this.fBaseType;
/* 1219 */     this.fGlobalStore[(this.fGlobalStorePos++)] = this.fAttrGrp;
/* 1220 */     this.fGlobalStore[(this.fGlobalStorePos++)] = this.fParticle;
/* 1221 */     this.fGlobalStore[(this.fGlobalStorePos++)] = this.fXSSimpleType;
/* 1222 */     this.fGlobalStore[(this.fGlobalStorePos++)] = this.fAnnotations;
/*      */   }
/*      */ 
/*      */   private void contentRestore() {
/* 1226 */     this.fAnnotations = ((XSAnnotationImpl[])this.fGlobalStore[(--this.fGlobalStorePos)]);
/* 1227 */     this.fXSSimpleType = ((XSSimpleType)this.fGlobalStore[(--this.fGlobalStorePos)]);
/* 1228 */     this.fParticle = ((XSParticleDecl)this.fGlobalStore[(--this.fGlobalStorePos)]);
/* 1229 */     this.fAttrGrp = ((XSAttributeGroupDecl)this.fGlobalStore[(--this.fGlobalStorePos)]);
/* 1230 */     this.fBaseType = ((XSTypeDefinition)this.fGlobalStore[(--this.fGlobalStorePos)]);
/* 1231 */     int i = ((Integer)this.fGlobalStore[(--this.fGlobalStorePos)]).intValue();
/* 1232 */     this.fBlock = ((short)(i >> 16));
/* 1233 */     this.fContentType = ((short)i);
/* 1234 */     i = ((Integer)this.fGlobalStore[(--this.fGlobalStorePos)]).intValue();
/* 1235 */     this.fDerivedBy = ((short)(i >> 16));
/* 1236 */     this.fFinal = ((short)i);
/* 1237 */     this.fTargetNamespace = ((String)this.fGlobalStore[(--this.fGlobalStorePos)]);
/* 1238 */     this.fName = ((String)this.fGlobalStore[(--this.fGlobalStorePos)]);
/* 1239 */     this.fIsAbstract = ((Boolean)this.fGlobalStore[(--this.fGlobalStorePos)]).booleanValue();
/* 1240 */     this.fComplexTypeDecl = ((XSComplexTypeDecl)this.fGlobalStore[(--this.fGlobalStorePos)]);
/*      */   }
/*      */ 
/*      */   private void addAnnotation(XSAnnotationImpl annotation) {
/* 1244 */     if (annotation == null) {
/* 1245 */       return;
/*      */     }
/*      */ 
/* 1250 */     if (this.fAnnotations == null) {
/* 1251 */       this.fAnnotations = new XSAnnotationImpl[1];
/*      */     } else {
/* 1253 */       XSAnnotationImpl[] tempArray = new XSAnnotationImpl[this.fAnnotations.length + 1];
/* 1254 */       System.arraycopy(this.fAnnotations, 0, tempArray, 0, this.fAnnotations.length);
/* 1255 */       this.fAnnotations = tempArray;
/*      */     }
/* 1257 */     this.fAnnotations[(this.fAnnotations.length - 1)] = annotation;
/*      */   }
/*      */ 
/*      */   private static final class ComplexTypeRecoverableError extends Exception
/*      */   {
/*      */     private static final long serialVersionUID = 6802729912091130335L;
/*  134 */     Object[] errorSubstText = null;
/*  135 */     Element errorElem = null;
/*      */ 
/*      */     ComplexTypeRecoverableError() {
/*      */     }
/*      */     ComplexTypeRecoverableError(String msgKey, Object[] args, Element e) {
/*  140 */       super();
/*  141 */       this.errorSubstText = args;
/*  142 */       this.errorElem = e;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.traversers.XSDComplexTypeTraverser
 * JD-Core Version:    0.6.2
 */