/*     */ package com.sun.org.apache.xerces.internal.xpointer;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType;
/*     */ import com.sun.org.apache.xerces.internal.util.SymbolTable;
/*     */ import com.sun.org.apache.xerces.internal.xni.Augmentations;
/*     */ import com.sun.org.apache.xerces.internal.xni.QName;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLAttributes;
/*     */ import com.sun.org.apache.xerces.internal.xni.XNIException;
/*     */ import com.sun.org.apache.xerces.internal.xs.AttributePSVI;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;
/*     */ 
/*     */ class ShortHandPointer
/*     */   implements XPointerPart
/*     */ {
/*     */   private String fShortHandPointer;
/*  48 */   private boolean fIsFragmentResolved = false;
/*     */   private SymbolTable fSymbolTable;
/*  81 */   int fMatchingChildCount = 0;
/*     */ 
/*     */   public ShortHandPointer()
/*     */   {
/*     */   }
/*     */ 
/*     */   public ShortHandPointer(SymbolTable symbolTable)
/*     */   {
/*  60 */     this.fSymbolTable = symbolTable;
/*     */   }
/*     */ 
/*     */   public void parseXPointer(String part)
/*     */     throws XNIException
/*     */   {
/*  69 */     this.fShortHandPointer = part;
/*     */ 
/*  71 */     this.fIsFragmentResolved = false;
/*     */   }
/*     */ 
/*     */   public boolean resolveXPointer(QName element, XMLAttributes attributes, Augmentations augs, int event)
/*     */     throws XNIException
/*     */   {
/*  86 */     if (this.fMatchingChildCount == 0) {
/*  87 */       this.fIsFragmentResolved = false;
/*     */     }
/*     */ 
/*  92 */     if (event == 0) {
/*  93 */       if (this.fMatchingChildCount == 0) {
/*  94 */         this.fIsFragmentResolved = hasMatchingIdentifier(element, attributes, augs, event);
/*     */       }
/*     */ 
/*  97 */       if (this.fIsFragmentResolved)
/*  98 */         this.fMatchingChildCount += 1;
/*     */     }
/* 100 */     else if (event == 2) {
/* 101 */       if (this.fMatchingChildCount == 0) {
/* 102 */         this.fIsFragmentResolved = hasMatchingIdentifier(element, attributes, augs, event);
/*     */       }
/*     */ 
/*     */     }
/* 109 */     else if (this.fIsFragmentResolved) {
/* 110 */       this.fMatchingChildCount -= 1;
/*     */     }
/*     */ 
/* 114 */     return this.fIsFragmentResolved;
/*     */   }
/*     */ 
/*     */   private boolean hasMatchingIdentifier(QName element, XMLAttributes attributes, Augmentations augs, int event)
/*     */     throws XNIException
/*     */   {
/* 129 */     String normalizedValue = null;
/*     */ 
/* 134 */     if (attributes != null) {
/* 135 */       for (int i = 0; i < attributes.getLength(); i++)
/*     */       {
/* 141 */         normalizedValue = getSchemaDeterminedID(attributes, i);
/* 142 */         if (normalizedValue != null)
/*     */         {
/*     */           break;
/*     */         }
/*     */ 
/* 150 */         normalizedValue = getChildrenSchemaDeterminedID(attributes, i);
/* 151 */         if (normalizedValue != null)
/*     */         {
/*     */           break;
/*     */         }
/*     */ 
/* 160 */         normalizedValue = getDTDDeterminedID(attributes, i);
/* 161 */         if (normalizedValue != null)
/*     */         {
/*     */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 168 */     if ((normalizedValue != null) && (normalizedValue.equals(this.fShortHandPointer)))
/*     */     {
/* 170 */       return true;
/*     */     }
/*     */ 
/* 173 */     return false;
/*     */   }
/*     */ 
/*     */   public String getDTDDeterminedID(XMLAttributes attributes, int index)
/*     */     throws XNIException
/*     */   {
/* 187 */     if (attributes.getType(index).equals("ID")) {
/* 188 */       return attributes.getValue(index);
/*     */     }
/* 190 */     return null;
/*     */   }
/*     */ 
/*     */   public String getSchemaDeterminedID(XMLAttributes attributes, int index)
/*     */     throws XNIException
/*     */   {
/* 204 */     Augmentations augs = attributes.getAugmentations(index);
/* 205 */     AttributePSVI attrPSVI = (AttributePSVI)augs.getItem("ATTRIBUTE_PSVI");
/*     */ 
/* 208 */     if (attrPSVI != null)
/*     */     {
/* 222 */       XSTypeDefinition typeDef = attrPSVI.getMemberTypeDefinition();
/* 223 */       if (typeDef != null) {
/* 224 */         typeDef = attrPSVI.getTypeDefinition();
/*     */       }
/*     */ 
/* 228 */       if ((typeDef != null) && (((XSSimpleType)typeDef).isIDType())) {
/* 229 */         return attrPSVI.getSchemaNormalizedValue();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 235 */     return null;
/*     */   }
/*     */ 
/*     */   public String getChildrenSchemaDeterminedID(XMLAttributes attributes, int index)
/*     */     throws XNIException
/*     */   {
/* 248 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean isFragmentResolved()
/*     */   {
/* 256 */     return this.fIsFragmentResolved;
/*     */   }
/*     */ 
/*     */   public boolean isChildFragmentResolved()
/*     */   {
/* 264 */     return this.fIsFragmentResolved & this.fMatchingChildCount > 0;
/*     */   }
/*     */ 
/*     */   public String getSchemeName()
/*     */   {
/* 273 */     return this.fShortHandPointer;
/*     */   }
/*     */ 
/*     */   public String getSchemeData()
/*     */   {
/* 280 */     return null;
/*     */   }
/*     */ 
/*     */   public void setSchemeName(String schemeName)
/*     */   {
/* 287 */     this.fShortHandPointer = schemeName;
/*     */   }
/*     */ 
/*     */   public void setSchemeData(String schemeData)
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.xpointer.ShortHandPointer
 * JD-Core Version:    0.6.2
 */