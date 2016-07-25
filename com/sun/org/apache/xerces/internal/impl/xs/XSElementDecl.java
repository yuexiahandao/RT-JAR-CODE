/*     */ package com.sun.org.apache.xerces.internal.impl.xs;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.ValidatedInfo;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.identity.IdentityConstraint;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.util.XSNamedMapImpl;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.util.XSObjectListImpl;
/*     */ import com.sun.org.apache.xerces.internal.xni.QName;
/*     */ import com.sun.org.apache.xerces.internal.xs.ShortList;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSAnnotation;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSComplexTypeDefinition;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSElementDeclaration;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSNamedMap;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSNamespaceItem;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSObjectList;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;
/*     */ 
/*     */ public class XSElementDecl
/*     */   implements XSElementDeclaration
/*     */ {
/*     */   public static final short SCOPE_ABSENT = 0;
/*     */   public static final short SCOPE_GLOBAL = 1;
/*     */   public static final short SCOPE_LOCAL = 2;
/*  56 */   public String fName = null;
/*     */ 
/*  58 */   public String fTargetNamespace = null;
/*     */ 
/*  60 */   public XSTypeDefinition fType = null;
/*  61 */   public QName fUnresolvedTypeName = null;
/*     */ 
/*  63 */   short fMiscFlags = 0;
/*  64 */   public short fScope = 0;
/*     */ 
/*  66 */   XSComplexTypeDecl fEnclosingCT = null;
/*     */ 
/*  68 */   public short fBlock = 0;
/*     */ 
/*  70 */   public short fFinal = 0;
/*     */ 
/*  72 */   public XSObjectList fAnnotations = null;
/*     */ 
/*  74 */   public ValidatedInfo fDefault = null;
/*     */ 
/*  76 */   public XSElementDecl fSubGroup = null;
/*     */   static final int INITIAL_SIZE = 2;
/*  79 */   int fIDCPos = 0;
/*  80 */   IdentityConstraint[] fIDConstraints = new IdentityConstraint[2];
/*     */ 
/*  83 */   private XSNamespaceItem fNamespaceItem = null;
/*     */   private static final short CONSTRAINT_MASK = 3;
/*     */   private static final short NILLABLE = 4;
/*     */   private static final short ABSTRACT = 8;
/* 136 */   private String fDescription = null;
/*     */ 
/*     */   public void setConstraintType(short constraintType)
/*     */   {
/*  92 */     this.fMiscFlags = ((short)(this.fMiscFlags ^ this.fMiscFlags & 0x3));
/*     */ 
/*  94 */     this.fMiscFlags = ((short)(this.fMiscFlags | constraintType & 0x3));
/*     */   }
/*     */   public void setIsNillable() {
/*  97 */     this.fMiscFlags = ((short)(this.fMiscFlags | 0x4));
/*     */   }
/*     */   public void setIsAbstract() {
/* 100 */     this.fMiscFlags = ((short)(this.fMiscFlags | 0x8));
/*     */   }
/*     */   public void setIsGlobal() {
/* 103 */     this.fScope = 1;
/*     */   }
/*     */   public void setIsLocal(XSComplexTypeDecl enclosingCT) {
/* 106 */     this.fScope = 2;
/* 107 */     this.fEnclosingCT = enclosingCT;
/*     */   }
/*     */ 
/*     */   public void addIDConstraint(IdentityConstraint idc) {
/* 111 */     if (this.fIDCPos == this.fIDConstraints.length) {
/* 112 */       this.fIDConstraints = resize(this.fIDConstraints, this.fIDCPos * 2);
/*     */     }
/* 114 */     this.fIDConstraints[(this.fIDCPos++)] = idc;
/*     */   }
/*     */ 
/*     */   public IdentityConstraint[] getIDConstraints() {
/* 118 */     if (this.fIDCPos == 0) {
/* 119 */       return null;
/*     */     }
/* 121 */     if (this.fIDCPos < this.fIDConstraints.length) {
/* 122 */       this.fIDConstraints = resize(this.fIDConstraints, this.fIDCPos);
/*     */     }
/* 124 */     return this.fIDConstraints;
/*     */   }
/*     */ 
/*     */   static final IdentityConstraint[] resize(IdentityConstraint[] oldArray, int newSize) {
/* 128 */     IdentityConstraint[] newArray = new IdentityConstraint[newSize];
/* 129 */     System.arraycopy(oldArray, 0, newArray, 0, Math.min(oldArray.length, newSize));
/* 130 */     return newArray;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 138 */     if (this.fDescription == null) {
/* 139 */       if (this.fTargetNamespace != null) {
/* 140 */         StringBuffer buffer = new StringBuffer(this.fTargetNamespace.length() + (this.fName != null ? this.fName.length() : 4) + 3);
/*     */ 
/* 143 */         buffer.append('"');
/* 144 */         buffer.append(this.fTargetNamespace);
/* 145 */         buffer.append('"');
/* 146 */         buffer.append(':');
/* 147 */         buffer.append(this.fName);
/* 148 */         this.fDescription = buffer.toString();
/*     */       }
/*     */       else {
/* 151 */         this.fDescription = this.fName;
/*     */       }
/*     */     }
/* 154 */     return this.fDescription;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 161 */     int code = this.fName.hashCode();
/* 162 */     if (this.fTargetNamespace != null)
/* 163 */       code = (code << 16) + this.fTargetNamespace.hashCode();
/* 164 */     return code;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 171 */     return o == this;
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/* 178 */     this.fScope = 0;
/* 179 */     this.fName = null;
/* 180 */     this.fTargetNamespace = null;
/* 181 */     this.fType = null;
/* 182 */     this.fUnresolvedTypeName = null;
/* 183 */     this.fMiscFlags = 0;
/* 184 */     this.fBlock = 0;
/* 185 */     this.fFinal = 0;
/* 186 */     this.fDefault = null;
/* 187 */     this.fAnnotations = null;
/* 188 */     this.fSubGroup = null;
/*     */ 
/* 190 */     for (int i = 0; i < this.fIDCPos; i++) {
/* 191 */       this.fIDConstraints[i] = null;
/*     */     }
/*     */ 
/* 194 */     this.fIDCPos = 0;
/*     */   }
/*     */ 
/*     */   public short getType()
/*     */   {
/* 201 */     return 2;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 209 */     return this.fName;
/*     */   }
/*     */ 
/*     */   public String getNamespace()
/*     */   {
/* 218 */     return this.fTargetNamespace;
/*     */   }
/*     */ 
/*     */   public XSTypeDefinition getTypeDefinition()
/*     */   {
/* 225 */     return this.fType;
/*     */   }
/*     */ 
/*     */   public short getScope()
/*     */   {
/* 236 */     return this.fScope;
/*     */   }
/*     */ 
/*     */   public XSComplexTypeDefinition getEnclosingCTDefinition()
/*     */   {
/* 245 */     return this.fEnclosingCT;
/*     */   }
/*     */ 
/*     */   public short getConstraintType()
/*     */   {
/* 252 */     return (short)(this.fMiscFlags & 0x3);
/*     */   }
/*     */ 
/*     */   public String getConstraintValue()
/*     */   {
/* 261 */     return getConstraintType() == 0 ? null : this.fDefault.stringValue();
/*     */   }
/*     */ 
/*     */   public boolean getNillable()
/*     */   {
/* 274 */     return (this.fMiscFlags & 0x4) != 0;
/*     */   }
/*     */ 
/*     */   public XSNamedMap getIdentityConstraints()
/*     */   {
/* 281 */     return new XSNamedMapImpl(this.fIDConstraints, this.fIDCPos);
/*     */   }
/*     */ 
/*     */   public XSElementDeclaration getSubstitutionGroupAffiliation()
/*     */   {
/* 289 */     return this.fSubGroup;
/*     */   }
/*     */ 
/*     */   public boolean isSubstitutionGroupExclusion(short exclusion)
/*     */   {
/* 301 */     return (this.fFinal & exclusion) != 0;
/*     */   }
/*     */ 
/*     */   public short getSubstitutionGroupExclusions()
/*     */   {
/* 313 */     return this.fFinal;
/*     */   }
/*     */ 
/*     */   public boolean isDisallowedSubstitution(short disallowed)
/*     */   {
/* 325 */     return (this.fBlock & disallowed) != 0;
/*     */   }
/*     */ 
/*     */   public short getDisallowedSubstitutions()
/*     */   {
/* 334 */     return this.fBlock;
/*     */   }
/*     */ 
/*     */   public boolean getAbstract()
/*     */   {
/* 341 */     return (this.fMiscFlags & 0x8) != 0;
/*     */   }
/*     */ 
/*     */   public XSAnnotation getAnnotation()
/*     */   {
/* 348 */     return this.fAnnotations != null ? (XSAnnotation)this.fAnnotations.item(0) : null;
/*     */   }
/*     */ 
/*     */   public XSObjectList getAnnotations()
/*     */   {
/* 355 */     return this.fAnnotations != null ? this.fAnnotations : XSObjectListImpl.EMPTY_LIST;
/*     */   }
/*     */ 
/*     */   public XSNamespaceItem getNamespaceItem()
/*     */   {
/* 363 */     return this.fNamespaceItem;
/*     */   }
/*     */ 
/*     */   void setNamespaceItem(XSNamespaceItem namespaceItem) {
/* 367 */     this.fNamespaceItem = namespaceItem;
/*     */   }
/*     */ 
/*     */   public Object getActualVC() {
/* 371 */     return getConstraintType() == 0 ? null : this.fDefault.actualValue;
/*     */   }
/*     */ 
/*     */   public short getActualVCType()
/*     */   {
/* 377 */     return getConstraintType() == 0 ? 45 : this.fDefault.actualValueType;
/*     */   }
/*     */ 
/*     */   public ShortList getItemValueTypes()
/*     */   {
/* 383 */     return getConstraintType() == 0 ? null : this.fDefault.itemValueTypes;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.XSElementDecl
 * JD-Core Version:    0.6.2
 */