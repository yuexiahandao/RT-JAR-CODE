/*     */ package com.sun.org.apache.xerces.internal.impl.xs.traversers;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XSAnnotationImpl;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XSConstraints;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XSDeclarationPool;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XSGroupDecl;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XSModelGroupImpl;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XSParticleDecl;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.util.XInt;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.util.XSObjectListImpl;
/*     */ import com.sun.org.apache.xerces.internal.util.DOMUtil;
/*     */ import com.sun.org.apache.xerces.internal.util.XMLSymbols;
/*     */ import com.sun.org.apache.xerces.internal.xni.QName;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSObjectList;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ class XSDGroupTraverser extends XSDAbstractParticleTraverser
/*     */ {
/*     */   XSDGroupTraverser(XSDHandler handler, XSAttributeChecker gAttrCheck)
/*     */   {
/*  57 */     super(handler, gAttrCheck);
/*     */   }
/*     */ 
/*     */   XSParticleDecl traverseLocal(Element elmNode, XSDocumentInfo schemaDoc, SchemaGrammar grammar)
/*     */   {
/*  65 */     Object[] attrValues = this.fAttrChecker.checkAttributes(elmNode, false, schemaDoc);
/*     */ 
/*  67 */     QName refAttr = (QName)attrValues[XSAttributeChecker.ATTIDX_REF];
/*  68 */     XInt minAttr = (XInt)attrValues[XSAttributeChecker.ATTIDX_MINOCCURS];
/*  69 */     XInt maxAttr = (XInt)attrValues[XSAttributeChecker.ATTIDX_MAXOCCURS];
/*     */ 
/*  71 */     XSGroupDecl group = null;
/*     */ 
/*  74 */     if (refAttr == null) {
/*  75 */       reportSchemaError("s4s-att-must-appear", new Object[] { "group (local)", "ref" }, elmNode);
/*     */     }
/*     */     else
/*     */     {
/*  79 */       group = (XSGroupDecl)this.fSchemaHandler.getGlobalDecl(schemaDoc, 4, refAttr, elmNode);
/*     */     }
/*     */ 
/*  82 */     XSAnnotationImpl annotation = null;
/*     */ 
/*  84 */     Element child = DOMUtil.getFirstChildElement(elmNode);
/*  85 */     if ((child != null) && (DOMUtil.getLocalName(child).equals(SchemaSymbols.ELT_ANNOTATION))) {
/*  86 */       annotation = traverseAnnotationDecl(child, attrValues, false, schemaDoc);
/*  87 */       child = DOMUtil.getNextSiblingElement(child);
/*     */     }
/*     */     else {
/*  90 */       String text = DOMUtil.getSyntheticAnnotation(elmNode);
/*  91 */       if (text != null) {
/*  92 */         annotation = traverseSyntheticAnnotation(elmNode, text, attrValues, false, schemaDoc);
/*     */       }
/*     */     }
/*     */ 
/*  96 */     if (child != null) {
/*  97 */       reportSchemaError("s4s-elt-must-match.1", new Object[] { "group (local)", "(annotation?)", DOMUtil.getLocalName(elmNode) }, elmNode);
/*     */     }
/*     */ 
/* 100 */     int minOccurs = minAttr.intValue();
/* 101 */     int maxOccurs = maxAttr.intValue();
/*     */ 
/* 103 */     XSParticleDecl particle = null;
/*     */ 
/* 106 */     if ((group != null) && (group.fModelGroup != null) && ((minOccurs != 0) || (maxOccurs != 0)))
/*     */     {
/* 109 */       if (this.fSchemaHandler.fDeclPool != null)
/* 110 */         particle = this.fSchemaHandler.fDeclPool.getParticleDecl();
/*     */       else {
/* 112 */         particle = new XSParticleDecl();
/*     */       }
/* 114 */       particle.fType = 3;
/* 115 */       particle.fValue = group.fModelGroup;
/* 116 */       particle.fMinOccurs = minOccurs;
/* 117 */       particle.fMaxOccurs = maxOccurs;
/* 118 */       if (group.fModelGroup.fCompositor == 103) {
/* 119 */         Long defaultVals = (Long)attrValues[XSAttributeChecker.ATTIDX_FROMDEFAULT];
/* 120 */         particle = checkOccurrences(particle, SchemaSymbols.ELT_GROUP, (Element)elmNode.getParentNode(), 2, defaultVals.longValue());
/*     */       }
/*     */ 
/* 124 */       if (refAttr != null)
/*     */       {
/*     */         XSObjectList annotations;
/* 126 */         if (annotation != null) {
/* 127 */           XSObjectList annotations = new XSObjectListImpl();
/* 128 */           ((XSObjectListImpl)annotations).addXSObject(annotation);
/*     */         } else {
/* 130 */           annotations = XSObjectListImpl.EMPTY_LIST;
/*     */         }
/* 132 */         particle.fAnnotations = annotations;
/*     */       } else {
/* 134 */         particle.fAnnotations = group.fAnnotations;
/*     */       }
/*     */     }
/*     */ 
/* 138 */     this.fAttrChecker.returnAttrArray(attrValues, schemaDoc);
/*     */ 
/* 140 */     return particle;
/*     */   }
/*     */ 
/*     */   XSGroupDecl traverseGlobal(Element elmNode, XSDocumentInfo schemaDoc, SchemaGrammar grammar)
/*     */   {
/* 149 */     Object[] attrValues = this.fAttrChecker.checkAttributes(elmNode, true, schemaDoc);
/*     */ 
/* 151 */     String strNameAttr = (String)attrValues[XSAttributeChecker.ATTIDX_NAME];
/*     */ 
/* 154 */     if (strNameAttr == null) {
/* 155 */       reportSchemaError("s4s-att-must-appear", new Object[] { "group (global)", "name" }, elmNode);
/*     */     }
/*     */ 
/* 160 */     XSGroupDecl group = new XSGroupDecl();
/* 161 */     XSParticleDecl particle = null;
/*     */ 
/* 164 */     Element l_elmChild = DOMUtil.getFirstChildElement(elmNode);
/* 165 */     XSAnnotationImpl annotation = null;
/* 166 */     if (l_elmChild == null) {
/* 167 */       reportSchemaError("s4s-elt-must-match.2", new Object[] { "group (global)", "(annotation?, (all | choice | sequence))" }, elmNode);
/*     */     }
/*     */     else
/*     */     {
/* 171 */       String childName = l_elmChild.getLocalName();
/* 172 */       if (childName.equals(SchemaSymbols.ELT_ANNOTATION)) {
/* 173 */         annotation = traverseAnnotationDecl(l_elmChild, attrValues, true, schemaDoc);
/* 174 */         l_elmChild = DOMUtil.getNextSiblingElement(l_elmChild);
/* 175 */         if (l_elmChild != null)
/* 176 */           childName = l_elmChild.getLocalName();
/*     */       }
/*     */       else {
/* 179 */         String text = DOMUtil.getSyntheticAnnotation(elmNode);
/* 180 */         if (text != null) {
/* 181 */           annotation = traverseSyntheticAnnotation(elmNode, text, attrValues, false, schemaDoc);
/*     */         }
/*     */       }
/*     */ 
/* 185 */       if (l_elmChild == null) {
/* 186 */         reportSchemaError("s4s-elt-must-match.2", new Object[] { "group (global)", "(annotation?, (all | choice | sequence))" }, elmNode);
/*     */       }
/* 189 */       else if (childName.equals(SchemaSymbols.ELT_ALL))
/* 190 */         particle = traverseAll(l_elmChild, schemaDoc, grammar, 4, group);
/* 191 */       else if (childName.equals(SchemaSymbols.ELT_CHOICE))
/* 192 */         particle = traverseChoice(l_elmChild, schemaDoc, grammar, 4, group);
/* 193 */       else if (childName.equals(SchemaSymbols.ELT_SEQUENCE))
/* 194 */         particle = traverseSequence(l_elmChild, schemaDoc, grammar, 4, group);
/*     */       else {
/* 196 */         reportSchemaError("s4s-elt-must-match.1", new Object[] { "group (global)", "(annotation?, (all | choice | sequence))", DOMUtil.getLocalName(l_elmChild) }, l_elmChild);
/*     */       }
/*     */ 
/* 201 */       if ((l_elmChild != null) && (DOMUtil.getNextSiblingElement(l_elmChild) != null))
/*     */       {
/* 203 */         reportSchemaError("s4s-elt-must-match.1", new Object[] { "group (global)", "(annotation?, (all | choice | sequence))", DOMUtil.getLocalName(DOMUtil.getNextSiblingElement(l_elmChild)) }, DOMUtil.getNextSiblingElement(l_elmChild));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 211 */     if (strNameAttr != null) {
/* 212 */       group.fName = strNameAttr;
/* 213 */       group.fTargetNamespace = schemaDoc.fTargetNamespace;
/* 214 */       if (particle == null) {
/* 215 */         particle = XSConstraints.getEmptySequence();
/*     */       }
/* 217 */       group.fModelGroup = ((XSModelGroupImpl)particle.fValue);
/*     */       XSObjectList annotations;
/* 219 */       if (annotation != null) {
/* 220 */         XSObjectList annotations = new XSObjectListImpl();
/* 221 */         ((XSObjectListImpl)annotations).addXSObject(annotation);
/*     */       } else {
/* 223 */         annotations = XSObjectListImpl.EMPTY_LIST;
/*     */       }
/* 225 */       group.fAnnotations = annotations;
/*     */ 
/* 227 */       if (grammar.getGlobalGroupDecl(group.fName) == null) {
/* 228 */         grammar.addGlobalGroupDecl(group);
/*     */       }
/*     */ 
/* 232 */       String loc = this.fSchemaHandler.schemaDocument2SystemId(schemaDoc);
/* 233 */       XSGroupDecl group2 = grammar.getGlobalGroupDecl(group.fName, loc);
/* 234 */       if (group2 == null) {
/* 235 */         grammar.addGlobalGroupDecl(group, loc);
/*     */       }
/*     */ 
/* 239 */       if (this.fSchemaHandler.fTolerateDuplicates) {
/* 240 */         if (group2 != null) {
/* 241 */           group = group2;
/*     */         }
/* 243 */         this.fSchemaHandler.addGlobalGroupDecl(group);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 248 */       group = null;
/*     */     }
/*     */ 
/* 251 */     if (group != null)
/*     */     {
/* 254 */       Object redefinedGrp = this.fSchemaHandler.getGrpOrAttrGrpRedefinedByRestriction(4, new QName(XMLSymbols.EMPTY_STRING, strNameAttr, strNameAttr, schemaDoc.fTargetNamespace), schemaDoc, elmNode);
/*     */ 
/* 257 */       if (redefinedGrp != null)
/*     */       {
/* 259 */         grammar.addRedefinedGroupDecl(group, (XSGroupDecl)redefinedGrp, this.fSchemaHandler.element2Locator(elmNode));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 264 */     this.fAttrChecker.returnAttrArray(attrValues, schemaDoc);
/*     */ 
/* 266 */     return group;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.traversers.XSDGroupTraverser
 * JD-Core Version:    0.6.2
 */