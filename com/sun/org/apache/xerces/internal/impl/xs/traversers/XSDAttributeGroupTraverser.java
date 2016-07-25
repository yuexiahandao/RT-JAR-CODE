/*     */ package com.sun.org.apache.xerces.internal.impl.xs.traversers;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XSAnnotationImpl;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XSAttributeGroupDecl;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.util.XSObjectListImpl;
/*     */ import com.sun.org.apache.xerces.internal.util.DOMUtil;
/*     */ import com.sun.org.apache.xerces.internal.util.XMLSymbols;
/*     */ import com.sun.org.apache.xerces.internal.xni.QName;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSObjectList;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ class XSDAttributeGroupTraverser extends XSDAbstractTraverser
/*     */ {
/*     */   XSDAttributeGroupTraverser(XSDHandler handler, XSAttributeChecker gAttrCheck)
/*     */   {
/*  56 */     super(handler, gAttrCheck);
/*     */   }
/*     */ 
/*     */   XSAttributeGroupDecl traverseLocal(Element elmNode, XSDocumentInfo schemaDoc, SchemaGrammar grammar)
/*     */   {
/*  65 */     Object[] attrValues = this.fAttrChecker.checkAttributes(elmNode, false, schemaDoc);
/*     */ 
/*  68 */     QName refAttr = (QName)attrValues[XSAttributeChecker.ATTIDX_REF];
/*     */ 
/*  70 */     XSAttributeGroupDecl attrGrp = null;
/*     */ 
/*  73 */     if (refAttr == null) {
/*  74 */       reportSchemaError("s4s-att-must-appear", new Object[] { "attributeGroup (local)", "ref" }, elmNode);
/*  75 */       this.fAttrChecker.returnAttrArray(attrValues, schemaDoc);
/*  76 */       return null;
/*     */     }
/*     */ 
/*  80 */     attrGrp = (XSAttributeGroupDecl)this.fSchemaHandler.getGlobalDecl(schemaDoc, 2, refAttr, elmNode);
/*     */ 
/*  83 */     Element child = DOMUtil.getFirstChildElement(elmNode);
/*  84 */     if (child != null) {
/*  85 */       String childName = DOMUtil.getLocalName(child);
/*  86 */       if (childName.equals(SchemaSymbols.ELT_ANNOTATION)) {
/*  87 */         traverseAnnotationDecl(child, attrValues, false, schemaDoc);
/*  88 */         child = DOMUtil.getNextSiblingElement(child);
/*     */       } else {
/*  90 */         String text = DOMUtil.getSyntheticAnnotation(child);
/*  91 */         if (text != null) {
/*  92 */           traverseSyntheticAnnotation(child, text, attrValues, false, schemaDoc);
/*     */         }
/*     */       }
/*     */ 
/*  96 */       if (child != null) {
/*  97 */         Object[] args = { refAttr.rawname, "(annotation?)", DOMUtil.getLocalName(child) };
/*  98 */         reportSchemaError("s4s-elt-must-match.1", args, child);
/*     */       }
/*     */     }
/*     */ 
/* 102 */     this.fAttrChecker.returnAttrArray(attrValues, schemaDoc);
/* 103 */     return attrGrp;
/*     */   }
/*     */ 
/*     */   XSAttributeGroupDecl traverseGlobal(Element elmNode, XSDocumentInfo schemaDoc, SchemaGrammar grammar)
/*     */   {
/* 111 */     XSAttributeGroupDecl attrGrp = new XSAttributeGroupDecl();
/*     */ 
/* 114 */     Object[] attrValues = this.fAttrChecker.checkAttributes(elmNode, true, schemaDoc);
/*     */ 
/* 116 */     String nameAttr = (String)attrValues[XSAttributeChecker.ATTIDX_NAME];
/*     */ 
/* 119 */     if (nameAttr == null) {
/* 120 */       reportSchemaError("s4s-att-must-appear", new Object[] { "attributeGroup (global)", "name" }, elmNode);
/* 121 */       nameAttr = "(no name)";
/*     */     }
/*     */ 
/* 124 */     attrGrp.fName = nameAttr;
/* 125 */     attrGrp.fTargetNamespace = schemaDoc.fTargetNamespace;
/*     */ 
/* 128 */     Element child = DOMUtil.getFirstChildElement(elmNode);
/* 129 */     XSAnnotationImpl annotation = null;
/*     */ 
/* 131 */     if ((child != null) && (DOMUtil.getLocalName(child).equals(SchemaSymbols.ELT_ANNOTATION))) {
/* 132 */       annotation = traverseAnnotationDecl(child, attrValues, false, schemaDoc);
/* 133 */       child = DOMUtil.getNextSiblingElement(child);
/*     */     }
/*     */     else {
/* 136 */       String text = DOMUtil.getSyntheticAnnotation(elmNode);
/* 137 */       if (text != null) {
/* 138 */         annotation = traverseSyntheticAnnotation(elmNode, text, attrValues, false, schemaDoc);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 145 */     Element nextNode = traverseAttrsAndAttrGrps(child, attrGrp, schemaDoc, grammar, null);
/* 146 */     if (nextNode != null)
/*     */     {
/* 148 */       Object[] args = { nameAttr, "(annotation?, ((attribute | attributeGroup)*, anyAttribute?))", DOMUtil.getLocalName(nextNode) };
/* 149 */       reportSchemaError("s4s-elt-must-match.1", args, nextNode);
/*     */     }
/*     */ 
/* 152 */     if (nameAttr.equals("(no name)"))
/*     */     {
/* 154 */       this.fAttrChecker.returnAttrArray(attrValues, schemaDoc);
/* 155 */       return null;
/*     */     }
/*     */ 
/* 159 */     attrGrp.removeProhibitedAttrs();
/*     */ 
/* 162 */     XSAttributeGroupDecl redefinedAttrGrp = (XSAttributeGroupDecl)this.fSchemaHandler.getGrpOrAttrGrpRedefinedByRestriction(2, new QName(XMLSymbols.EMPTY_STRING, nameAttr, nameAttr, schemaDoc.fTargetNamespace), schemaDoc, elmNode);
/*     */ 
/* 166 */     if (redefinedAttrGrp != null) {
/* 167 */       Object[] errArgs = attrGrp.validRestrictionOf(nameAttr, redefinedAttrGrp);
/* 168 */       if (errArgs != null) {
/* 169 */         reportSchemaError((String)errArgs[(errArgs.length - 1)], errArgs, child);
/* 170 */         reportSchemaError("src-redefine.7.2.2", new Object[] { nameAttr, errArgs[(errArgs.length - 1)] }, child);
/*     */       }
/*     */     }
/*     */     XSObjectList annotations;
/* 175 */     if (annotation != null) {
/* 176 */       XSObjectList annotations = new XSObjectListImpl();
/* 177 */       ((XSObjectListImpl)annotations).addXSObject(annotation);
/*     */     } else {
/* 179 */       annotations = XSObjectListImpl.EMPTY_LIST;
/*     */     }
/*     */ 
/* 182 */     attrGrp.fAnnotations = annotations;
/*     */ 
/* 185 */     if (grammar.getGlobalAttributeGroupDecl(attrGrp.fName) == null) {
/* 186 */       grammar.addGlobalAttributeGroupDecl(attrGrp);
/*     */     }
/*     */ 
/* 190 */     String loc = this.fSchemaHandler.schemaDocument2SystemId(schemaDoc);
/* 191 */     XSAttributeGroupDecl attrGrp2 = grammar.getGlobalAttributeGroupDecl(attrGrp.fName, loc);
/* 192 */     if (attrGrp2 == null) {
/* 193 */       grammar.addGlobalAttributeGroupDecl(attrGrp, loc);
/*     */     }
/*     */ 
/* 197 */     if (this.fSchemaHandler.fTolerateDuplicates) {
/* 198 */       if (attrGrp2 != null) {
/* 199 */         attrGrp = attrGrp2;
/*     */       }
/* 201 */       this.fSchemaHandler.addGlobalAttributeGroupDecl(attrGrp);
/*     */     }
/*     */ 
/* 204 */     this.fAttrChecker.returnAttrArray(attrValues, schemaDoc);
/* 205 */     return attrGrp;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.traversers.XSDAttributeGroupTraverser
 * JD-Core Version:    0.6.2
 */