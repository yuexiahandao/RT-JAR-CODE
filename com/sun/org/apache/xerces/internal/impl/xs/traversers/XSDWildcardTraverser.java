/*     */ package com.sun.org.apache.xerces.internal.impl.xs.traversers;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XSAnnotationImpl;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XSDeclarationPool;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XSParticleDecl;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XSWildcardDecl;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.util.XInt;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.util.XSObjectListImpl;
/*     */ import com.sun.org.apache.xerces.internal.util.DOMUtil;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSObjectList;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ class XSDWildcardTraverser extends XSDAbstractTraverser
/*     */ {
/*     */   XSDWildcardTraverser(XSDHandler handler, XSAttributeChecker gAttrCheck)
/*     */   {
/*  73 */     super(handler, gAttrCheck);
/*     */   }
/*     */ 
/*     */   XSParticleDecl traverseAny(Element elmNode, XSDocumentInfo schemaDoc, SchemaGrammar grammar)
/*     */   {
/*  90 */     Object[] attrValues = this.fAttrChecker.checkAttributes(elmNode, false, schemaDoc);
/*  91 */     XSWildcardDecl wildcard = traverseWildcardDecl(elmNode, attrValues, schemaDoc, grammar);
/*     */ 
/*  94 */     XSParticleDecl particle = null;
/*  95 */     if (wildcard != null) {
/*  96 */       int min = ((XInt)attrValues[XSAttributeChecker.ATTIDX_MINOCCURS]).intValue();
/*  97 */       int max = ((XInt)attrValues[XSAttributeChecker.ATTIDX_MAXOCCURS]).intValue();
/*  98 */       if (max != 0) {
/*  99 */         if (this.fSchemaHandler.fDeclPool != null)
/* 100 */           particle = this.fSchemaHandler.fDeclPool.getParticleDecl();
/*     */         else {
/* 102 */           particle = new XSParticleDecl();
/*     */         }
/* 104 */         particle.fType = 2;
/* 105 */         particle.fValue = wildcard;
/* 106 */         particle.fMinOccurs = min;
/* 107 */         particle.fMaxOccurs = max;
/* 108 */         particle.fAnnotations = wildcard.fAnnotations;
/*     */       }
/*     */     }
/*     */ 
/* 112 */     this.fAttrChecker.returnAttrArray(attrValues, schemaDoc);
/*     */ 
/* 114 */     return particle;
/*     */   }
/*     */ 
/*     */   XSWildcardDecl traverseAnyAttribute(Element elmNode, XSDocumentInfo schemaDoc, SchemaGrammar grammar)
/*     */   {
/* 131 */     Object[] attrValues = this.fAttrChecker.checkAttributes(elmNode, false, schemaDoc);
/* 132 */     XSWildcardDecl wildcard = traverseWildcardDecl(elmNode, attrValues, schemaDoc, grammar);
/* 133 */     this.fAttrChecker.returnAttrArray(attrValues, schemaDoc);
/*     */ 
/* 135 */     return wildcard;
/*     */   }
/*     */ 
/*     */   XSWildcardDecl traverseWildcardDecl(Element elmNode, Object[] attrValues, XSDocumentInfo schemaDoc, SchemaGrammar grammar)
/*     */   {
/* 153 */     XSWildcardDecl wildcard = new XSWildcardDecl();
/*     */ 
/* 155 */     XInt namespaceTypeAttr = (XInt)attrValues[XSAttributeChecker.ATTIDX_NAMESPACE];
/* 156 */     wildcard.fType = namespaceTypeAttr.shortValue();
/*     */ 
/* 158 */     wildcard.fNamespaceList = ((String[])attrValues[XSAttributeChecker.ATTIDX_NAMESPACE_LIST]);
/*     */ 
/* 160 */     XInt processContentsAttr = (XInt)attrValues[XSAttributeChecker.ATTIDX_PROCESSCONTENTS];
/* 161 */     wildcard.fProcessContents = processContentsAttr.shortValue();
/*     */ 
/* 164 */     Element child = DOMUtil.getFirstChildElement(elmNode);
/* 165 */     XSAnnotationImpl annotation = null;
/* 166 */     if (child != null)
/*     */     {
/* 168 */       if (DOMUtil.getLocalName(child).equals(SchemaSymbols.ELT_ANNOTATION)) {
/* 169 */         annotation = traverseAnnotationDecl(child, attrValues, false, schemaDoc);
/* 170 */         child = DOMUtil.getNextSiblingElement(child);
/*     */       }
/*     */       else {
/* 173 */         String text = DOMUtil.getSyntheticAnnotation(elmNode);
/* 174 */         if (text != null) {
/* 175 */           annotation = traverseSyntheticAnnotation(elmNode, text, attrValues, false, schemaDoc);
/*     */         }
/*     */       }
/*     */ 
/* 179 */       if (child != null)
/* 180 */         reportSchemaError("s4s-elt-must-match.1", new Object[] { "wildcard", "(annotation?)", DOMUtil.getLocalName(child) }, elmNode);
/*     */     }
/*     */     else
/*     */     {
/* 184 */       String text = DOMUtil.getSyntheticAnnotation(elmNode);
/* 185 */       if (text != null)
/* 186 */         annotation = traverseSyntheticAnnotation(elmNode, text, attrValues, false, schemaDoc);
/*     */     }
/*     */     XSObjectList annotations;
/* 190 */     if (annotation != null) {
/* 191 */       XSObjectList annotations = new XSObjectListImpl();
/* 192 */       ((XSObjectListImpl)annotations).addXSObject(annotation);
/*     */     } else {
/* 194 */       annotations = XSObjectListImpl.EMPTY_LIST;
/*     */     }
/* 196 */     wildcard.fAnnotations = annotations;
/*     */ 
/* 198 */     return wildcard;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.traversers.XSDWildcardTraverser
 * JD-Core Version:    0.6.2
 */