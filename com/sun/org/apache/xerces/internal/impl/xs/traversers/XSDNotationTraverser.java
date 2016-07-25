/*     */ package com.sun.org.apache.xerces.internal.impl.xs.traversers;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XSAnnotationImpl;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XSNotationDecl;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.util.XSObjectListImpl;
/*     */ import com.sun.org.apache.xerces.internal.util.DOMUtil;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSObjectList;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ class XSDNotationTraverser extends XSDAbstractTraverser
/*     */ {
/*     */   XSDNotationTraverser(XSDHandler handler, XSAttributeChecker gAttrCheck)
/*     */   {
/*  54 */     super(handler, gAttrCheck);
/*     */   }
/*     */ 
/*     */   XSNotationDecl traverse(Element elmNode, XSDocumentInfo schemaDoc, SchemaGrammar grammar)
/*     */   {
/*  62 */     Object[] attrValues = this.fAttrChecker.checkAttributes(elmNode, true, schemaDoc);
/*     */ 
/*  64 */     String nameAttr = (String)attrValues[XSAttributeChecker.ATTIDX_NAME];
/*     */ 
/*  66 */     String publicAttr = (String)attrValues[XSAttributeChecker.ATTIDX_PUBLIC];
/*  67 */     String systemAttr = (String)attrValues[XSAttributeChecker.ATTIDX_SYSTEM];
/*  68 */     if (nameAttr == null) {
/*  69 */       reportSchemaError("s4s-att-must-appear", new Object[] { SchemaSymbols.ELT_NOTATION, SchemaSymbols.ATT_NAME }, elmNode);
/*  70 */       this.fAttrChecker.returnAttrArray(attrValues, schemaDoc);
/*  71 */       return null;
/*     */     }
/*     */ 
/*  74 */     if ((systemAttr == null) && (publicAttr == null)) {
/*  75 */       reportSchemaError("PublicSystemOnNotation", null, elmNode);
/*  76 */       publicAttr = "missing";
/*     */     }
/*     */ 
/*  79 */     XSNotationDecl notation = new XSNotationDecl();
/*  80 */     notation.fName = nameAttr;
/*  81 */     notation.fTargetNamespace = schemaDoc.fTargetNamespace;
/*  82 */     notation.fPublicId = publicAttr;
/*  83 */     notation.fSystemId = systemAttr;
/*     */ 
/*  86 */     Element content = DOMUtil.getFirstChildElement(elmNode);
/*  87 */     XSAnnotationImpl annotation = null;
/*     */ 
/*  89 */     if ((content != null) && (DOMUtil.getLocalName(content).equals(SchemaSymbols.ELT_ANNOTATION))) {
/*  90 */       annotation = traverseAnnotationDecl(content, attrValues, false, schemaDoc);
/*  91 */       content = DOMUtil.getNextSiblingElement(content);
/*     */     }
/*     */     else {
/*  94 */       String text = DOMUtil.getSyntheticAnnotation(elmNode);
/*  95 */       if (text != null)
/*  96 */         annotation = traverseSyntheticAnnotation(elmNode, text, attrValues, false, schemaDoc);
/*     */     }
/*     */     XSObjectList annotations;
/* 100 */     if (annotation != null) {
/* 101 */       XSObjectList annotations = new XSObjectListImpl();
/* 102 */       ((XSObjectListImpl)annotations).addXSObject(annotation);
/*     */     } else {
/* 104 */       annotations = XSObjectListImpl.EMPTY_LIST;
/*     */     }
/* 106 */     notation.fAnnotations = annotations;
/* 107 */     if (content != null) {
/* 108 */       Object[] args = { SchemaSymbols.ELT_NOTATION, "(annotation?)", DOMUtil.getLocalName(content) };
/* 109 */       reportSchemaError("s4s-elt-must-match.1", args, content);
/*     */     }
/*     */ 
/* 112 */     if (grammar.getGlobalNotationDecl(notation.fName) == null) {
/* 113 */       grammar.addGlobalNotationDecl(notation);
/*     */     }
/*     */ 
/* 117 */     String loc = this.fSchemaHandler.schemaDocument2SystemId(schemaDoc);
/* 118 */     XSNotationDecl notation2 = grammar.getGlobalNotationDecl(notation.fName, loc);
/* 119 */     if (notation2 == null) {
/* 120 */       grammar.addGlobalNotationDecl(notation, loc);
/*     */     }
/*     */ 
/* 124 */     if (this.fSchemaHandler.fTolerateDuplicates) {
/* 125 */       if (notation2 != null) {
/* 126 */         notation = notation2;
/*     */       }
/* 128 */       this.fSchemaHandler.addGlobalNotationDecl(notation);
/*     */     }
/* 130 */     this.fAttrChecker.returnAttrArray(attrValues, schemaDoc);
/*     */ 
/* 132 */     return notation;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.traversers.XSDNotationTraverser
 * JD-Core Version:    0.6.2
 */