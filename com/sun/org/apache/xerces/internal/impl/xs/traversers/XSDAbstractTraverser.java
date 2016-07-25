/*     */ package com.sun.org.apache.xerces.internal.impl.xs.traversers;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.XSFacets;
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType;
/*     */ import com.sun.org.apache.xerces.internal.impl.validation.ValidationState;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar.BuiltinSchemaGrammar;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.SchemaNamespaceSupport;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XSAnnotationImpl;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XSAttributeDecl;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XSAttributeGroupDecl;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XSAttributeUseImpl;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XSComplexTypeDecl;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XSElementDecl;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XSParticleDecl;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XSWildcardDecl;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.util.XInt;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.util.XSObjectListImpl;
/*     */ import com.sun.org.apache.xerces.internal.util.DOMUtil;
/*     */ import com.sun.org.apache.xerces.internal.util.NamespaceSupport;
/*     */ import com.sun.org.apache.xerces.internal.util.SymbolTable;
/*     */ import com.sun.org.apache.xerces.internal.xni.QName;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSAttributeUse;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSObjectList;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;
/*     */ import java.util.Locale;
/*     */ import java.util.Vector;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ abstract class XSDAbstractTraverser
/*     */ {
/*     */   protected static final String NO_NAME = "(no name)";
/*     */   protected static final int NOT_ALL_CONTEXT = 0;
/*     */   protected static final int PROCESSING_ALL_EL = 1;
/*     */   protected static final int GROUP_REF_WITH_ALL = 2;
/*     */   protected static final int CHILD_OF_GROUP = 4;
/*     */   protected static final int PROCESSING_ALL_GP = 8;
/*  82 */   protected XSDHandler fSchemaHandler = null;
/*  83 */   protected SymbolTable fSymbolTable = null;
/*  84 */   protected XSAttributeChecker fAttrChecker = null;
/*  85 */   protected boolean fValidateAnnotations = false;
/*     */ 
/*  88 */   ValidationState fValidationState = new ValidationState();
/*     */ 
/* 259 */   private static final XSSimpleType fQNameDV = (XSSimpleType)SchemaGrammar.SG_SchemaNS.getGlobalTypeDecl("QName");
/*     */ 
/* 261 */   private StringBuffer fPattern = new StringBuffer();
/* 262 */   private final XSFacets xsFacets = new XSFacets();
/*     */ 
/*     */   XSDAbstractTraverser(XSDHandler handler, XSAttributeChecker attrChecker)
/*     */   {
/*  92 */     this.fSchemaHandler = handler;
/*  93 */     this.fAttrChecker = attrChecker;
/*     */   }
/*     */ 
/*     */   void reset(SymbolTable symbolTable, boolean validateAnnotations, Locale locale) {
/*  97 */     this.fSymbolTable = symbolTable;
/*  98 */     this.fValidateAnnotations = validateAnnotations;
/*  99 */     this.fValidationState.setExtraChecking(false);
/* 100 */     this.fValidationState.setSymbolTable(symbolTable);
/* 101 */     this.fValidationState.setLocale(locale);
/*     */   }
/*     */ 
/*     */   XSAnnotationImpl traverseAnnotationDecl(Element annotationDecl, Object[] parentAttrs, boolean isGlobal, XSDocumentInfo schemaDoc)
/*     */   {
/* 111 */     Object[] attrValues = this.fAttrChecker.checkAttributes(annotationDecl, isGlobal, schemaDoc);
/* 112 */     this.fAttrChecker.returnAttrArray(attrValues, schemaDoc);
/*     */ 
/* 114 */     String contents = DOMUtil.getAnnotation(annotationDecl);
/* 115 */     Element child = DOMUtil.getFirstChildElement(annotationDecl);
/* 116 */     if (child != null) {
/*     */       do {
/* 118 */         String name = DOMUtil.getLocalName(child);
/*     */ 
/* 122 */         if ((!name.equals(SchemaSymbols.ELT_APPINFO)) && (!name.equals(SchemaSymbols.ELT_DOCUMENTATION)))
/*     */         {
/* 124 */           reportSchemaError("src-annotation", new Object[] { name }, child);
/*     */         }
/*     */         else
/*     */         {
/* 130 */           attrValues = this.fAttrChecker.checkAttributes(child, true, schemaDoc);
/* 131 */           this.fAttrChecker.returnAttrArray(attrValues, schemaDoc);
/*     */         }
/*     */ 
/* 134 */         child = DOMUtil.getNextSiblingElement(child);
/*     */       }
/* 136 */       while (child != null);
/*     */     }
/*     */ 
/* 140 */     if (contents == null) return null;
/*     */ 
/* 143 */     SchemaGrammar grammar = this.fSchemaHandler.getGrammar(schemaDoc.fTargetNamespace);
/*     */ 
/* 145 */     Vector annotationLocalAttrs = (Vector)parentAttrs[XSAttributeChecker.ATTIDX_NONSCHEMA];
/*     */ 
/* 147 */     if ((annotationLocalAttrs != null) && (!annotationLocalAttrs.isEmpty())) {
/* 148 */       StringBuffer localStrBuffer = new StringBuffer(64);
/* 149 */       localStrBuffer.append(" ");
/*     */ 
/* 151 */       int i = 0;
/* 152 */       while (i < annotationLocalAttrs.size()) {
/* 153 */         String rawname = (String)annotationLocalAttrs.elementAt(i++);
/* 154 */         int colonIndex = rawname.indexOf(':');
/*     */         String localpart;
/*     */         String prefix;
/*     */         String localpart;
/* 156 */         if (colonIndex == -1) {
/* 157 */           String prefix = "";
/* 158 */           localpart = rawname;
/*     */         }
/*     */         else {
/* 161 */           prefix = rawname.substring(0, colonIndex);
/* 162 */           localpart = rawname.substring(colonIndex + 1);
/*     */         }
/* 164 */         String uri = schemaDoc.fNamespaceSupport.getURI(this.fSymbolTable.addSymbol(prefix));
/* 165 */         if (annotationDecl.getAttributeNS(uri, localpart).length() != 0) {
/* 166 */           i++;
/*     */         }
/*     */         else {
/* 169 */           localStrBuffer.append(rawname).append("=\"");
/*     */ 
/* 171 */           String value = (String)annotationLocalAttrs.elementAt(i++);
/*     */ 
/* 173 */           value = processAttValue(value);
/* 174 */           localStrBuffer.append(value).append("\" ");
/*     */         }
/*     */       }
/*     */ 
/* 178 */       StringBuffer contentBuffer = new StringBuffer(contents.length() + localStrBuffer.length());
/* 179 */       int annotationTokenEnd = contents.indexOf(SchemaSymbols.ELT_ANNOTATION);
/*     */ 
/* 181 */       if (annotationTokenEnd == -1) return null;
/* 182 */       annotationTokenEnd += SchemaSymbols.ELT_ANNOTATION.length();
/* 183 */       contentBuffer.append(contents.substring(0, annotationTokenEnd));
/* 184 */       contentBuffer.append(localStrBuffer.toString());
/* 185 */       contentBuffer.append(contents.substring(annotationTokenEnd, contents.length()));
/* 186 */       String annotation = contentBuffer.toString();
/* 187 */       if (this.fValidateAnnotations) {
/* 188 */         schemaDoc.addAnnotation(new XSAnnotationInfo(annotation, annotationDecl));
/*     */       }
/* 190 */       return new XSAnnotationImpl(annotation, grammar);
/*     */     }
/* 192 */     if (this.fValidateAnnotations) {
/* 193 */       schemaDoc.addAnnotation(new XSAnnotationInfo(contents, annotationDecl));
/*     */     }
/* 195 */     return new XSAnnotationImpl(contents, grammar);
/*     */   }
/*     */ 
/*     */   XSAnnotationImpl traverseSyntheticAnnotation(Element annotationParent, String initialContent, Object[] parentAttrs, boolean isGlobal, XSDocumentInfo schemaDoc)
/*     */   {
/* 203 */     String contents = initialContent;
/*     */ 
/* 206 */     SchemaGrammar grammar = this.fSchemaHandler.getGrammar(schemaDoc.fTargetNamespace);
/*     */ 
/* 208 */     Vector annotationLocalAttrs = (Vector)parentAttrs[XSAttributeChecker.ATTIDX_NONSCHEMA];
/*     */ 
/* 210 */     if ((annotationLocalAttrs != null) && (!annotationLocalAttrs.isEmpty())) {
/* 211 */       StringBuffer localStrBuffer = new StringBuffer(64);
/* 212 */       localStrBuffer.append(" ");
/*     */ 
/* 214 */       int i = 0;
/* 215 */       while (i < annotationLocalAttrs.size()) {
/* 216 */         String rawname = (String)annotationLocalAttrs.elementAt(i++);
/* 217 */         int colonIndex = rawname.indexOf(':');
/*     */         String localpart;
/*     */         String prefix;
/*     */         String localpart;
/* 219 */         if (colonIndex == -1) {
/* 220 */           String prefix = "";
/* 221 */           localpart = rawname;
/*     */         }
/*     */         else {
/* 224 */           prefix = rawname.substring(0, colonIndex);
/* 225 */           localpart = rawname.substring(colonIndex + 1);
/*     */         }
/* 227 */         String uri = schemaDoc.fNamespaceSupport.getURI(this.fSymbolTable.addSymbol(prefix));
/* 228 */         localStrBuffer.append(rawname).append("=\"");
/*     */ 
/* 230 */         String value = (String)annotationLocalAttrs.elementAt(i++);
/*     */ 
/* 232 */         value = processAttValue(value);
/* 233 */         localStrBuffer.append(value).append("\" ");
/*     */       }
/*     */ 
/* 237 */       StringBuffer contentBuffer = new StringBuffer(contents.length() + localStrBuffer.length());
/* 238 */       int annotationTokenEnd = contents.indexOf(SchemaSymbols.ELT_ANNOTATION);
/*     */ 
/* 240 */       if (annotationTokenEnd == -1) return null;
/* 241 */       annotationTokenEnd += SchemaSymbols.ELT_ANNOTATION.length();
/* 242 */       contentBuffer.append(contents.substring(0, annotationTokenEnd));
/* 243 */       contentBuffer.append(localStrBuffer.toString());
/* 244 */       contentBuffer.append(contents.substring(annotationTokenEnd, contents.length()));
/* 245 */       String annotation = contentBuffer.toString();
/* 246 */       if (this.fValidateAnnotations) {
/* 247 */         schemaDoc.addAnnotation(new XSAnnotationInfo(annotation, annotationParent));
/*     */       }
/* 249 */       return new XSAnnotationImpl(annotation, grammar);
/*     */     }
/* 251 */     if (this.fValidateAnnotations) {
/* 252 */       schemaDoc.addAnnotation(new XSAnnotationInfo(contents, annotationParent));
/*     */     }
/* 254 */     return new XSAnnotationImpl(contents, grammar);
/*     */   }
/*     */ 
/*     */   FacetInfo traverseFacets(Element content, XSSimpleType baseValidator, XSDocumentInfo schemaDoc)
/*     */   {
/* 283 */     short facetsPresent = 0;
/* 284 */     short facetsFixed = 0;
/*     */ 
/* 286 */     boolean hasQName = containsQName(baseValidator);
/* 287 */     Vector enumData = null;
/* 288 */     XSObjectListImpl enumAnnotations = null;
/* 289 */     XSObjectListImpl patternAnnotations = null;
/* 290 */     Vector enumNSDecls = hasQName ? new Vector() : null;
/* 291 */     int currentFacet = 0;
/* 292 */     this.xsFacets.reset();
/* 293 */     while (content != null)
/*     */     {
/* 295 */       Object[] attrs = null;
/* 296 */       String facet = DOMUtil.getLocalName(content);
/* 297 */       if (facet.equals(SchemaSymbols.ELT_ENUMERATION)) {
/* 298 */         attrs = this.fAttrChecker.checkAttributes(content, false, schemaDoc, hasQName);
/* 299 */         String enumVal = (String)attrs[XSAttributeChecker.ATTIDX_VALUE];
/*     */ 
/* 302 */         if (enumVal == null) {
/* 303 */           reportSchemaError("s4s-att-must-appear", new Object[] { SchemaSymbols.ELT_ENUMERATION, SchemaSymbols.ATT_VALUE }, content);
/* 304 */           this.fAttrChecker.returnAttrArray(attrs, schemaDoc);
/* 305 */           content = DOMUtil.getNextSiblingElement(content);
/*     */         }
/*     */         else
/*     */         {
/* 309 */           NamespaceSupport nsDecls = (NamespaceSupport)attrs[XSAttributeChecker.ATTIDX_ENUMNSDECLS];
/*     */ 
/* 313 */           if ((baseValidator.getVariety() == 1) && (baseValidator.getPrimitiveKind() == 20))
/*     */           {
/* 316 */             schemaDoc.fValidationContext.setNamespaceSupport(nsDecls);
/* 317 */             Object notation = null;
/*     */             try {
/* 319 */               QName temp = (QName)fQNameDV.validate(enumVal, schemaDoc.fValidationContext, null);
/*     */ 
/* 322 */               notation = this.fSchemaHandler.getGlobalDecl(schemaDoc, 6, temp, content);
/*     */             } catch (InvalidDatatypeValueException ex) {
/* 324 */               reportSchemaError(ex.getKey(), ex.getArgs(), content);
/*     */             }
/* 326 */             if (notation == null)
/*     */             {
/* 330 */               this.fAttrChecker.returnAttrArray(attrs, schemaDoc);
/* 331 */               content = DOMUtil.getNextSiblingElement(content);
/*     */             }
/*     */             else
/*     */             {
/* 335 */               schemaDoc.fValidationContext.setNamespaceSupport(schemaDoc.fNamespaceSupport);
/*     */             }
/*     */           } else { if (enumData == null) {
/* 338 */               enumData = new Vector();
/* 339 */               enumAnnotations = new XSObjectListImpl();
/*     */             }
/* 341 */             enumData.addElement(enumVal);
/* 342 */             enumAnnotations.addXSObject(null);
/* 343 */             if (hasQName)
/* 344 */               enumNSDecls.addElement(nsDecls);
/* 345 */             Element child = DOMUtil.getFirstChildElement(content);
/*     */ 
/* 347 */             if ((child != null) && (DOMUtil.getLocalName(child).equals(SchemaSymbols.ELT_ANNOTATION)))
/*     */             {
/* 350 */               enumAnnotations.addXSObject(enumAnnotations.getLength() - 1, traverseAnnotationDecl(child, attrs, false, schemaDoc));
/* 351 */               child = DOMUtil.getNextSiblingElement(child);
/*     */             }
/*     */             else {
/* 354 */               String text = DOMUtil.getSyntheticAnnotation(content);
/* 355 */               if (text != null) {
/* 356 */                 enumAnnotations.addXSObject(enumAnnotations.getLength() - 1, traverseSyntheticAnnotation(content, text, attrs, false, schemaDoc));
/*     */               }
/*     */             }
/* 359 */             if (child != null)
/* 360 */               reportSchemaError("s4s-elt-must-match.1", new Object[] { "enumeration", "(annotation?)", DOMUtil.getLocalName(child) }, child); }
/*     */         }
/*     */       }
/* 363 */       else if (facet.equals(SchemaSymbols.ELT_PATTERN)) {
/* 364 */         facetsPresent = (short)(facetsPresent | 0x8);
/* 365 */         attrs = this.fAttrChecker.checkAttributes(content, false, schemaDoc);
/* 366 */         String patternVal = (String)attrs[XSAttributeChecker.ATTIDX_VALUE];
/*     */ 
/* 369 */         if (patternVal == null) {
/* 370 */           reportSchemaError("s4s-att-must-appear", new Object[] { SchemaSymbols.ELT_PATTERN, SchemaSymbols.ATT_VALUE }, content);
/* 371 */           this.fAttrChecker.returnAttrArray(attrs, schemaDoc);
/* 372 */           content = DOMUtil.getNextSiblingElement(content);
/*     */         }
/*     */         else
/*     */         {
/* 376 */           if (this.fPattern.length() == 0) {
/* 377 */             this.fPattern.append(patternVal);
/*     */           }
/*     */           else
/*     */           {
/* 382 */             this.fPattern.append("|");
/* 383 */             this.fPattern.append(patternVal);
/*     */           }
/* 385 */           Element child = DOMUtil.getFirstChildElement(content);
/* 386 */           if ((child != null) && (DOMUtil.getLocalName(child).equals(SchemaSymbols.ELT_ANNOTATION)))
/*     */           {
/* 389 */             if (patternAnnotations == null) {
/* 390 */               patternAnnotations = new XSObjectListImpl();
/*     */             }
/* 392 */             patternAnnotations.addXSObject(traverseAnnotationDecl(child, attrs, false, schemaDoc));
/* 393 */             child = DOMUtil.getNextSiblingElement(child);
/*     */           }
/*     */           else {
/* 396 */             String text = DOMUtil.getSyntheticAnnotation(content);
/* 397 */             if (text != null) {
/* 398 */               if (patternAnnotations == null) {
/* 399 */                 patternAnnotations = new XSObjectListImpl();
/*     */               }
/* 401 */               patternAnnotations.addXSObject(traverseSyntheticAnnotation(content, text, attrs, false, schemaDoc));
/*     */             }
/*     */           }
/* 404 */           if (child != null)
/* 405 */             reportSchemaError("s4s-elt-must-match.1", new Object[] { "pattern", "(annotation?)", DOMUtil.getLocalName(child) }, child);
/*     */         }
/*     */       }
/*     */       else {
/* 409 */         if (facet.equals(SchemaSymbols.ELT_MINLENGTH)) {
/* 410 */           currentFacet = 2;
/*     */         }
/* 412 */         else if (facet.equals(SchemaSymbols.ELT_MAXLENGTH)) {
/* 413 */           currentFacet = 4;
/*     */         }
/* 415 */         else if (facet.equals(SchemaSymbols.ELT_MAXEXCLUSIVE)) {
/* 416 */           currentFacet = 64;
/*     */         }
/* 418 */         else if (facet.equals(SchemaSymbols.ELT_MAXINCLUSIVE)) {
/* 419 */           currentFacet = 32;
/*     */         }
/* 421 */         else if (facet.equals(SchemaSymbols.ELT_MINEXCLUSIVE)) {
/* 422 */           currentFacet = 128;
/*     */         }
/* 424 */         else if (facet.equals(SchemaSymbols.ELT_MININCLUSIVE)) {
/* 425 */           currentFacet = 256;
/*     */         }
/* 427 */         else if (facet.equals(SchemaSymbols.ELT_TOTALDIGITS)) {
/* 428 */           currentFacet = 512;
/*     */         }
/* 430 */         else if (facet.equals(SchemaSymbols.ELT_FRACTIONDIGITS)) {
/* 431 */           currentFacet = 1024;
/*     */         }
/* 433 */         else if (facet.equals(SchemaSymbols.ELT_WHITESPACE)) {
/* 434 */           currentFacet = 16;
/*     */         } else {
/* 436 */           if (!facet.equals(SchemaSymbols.ELT_LENGTH)) break;
/* 437 */           currentFacet = 1;
/*     */         }
/*     */ 
/* 443 */         attrs = this.fAttrChecker.checkAttributes(content, false, schemaDoc);
/*     */ 
/* 446 */         if ((facetsPresent & currentFacet) != 0)
/*     */         {
/* 448 */           reportSchemaError("src-single-facet-value", new Object[] { facet }, content);
/* 449 */           this.fAttrChecker.returnAttrArray(attrs, schemaDoc);
/* 450 */           content = DOMUtil.getNextSiblingElement(content);
/*     */         }
/* 456 */         else if (attrs[XSAttributeChecker.ATTIDX_VALUE] == null)
/*     */         {
/* 461 */           if (content.getAttributeNodeNS(null, "value") == null) {
/* 462 */             reportSchemaError("s4s-att-must-appear", new Object[] { content.getLocalName(), SchemaSymbols.ATT_VALUE }, content);
/*     */           }
/* 464 */           this.fAttrChecker.returnAttrArray(attrs, schemaDoc);
/* 465 */           content = DOMUtil.getNextSiblingElement(content);
/*     */         }
/*     */         else
/*     */         {
/* 469 */           facetsPresent = (short)(facetsPresent | currentFacet);
/*     */ 
/* 471 */           if (((Boolean)attrs[XSAttributeChecker.ATTIDX_FIXED]).booleanValue()) {
/* 472 */             facetsFixed = (short)(facetsFixed | currentFacet);
/*     */           }
/* 474 */           switch (currentFacet) {
/*     */           case 2:
/* 476 */             this.xsFacets.minLength = ((XInt)attrs[XSAttributeChecker.ATTIDX_VALUE]).intValue();
/* 477 */             break;
/*     */           case 4:
/* 479 */             this.xsFacets.maxLength = ((XInt)attrs[XSAttributeChecker.ATTIDX_VALUE]).intValue();
/* 480 */             break;
/*     */           case 64:
/* 482 */             this.xsFacets.maxExclusive = ((String)attrs[XSAttributeChecker.ATTIDX_VALUE]);
/* 483 */             break;
/*     */           case 32:
/* 485 */             this.xsFacets.maxInclusive = ((String)attrs[XSAttributeChecker.ATTIDX_VALUE]);
/* 486 */             break;
/*     */           case 128:
/* 488 */             this.xsFacets.minExclusive = ((String)attrs[XSAttributeChecker.ATTIDX_VALUE]);
/* 489 */             break;
/*     */           case 256:
/* 491 */             this.xsFacets.minInclusive = ((String)attrs[XSAttributeChecker.ATTIDX_VALUE]);
/* 492 */             break;
/*     */           case 512:
/* 494 */             this.xsFacets.totalDigits = ((XInt)attrs[XSAttributeChecker.ATTIDX_VALUE]).intValue();
/* 495 */             break;
/*     */           case 1024:
/* 497 */             this.xsFacets.fractionDigits = ((XInt)attrs[XSAttributeChecker.ATTIDX_VALUE]).intValue();
/* 498 */             break;
/*     */           case 16:
/* 500 */             this.xsFacets.whiteSpace = ((XInt)attrs[XSAttributeChecker.ATTIDX_VALUE]).shortValue();
/* 501 */             break;
/*     */           case 1:
/* 503 */             this.xsFacets.length = ((XInt)attrs[XSAttributeChecker.ATTIDX_VALUE]).intValue();
/*     */           }
/*     */ 
/* 507 */           Element child = DOMUtil.getFirstChildElement(content);
/* 508 */           XSAnnotationImpl annotation = null;
/* 509 */           if ((child != null) && (DOMUtil.getLocalName(child).equals(SchemaSymbols.ELT_ANNOTATION)))
/*     */           {
/* 512 */             annotation = traverseAnnotationDecl(child, attrs, false, schemaDoc);
/* 513 */             child = DOMUtil.getNextSiblingElement(child);
/*     */           }
/*     */           else {
/* 516 */             String text = DOMUtil.getSyntheticAnnotation(content);
/* 517 */             if (text != null) {
/* 518 */               annotation = traverseSyntheticAnnotation(content, text, attrs, false, schemaDoc);
/*     */             }
/*     */           }
/* 521 */           switch (currentFacet) {
/*     */           case 2:
/* 523 */             this.xsFacets.minLengthAnnotation = annotation;
/* 524 */             break;
/*     */           case 4:
/* 526 */             this.xsFacets.maxLengthAnnotation = annotation;
/* 527 */             break;
/*     */           case 64:
/* 529 */             this.xsFacets.maxExclusiveAnnotation = annotation;
/* 530 */             break;
/*     */           case 32:
/* 532 */             this.xsFacets.maxInclusiveAnnotation = annotation;
/* 533 */             break;
/*     */           case 128:
/* 535 */             this.xsFacets.minExclusiveAnnotation = annotation;
/* 536 */             break;
/*     */           case 256:
/* 538 */             this.xsFacets.minInclusiveAnnotation = annotation;
/* 539 */             break;
/*     */           case 512:
/* 541 */             this.xsFacets.totalDigitsAnnotation = annotation;
/* 542 */             break;
/*     */           case 1024:
/* 544 */             this.xsFacets.fractionDigitsAnnotation = annotation;
/* 545 */             break;
/*     */           case 16:
/* 547 */             this.xsFacets.whiteSpaceAnnotation = annotation;
/* 548 */             break;
/*     */           case 1:
/* 550 */             this.xsFacets.lengthAnnotation = annotation;
/*     */           }
/*     */ 
/* 553 */           if (child != null) {
/* 554 */             reportSchemaError("s4s-elt-must-match.1", new Object[] { facet, "(annotation?)", DOMUtil.getLocalName(child) }, child);
/*     */           }
/*     */ 
/* 557 */           this.fAttrChecker.returnAttrArray(attrs, schemaDoc);
/* 558 */           content = DOMUtil.getNextSiblingElement(content);
/*     */         }
/*     */       }
/*     */     }
/* 560 */     if (enumData != null) {
/* 561 */       facetsPresent = (short)(facetsPresent | 0x800);
/* 562 */       this.xsFacets.enumeration = enumData;
/* 563 */       this.xsFacets.enumNSDecls = enumNSDecls;
/* 564 */       this.xsFacets.enumAnnotations = enumAnnotations;
/*     */     }
/* 566 */     if ((facetsPresent & 0x8) != 0) {
/* 567 */       this.xsFacets.pattern = this.fPattern.toString();
/* 568 */       this.xsFacets.patternAnnotations = patternAnnotations;
/*     */     }
/*     */ 
/* 571 */     this.fPattern.setLength(0);
/*     */ 
/* 573 */     return new FacetInfo(this.xsFacets, content, facetsPresent, facetsFixed);
/*     */   }
/*     */ 
/*     */   private boolean containsQName(XSSimpleType type)
/*     */   {
/* 579 */     if (type.getVariety() == 1) {
/* 580 */       short primitive = type.getPrimitiveKind();
/* 581 */       return (primitive == 18) || (primitive == 20);
/*     */     }
/*     */ 
/* 584 */     if (type.getVariety() == 2) {
/* 585 */       return containsQName((XSSimpleType)type.getItemType());
/*     */     }
/* 587 */     if (type.getVariety() == 3) {
/* 588 */       XSObjectList members = type.getMemberTypes();
/* 589 */       for (int i = 0; i < members.getLength(); i++) {
/* 590 */         if (containsQName((XSSimpleType)members.item(i)))
/* 591 */           return true;
/*     */       }
/*     */     }
/* 594 */     return false;
/*     */   }
/*     */ 
/*     */   Element traverseAttrsAndAttrGrps(Element firstAttr, XSAttributeGroupDecl attrGrp, XSDocumentInfo schemaDoc, SchemaGrammar grammar, XSComplexTypeDecl enclosingCT)
/*     */   {
/* 606 */     Element child = null;
/* 607 */     XSAttributeGroupDecl tempAttrGrp = null;
/* 608 */     XSAttributeUseImpl tempAttrUse = null;
/* 609 */     XSAttributeUse otherUse = null;
/*     */ 
/* 612 */     for (child = firstAttr; child != null; child = DOMUtil.getNextSiblingElement(child)) {
/* 613 */       String childName = DOMUtil.getLocalName(child);
/* 614 */       if (childName.equals(SchemaSymbols.ELT_ATTRIBUTE)) {
/* 615 */         tempAttrUse = this.fSchemaHandler.fAttributeTraverser.traverseLocal(child, schemaDoc, grammar, enclosingCT);
/*     */ 
/* 619 */         if (tempAttrUse != null)
/* 620 */           if (tempAttrUse.fUse == 2) {
/* 621 */             attrGrp.addAttributeUse(tempAttrUse);
/*     */           }
/*     */           else {
/* 624 */             otherUse = attrGrp.getAttributeUseNoProhibited(tempAttrUse.fAttrDecl.getNamespace(), tempAttrUse.fAttrDecl.getName());
/*     */ 
/* 627 */             if (otherUse == null) {
/* 628 */               String idName = attrGrp.addAttributeUse(tempAttrUse);
/* 629 */               if (idName != null) {
/* 630 */                 String code = enclosingCT == null ? "ag-props-correct.3" : "ct-props-correct.5";
/* 631 */                 String name = enclosingCT == null ? attrGrp.fName : enclosingCT.getName();
/* 632 */                 reportSchemaError(code, new Object[] { name, tempAttrUse.fAttrDecl.getName(), idName }, child);
/*     */               }
/*     */             }
/* 635 */             else if (otherUse != tempAttrUse) {
/* 636 */               String code = enclosingCT == null ? "ag-props-correct.2" : "ct-props-correct.4";
/* 637 */               String name = enclosingCT == null ? attrGrp.fName : enclosingCT.getName();
/* 638 */               reportSchemaError(code, new Object[] { name, tempAttrUse.fAttrDecl.getName() }, child);
/*     */             }
/*     */           }
/*     */       } else { if (!childName.equals(SchemaSymbols.ELT_ATTRIBUTEGROUP))
/*     */           break;
/* 643 */         tempAttrGrp = this.fSchemaHandler.fAttributeGroupTraverser.traverseLocal(child, schemaDoc, grammar);
/*     */ 
/* 645 */         if (tempAttrGrp != null) {
/* 646 */           XSObjectList attrUseS = tempAttrGrp.getAttributeUses();
/*     */ 
/* 648 */           int attrCount = attrUseS.getLength();
/* 649 */           for (int i = 0; i < attrCount; i++) {
/* 650 */             XSAttributeUseImpl oneAttrUse = (XSAttributeUseImpl)attrUseS.item(i);
/* 651 */             if (oneAttrUse.fUse == 2) {
/* 652 */               attrGrp.addAttributeUse(oneAttrUse);
/*     */             }
/*     */             else {
/* 655 */               otherUse = attrGrp.getAttributeUseNoProhibited(oneAttrUse.fAttrDecl.getNamespace(), oneAttrUse.fAttrDecl.getName());
/*     */ 
/* 658 */               if (otherUse == null) {
/* 659 */                 String idName = attrGrp.addAttributeUse(oneAttrUse);
/* 660 */                 if (idName != null) {
/* 661 */                   String code = enclosingCT == null ? "ag-props-correct.3" : "ct-props-correct.5";
/* 662 */                   String name = enclosingCT == null ? attrGrp.fName : enclosingCT.getName();
/* 663 */                   reportSchemaError(code, new Object[] { name, oneAttrUse.fAttrDecl.getName(), idName }, child);
/*     */                 }
/*     */               }
/* 666 */               else if (oneAttrUse != otherUse) {
/* 667 */                 String code = enclosingCT == null ? "ag-props-correct.2" : "ct-props-correct.4";
/* 668 */                 String name = enclosingCT == null ? attrGrp.fName : enclosingCT.getName();
/* 669 */                 reportSchemaError(code, new Object[] { name, oneAttrUse.fAttrDecl.getName() }, child);
/*     */               }
/*     */             }
/*     */           }
/* 673 */           if (tempAttrGrp.fAttributeWC != null) {
/* 674 */             if (attrGrp.fAttributeWC == null) {
/* 675 */               attrGrp.fAttributeWC = tempAttrGrp.fAttributeWC;
/*     */             }
/*     */             else
/*     */             {
/* 679 */               attrGrp.fAttributeWC = attrGrp.fAttributeWC.performIntersectionWith(tempAttrGrp.fAttributeWC, attrGrp.fAttributeWC.fProcessContents);
/*     */ 
/* 681 */               if (attrGrp.fAttributeWC == null) {
/* 682 */                 String code = enclosingCT == null ? "src-attribute_group.2" : "src-ct.4";
/* 683 */                 String name = enclosingCT == null ? attrGrp.fName : enclosingCT.getName();
/* 684 */                 reportSchemaError(code, new Object[] { name }, child);
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 693 */     if (child != null) {
/* 694 */       String childName = DOMUtil.getLocalName(child);
/* 695 */       if (childName.equals(SchemaSymbols.ELT_ANYATTRIBUTE)) {
/* 696 */         XSWildcardDecl tempAttrWC = this.fSchemaHandler.fWildCardTraverser.traverseAnyAttribute(child, schemaDoc, grammar);
/*     */ 
/* 698 */         if (attrGrp.fAttributeWC == null) {
/* 699 */           attrGrp.fAttributeWC = tempAttrWC;
/*     */         }
/*     */         else
/*     */         {
/* 703 */           attrGrp.fAttributeWC = tempAttrWC.performIntersectionWith(attrGrp.fAttributeWC, tempAttrWC.fProcessContents);
/*     */ 
/* 705 */           if (attrGrp.fAttributeWC == null) {
/* 706 */             String code = enclosingCT == null ? "src-attribute_group.2" : "src-ct.4";
/* 707 */             String name = enclosingCT == null ? attrGrp.fName : enclosingCT.getName();
/* 708 */             reportSchemaError(code, new Object[] { name }, child);
/*     */           }
/*     */         }
/* 711 */         child = DOMUtil.getNextSiblingElement(child);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 716 */     return child;
/*     */   }
/*     */ 
/*     */   void reportSchemaError(String key, Object[] args, Element ele)
/*     */   {
/* 721 */     this.fSchemaHandler.reportSchemaError(key, args, ele);
/*     */   }
/*     */ 
/*     */   void checkNotationType(String refName, XSTypeDefinition typeDecl, Element elem)
/*     */   {
/* 729 */     if ((typeDecl.getTypeCategory() == 16) && (((XSSimpleType)typeDecl).getVariety() == 1) && (((XSSimpleType)typeDecl).getPrimitiveKind() == 20))
/*     */     {
/* 732 */       if ((((XSSimpleType)typeDecl).getDefinedFacets() & 0x800) == 0)
/* 733 */         reportSchemaError("enumeration-required-notation", new Object[] { typeDecl.getName(), refName, DOMUtil.getLocalName(elem) }, elem);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected XSParticleDecl checkOccurrences(XSParticleDecl particle, String particleName, Element parent, int allContextFlags, long defaultVals)
/*     */   {
/* 744 */     int min = particle.fMinOccurs;
/* 745 */     int max = particle.fMaxOccurs;
/* 746 */     boolean defaultMin = (defaultVals & 1 << XSAttributeChecker.ATTIDX_MINOCCURS) != 0L;
/* 747 */     boolean defaultMax = (defaultVals & 1 << XSAttributeChecker.ATTIDX_MAXOCCURS) != 0L;
/*     */ 
/* 749 */     boolean processingAllEl = (allContextFlags & 0x1) != 0;
/* 750 */     boolean processingAllGP = (allContextFlags & 0x8) != 0;
/* 751 */     boolean groupRefWithAll = (allContextFlags & 0x2) != 0;
/* 752 */     boolean isGroupChild = (allContextFlags & 0x4) != 0;
/*     */ 
/* 756 */     if (isGroupChild) {
/* 757 */       if (!defaultMin) {
/* 758 */         Object[] args = { particleName, "minOccurs" };
/* 759 */         reportSchemaError("s4s-att-not-allowed", args, parent);
/* 760 */         min = 1;
/*     */       }
/* 762 */       if (!defaultMax) {
/* 763 */         Object[] args = { particleName, "maxOccurs" };
/* 764 */         reportSchemaError("s4s-att-not-allowed", args, parent);
/* 765 */         max = 1;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 770 */     if ((min == 0) && (max == 0)) {
/* 771 */       particle.fType = 0;
/* 772 */       return null;
/*     */     }
/*     */ 
/* 780 */     if (processingAllEl) {
/* 781 */       if (max != 1) {
/* 782 */         reportSchemaError("cos-all-limited.2", new Object[] { max == -1 ? "unbounded" : Integer.toString(max), ((XSElementDecl)particle.fValue).getName() }, parent);
/*     */ 
/* 785 */         max = 1;
/* 786 */         if (min > 1)
/* 787 */           min = 1;
/*     */       }
/*     */     }
/* 790 */     else if (((processingAllGP) || (groupRefWithAll)) && 
/* 791 */       (max != 1)) {
/* 792 */       reportSchemaError("cos-all-limited.1.2", null, parent);
/* 793 */       if (min > 1)
/* 794 */         min = 1;
/* 795 */       max = 1;
/*     */     }
/*     */ 
/* 799 */     particle.fMinOccurs = min;
/* 800 */     particle.fMaxOccurs = max;
/*     */ 
/* 802 */     return particle;
/*     */   }
/*     */ 
/*     */   private static String processAttValue(String original) {
/* 806 */     int length = original.length();
/*     */ 
/* 808 */     for (int i = 0; i < length; i++) {
/* 809 */       char currChar = original.charAt(i);
/* 810 */       if ((currChar == '"') || (currChar == '<') || (currChar == '&') || (currChar == '\t') || (currChar == '\n') || (currChar == '\r'))
/*     */       {
/* 812 */         return escapeAttValue(original, i);
/*     */       }
/*     */     }
/* 815 */     return original;
/*     */   }
/*     */ 
/*     */   private static String escapeAttValue(String original, int from)
/*     */   {
/* 821 */     int length = original.length();
/* 822 */     StringBuffer newVal = new StringBuffer(length);
/* 823 */     newVal.append(original.substring(0, from));
/* 824 */     for (int i = from; i < length; i++) {
/* 825 */       char currChar = original.charAt(i);
/* 826 */       if (currChar == '"') {
/* 827 */         newVal.append("&quot;");
/*     */       }
/* 829 */       else if (currChar == '<') {
/* 830 */         newVal.append("&lt;");
/*     */       }
/* 832 */       else if (currChar == '&') {
/* 833 */         newVal.append("&amp;");
/*     */       }
/* 838 */       else if (currChar == '\t') {
/* 839 */         newVal.append("&#x9;");
/*     */       }
/* 841 */       else if (currChar == '\n') {
/* 842 */         newVal.append("&#xA;");
/*     */       }
/* 844 */       else if (currChar == '\r') {
/* 845 */         newVal.append("&#xD;");
/*     */       }
/*     */       else {
/* 848 */         newVal.append(currChar);
/*     */       }
/*     */     }
/* 851 */     return newVal.toString();
/*     */   }
/*     */ 
/*     */   static final class FacetInfo
/*     */   {
/*     */     final XSFacets facetdata;
/*     */     final Element nodeAfterFacets;
/*     */     final short fPresentFacets;
/*     */     final short fFixedFacets;
/*     */ 
/*     */     FacetInfo(XSFacets facets, Element nodeAfterFacets, short presentFacets, short fixedFacets)
/*     */     {
/* 272 */       this.facetdata = facets;
/* 273 */       this.nodeAfterFacets = nodeAfterFacets;
/* 274 */       this.fPresentFacets = presentFacets;
/* 275 */       this.fFixedFacets = fixedFacets;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.traversers.XSDAbstractTraverser
 * JD-Core Version:    0.6.2
 */