/*     */ package com.sun.org.apache.xerces.internal.impl.xs;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.ValidatedInfo;
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.util.XSObjectListImpl;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSAnnotation;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSAttributeGroupDefinition;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSAttributeUse;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSNamespaceItem;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSObjectList;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSWildcard;
/*     */ 
/*     */ public class XSAttributeGroupDecl
/*     */   implements XSAttributeGroupDefinition
/*     */ {
/*  47 */   public String fName = null;
/*     */ 
/*  49 */   public String fTargetNamespace = null;
/*     */ 
/*  51 */   int fAttrUseNum = 0;
/*     */   private static final int INITIAL_SIZE = 5;
/*  54 */   XSAttributeUseImpl[] fAttributeUses = new XSAttributeUseImpl[5];
/*     */ 
/*  56 */   public XSWildcardDecl fAttributeWC = null;
/*     */ 
/*  58 */   public String fIDAttrName = null;
/*     */   public XSObjectList fAnnotations;
/*  63 */   protected XSObjectListImpl fAttrUses = null;
/*     */ 
/*  67 */   private XSNamespaceItem fNamespaceItem = null;
/*     */ 
/*     */   public String addAttributeUse(XSAttributeUseImpl attrUse)
/*     */   {
/*  77 */     if ((attrUse.fUse != 2) && 
/*  78 */       (attrUse.fAttrDecl.fType.isIDType()))
/*     */     {
/*  82 */       if (this.fIDAttrName == null)
/*  83 */         this.fIDAttrName = attrUse.fAttrDecl.fName;
/*     */       else {
/*  85 */         return this.fIDAttrName;
/*     */       }
/*     */     }
/*     */ 
/*  89 */     if (this.fAttrUseNum == this.fAttributeUses.length) {
/*  90 */       this.fAttributeUses = resize(this.fAttributeUses, this.fAttrUseNum * 2);
/*     */     }
/*  92 */     this.fAttributeUses[(this.fAttrUseNum++)] = attrUse;
/*     */ 
/*  94 */     return null;
/*     */   }
/*     */ 
/*     */   public void replaceAttributeUse(XSAttributeUse oldUse, XSAttributeUseImpl newUse) {
/*  98 */     for (int i = 0; i < this.fAttrUseNum; i++)
/*  99 */       if (this.fAttributeUses[i] == oldUse)
/* 100 */         this.fAttributeUses[i] = newUse;
/*     */   }
/*     */ 
/*     */   public XSAttributeUse getAttributeUse(String namespace, String name)
/*     */   {
/* 106 */     for (int i = 0; i < this.fAttrUseNum; i++) {
/* 107 */       if ((this.fAttributeUses[i].fAttrDecl.fTargetNamespace == namespace) && (this.fAttributeUses[i].fAttrDecl.fName == name))
/*     */       {
/* 109 */         return this.fAttributeUses[i];
/*     */       }
/*     */     }
/* 112 */     return null;
/*     */   }
/*     */ 
/*     */   public XSAttributeUse getAttributeUseNoProhibited(String namespace, String name) {
/* 116 */     for (int i = 0; i < this.fAttrUseNum; i++) {
/* 117 */       if ((this.fAttributeUses[i].fAttrDecl.fTargetNamespace == namespace) && (this.fAttributeUses[i].fAttrDecl.fName == name) && (this.fAttributeUses[i].fUse != 2))
/*     */       {
/* 120 */         return this.fAttributeUses[i];
/*     */       }
/*     */     }
/* 123 */     return null;
/*     */   }
/*     */ 
/*     */   public void removeProhibitedAttrs() {
/* 127 */     if (this.fAttrUseNum == 0) return;
/*     */ 
/* 129 */     int count = 0;
/* 130 */     XSAttributeUseImpl[] uses = new XSAttributeUseImpl[this.fAttrUseNum];
/* 131 */     for (int i = 0; i < this.fAttrUseNum; i++) {
/* 132 */       if (this.fAttributeUses[i].fUse != 2) {
/* 133 */         uses[(count++)] = this.fAttributeUses[i];
/*     */       }
/*     */     }
/* 136 */     this.fAttributeUses = uses;
/* 137 */     this.fAttrUseNum = count;
/*     */   }
/*     */ 
/*     */   public Object[] validRestrictionOf(String typeName, XSAttributeGroupDecl baseGroup)
/*     */   {
/* 174 */     Object[] errorArgs = null;
/* 175 */     XSAttributeUseImpl attrUse = null;
/* 176 */     XSAttributeDecl attrDecl = null;
/* 177 */     XSAttributeUseImpl baseAttrUse = null;
/* 178 */     XSAttributeDecl baseAttrDecl = null;
/*     */ 
/* 180 */     for (int i = 0; i < this.fAttrUseNum; i++)
/*     */     {
/* 182 */       attrUse = this.fAttributeUses[i];
/* 183 */       attrDecl = attrUse.fAttrDecl;
/*     */ 
/* 186 */       baseAttrUse = (XSAttributeUseImpl)baseGroup.getAttributeUse(attrDecl.fTargetNamespace, attrDecl.fName);
/* 187 */       if (baseAttrUse != null)
/*     */       {
/* 192 */         if ((baseAttrUse.getRequired()) && (!attrUse.getRequired())) {
/* 193 */           errorArgs = new Object[] { typeName, attrDecl.fName, attrUse.fUse == 0 ? "optional" : "prohibited", "derivation-ok-restriction.2.1.1" };
/*     */ 
/* 196 */           return errorArgs;
/*     */         }
/*     */ 
/* 201 */         if (attrUse.fUse != 2)
/*     */         {
/* 205 */           baseAttrDecl = baseAttrUse.fAttrDecl;
/*     */ 
/* 209 */           if (!XSConstraints.checkSimpleDerivationOk(attrDecl.fType, baseAttrDecl.fType, baseAttrDecl.fType.getFinal()))
/*     */           {
/* 212 */             errorArgs = new Object[] { typeName, attrDecl.fName, attrDecl.fType.getName(), baseAttrDecl.fType.getName(), "derivation-ok-restriction.2.1.2" };
/*     */ 
/* 214 */             return errorArgs;
/*     */           }
/*     */ 
/* 221 */           int baseConsType = baseAttrUse.fConstraintType != 0 ? baseAttrUse.fConstraintType : baseAttrDecl.getConstraintType();
/*     */ 
/* 223 */           int thisConstType = attrUse.fConstraintType != 0 ? attrUse.fConstraintType : attrDecl.getConstraintType();
/*     */ 
/* 226 */           if (baseConsType == 2)
/*     */           {
/* 228 */             if (thisConstType != 2) {
/* 229 */               errorArgs = new Object[] { typeName, attrDecl.fName, "derivation-ok-restriction.2.1.3.a" };
/*     */ 
/* 231 */               return errorArgs;
/*     */             }
/*     */ 
/* 234 */             ValidatedInfo baseFixedValue = baseAttrUse.fDefault != null ? baseAttrUse.fDefault : baseAttrDecl.fDefault;
/*     */ 
/* 236 */             ValidatedInfo thisFixedValue = attrUse.fDefault != null ? attrUse.fDefault : attrDecl.fDefault;
/*     */ 
/* 238 */             if (!baseFixedValue.actualValue.equals(thisFixedValue.actualValue)) {
/* 239 */               errorArgs = new Object[] { typeName, attrDecl.fName, thisFixedValue.stringValue(), baseFixedValue.stringValue(), "derivation-ok-restriction.2.1.3.b" };
/*     */ 
/* 241 */               return errorArgs;
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 253 */         if (baseGroup.fAttributeWC == null) {
/* 254 */           errorArgs = new Object[] { typeName, attrDecl.fName, "derivation-ok-restriction.2.2.a" };
/*     */ 
/* 256 */           return errorArgs;
/*     */         }
/* 258 */         if (!baseGroup.fAttributeWC.allowNamespace(attrDecl.fTargetNamespace)) {
/* 259 */           errorArgs = new Object[] { typeName, attrDecl.fName, attrDecl.fTargetNamespace == null ? "" : attrDecl.fTargetNamespace, "derivation-ok-restriction.2.2.b" };
/*     */ 
/* 262 */           return errorArgs;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 272 */     for (int i = 0; i < baseGroup.fAttrUseNum; i++)
/*     */     {
/* 274 */       baseAttrUse = baseGroup.fAttributeUses[i];
/*     */ 
/* 276 */       if (baseAttrUse.fUse == 1)
/*     */       {
/* 278 */         baseAttrDecl = baseAttrUse.fAttrDecl;
/*     */ 
/* 280 */         if (getAttributeUse(baseAttrDecl.fTargetNamespace, baseAttrDecl.fName) == null) {
/* 281 */           errorArgs = new Object[] { typeName, baseAttrUse.fAttrDecl.fName, "derivation-ok-restriction.3" };
/*     */ 
/* 283 */           return errorArgs;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 293 */     if (this.fAttributeWC != null) {
/* 294 */       if (baseGroup.fAttributeWC == null) {
/* 295 */         errorArgs = new Object[] { typeName, "derivation-ok-restriction.4.1" };
/* 296 */         return errorArgs;
/*     */       }
/* 298 */       if (!this.fAttributeWC.isSubsetOf(baseGroup.fAttributeWC)) {
/* 299 */         errorArgs = new Object[] { typeName, "derivation-ok-restriction.4.2" };
/* 300 */         return errorArgs;
/*     */       }
/* 302 */       if (this.fAttributeWC.weakerProcessContents(baseGroup.fAttributeWC)) {
/* 303 */         errorArgs = new Object[] { typeName, this.fAttributeWC.getProcessContentsAsString(), baseGroup.fAttributeWC.getProcessContentsAsString(), "derivation-ok-restriction.4.3" };
/*     */ 
/* 307 */         return errorArgs;
/*     */       }
/*     */     }
/*     */ 
/* 311 */     return null;
/*     */   }
/*     */ 
/*     */   static final XSAttributeUseImpl[] resize(XSAttributeUseImpl[] oldArray, int newSize)
/*     */   {
/* 316 */     XSAttributeUseImpl[] newArray = new XSAttributeUseImpl[newSize];
/* 317 */     System.arraycopy(oldArray, 0, newArray, 0, Math.min(oldArray.length, newSize));
/* 318 */     return newArray;
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/* 323 */     this.fName = null;
/* 324 */     this.fTargetNamespace = null;
/*     */ 
/* 326 */     for (int i = 0; i < this.fAttrUseNum; i++) {
/* 327 */       this.fAttributeUses[i] = null;
/*     */     }
/* 329 */     this.fAttrUseNum = 0;
/* 330 */     this.fAttributeWC = null;
/* 331 */     this.fAnnotations = null;
/* 332 */     this.fIDAttrName = null;
/*     */   }
/*     */ 
/*     */   public short getType()
/*     */   {
/* 340 */     return 5;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 348 */     return this.fName;
/*     */   }
/*     */ 
/*     */   public String getNamespace()
/*     */   {
/* 357 */     return this.fTargetNamespace;
/*     */   }
/*     */ 
/*     */   public XSObjectList getAttributeUses()
/*     */   {
/* 364 */     if (this.fAttrUses == null) {
/* 365 */       this.fAttrUses = new XSObjectListImpl(this.fAttributeUses, this.fAttrUseNum);
/*     */     }
/* 367 */     return this.fAttrUses;
/*     */   }
/*     */ 
/*     */   public XSWildcard getAttributeWildcard()
/*     */   {
/* 374 */     return this.fAttributeWC;
/*     */   }
/*     */ 
/*     */   public XSAnnotation getAnnotation()
/*     */   {
/* 381 */     return this.fAnnotations != null ? (XSAnnotation)this.fAnnotations.item(0) : null;
/*     */   }
/*     */ 
/*     */   public XSObjectList getAnnotations()
/*     */   {
/* 388 */     return this.fAnnotations != null ? this.fAnnotations : XSObjectListImpl.EMPTY_LIST;
/*     */   }
/*     */ 
/*     */   public XSNamespaceItem getNamespaceItem()
/*     */   {
/* 395 */     return this.fNamespaceItem;
/*     */   }
/*     */ 
/*     */   void setNamespaceItem(XSNamespaceItem namespaceItem) {
/* 399 */     this.fNamespaceItem = namespaceItem;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.XSAttributeGroupDecl
 * JD-Core Version:    0.6.2
 */