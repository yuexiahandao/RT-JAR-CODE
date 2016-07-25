/*     */ package com.sun.org.apache.xerces.internal.impl.xs.traversers;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.ValidatedInfo;
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType;
/*     */ import com.sun.org.apache.xerces.internal.impl.validation.ValidationState;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XSAnnotationImpl;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XSAttributeDecl;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XSAttributeUseImpl;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XSComplexTypeDecl;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XSDeclarationPool;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.util.XInt;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.util.XSObjectListImpl;
/*     */ import com.sun.org.apache.xerces.internal.util.DOMUtil;
/*     */ import com.sun.org.apache.xerces.internal.util.SymbolTable;
/*     */ import com.sun.org.apache.xerces.internal.util.XMLSymbols;
/*     */ import com.sun.org.apache.xerces.internal.xni.QName;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSObjectList;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ class XSDAttributeTraverser extends XSDAbstractTraverser
/*     */ {
/*     */   public XSDAttributeTraverser(XSDHandler handler, XSAttributeChecker gAttrCheck)
/*     */   {
/*  68 */     super(handler, gAttrCheck);
/*     */   }
/*     */ 
/*     */   protected XSAttributeUseImpl traverseLocal(Element attrDecl, XSDocumentInfo schemaDoc, SchemaGrammar grammar, XSComplexTypeDecl enclosingCT)
/*     */   {
/*  77 */     Object[] attrValues = this.fAttrChecker.checkAttributes(attrDecl, false, schemaDoc);
/*     */ 
/*  79 */     String defaultAtt = (String)attrValues[XSAttributeChecker.ATTIDX_DEFAULT];
/*  80 */     String fixedAtt = (String)attrValues[XSAttributeChecker.ATTIDX_FIXED];
/*  81 */     String nameAtt = (String)attrValues[XSAttributeChecker.ATTIDX_NAME];
/*  82 */     QName refAtt = (QName)attrValues[XSAttributeChecker.ATTIDX_REF];
/*  83 */     XInt useAtt = (XInt)attrValues[XSAttributeChecker.ATTIDX_USE];
/*     */ 
/*  86 */     XSAttributeDecl attribute = null;
/*  87 */     XSAnnotationImpl annotation = null;
/*  88 */     if (attrDecl.getAttributeNode(SchemaSymbols.ATT_REF) != null) {
/*  89 */       if (refAtt != null) {
/*  90 */         attribute = (XSAttributeDecl)this.fSchemaHandler.getGlobalDecl(schemaDoc, 1, refAtt, attrDecl);
/*     */ 
/*  92 */         Element child = DOMUtil.getFirstChildElement(attrDecl);
/*  93 */         if ((child != null) && (DOMUtil.getLocalName(child).equals(SchemaSymbols.ELT_ANNOTATION))) {
/*  94 */           annotation = traverseAnnotationDecl(child, attrValues, false, schemaDoc);
/*  95 */           child = DOMUtil.getNextSiblingElement(child);
/*     */         }
/*     */         else {
/*  98 */           String text = DOMUtil.getSyntheticAnnotation(attrDecl);
/*  99 */           if (text != null) {
/* 100 */             annotation = traverseSyntheticAnnotation(attrDecl, text, attrValues, false, schemaDoc);
/*     */           }
/*     */         }
/*     */ 
/* 104 */         if (child != null) {
/* 105 */           reportSchemaError("src-attribute.3.2", new Object[] { refAtt.rawname }, child);
/*     */         }
/*     */ 
/* 108 */         nameAtt = refAtt.localpart;
/*     */       } else {
/* 110 */         attribute = null;
/*     */       }
/*     */     }
/* 113 */     else attribute = traverseNamedAttr(attrDecl, attrValues, schemaDoc, grammar, false, enclosingCT);
/*     */ 
/* 117 */     short consType = 0;
/* 118 */     if (defaultAtt != null) {
/* 119 */       consType = 1;
/* 120 */     } else if (fixedAtt != null) {
/* 121 */       consType = 2;
/* 122 */       defaultAtt = fixedAtt;
/* 123 */       fixedAtt = null;
/*     */     }
/*     */ 
/* 126 */     XSAttributeUseImpl attrUse = null;
/* 127 */     if (attribute != null) {
/* 128 */       if (this.fSchemaHandler.fDeclPool != null)
/* 129 */         attrUse = this.fSchemaHandler.fDeclPool.getAttributeUse();
/*     */       else {
/* 131 */         attrUse = new XSAttributeUseImpl();
/*     */       }
/* 133 */       attrUse.fAttrDecl = attribute;
/* 134 */       attrUse.fUse = useAtt.shortValue();
/* 135 */       attrUse.fConstraintType = consType;
/* 136 */       if (defaultAtt != null) {
/* 137 */         attrUse.fDefault = new ValidatedInfo();
/* 138 */         attrUse.fDefault.normalizedValue = defaultAtt;
/*     */       }
/*     */ 
/* 141 */       if (attrDecl.getAttributeNode(SchemaSymbols.ATT_REF) == null) {
/* 142 */         attrUse.fAnnotations = attribute.getAnnotations();
/*     */       }
/*     */       else
/*     */       {
/*     */         XSObjectList annotations;
/* 145 */         if (annotation != null) {
/* 146 */           XSObjectList annotations = new XSObjectListImpl();
/* 147 */           ((XSObjectListImpl)annotations).addXSObject(annotation);
/*     */         } else {
/* 149 */           annotations = XSObjectListImpl.EMPTY_LIST;
/*     */         }
/* 151 */         attrUse.fAnnotations = annotations;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 158 */     if ((defaultAtt != null) && (fixedAtt != null)) {
/* 159 */       reportSchemaError("src-attribute.1", new Object[] { nameAtt }, attrDecl);
/*     */     }
/*     */ 
/* 163 */     if ((consType == 1) && (useAtt != null) && (useAtt.intValue() != 0))
/*     */     {
/* 165 */       reportSchemaError("src-attribute.2", new Object[] { nameAtt }, attrDecl);
/*     */ 
/* 167 */       attrUse.fUse = 0;
/*     */     }
/*     */ 
/* 172 */     if ((defaultAtt != null) && (attrUse != null))
/*     */     {
/* 174 */       this.fValidationState.setNamespaceSupport(schemaDoc.fNamespaceSupport);
/*     */       try {
/* 176 */         checkDefaultValid(attrUse);
/*     */       }
/*     */       catch (InvalidDatatypeValueException ide) {
/* 179 */         reportSchemaError(ide.getKey(), ide.getArgs(), attrDecl);
/* 180 */         reportSchemaError("a-props-correct.2", new Object[] { nameAtt, defaultAtt }, attrDecl);
/*     */ 
/* 182 */         attrUse.fDefault = null;
/* 183 */         attrUse.fConstraintType = 0;
/*     */       }
/*     */ 
/* 187 */       if (((XSSimpleType)attribute.getTypeDefinition()).isIDType()) {
/* 188 */         reportSchemaError("a-props-correct.3", new Object[] { nameAtt }, attrDecl);
/*     */ 
/* 190 */         attrUse.fDefault = null;
/* 191 */         attrUse.fConstraintType = 0;
/*     */       }
/*     */ 
/* 197 */       if ((attrUse.fAttrDecl.getConstraintType() == 2) && (attrUse.fConstraintType != 0))
/*     */       {
/* 199 */         if ((attrUse.fConstraintType != 2) || (!attrUse.fAttrDecl.getValInfo().actualValue.equals(attrUse.fDefault.actualValue)))
/*     */         {
/* 201 */           reportSchemaError("au-props-correct.2", new Object[] { nameAtt, attrUse.fAttrDecl.getValInfo().stringValue() }, attrDecl);
/*     */ 
/* 203 */           attrUse.fDefault = attrUse.fAttrDecl.getValInfo();
/* 204 */           attrUse.fConstraintType = 2;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 209 */     this.fAttrChecker.returnAttrArray(attrValues, schemaDoc);
/* 210 */     return attrUse;
/*     */   }
/*     */ 
/*     */   protected XSAttributeDecl traverseGlobal(Element attrDecl, XSDocumentInfo schemaDoc, SchemaGrammar grammar)
/*     */   {
/* 218 */     Object[] attrValues = this.fAttrChecker.checkAttributes(attrDecl, true, schemaDoc);
/* 219 */     XSAttributeDecl attribute = traverseNamedAttr(attrDecl, attrValues, schemaDoc, grammar, true, null);
/* 220 */     this.fAttrChecker.returnAttrArray(attrValues, schemaDoc);
/* 221 */     return attribute;
/*     */   }
/*     */ 
/*     */   XSAttributeDecl traverseNamedAttr(Element attrDecl, Object[] attrValues, XSDocumentInfo schemaDoc, SchemaGrammar grammar, boolean isGlobal, XSComplexTypeDecl enclosingCT)
/*     */   {
/* 242 */     String defaultAtt = (String)attrValues[XSAttributeChecker.ATTIDX_DEFAULT];
/* 243 */     String fixedAtt = (String)attrValues[XSAttributeChecker.ATTIDX_FIXED];
/* 244 */     XInt formAtt = (XInt)attrValues[XSAttributeChecker.ATTIDX_FORM];
/* 245 */     String nameAtt = (String)attrValues[XSAttributeChecker.ATTIDX_NAME];
/* 246 */     QName typeAtt = (QName)attrValues[XSAttributeChecker.ATTIDX_TYPE];
/*     */ 
/* 249 */     XSAttributeDecl attribute = null;
/* 250 */     if (this.fSchemaHandler.fDeclPool != null)
/* 251 */       attribute = this.fSchemaHandler.fDeclPool.getAttributeDecl();
/*     */     else {
/* 253 */       attribute = new XSAttributeDecl();
/*     */     }
/*     */ 
/* 257 */     if (nameAtt != null) {
/* 258 */       nameAtt = this.fSymbolTable.addSymbol(nameAtt);
/*     */     }
/*     */ 
/* 261 */     String tnsAtt = null;
/* 262 */     XSComplexTypeDecl enclCT = null;
/* 263 */     short scope = 0;
/* 264 */     if (isGlobal) {
/* 265 */       tnsAtt = schemaDoc.fTargetNamespace;
/* 266 */       scope = 1;
/*     */     }
/*     */     else {
/* 269 */       if (enclosingCT != null) {
/* 270 */         enclCT = enclosingCT;
/* 271 */         scope = 2;
/*     */       }
/* 273 */       if (formAtt != null) {
/* 274 */         if (formAtt.intValue() == 1)
/* 275 */           tnsAtt = schemaDoc.fTargetNamespace;
/* 276 */       } else if (schemaDoc.fAreLocalAttributesQualified) {
/* 277 */         tnsAtt = schemaDoc.fTargetNamespace;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 282 */     ValidatedInfo attDefault = null;
/* 283 */     short constraintType = 0;
/* 284 */     if (isGlobal) {
/* 285 */       if (fixedAtt != null) {
/* 286 */         attDefault = new ValidatedInfo();
/* 287 */         attDefault.normalizedValue = fixedAtt;
/* 288 */         constraintType = 2;
/* 289 */       } else if (defaultAtt != null) {
/* 290 */         attDefault = new ValidatedInfo();
/* 291 */         attDefault.normalizedValue = defaultAtt;
/* 292 */         constraintType = 1;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 297 */     Element child = DOMUtil.getFirstChildElement(attrDecl);
/* 298 */     XSAnnotationImpl annotation = null;
/* 299 */     if ((child != null) && (DOMUtil.getLocalName(child).equals(SchemaSymbols.ELT_ANNOTATION))) {
/* 300 */       annotation = traverseAnnotationDecl(child, attrValues, false, schemaDoc);
/* 301 */       child = DOMUtil.getNextSiblingElement(child);
/*     */     }
/*     */     else {
/* 304 */       String text = DOMUtil.getSyntheticAnnotation(attrDecl);
/* 305 */       if (text != null) {
/* 306 */         annotation = traverseSyntheticAnnotation(attrDecl, text, attrValues, false, schemaDoc);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 311 */     XSSimpleType attrType = null;
/* 312 */     boolean haveAnonType = false;
/*     */ 
/* 315 */     if (child != null) {
/* 316 */       String childName = DOMUtil.getLocalName(child);
/*     */ 
/* 318 */       if (childName.equals(SchemaSymbols.ELT_SIMPLETYPE)) {
/* 319 */         attrType = this.fSchemaHandler.fSimpleTypeTraverser.traverseLocal(child, schemaDoc, grammar);
/* 320 */         haveAnonType = true;
/* 321 */         child = DOMUtil.getNextSiblingElement(child);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 326 */     if ((attrType == null) && (typeAtt != null)) {
/* 327 */       XSTypeDefinition type = (XSTypeDefinition)this.fSchemaHandler.getGlobalDecl(schemaDoc, 7, typeAtt, attrDecl);
/* 328 */       if ((type != null) && (type.getTypeCategory() == 16)) {
/* 329 */         attrType = (XSSimpleType)type;
/*     */       }
/*     */       else {
/* 332 */         reportSchemaError("src-resolve", new Object[] { typeAtt.rawname, "simpleType definition" }, attrDecl);
/* 333 */         if (type == null) {
/* 334 */           attribute.fUnresolvedTypeName = typeAtt;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 339 */     if (attrType == null)
/* 340 */       attrType = SchemaGrammar.fAnySimpleType;
/*     */     XSObjectList annotations;
/* 344 */     if (annotation != null) {
/* 345 */       XSObjectList annotations = new XSObjectListImpl();
/* 346 */       ((XSObjectListImpl)annotations).addXSObject(annotation);
/*     */     } else {
/* 348 */       annotations = XSObjectListImpl.EMPTY_LIST;
/*     */     }
/* 350 */     attribute.setValues(nameAtt, tnsAtt, attrType, constraintType, scope, attDefault, enclCT, annotations);
/*     */ 
/* 356 */     if (nameAtt == null) {
/* 357 */       if (isGlobal)
/* 358 */         reportSchemaError("s4s-att-must-appear", new Object[] { SchemaSymbols.ELT_ATTRIBUTE, SchemaSymbols.ATT_NAME }, attrDecl);
/*     */       else
/* 360 */         reportSchemaError("src-attribute.3.1", null, attrDecl);
/* 361 */       nameAtt = "(no name)";
/*     */     }
/*     */ 
/* 365 */     if (child != null) {
/* 366 */       reportSchemaError("s4s-elt-must-match.1", new Object[] { nameAtt, "(annotation?, (simpleType?))", DOMUtil.getLocalName(child) }, child);
/*     */     }
/*     */ 
/* 374 */     if ((defaultAtt != null) && (fixedAtt != null)) {
/* 375 */       reportSchemaError("src-attribute.1", new Object[] { nameAtt }, attrDecl);
/*     */     }
/*     */ 
/* 389 */     if ((haveAnonType) && (typeAtt != null)) {
/* 390 */       reportSchemaError("src-attribute.4", new Object[] { nameAtt }, attrDecl);
/*     */     }
/*     */ 
/* 395 */     checkNotationType(nameAtt, attrType, attrDecl);
/*     */ 
/* 400 */     if (attDefault != null) {
/* 401 */       this.fValidationState.setNamespaceSupport(schemaDoc.fNamespaceSupport);
/*     */       try {
/* 403 */         checkDefaultValid(attribute);
/*     */       }
/*     */       catch (InvalidDatatypeValueException ide) {
/* 406 */         reportSchemaError(ide.getKey(), ide.getArgs(), attrDecl);
/* 407 */         reportSchemaError("a-props-correct.2", new Object[] { nameAtt, attDefault.normalizedValue }, attrDecl);
/*     */ 
/* 409 */         attDefault = null;
/* 410 */         constraintType = 0;
/* 411 */         attribute.setValues(nameAtt, tnsAtt, attrType, constraintType, scope, attDefault, enclCT, annotations);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 417 */     if ((attDefault != null) && 
/* 418 */       (attrType.isIDType())) {
/* 419 */       reportSchemaError("a-props-correct.3", new Object[] { nameAtt }, attrDecl);
/*     */ 
/* 421 */       attDefault = null;
/* 422 */       constraintType = 0;
/* 423 */       attribute.setValues(nameAtt, tnsAtt, attrType, constraintType, scope, attDefault, enclCT, annotations);
/*     */     }
/*     */ 
/* 431 */     if ((nameAtt != null) && (nameAtt.equals(XMLSymbols.PREFIX_XMLNS))) {
/* 432 */       reportSchemaError("no-xmlns", null, attrDecl);
/* 433 */       return null;
/*     */     }
/*     */ 
/* 439 */     if ((tnsAtt != null) && (tnsAtt.equals(SchemaSymbols.URI_XSI))) {
/* 440 */       reportSchemaError("no-xsi", new Object[] { SchemaSymbols.URI_XSI }, attrDecl);
/* 441 */       return null;
/*     */     }
/*     */ 
/* 445 */     if (nameAtt.equals("(no name)")) {
/* 446 */       return null;
/*     */     }
/*     */ 
/* 449 */     if (isGlobal) {
/* 450 */       if (grammar.getGlobalAttributeDecl(nameAtt) == null) {
/* 451 */         grammar.addGlobalAttributeDecl(attribute);
/*     */       }
/*     */ 
/* 455 */       String loc = this.fSchemaHandler.schemaDocument2SystemId(schemaDoc);
/* 456 */       XSAttributeDecl attribute2 = grammar.getGlobalAttributeDecl(nameAtt, loc);
/* 457 */       if (attribute2 == null) {
/* 458 */         grammar.addGlobalAttributeDecl(attribute, loc);
/*     */       }
/*     */ 
/* 461 */       if (this.fSchemaHandler.fTolerateDuplicates) {
/* 462 */         if (attribute2 != null) {
/* 463 */           attribute = attribute2;
/*     */         }
/* 465 */         this.fSchemaHandler.addGlobalAttributeDecl(attribute);
/*     */       }
/*     */     }
/*     */ 
/* 469 */     return attribute;
/*     */   }
/*     */ 
/*     */   void checkDefaultValid(XSAttributeDecl attribute)
/*     */     throws InvalidDatatypeValueException
/*     */   {
/* 475 */     ((XSSimpleType)attribute.getTypeDefinition()).validate(attribute.getValInfo().normalizedValue, this.fValidationState, attribute.getValInfo());
/*     */ 
/* 477 */     ((XSSimpleType)attribute.getTypeDefinition()).validate(attribute.getValInfo().stringValue(), this.fValidationState, attribute.getValInfo());
/*     */   }
/*     */ 
/*     */   void checkDefaultValid(XSAttributeUseImpl attrUse)
/*     */     throws InvalidDatatypeValueException
/*     */   {
/* 483 */     ((XSSimpleType)attrUse.fAttrDecl.getTypeDefinition()).validate(attrUse.fDefault.normalizedValue, this.fValidationState, attrUse.fDefault);
/*     */ 
/* 485 */     ((XSSimpleType)attrUse.fAttrDecl.getTypeDefinition()).validate(attrUse.fDefault.stringValue(), this.fValidationState, attrUse.fDefault);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.traversers.XSDAttributeTraverser
 * JD-Core Version:    0.6.2
 */