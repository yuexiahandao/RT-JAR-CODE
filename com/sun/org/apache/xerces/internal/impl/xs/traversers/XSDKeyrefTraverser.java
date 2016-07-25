/*     */ package com.sun.org.apache.xerces.internal.impl.xs.traversers;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XSElementDecl;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.identity.IdentityConstraint;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.identity.KeyRef;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.identity.UniqueOrKey;
/*     */ import com.sun.org.apache.xerces.internal.xni.QName;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ class XSDKeyrefTraverser extends XSDAbstractIDConstraintTraverser
/*     */ {
/*     */   public XSDKeyrefTraverser(XSDHandler handler, XSAttributeChecker gAttrCheck)
/*     */   {
/*  44 */     super(handler, gAttrCheck);
/*     */   }
/*     */ 
/*     */   void traverse(Element krElem, XSElementDecl element, XSDocumentInfo schemaDoc, SchemaGrammar grammar)
/*     */   {
/*  51 */     Object[] attrValues = this.fAttrChecker.checkAttributes(krElem, false, schemaDoc);
/*     */ 
/*  54 */     String krName = (String)attrValues[XSAttributeChecker.ATTIDX_NAME];
/*  55 */     if (krName == null) {
/*  56 */       reportSchemaError("s4s-att-must-appear", new Object[] { SchemaSymbols.ELT_KEYREF, SchemaSymbols.ATT_NAME }, krElem);
/*     */ 
/*  58 */       this.fAttrChecker.returnAttrArray(attrValues, schemaDoc);
/*  59 */       return;
/*     */     }
/*  61 */     QName kName = (QName)attrValues[XSAttributeChecker.ATTIDX_REFER];
/*  62 */     if (kName == null) {
/*  63 */       reportSchemaError("s4s-att-must-appear", new Object[] { SchemaSymbols.ELT_KEYREF, SchemaSymbols.ATT_REFER }, krElem);
/*     */ 
/*  65 */       this.fAttrChecker.returnAttrArray(attrValues, schemaDoc);
/*  66 */       return;
/*     */     }
/*     */ 
/*  69 */     UniqueOrKey key = null;
/*  70 */     IdentityConstraint ret = (IdentityConstraint)this.fSchemaHandler.getGlobalDecl(schemaDoc, 5, kName, krElem);
/*     */ 
/*  73 */     if (ret != null) {
/*  74 */       if ((ret.getCategory() == 1) || (ret.getCategory() == 3))
/*     */       {
/*  76 */         key = (UniqueOrKey)ret;
/*     */       }
/*  78 */       else reportSchemaError("src-resolve", new Object[] { kName.rawname, "identity constraint key/unique" }, krElem);
/*     */ 
/*     */     }
/*     */ 
/*  82 */     if (key == null) {
/*  83 */       this.fAttrChecker.returnAttrArray(attrValues, schemaDoc);
/*  84 */       return;
/*     */     }
/*     */ 
/*  87 */     KeyRef keyRef = new KeyRef(schemaDoc.fTargetNamespace, krName, element.fName, key);
/*     */ 
/*  91 */     if (traverseIdentityConstraint(keyRef, krElem, schemaDoc, attrValues))
/*     */     {
/*  94 */       if (key.getFieldCount() != keyRef.getFieldCount()) {
/*  95 */         reportSchemaError("c-props-correct.2", new Object[] { krName, key.getIdentityConstraintName() }, krElem);
/*     */       }
/*     */       else
/*     */       {
/*  99 */         if (grammar.getIDConstraintDecl(keyRef.getIdentityConstraintName()) == null) {
/* 100 */           grammar.addIDConstraintDecl(element, keyRef);
/*     */         }
/*     */ 
/* 104 */         String loc = this.fSchemaHandler.schemaDocument2SystemId(schemaDoc);
/* 105 */         IdentityConstraint idc = grammar.getIDConstraintDecl(keyRef.getIdentityConstraintName(), loc);
/* 106 */         if (idc == null) {
/* 107 */           grammar.addIDConstraintDecl(element, keyRef, loc);
/*     */         }
/*     */ 
/* 111 */         if (this.fSchemaHandler.fTolerateDuplicates) {
/* 112 */           if ((idc != null) && 
/* 113 */             ((idc instanceof KeyRef))) {
/* 114 */             keyRef = (KeyRef)idc;
/*     */           }
/*     */ 
/* 117 */           this.fSchemaHandler.addIDConstraintDecl(keyRef);
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 123 */     this.fAttrChecker.returnAttrArray(attrValues, schemaDoc);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.traversers.XSDKeyrefTraverser
 * JD-Core Version:    0.6.2
 */