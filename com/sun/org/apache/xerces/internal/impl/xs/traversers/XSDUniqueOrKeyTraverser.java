/*     */ package com.sun.org.apache.xerces.internal.impl.xs.traversers;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XSElementDecl;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.identity.IdentityConstraint;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.identity.UniqueOrKey;
/*     */ import com.sun.org.apache.xerces.internal.util.DOMUtil;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ class XSDUniqueOrKeyTraverser extends XSDAbstractIDConstraintTraverser
/*     */ {
/*     */   public XSDUniqueOrKeyTraverser(XSDHandler handler, XSAttributeChecker gAttrCheck)
/*     */   {
/*  44 */     super(handler, gAttrCheck);
/*     */   }
/*     */ 
/*     */   void traverse(Element uElem, XSElementDecl element, XSDocumentInfo schemaDoc, SchemaGrammar grammar)
/*     */   {
/*  52 */     Object[] attrValues = this.fAttrChecker.checkAttributes(uElem, false, schemaDoc);
/*     */ 
/*  55 */     String uName = (String)attrValues[XSAttributeChecker.ATTIDX_NAME];
/*     */ 
/*  57 */     if (uName == null) {
/*  58 */       reportSchemaError("s4s-att-must-appear", new Object[] { DOMUtil.getLocalName(uElem), SchemaSymbols.ATT_NAME }, uElem);
/*     */ 
/*  60 */       this.fAttrChecker.returnAttrArray(attrValues, schemaDoc);
/*  61 */       return;
/*     */     }
/*     */ 
/*  64 */     UniqueOrKey uniqueOrKey = null;
/*  65 */     if (DOMUtil.getLocalName(uElem).equals(SchemaSymbols.ELT_UNIQUE))
/*  66 */       uniqueOrKey = new UniqueOrKey(schemaDoc.fTargetNamespace, uName, element.fName, (short)3);
/*     */     else {
/*  68 */       uniqueOrKey = new UniqueOrKey(schemaDoc.fTargetNamespace, uName, element.fName, (short)1);
/*     */     }
/*     */ 
/*  76 */     if (traverseIdentityConstraint(uniqueOrKey, uElem, schemaDoc, attrValues))
/*     */     {
/*  78 */       if (grammar.getIDConstraintDecl(uniqueOrKey.getIdentityConstraintName()) == null) {
/*  79 */         grammar.addIDConstraintDecl(element, uniqueOrKey);
/*     */       }
/*     */ 
/*  82 */       String loc = this.fSchemaHandler.schemaDocument2SystemId(schemaDoc);
/*  83 */       IdentityConstraint idc = grammar.getIDConstraintDecl(uniqueOrKey.getIdentityConstraintName(), loc);
/*  84 */       if (idc == null) {
/*  85 */         grammar.addIDConstraintDecl(element, uniqueOrKey, loc);
/*     */       }
/*     */ 
/*  89 */       if (this.fSchemaHandler.fTolerateDuplicates) {
/*  90 */         if ((idc != null) && 
/*  91 */           ((idc instanceof UniqueOrKey))) {
/*  92 */           uniqueOrKey = uniqueOrKey;
/*     */         }
/*     */ 
/*  95 */         this.fSchemaHandler.addIDConstraintDecl(uniqueOrKey);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 100 */     this.fAttrChecker.returnAttrArray(attrValues, schemaDoc);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.traversers.XSDUniqueOrKeyTraverser
 * JD-Core Version:    0.6.2
 */