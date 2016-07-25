/*     */ package com.sun.org.apache.xerces.internal.impl.xs.models;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.dtd.models.CMNode;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XSComplexTypeDecl;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XSDeclarationPool;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XSElementDecl;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XSModelGroupImpl;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XSParticleDecl;
/*     */ 
/*     */ public class CMBuilder
/*     */ {
/*  44 */   private XSDeclarationPool fDeclPool = null;
/*     */ 
/*  47 */   private static XSEmptyCM fEmptyCM = new XSEmptyCM();
/*     */   private int fLeafCount;
/*     */   private int fParticleCount;
/*     */   private CMNodeFactory fNodeFactory;
/*     */ 
/*     */   public CMBuilder(CMNodeFactory nodeFactory)
/*     */   {
/*  57 */     this.fDeclPool = null;
/*  58 */     this.fNodeFactory = nodeFactory;
/*     */   }
/*     */ 
/*     */   public void setDeclPool(XSDeclarationPool declPool) {
/*  62 */     this.fDeclPool = declPool;
/*     */   }
/*     */ 
/*     */   public XSCMValidator getContentModel(XSComplexTypeDecl typeDecl)
/*     */   {
/*  75 */     short contentType = typeDecl.getContentType();
/*  76 */     if ((contentType == 1) || (contentType == 0))
/*     */     {
/*  78 */       return null;
/*     */     }
/*     */ 
/*  81 */     XSParticleDecl particle = (XSParticleDecl)typeDecl.getParticle();
/*     */ 
/*  85 */     if (particle == null) {
/*  86 */       return fEmptyCM;
/*     */     }
/*     */ 
/*  90 */     XSCMValidator cmValidator = null;
/*  91 */     if ((particle.fType == 3) && (((XSModelGroupImpl)particle.fValue).fCompositor == 103))
/*     */     {
/*  93 */       cmValidator = createAllCM(particle);
/*     */     }
/*     */     else {
/*  96 */       cmValidator = createDFACM(particle);
/*     */     }
/*     */ 
/* 101 */     this.fNodeFactory.resetNodeCount();
/*     */ 
/* 105 */     if (cmValidator == null) {
/* 106 */       cmValidator = fEmptyCM;
/*     */     }
/* 108 */     return cmValidator;
/*     */   }
/*     */ 
/*     */   XSCMValidator createAllCM(XSParticleDecl particle) {
/* 112 */     if (particle.fMaxOccurs == 0) {
/* 113 */       return null;
/*     */     }
/*     */ 
/* 116 */     XSModelGroupImpl group = (XSModelGroupImpl)particle.fValue;
/*     */ 
/* 119 */     XSAllCM allContent = new XSAllCM(particle.fMinOccurs == 0, group.fParticleCount);
/* 120 */     for (int i = 0; i < group.fParticleCount; i++)
/*     */     {
/* 122 */       allContent.addElement((XSElementDecl)group.fParticles[i].fValue, group.fParticles[i].fMinOccurs == 0);
/*     */     }
/*     */ 
/* 125 */     return allContent;
/*     */   }
/*     */ 
/*     */   XSCMValidator createDFACM(XSParticleDecl particle) {
/* 129 */     this.fLeafCount = 0;
/* 130 */     this.fParticleCount = 0;
/*     */ 
/* 132 */     CMNode node = useRepeatingLeafNodes(particle) ? buildCompactSyntaxTree(particle) : buildSyntaxTree(particle, true);
/* 133 */     if (node == null) {
/* 134 */       return null;
/*     */     }
/* 136 */     return new XSDFACM(node, this.fLeafCount);
/*     */   }
/*     */ 
/*     */   private CMNode buildSyntaxTree(XSParticleDecl particle, boolean optimize)
/*     */   {
/* 147 */     int maxOccurs = particle.fMaxOccurs;
/* 148 */     int minOccurs = particle.fMinOccurs;
/* 149 */     short type = particle.fType;
/* 150 */     CMNode nodeRet = null;
/*     */ 
/* 152 */     if ((type == 2) || (type == 1))
/*     */     {
/* 160 */       nodeRet = this.fNodeFactory.getCMLeafNode(particle.fType, particle.fValue, this.fParticleCount++, this.fLeafCount++);
/*     */ 
/* 162 */       nodeRet = expandContentModel(nodeRet, minOccurs, maxOccurs, optimize);
/*     */     }
/* 164 */     else if (type == 3)
/*     */     {
/* 166 */       XSModelGroupImpl group = (XSModelGroupImpl)particle.fValue;
/* 167 */       CMNode temp = null;
/*     */ 
/* 178 */       boolean twoChildren = false;
/* 179 */       for (int i = 0; i < group.fParticleCount; i++)
/*     */       {
/* 181 */         temp = buildSyntaxTree(group.fParticles[i], (optimize) && (minOccurs == 1) && (maxOccurs == 1) && ((group.fCompositor == 102) || (group.fParticleCount == 1)));
/*     */ 
/* 187 */         if (temp != null) {
/* 188 */           if (nodeRet == null) {
/* 189 */             nodeRet = temp;
/*     */           }
/*     */           else {
/* 192 */             nodeRet = this.fNodeFactory.getCMBinOpNode(group.fCompositor, nodeRet, temp);
/*     */ 
/* 194 */             twoChildren = true;
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 199 */       if (nodeRet != null)
/*     */       {
/* 204 */         if ((group.fCompositor == 101) && (!twoChildren) && (group.fParticleCount > 1))
/*     */         {
/* 206 */           nodeRet = this.fNodeFactory.getCMUniOpNode(5, nodeRet);
/*     */         }
/* 208 */         nodeRet = expandContentModel(nodeRet, minOccurs, maxOccurs, false);
/*     */       }
/*     */     }
/*     */ 
/* 212 */     return nodeRet;
/*     */   }
/*     */ 
/*     */   private CMNode expandContentModel(CMNode node, int minOccurs, int maxOccurs, boolean optimize)
/*     */   {
/* 221 */     CMNode nodeRet = null;
/*     */ 
/* 223 */     if ((minOccurs == 1) && (maxOccurs == 1)) {
/* 224 */       nodeRet = node;
/*     */     }
/* 226 */     else if ((minOccurs == 0) && (maxOccurs == 1))
/*     */     {
/* 228 */       nodeRet = this.fNodeFactory.getCMUniOpNode(5, node);
/*     */     }
/* 230 */     else if ((minOccurs == 0) && (maxOccurs == -1))
/*     */     {
/* 232 */       nodeRet = this.fNodeFactory.getCMUniOpNode(4, node);
/*     */     }
/* 234 */     else if ((minOccurs == 1) && (maxOccurs == -1))
/*     */     {
/* 236 */       nodeRet = this.fNodeFactory.getCMUniOpNode(6, node);
/*     */     }
/* 238 */     else if (((optimize) && (node.type() == 1)) || (node.type() == 2))
/*     */     {
/* 246 */       nodeRet = this.fNodeFactory.getCMUniOpNode(minOccurs == 0 ? 4 : 6, node);
/*     */ 
/* 249 */       nodeRet.setUserData(new int[] { minOccurs, maxOccurs });
/*     */     }
/* 251 */     else if (maxOccurs == -1)
/*     */     {
/* 256 */       nodeRet = this.fNodeFactory.getCMUniOpNode(6, node);
/*     */ 
/* 261 */       nodeRet = this.fNodeFactory.getCMBinOpNode(102, multiNodes(node, minOccurs - 1, true), nodeRet);
/*     */     }
/*     */     else
/*     */     {
/* 268 */       if (minOccurs > 0) {
/* 269 */         nodeRet = multiNodes(node, minOccurs, false);
/*     */       }
/* 271 */       if (maxOccurs > minOccurs) {
/* 272 */         node = this.fNodeFactory.getCMUniOpNode(5, node);
/* 273 */         if (nodeRet == null) {
/* 274 */           nodeRet = multiNodes(node, maxOccurs - minOccurs, false);
/*     */         }
/*     */         else {
/* 277 */           nodeRet = this.fNodeFactory.getCMBinOpNode(102, nodeRet, multiNodes(node, maxOccurs - minOccurs, true));
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 283 */     return nodeRet;
/*     */   }
/*     */ 
/*     */   private CMNode multiNodes(CMNode node, int num, boolean copyFirst) {
/* 287 */     if (num == 0) {
/* 288 */       return null;
/*     */     }
/* 290 */     if (num == 1) {
/* 291 */       return copyFirst ? copyNode(node) : node;
/*     */     }
/* 293 */     int num1 = num / 2;
/* 294 */     return this.fNodeFactory.getCMBinOpNode(102, multiNodes(node, num1, copyFirst), multiNodes(node, num - num1, true));
/*     */   }
/*     */ 
/*     */   private CMNode copyNode(CMNode node)
/*     */   {
/* 301 */     int type = node.type();
/*     */ 
/* 303 */     if ((type == 101) || (type == 102))
/*     */     {
/* 305 */       XSCMBinOp bin = (XSCMBinOp)node;
/* 306 */       node = this.fNodeFactory.getCMBinOpNode(type, copyNode(bin.getLeft()), copyNode(bin.getRight()));
/*     */     }
/* 310 */     else if ((type == 4) || (type == 6) || (type == 5))
/*     */     {
/* 313 */       XSCMUniOp uni = (XSCMUniOp)node;
/* 314 */       node = this.fNodeFactory.getCMUniOpNode(type, copyNode(uni.getChild()));
/*     */     }
/* 318 */     else if ((type == 1) || (type == 2))
/*     */     {
/* 320 */       XSCMLeaf leaf = (XSCMLeaf)node;
/* 321 */       node = this.fNodeFactory.getCMLeafNode(leaf.type(), leaf.getLeaf(), leaf.getParticleId(), this.fLeafCount++);
/*     */     }
/*     */ 
/* 324 */     return node;
/*     */   }
/*     */ 
/*     */   private CMNode buildCompactSyntaxTree(XSParticleDecl particle)
/*     */   {
/* 332 */     int maxOccurs = particle.fMaxOccurs;
/* 333 */     int minOccurs = particle.fMinOccurs;
/* 334 */     short type = particle.fType;
/* 335 */     CMNode nodeRet = null;
/*     */ 
/* 337 */     if ((type == 2) || (type == 1))
/*     */     {
/* 339 */       return buildCompactSyntaxTree2(particle, minOccurs, maxOccurs);
/*     */     }
/* 341 */     if (type == 3) {
/* 342 */       XSModelGroupImpl group = (XSModelGroupImpl)particle.fValue;
/* 343 */       if ((group.fParticleCount == 1) && ((minOccurs != 1) || (maxOccurs != 1))) {
/* 344 */         return buildCompactSyntaxTree2(group.fParticles[0], minOccurs, maxOccurs);
/*     */       }
/*     */ 
/* 347 */       CMNode temp = null;
/*     */ 
/* 358 */       int count = 0;
/* 359 */       for (int i = 0; i < group.fParticleCount; i++)
/*     */       {
/* 361 */         temp = buildCompactSyntaxTree(group.fParticles[i]);
/*     */ 
/* 363 */         if (temp != null) {
/* 364 */           count++;
/* 365 */           if (nodeRet == null) {
/* 366 */             nodeRet = temp;
/*     */           }
/*     */           else {
/* 369 */             nodeRet = this.fNodeFactory.getCMBinOpNode(group.fCompositor, nodeRet, temp);
/*     */           }
/*     */         }
/*     */       }
/* 373 */       if (nodeRet != null)
/*     */       {
/* 376 */         if ((group.fCompositor == 101) && (count < group.fParticleCount)) {
/* 377 */           nodeRet = this.fNodeFactory.getCMUniOpNode(5, nodeRet);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 382 */     return nodeRet;
/*     */   }
/*     */ 
/*     */   private CMNode buildCompactSyntaxTree2(XSParticleDecl particle, int minOccurs, int maxOccurs)
/*     */   {
/* 387 */     CMNode nodeRet = null;
/* 388 */     if ((minOccurs == 1) && (maxOccurs == 1)) {
/* 389 */       nodeRet = this.fNodeFactory.getCMLeafNode(particle.fType, particle.fValue, this.fParticleCount++, this.fLeafCount++);
/*     */     }
/* 391 */     else if ((minOccurs == 0) && (maxOccurs == 1))
/*     */     {
/* 393 */       nodeRet = this.fNodeFactory.getCMLeafNode(particle.fType, particle.fValue, this.fParticleCount++, this.fLeafCount++);
/* 394 */       nodeRet = this.fNodeFactory.getCMUniOpNode(5, nodeRet);
/*     */     }
/* 396 */     else if ((minOccurs == 0) && (maxOccurs == -1))
/*     */     {
/* 398 */       nodeRet = this.fNodeFactory.getCMLeafNode(particle.fType, particle.fValue, this.fParticleCount++, this.fLeafCount++);
/* 399 */       nodeRet = this.fNodeFactory.getCMUniOpNode(4, nodeRet);
/*     */     }
/* 401 */     else if ((minOccurs == 1) && (maxOccurs == -1))
/*     */     {
/* 403 */       nodeRet = this.fNodeFactory.getCMLeafNode(particle.fType, particle.fValue, this.fParticleCount++, this.fLeafCount++);
/* 404 */       nodeRet = this.fNodeFactory.getCMUniOpNode(6, nodeRet);
/*     */     }
/*     */     else
/*     */     {
/* 409 */       nodeRet = this.fNodeFactory.getCMRepeatingLeafNode(particle.fType, particle.fValue, minOccurs, maxOccurs, this.fParticleCount++, this.fLeafCount++);
/* 410 */       if (minOccurs == 0) {
/* 411 */         nodeRet = this.fNodeFactory.getCMUniOpNode(4, nodeRet);
/*     */       }
/*     */       else {
/* 414 */         nodeRet = this.fNodeFactory.getCMUniOpNode(6, nodeRet);
/*     */       }
/*     */     }
/* 417 */     return nodeRet;
/*     */   }
/*     */ 
/*     */   private boolean useRepeatingLeafNodes(XSParticleDecl particle)
/*     */   {
/* 425 */     int maxOccurs = particle.fMaxOccurs;
/* 426 */     int minOccurs = particle.fMinOccurs;
/* 427 */     short type = particle.fType;
/*     */ 
/* 429 */     if (type == 3) {
/* 430 */       XSModelGroupImpl group = (XSModelGroupImpl)particle.fValue;
/* 431 */       if ((minOccurs != 1) || (maxOccurs != 1)) {
/* 432 */         if (group.fParticleCount == 1) {
/* 433 */           XSParticleDecl particle2 = group.fParticles[0];
/* 434 */           short type2 = particle2.fType;
/* 435 */           return ((type2 == 1) || (type2 == 2)) && (particle2.fMinOccurs == 1) && (particle2.fMaxOccurs == 1);
/*     */         }
/*     */ 
/* 440 */         return group.fParticleCount == 0;
/*     */       }
/* 442 */       for (int i = 0; i < group.fParticleCount; i++) {
/* 443 */         if (!useRepeatingLeafNodes(group.fParticles[i])) {
/* 444 */           return false;
/*     */         }
/*     */       }
/*     */     }
/* 448 */     return true;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.models.CMBuilder
 * JD-Core Version:    0.6.2
 */