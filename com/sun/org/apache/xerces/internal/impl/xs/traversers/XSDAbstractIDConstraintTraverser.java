/*     */ package com.sun.org.apache.xerces.internal.impl.xs.traversers;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.xpath.XPathException;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.identity.Field;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.identity.Field.XPath;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.identity.IdentityConstraint;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.identity.Selector;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.identity.Selector.XPath;
/*     */ import com.sun.org.apache.xerces.internal.util.DOMUtil;
/*     */ import com.sun.org.apache.xerces.internal.util.XMLChar;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ class XSDAbstractIDConstraintTraverser extends XSDAbstractTraverser
/*     */ {
/*     */   public XSDAbstractIDConstraintTraverser(XSDHandler handler, XSAttributeChecker gAttrCheck)
/*     */   {
/*  45 */     super(handler, gAttrCheck);
/*     */   }
/*     */ 
/*     */   boolean traverseIdentityConstraint(IdentityConstraint ic, Element icElem, XSDocumentInfo schemaDoc, Object[] icElemAttrs)
/*     */   {
/*  54 */     Element sElem = DOMUtil.getFirstChildElement(icElem);
/*  55 */     if (sElem == null) {
/*  56 */       reportSchemaError("s4s-elt-must-match.2", new Object[] { "identity constraint", "(annotation?, selector, field+)" }, icElem);
/*     */ 
/*  59 */       return false;
/*     */     }
/*     */ 
/*  64 */     if (DOMUtil.getLocalName(sElem).equals(SchemaSymbols.ELT_ANNOTATION)) {
/*  65 */       ic.addAnnotation(traverseAnnotationDecl(sElem, icElemAttrs, false, schemaDoc));
/*  66 */       sElem = DOMUtil.getNextSiblingElement(sElem);
/*     */ 
/*  68 */       if (sElem == null) {
/*  69 */         reportSchemaError("s4s-elt-must-match.2", new Object[] { "identity constraint", "(annotation?, selector, field+)" }, icElem);
/*  70 */         return false;
/*     */       }
/*     */     }
/*     */     else {
/*  74 */       String text = DOMUtil.getSyntheticAnnotation(icElem);
/*  75 */       if (text != null) {
/*  76 */         ic.addAnnotation(traverseSyntheticAnnotation(icElem, text, icElemAttrs, false, schemaDoc));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  81 */     if (!DOMUtil.getLocalName(sElem).equals(SchemaSymbols.ELT_SELECTOR)) {
/*  82 */       reportSchemaError("s4s-elt-must-match.1", new Object[] { "identity constraint", "(annotation?, selector, field+)", SchemaSymbols.ELT_SELECTOR }, sElem);
/*  83 */       return false;
/*     */     }
/*  85 */     Object[] attrValues = this.fAttrChecker.checkAttributes(sElem, false, schemaDoc);
/*     */ 
/*  88 */     Element selChild = DOMUtil.getFirstChildElement(sElem);
/*     */ 
/*  90 */     if (selChild != null)
/*     */     {
/*  92 */       if (DOMUtil.getLocalName(selChild).equals(SchemaSymbols.ELT_ANNOTATION)) {
/*  93 */         ic.addAnnotation(traverseAnnotationDecl(selChild, attrValues, false, schemaDoc));
/*  94 */         selChild = DOMUtil.getNextSiblingElement(selChild);
/*     */       }
/*     */       else {
/*  97 */         reportSchemaError("s4s-elt-must-match.1", new Object[] { SchemaSymbols.ELT_SELECTOR, "(annotation?)", DOMUtil.getLocalName(selChild) }, selChild);
/*     */       }
/*  99 */       if (selChild != null)
/* 100 */         reportSchemaError("s4s-elt-must-match.1", new Object[] { SchemaSymbols.ELT_SELECTOR, "(annotation?)", DOMUtil.getLocalName(selChild) }, selChild);
/*     */     }
/*     */     else
/*     */     {
/* 104 */       String text = DOMUtil.getSyntheticAnnotation(sElem);
/* 105 */       if (text != null) {
/* 106 */         ic.addAnnotation(traverseSyntheticAnnotation(icElem, text, attrValues, false, schemaDoc));
/*     */       }
/*     */     }
/*     */ 
/* 110 */     String sText = (String)attrValues[XSAttributeChecker.ATTIDX_XPATH];
/* 111 */     if (sText == null) {
/* 112 */       reportSchemaError("s4s-att-must-appear", new Object[] { SchemaSymbols.ELT_SELECTOR, SchemaSymbols.ATT_XPATH }, sElem);
/* 113 */       return false;
/*     */     }
/* 115 */     sText = XMLChar.trim(sText);
/*     */ 
/* 117 */     Selector.XPath sXpath = null;
/*     */     try {
/* 119 */       sXpath = new Selector.XPath(sText, this.fSymbolTable, schemaDoc.fNamespaceSupport);
/*     */ 
/* 121 */       Selector selector = new Selector(sXpath, ic);
/* 122 */       ic.setSelector(selector);
/*     */     }
/*     */     catch (XPathException e) {
/* 125 */       reportSchemaError(e.getKey(), new Object[] { sText }, sElem);
/*     */ 
/* 127 */       this.fAttrChecker.returnAttrArray(attrValues, schemaDoc);
/* 128 */       return false;
/*     */     }
/*     */ 
/* 132 */     this.fAttrChecker.returnAttrArray(attrValues, schemaDoc);
/*     */ 
/* 135 */     Element fElem = DOMUtil.getNextSiblingElement(sElem);
/* 136 */     if (fElem == null) {
/* 137 */       reportSchemaError("s4s-elt-must-match.2", new Object[] { "identity constraint", "(annotation?, selector, field+)" }, sElem);
/* 138 */       return false;
/*     */     }
/* 140 */     while (fElem != null) {
/* 141 */       if (!DOMUtil.getLocalName(fElem).equals(SchemaSymbols.ELT_FIELD)) {
/* 142 */         reportSchemaError("s4s-elt-must-match.1", new Object[] { "identity constraint", "(annotation?, selector, field+)", SchemaSymbols.ELT_FIELD }, fElem);
/* 143 */         fElem = DOMUtil.getNextSiblingElement(fElem);
/*     */       }
/*     */       else
/*     */       {
/* 148 */         attrValues = this.fAttrChecker.checkAttributes(fElem, false, schemaDoc);
/*     */ 
/* 151 */         Element fieldChild = DOMUtil.getFirstChildElement(fElem);
/* 152 */         if (fieldChild != null)
/*     */         {
/* 154 */           if (DOMUtil.getLocalName(fieldChild).equals(SchemaSymbols.ELT_ANNOTATION)) {
/* 155 */             ic.addAnnotation(traverseAnnotationDecl(fieldChild, attrValues, false, schemaDoc));
/* 156 */             fieldChild = DOMUtil.getNextSiblingElement(fieldChild);
/*     */           }
/*     */         }
/* 159 */         if (fieldChild != null) {
/* 160 */           reportSchemaError("s4s-elt-must-match.1", new Object[] { SchemaSymbols.ELT_FIELD, "(annotation?)", DOMUtil.getLocalName(fieldChild) }, fieldChild);
/*     */         }
/*     */         else {
/* 163 */           String text = DOMUtil.getSyntheticAnnotation(fElem);
/* 164 */           if (text != null) {
/* 165 */             ic.addAnnotation(traverseSyntheticAnnotation(icElem, text, attrValues, false, schemaDoc));
/*     */           }
/*     */         }
/* 168 */         String fText = (String)attrValues[XSAttributeChecker.ATTIDX_XPATH];
/* 169 */         if (fText == null) {
/* 170 */           reportSchemaError("s4s-att-must-appear", new Object[] { SchemaSymbols.ELT_FIELD, SchemaSymbols.ATT_XPATH }, fElem);
/* 171 */           this.fAttrChecker.returnAttrArray(attrValues, schemaDoc);
/* 172 */           return false;
/*     */         }
/* 174 */         fText = XMLChar.trim(fText);
/*     */         try {
/* 176 */           Field.XPath fXpath = new Field.XPath(fText, this.fSymbolTable, schemaDoc.fNamespaceSupport);
/*     */ 
/* 178 */           Field field = new Field(fXpath, ic);
/* 179 */           ic.addField(field);
/*     */         }
/*     */         catch (XPathException e) {
/* 182 */           reportSchemaError(e.getKey(), new Object[] { fText }, fElem);
/*     */ 
/* 184 */           this.fAttrChecker.returnAttrArray(attrValues, schemaDoc);
/* 185 */           return false;
/*     */         }
/* 187 */         fElem = DOMUtil.getNextSiblingElement(fElem);
/*     */ 
/* 189 */         this.fAttrChecker.returnAttrArray(attrValues, schemaDoc);
/*     */       }
/*     */     }
/* 192 */     return ic.getFieldCount() > 0;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.traversers.XSDAbstractIDConstraintTraverser
 * JD-Core Version:    0.6.2
 */