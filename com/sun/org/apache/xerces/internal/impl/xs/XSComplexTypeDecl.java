/*     */ package com.sun.org.apache.xerces.internal.impl.xs;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType;
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.xs.XSSimpleTypeDecl;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.models.CMBuilder;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.models.XSCMValidator;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.util.XSObjectListImpl;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSAttributeUse;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSComplexTypeDefinition;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSNamespaceItem;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSObjectList;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSParticle;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSSimpleTypeDefinition;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSWildcard;
/*     */ import org.w3c.dom.TypeInfo;
/*     */ 
/*     */ public class XSComplexTypeDecl
/*     */   implements XSComplexTypeDefinition, TypeInfo
/*     */ {
/*  44 */   String fName = null;
/*     */ 
/*  47 */   String fTargetNamespace = null;
/*     */ 
/*  50 */   XSTypeDefinition fBaseType = null;
/*     */ 
/*  53 */   short fDerivedBy = 2;
/*     */ 
/*  56 */   short fFinal = 0;
/*     */ 
/*  59 */   short fBlock = 0;
/*     */ 
/*  63 */   short fMiscFlags = 0;
/*     */ 
/*  66 */   XSAttributeGroupDecl fAttrGrp = null;
/*     */ 
/*  69 */   short fContentType = 0;
/*     */ 
/*  72 */   XSSimpleType fXSSimpleType = null;
/*     */ 
/*  75 */   XSParticleDecl fParticle = null;
/*     */ 
/*  78 */   volatile XSCMValidator fCMValidator = null;
/*     */ 
/*  81 */   XSCMValidator fUPACMValidator = null;
/*     */ 
/*  84 */   XSObjectListImpl fAnnotations = null;
/*     */ 
/*  88 */   private XSNamespaceItem fNamespaceItem = null;
/*     */   static final int DERIVATION_ANY = 0;
/*     */   static final int DERIVATION_RESTRICTION = 1;
/*     */   static final int DERIVATION_EXTENSION = 2;
/*     */   static final int DERIVATION_UNION = 4;
/*     */   static final int DERIVATION_LIST = 8;
/*     */   private static final short CT_IS_ABSTRACT = 1;
/*     */   private static final short CT_HAS_TYPE_ID = 2;
/*     */   private static final short CT_IS_ANONYMOUS = 4;
/*     */ 
/*     */   public void setValues(String name, String targetNamespace, XSTypeDefinition baseType, short derivedBy, short schemaFinal, short block, short contentType, boolean isAbstract, XSAttributeGroupDecl attrGrp, XSSimpleType simpleType, XSParticleDecl particle, XSObjectListImpl annotations)
/*     */   {
/* 107 */     this.fTargetNamespace = targetNamespace;
/* 108 */     this.fBaseType = baseType;
/* 109 */     this.fDerivedBy = derivedBy;
/* 110 */     this.fFinal = schemaFinal;
/* 111 */     this.fBlock = block;
/* 112 */     this.fContentType = contentType;
/* 113 */     if (isAbstract)
/* 114 */       this.fMiscFlags = ((short)(this.fMiscFlags | 0x1));
/* 115 */     this.fAttrGrp = attrGrp;
/* 116 */     this.fXSSimpleType = simpleType;
/* 117 */     this.fParticle = particle;
/* 118 */     this.fAnnotations = annotations;
/*     */   }
/*     */ 
/*     */   public void setName(String name) {
/* 122 */     this.fName = name;
/*     */   }
/*     */ 
/*     */   public short getTypeCategory() {
/* 126 */     return 15;
/*     */   }
/*     */ 
/*     */   public String getTypeName() {
/* 130 */     return this.fName;
/*     */   }
/*     */ 
/*     */   public short getFinalSet() {
/* 134 */     return this.fFinal;
/*     */   }
/*     */ 
/*     */   public String getTargetNamespace() {
/* 138 */     return this.fTargetNamespace;
/*     */   }
/*     */ 
/*     */   public boolean containsTypeID()
/*     */   {
/* 149 */     return (this.fMiscFlags & 0x2) != 0;
/*     */   }
/*     */ 
/*     */   public void setIsAbstractType() {
/* 153 */     this.fMiscFlags = ((short)(this.fMiscFlags | 0x1));
/*     */   }
/*     */   public void setContainsTypeID() {
/* 156 */     this.fMiscFlags = ((short)(this.fMiscFlags | 0x2));
/*     */   }
/*     */   public void setIsAnonymous() {
/* 159 */     this.fMiscFlags = ((short)(this.fMiscFlags | 0x4));
/*     */   }
/*     */ 
/*     */   public XSCMValidator getContentModel(CMBuilder cmBuilder)
/*     */   {
/* 165 */     if ((this.fContentType == 1) || (this.fContentType == 0))
/*     */     {
/* 167 */       return null;
/*     */     }
/* 169 */     if (this.fCMValidator == null) {
/* 170 */       synchronized (this) {
/* 171 */         if (this.fCMValidator == null)
/* 172 */           this.fCMValidator = cmBuilder.getContentModel(this);
/*     */       }
/*     */     }
/* 175 */     return this.fCMValidator;
/*     */   }
/*     */ 
/*     */   public XSAttributeGroupDecl getAttrGrp()
/*     */   {
/* 182 */     return this.fAttrGrp;
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 186 */     StringBuilder str = new StringBuilder(192);
/* 187 */     appendTypeInfo(str);
/* 188 */     return str.toString();
/*     */   }
/*     */ 
/*     */   void appendTypeInfo(StringBuilder str) {
/* 192 */     String[] contentType = { "EMPTY", "SIMPLE", "ELEMENT", "MIXED" };
/* 193 */     String[] derivedBy = { "EMPTY", "EXTENSION", "RESTRICTION" };
/*     */ 
/* 195 */     str.append("Complex type name='").append(this.fTargetNamespace).append(',').append(getTypeName()).append("', ");
/* 196 */     if (this.fBaseType != null) {
/* 197 */       str.append(" base type name='").append(this.fBaseType.getName()).append("', ");
/*     */     }
/* 199 */     str.append(" content type='").append(contentType[this.fContentType]).append("', ");
/* 200 */     str.append(" isAbstract='").append(getAbstract()).append("', ");
/* 201 */     str.append(" hasTypeId='").append(containsTypeID()).append("', ");
/* 202 */     str.append(" final='").append(this.fFinal).append("', ");
/* 203 */     str.append(" block='").append(this.fBlock).append("', ");
/* 204 */     if (this.fParticle != null) {
/* 205 */       str.append(" particle='").append(this.fParticle.toString()).append("', ");
/*     */     }
/* 207 */     str.append(" derivedBy='").append(derivedBy[this.fDerivedBy]).append("'. ");
/*     */   }
/*     */ 
/*     */   public boolean derivedFromType(XSTypeDefinition ancestor, short derivationMethod)
/*     */   {
/* 213 */     if (ancestor == null) {
/* 214 */       return false;
/*     */     }
/* 216 */     if (ancestor == SchemaGrammar.fAnyType) {
/* 217 */       return true;
/*     */     }
/* 219 */     XSTypeDefinition type = this;
/*     */ 
/* 221 */     while ((type != ancestor) && (type != SchemaGrammar.fAnySimpleType) && (type != SchemaGrammar.fAnyType))
/*     */     {
/* 223 */       type = type.getBaseType();
/*     */     }
/*     */ 
/* 226 */     return type == ancestor;
/*     */   }
/*     */ 
/*     */   public boolean derivedFrom(String ancestorNS, String ancestorName, short derivationMethod)
/*     */   {
/* 231 */     if (ancestorName == null) {
/* 232 */       return false;
/*     */     }
/* 234 */     if ((ancestorNS != null) && (ancestorNS.equals(SchemaSymbols.URI_SCHEMAFORSCHEMA)) && (ancestorName.equals("anyType")))
/*     */     {
/* 237 */       return true;
/*     */     }
/*     */ 
/* 241 */     XSTypeDefinition type = this;
/*     */ 
/* 245 */     while (((!ancestorName.equals(type.getName())) || (((ancestorNS != null) || (type.getNamespace() != null)) && ((ancestorNS == null) || (!ancestorNS.equals(type.getNamespace()))))) && (type != SchemaGrammar.fAnySimpleType) && (type != SchemaGrammar.fAnyType))
/*     */     {
/* 247 */       type = type.getBaseType();
/*     */     }
/*     */ 
/* 250 */     return (type != SchemaGrammar.fAnySimpleType) && (type != SchemaGrammar.fAnyType);
/*     */   }
/*     */ 
/*     */   public boolean isDOMDerivedFrom(String ancestorNS, String ancestorName, int derivationMethod)
/*     */   {
/* 272 */     if (ancestorName == null) {
/* 273 */       return false;
/*     */     }
/*     */ 
/* 276 */     if ((ancestorNS != null) && (ancestorNS.equals(SchemaSymbols.URI_SCHEMAFORSCHEMA)) && (ancestorName.equals("anyType")) && (derivationMethod == 1) && (derivationMethod == 2))
/*     */     {
/* 281 */       return true;
/*     */     }
/*     */ 
/* 285 */     if (((derivationMethod & 0x1) != 0) && 
/* 286 */       (isDerivedByRestriction(ancestorNS, ancestorName, derivationMethod, this)))
/*     */     {
/* 288 */       return true;
/*     */     }
/*     */ 
/* 293 */     if (((derivationMethod & 0x2) != 0) && 
/* 294 */       (isDerivedByExtension(ancestorNS, ancestorName, derivationMethod, this)))
/*     */     {
/* 296 */       return true;
/*     */     }
/*     */ 
/* 301 */     if ((((derivationMethod & 0x8) != 0) || ((derivationMethod & 0x4) != 0)) && ((derivationMethod & 0x1) == 0) && ((derivationMethod & 0x2) == 0))
/*     */     {
/* 305 */       if ((ancestorNS.equals(SchemaSymbols.URI_SCHEMAFORSCHEMA)) && (ancestorName.equals("anyType")))
/*     */       {
/* 307 */         ancestorName = "anySimpleType";
/*     */       }
/*     */ 
/* 310 */       if ((!this.fName.equals("anyType")) || (!this.fTargetNamespace.equals(SchemaSymbols.URI_SCHEMAFORSCHEMA)))
/*     */       {
/* 312 */         if ((this.fBaseType != null) && ((this.fBaseType instanceof XSSimpleTypeDecl)))
/*     */         {
/* 314 */           return ((XSSimpleTypeDecl)this.fBaseType).isDOMDerivedFrom(ancestorNS, ancestorName, derivationMethod);
/*     */         }
/* 316 */         if ((this.fBaseType != null) && ((this.fBaseType instanceof XSComplexTypeDecl)))
/*     */         {
/* 318 */           return ((XSComplexTypeDecl)this.fBaseType).isDOMDerivedFrom(ancestorNS, ancestorName, derivationMethod);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 327 */     if (((derivationMethod & 0x2) == 0) && ((derivationMethod & 0x1) == 0) && ((derivationMethod & 0x8) == 0) && ((derivationMethod & 0x4) == 0))
/*     */     {
/* 331 */       return isDerivedByAny(ancestorNS, ancestorName, derivationMethod, this);
/*     */     }
/*     */ 
/* 334 */     return false;
/*     */   }
/*     */ 
/*     */   private boolean isDerivedByAny(String ancestorNS, String ancestorName, int derivationMethod, XSTypeDefinition type)
/*     */   {
/* 356 */     XSTypeDefinition oldType = null;
/* 357 */     boolean derivedFrom = false;
/* 358 */     while ((type != null) && (type != oldType))
/*     */     {
/* 361 */       if ((ancestorName.equals(type.getName())) && (((ancestorNS == null) && (type.getNamespace() == null)) || ((ancestorNS != null) && (ancestorNS.equals(type.getNamespace())))))
/*     */       {
/* 364 */         derivedFrom = true;
/* 365 */         break;
/*     */       }
/*     */ 
/* 370 */       if (isDerivedByRestriction(ancestorNS, ancestorName, derivationMethod, type))
/*     */       {
/* 372 */         return true;
/* 373 */       }if (!isDerivedByExtension(ancestorNS, ancestorName, derivationMethod, type))
/*     */       {
/* 375 */         return true;
/*     */       }
/* 377 */       oldType = type;
/* 378 */       type = type.getBaseType();
/*     */     }
/*     */ 
/* 381 */     return derivedFrom;
/*     */   }
/*     */ 
/*     */   private boolean isDerivedByRestriction(String ancestorNS, String ancestorName, int derivationMethod, XSTypeDefinition type)
/*     */   {
/* 403 */     XSTypeDefinition oldType = null;
/* 404 */     while ((type != null) && (type != oldType))
/*     */     {
/* 407 */       if ((ancestorNS != null) && (ancestorNS.equals(SchemaSymbols.URI_SCHEMAFORSCHEMA)) && (ancestorName.equals("anySimpleType")))
/*     */       {
/* 410 */         return false;
/*     */       }
/*     */ 
/* 415 */       if (((ancestorName.equals(type.getName())) && (ancestorNS != null) && (ancestorNS.equals(type.getNamespace()))) || ((type.getNamespace() == null) && (ancestorNS == null)))
/*     */       {
/* 419 */         return true;
/*     */       }
/*     */ 
/* 423 */       if ((type instanceof XSSimpleTypeDecl)) {
/* 424 */         if ((ancestorNS.equals(SchemaSymbols.URI_SCHEMAFORSCHEMA)) && (ancestorName.equals("anyType")))
/*     */         {
/* 426 */           ancestorName = "anySimpleType";
/*     */         }
/* 428 */         return ((XSSimpleTypeDecl)type).isDOMDerivedFrom(ancestorNS, ancestorName, derivationMethod);
/*     */       }
/*     */ 
/* 434 */       if (((XSComplexTypeDecl)type).getDerivationMethod() != 2) {
/* 435 */         return false;
/*     */       }
/*     */ 
/* 438 */       oldType = type;
/* 439 */       type = type.getBaseType();
/*     */     }
/*     */ 
/* 443 */     return false;
/*     */   }
/*     */ 
/*     */   private boolean isDerivedByExtension(String ancestorNS, String ancestorName, int derivationMethod, XSTypeDefinition type)
/*     */   {
/* 465 */     boolean extension = false;
/* 466 */     XSTypeDefinition oldType = null;
/* 467 */     while ((type != null) && (type != oldType))
/*     */     {
/* 469 */       if ((ancestorNS != null) && (ancestorNS.equals(SchemaSymbols.URI_SCHEMAFORSCHEMA)) && (ancestorName.equals("anySimpleType")) && (SchemaSymbols.URI_SCHEMAFORSCHEMA.equals(type.getNamespace())) && ("anyType".equals(type.getName())))
/*     */       {
/*     */         break;
/*     */       }
/*     */ 
/* 477 */       if ((ancestorName.equals(type.getName())) && (((ancestorNS == null) && (type.getNamespace() == null)) || ((ancestorNS != null) && (ancestorNS.equals(type.getNamespace())))))
/*     */       {
/* 481 */         return extension;
/*     */       }
/*     */ 
/* 485 */       if ((type instanceof XSSimpleTypeDecl)) {
/* 486 */         if ((ancestorNS.equals(SchemaSymbols.URI_SCHEMAFORSCHEMA)) && (ancestorName.equals("anyType")))
/*     */         {
/* 488 */           ancestorName = "anySimpleType";
/*     */         }
/*     */ 
/* 494 */         if ((derivationMethod & 0x2) != 0) {
/* 495 */           return extension & ((XSSimpleTypeDecl)type).isDOMDerivedFrom(ancestorNS, ancestorName, derivationMethod & 0x1);
/*     */         }
/*     */ 
/* 500 */         return extension & ((XSSimpleTypeDecl)type).isDOMDerivedFrom(ancestorNS, ancestorName, derivationMethod);
/*     */       }
/*     */ 
/* 509 */       if (((XSComplexTypeDecl)type).getDerivationMethod() == 1) {
/* 510 */         extension |= true;
/*     */       }
/*     */ 
/* 513 */       oldType = type;
/* 514 */       type = type.getBaseType();
/*     */     }
/*     */ 
/* 517 */     return false;
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/* 523 */     this.fName = null;
/* 524 */     this.fTargetNamespace = null;
/* 525 */     this.fBaseType = null;
/* 526 */     this.fDerivedBy = 2;
/* 527 */     this.fFinal = 0;
/* 528 */     this.fBlock = 0;
/*     */ 
/* 530 */     this.fMiscFlags = 0;
/*     */ 
/* 533 */     this.fAttrGrp.reset();
/* 534 */     this.fContentType = 0;
/* 535 */     this.fXSSimpleType = null;
/* 536 */     this.fParticle = null;
/* 537 */     this.fCMValidator = null;
/* 538 */     this.fUPACMValidator = null;
/* 539 */     if (this.fAnnotations != null)
/*     */     {
/* 541 */       this.fAnnotations.clearXSObjectList();
/*     */     }
/* 543 */     this.fAnnotations = null;
/*     */   }
/*     */ 
/*     */   public short getType()
/*     */   {
/* 550 */     return 3;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 558 */     return getAnonymous() ? null : this.fName;
/*     */   }
/*     */ 
/*     */   public boolean getAnonymous()
/*     */   {
/* 567 */     return (this.fMiscFlags & 0x4) != 0;
/*     */   }
/*     */ 
/*     */   public String getNamespace()
/*     */   {
/* 576 */     return this.fTargetNamespace;
/*     */   }
/*     */ 
/*     */   public XSTypeDefinition getBaseType()
/*     */   {
/* 584 */     return this.fBaseType;
/*     */   }
/*     */ 
/*     */   public short getDerivationMethod()
/*     */   {
/* 592 */     return this.fDerivedBy;
/*     */   }
/*     */ 
/*     */   public boolean isFinal(short derivation)
/*     */   {
/* 604 */     return (this.fFinal & derivation) != 0;
/*     */   }
/*     */ 
/*     */   public short getFinal()
/*     */   {
/* 615 */     return this.fFinal;
/*     */   }
/*     */ 
/*     */   public boolean getAbstract()
/*     */   {
/* 624 */     return (this.fMiscFlags & 0x1) != 0;
/*     */   }
/*     */ 
/*     */   public XSObjectList getAttributeUses()
/*     */   {
/* 631 */     return this.fAttrGrp.getAttributeUses();
/*     */   }
/*     */ 
/*     */   public XSWildcard getAttributeWildcard()
/*     */   {
/* 638 */     return this.fAttrGrp.getAttributeWildcard();
/*     */   }
/*     */ 
/*     */   public short getContentType()
/*     */   {
/* 647 */     return this.fContentType;
/*     */   }
/*     */ 
/*     */   public XSSimpleTypeDefinition getSimpleType()
/*     */   {
/* 655 */     return this.fXSSimpleType;
/*     */   }
/*     */ 
/*     */   public XSParticle getParticle()
/*     */   {
/* 663 */     return this.fParticle;
/*     */   }
/*     */ 
/*     */   public boolean isProhibitedSubstitution(short prohibited)
/*     */   {
/* 674 */     return (this.fBlock & prohibited) != 0;
/*     */   }
/*     */ 
/*     */   public short getProhibitedSubstitutions()
/*     */   {
/* 683 */     return this.fBlock;
/*     */   }
/*     */ 
/*     */   public XSObjectList getAnnotations()
/*     */   {
/* 690 */     return this.fAnnotations != null ? this.fAnnotations : XSObjectListImpl.EMPTY_LIST;
/*     */   }
/*     */ 
/*     */   public XSNamespaceItem getNamespaceItem()
/*     */   {
/* 697 */     return this.fNamespaceItem;
/*     */   }
/*     */ 
/*     */   void setNamespaceItem(XSNamespaceItem namespaceItem) {
/* 701 */     this.fNamespaceItem = namespaceItem;
/*     */   }
/*     */ 
/*     */   public XSAttributeUse getAttributeUse(String namespace, String name)
/*     */   {
/* 708 */     return this.fAttrGrp.getAttributeUse(namespace, name);
/*     */   }
/*     */ 
/*     */   public String getTypeNamespace() {
/* 712 */     return getNamespace();
/*     */   }
/*     */ 
/*     */   public boolean isDerivedFrom(String typeNamespaceArg, String typeNameArg, int derivationMethod) {
/* 716 */     return isDOMDerivedFrom(typeNamespaceArg, typeNameArg, derivationMethod);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.XSComplexTypeDecl
 * JD-Core Version:    0.6.2
 */