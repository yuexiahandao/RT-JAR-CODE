/*     */ package com.sun.org.apache.xerces.internal.impl.xs.traversers;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XSAnnotationImpl;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XSModelGroupImpl;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XSParticleDecl;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.util.XInt;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.util.XSObjectListImpl;
/*     */ import com.sun.org.apache.xerces.internal.util.DOMUtil;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSObject;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSObjectList;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ abstract class XSDAbstractParticleTraverser extends XSDAbstractTraverser
/*     */ {
/* 384 */   ParticleArray fPArray = new ParticleArray();
/*     */ 
/*     */   XSDAbstractParticleTraverser(XSDHandler handler, XSAttributeChecker gAttrCheck)
/*     */   {
/*  46 */     super(handler, gAttrCheck);
/*     */   }
/*     */ 
/*     */   XSParticleDecl traverseAll(Element allDecl, XSDocumentInfo schemaDoc, SchemaGrammar grammar, int allContextFlags, XSObject parent)
/*     */   {
/*  68 */     Object[] attrValues = this.fAttrChecker.checkAttributes(allDecl, false, schemaDoc);
/*     */ 
/*  70 */     Element child = DOMUtil.getFirstChildElement(allDecl);
/*     */ 
/*  72 */     XSAnnotationImpl annotation = null;
/*  73 */     if ((child != null) && (DOMUtil.getLocalName(child).equals(SchemaSymbols.ELT_ANNOTATION))) {
/*  74 */       annotation = traverseAnnotationDecl(child, attrValues, false, schemaDoc);
/*  75 */       child = DOMUtil.getNextSiblingElement(child);
/*     */     }
/*     */     else {
/*  78 */       String text = DOMUtil.getSyntheticAnnotation(allDecl);
/*  79 */       if (text != null) {
/*  80 */         annotation = traverseSyntheticAnnotation(allDecl, text, attrValues, false, schemaDoc);
/*     */       }
/*     */     }
/*  83 */     String childName = null;
/*     */ 
/*  85 */     this.fPArray.pushContext();
/*     */ 
/*  87 */     for (; child != null; child = DOMUtil.getNextSiblingElement(child))
/*     */     {
/*  89 */       XSParticleDecl particle = null;
/*  90 */       childName = DOMUtil.getLocalName(child);
/*     */ 
/*  93 */       if (childName.equals(SchemaSymbols.ELT_ELEMENT)) {
/*  94 */         particle = this.fSchemaHandler.fElementTraverser.traverseLocal(child, schemaDoc, grammar, 1, parent);
/*     */       }
/*     */       else {
/*  97 */         Object[] args = { "all", "(annotation?, element*)", DOMUtil.getLocalName(child) };
/*  98 */         reportSchemaError("s4s-elt-must-match.1", args, child);
/*     */       }
/*     */ 
/* 101 */       if (particle != null) {
/* 102 */         this.fPArray.addParticle(particle);
/*     */       }
/*     */     }
/* 105 */     XSParticleDecl particle = null;
/* 106 */     XInt minAtt = (XInt)attrValues[XSAttributeChecker.ATTIDX_MINOCCURS];
/* 107 */     XInt maxAtt = (XInt)attrValues[XSAttributeChecker.ATTIDX_MAXOCCURS];
/* 108 */     Long defaultVals = (Long)attrValues[XSAttributeChecker.ATTIDX_FROMDEFAULT];
/*     */ 
/* 110 */     XSModelGroupImpl group = new XSModelGroupImpl();
/* 111 */     group.fCompositor = 103;
/* 112 */     group.fParticleCount = this.fPArray.getParticleCount();
/* 113 */     group.fParticles = this.fPArray.popContext();
/*     */     XSObjectList annotations;
/* 115 */     if (annotation != null) {
/* 116 */       XSObjectList annotations = new XSObjectListImpl();
/* 117 */       ((XSObjectListImpl)annotations).addXSObject(annotation);
/*     */     } else {
/* 119 */       annotations = XSObjectListImpl.EMPTY_LIST;
/*     */     }
/* 121 */     group.fAnnotations = annotations;
/* 122 */     particle = new XSParticleDecl();
/* 123 */     particle.fType = 3;
/* 124 */     particle.fMinOccurs = minAtt.intValue();
/* 125 */     particle.fMaxOccurs = maxAtt.intValue();
/* 126 */     particle.fValue = group;
/* 127 */     particle.fAnnotations = annotations;
/*     */ 
/* 129 */     particle = checkOccurrences(particle, SchemaSymbols.ELT_ALL, (Element)allDecl.getParentNode(), allContextFlags, defaultVals.longValue());
/*     */ 
/* 134 */     this.fAttrChecker.returnAttrArray(attrValues, schemaDoc);
/*     */ 
/* 136 */     return particle;
/*     */   }
/*     */ 
/*     */   XSParticleDecl traverseSequence(Element seqDecl, XSDocumentInfo schemaDoc, SchemaGrammar grammar, int allContextFlags, XSObject parent)
/*     */   {
/* 160 */     return traverseSeqChoice(seqDecl, schemaDoc, grammar, allContextFlags, false, parent);
/*     */   }
/*     */ 
/*     */   XSParticleDecl traverseChoice(Element choiceDecl, XSDocumentInfo schemaDoc, SchemaGrammar grammar, int allContextFlags, XSObject parent)
/*     */   {
/* 184 */     return traverseSeqChoice(choiceDecl, schemaDoc, grammar, allContextFlags, true, parent);
/*     */   }
/*     */ 
/*     */   private XSParticleDecl traverseSeqChoice(Element decl, XSDocumentInfo schemaDoc, SchemaGrammar grammar, int allContextFlags, boolean choice, XSObject parent)
/*     */   {
/* 204 */     Object[] attrValues = this.fAttrChecker.checkAttributes(decl, false, schemaDoc);
/*     */ 
/* 206 */     Element child = DOMUtil.getFirstChildElement(decl);
/* 207 */     XSAnnotationImpl annotation = null;
/* 208 */     if ((child != null) && (DOMUtil.getLocalName(child).equals(SchemaSymbols.ELT_ANNOTATION))) {
/* 209 */       annotation = traverseAnnotationDecl(child, attrValues, false, schemaDoc);
/* 210 */       child = DOMUtil.getNextSiblingElement(child);
/*     */     }
/*     */     else {
/* 213 */       String text = DOMUtil.getSyntheticAnnotation(decl);
/* 214 */       if (text != null) {
/* 215 */         annotation = traverseSyntheticAnnotation(decl, text, attrValues, false, schemaDoc);
/*     */       }
/*     */     }
/*     */ 
/* 219 */     String childName = null;
/*     */ 
/* 221 */     this.fPArray.pushContext();
/*     */ 
/* 223 */     for (; child != null; child = DOMUtil.getNextSiblingElement(child))
/*     */     {
/* 225 */       XSParticleDecl particle = null;
/*     */ 
/* 227 */       childName = DOMUtil.getLocalName(child);
/* 228 */       if (childName.equals(SchemaSymbols.ELT_ELEMENT)) {
/* 229 */         particle = this.fSchemaHandler.fElementTraverser.traverseLocal(child, schemaDoc, grammar, 0, parent);
/*     */       }
/* 231 */       else if (childName.equals(SchemaSymbols.ELT_GROUP)) {
/* 232 */         particle = this.fSchemaHandler.fGroupTraverser.traverseLocal(child, schemaDoc, grammar);
/*     */ 
/* 236 */         if (hasAllContent(particle))
/*     */         {
/* 239 */           particle = null;
/* 240 */           reportSchemaError("cos-all-limited.1.2", null, child);
/*     */         }
/*     */ 
/*     */       }
/* 244 */       else if (childName.equals(SchemaSymbols.ELT_CHOICE)) {
/* 245 */         particle = traverseChoice(child, schemaDoc, grammar, 0, parent);
/*     */       }
/* 247 */       else if (childName.equals(SchemaSymbols.ELT_SEQUENCE)) {
/* 248 */         particle = traverseSequence(child, schemaDoc, grammar, 0, parent);
/*     */       }
/* 250 */       else if (childName.equals(SchemaSymbols.ELT_ANY)) {
/* 251 */         particle = this.fSchemaHandler.fWildCardTraverser.traverseAny(child, schemaDoc, grammar);
/*     */       }
/*     */       else
/*     */       {
/*     */         Object[] args;
/*     */         Object[] args;
/* 255 */         if (choice) {
/* 256 */           args = new Object[] { "choice", "(annotation?, (element | group | choice | sequence | any)*)", DOMUtil.getLocalName(child) };
/*     */         }
/*     */         else {
/* 259 */           args = new Object[] { "sequence", "(annotation?, (element | group | choice | sequence | any)*)", DOMUtil.getLocalName(child) };
/*     */         }
/* 261 */         reportSchemaError("s4s-elt-must-match.1", args, child);
/*     */       }
/*     */ 
/* 264 */       if (particle != null) {
/* 265 */         this.fPArray.addParticle(particle);
/*     */       }
/*     */     }
/* 268 */     XSParticleDecl particle = null;
/*     */ 
/* 270 */     XInt minAtt = (XInt)attrValues[XSAttributeChecker.ATTIDX_MINOCCURS];
/* 271 */     XInt maxAtt = (XInt)attrValues[XSAttributeChecker.ATTIDX_MAXOCCURS];
/* 272 */     Long defaultVals = (Long)attrValues[XSAttributeChecker.ATTIDX_FROMDEFAULT];
/*     */ 
/* 274 */     XSModelGroupImpl group = new XSModelGroupImpl();
/* 275 */     group.fCompositor = (choice ? 101 : 102);
/* 276 */     group.fParticleCount = this.fPArray.getParticleCount();
/* 277 */     group.fParticles = this.fPArray.popContext();
/*     */     XSObjectList annotations;
/* 279 */     if (annotation != null) {
/* 280 */       XSObjectList annotations = new XSObjectListImpl();
/* 281 */       ((XSObjectListImpl)annotations).addXSObject(annotation);
/*     */     } else {
/* 283 */       annotations = XSObjectListImpl.EMPTY_LIST;
/*     */     }
/* 285 */     group.fAnnotations = annotations;
/* 286 */     particle = new XSParticleDecl();
/* 287 */     particle.fType = 3;
/* 288 */     particle.fMinOccurs = minAtt.intValue();
/* 289 */     particle.fMaxOccurs = maxAtt.intValue();
/* 290 */     particle.fValue = group;
/* 291 */     particle.fAnnotations = annotations;
/*     */ 
/* 293 */     particle = checkOccurrences(particle, choice ? SchemaSymbols.ELT_CHOICE : SchemaSymbols.ELT_SEQUENCE, (Element)decl.getParentNode(), allContextFlags, defaultVals.longValue());
/*     */ 
/* 298 */     this.fAttrChecker.returnAttrArray(attrValues, schemaDoc);
/*     */ 
/* 300 */     return particle;
/*     */   }
/*     */ 
/*     */   protected boolean hasAllContent(XSParticleDecl particle)
/*     */   {
/* 306 */     if ((particle != null) && (particle.fType == 3)) {
/* 307 */       return ((XSModelGroupImpl)particle.fValue).fCompositor == 103;
/*     */     }
/*     */ 
/* 310 */     return false;
/*     */   }
/*     */ 
/*     */   protected static class ParticleArray
/*     */   {
/* 324 */     XSParticleDecl[] fParticles = new XSParticleDecl[10];
/*     */ 
/* 329 */     int[] fPos = new int[5];
/*     */ 
/* 331 */     int fContextCount = 0;
/*     */ 
/*     */     void pushContext()
/*     */     {
/* 335 */       this.fContextCount += 1;
/*     */ 
/* 337 */       if (this.fContextCount == this.fPos.length) {
/* 338 */         int newSize = this.fContextCount * 2;
/* 339 */         int[] newArray = new int[newSize];
/* 340 */         System.arraycopy(this.fPos, 0, newArray, 0, this.fContextCount);
/* 341 */         this.fPos = newArray;
/*     */       }
/*     */ 
/* 346 */       this.fPos[this.fContextCount] = this.fPos[(this.fContextCount - 1)];
/*     */     }
/*     */ 
/*     */     int getParticleCount()
/*     */     {
/* 351 */       return this.fPos[this.fContextCount] - this.fPos[(this.fContextCount - 1)];
/*     */     }
/*     */ 
/*     */     void addParticle(XSParticleDecl particle)
/*     */     {
/* 357 */       if (this.fPos[this.fContextCount] == this.fParticles.length) {
/* 358 */         int newSize = this.fPos[this.fContextCount] * 2;
/* 359 */         XSParticleDecl[] newArray = new XSParticleDecl[newSize];
/* 360 */         System.arraycopy(this.fParticles, 0, newArray, 0, this.fPos[this.fContextCount]);
/* 361 */         this.fParticles = newArray;
/*     */       }
/*     */       int tmp70_67 = this.fContextCount;
/*     */       int[] tmp70_63 = this.fPos;
/*     */       int tmp72_71 = tmp70_63[tmp70_67]; tmp70_63[tmp70_67] = (tmp72_71 + 1); this.fParticles[tmp72_71] = particle;
/*     */     }
/*     */ 
/*     */     XSParticleDecl[] popContext()
/*     */     {
/* 368 */       int count = this.fPos[this.fContextCount] - this.fPos[(this.fContextCount - 1)];
/* 369 */       XSParticleDecl[] array = null;
/* 370 */       if (count != 0) {
/* 371 */         array = new XSParticleDecl[count];
/* 372 */         System.arraycopy(this.fParticles, this.fPos[(this.fContextCount - 1)], array, 0, count);
/*     */ 
/* 374 */         for (int i = this.fPos[(this.fContextCount - 1)]; i < this.fPos[this.fContextCount]; i++)
/* 375 */           this.fParticles[i] = null;
/*     */       }
/* 377 */       this.fContextCount -= 1;
/* 378 */       return array;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.traversers.XSDAbstractParticleTraverser
 * JD-Core Version:    0.6.2
 */