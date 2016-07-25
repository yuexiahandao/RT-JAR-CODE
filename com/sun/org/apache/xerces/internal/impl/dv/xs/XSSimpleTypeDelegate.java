/*     */ package com.sun.org.apache.xerces.internal.impl.dv.xs;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.DatatypeException;
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeFacetException;
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.ValidatedInfo;
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.ValidationContext;
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.XSFacets;
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType;
/*     */ import com.sun.org.apache.xerces.internal.xs.StringList;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSNamespaceItem;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSObjectList;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSSimpleTypeDefinition;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;
/*     */ 
/*     */ public class XSSimpleTypeDelegate
/*     */   implements XSSimpleType
/*     */ {
/*     */   protected final XSSimpleType type;
/*     */ 
/*     */   public XSSimpleTypeDelegate(XSSimpleType type)
/*     */   {
/*  50 */     if (type == null) {
/*  51 */       throw new NullPointerException();
/*     */     }
/*  53 */     this.type = type;
/*     */   }
/*     */ 
/*     */   public XSSimpleType getWrappedXSSimpleType() {
/*  57 */     return this.type;
/*     */   }
/*     */ 
/*     */   public XSObjectList getAnnotations() {
/*  61 */     return this.type.getAnnotations();
/*     */   }
/*     */ 
/*     */   public boolean getBounded() {
/*  65 */     return this.type.getBounded();
/*     */   }
/*     */ 
/*     */   public short getBuiltInKind() {
/*  69 */     return this.type.getBuiltInKind();
/*     */   }
/*     */ 
/*     */   public short getDefinedFacets() {
/*  73 */     return this.type.getDefinedFacets();
/*     */   }
/*     */ 
/*     */   public XSObjectList getFacets() {
/*  77 */     return this.type.getFacets();
/*     */   }
/*     */ 
/*     */   public boolean getFinite() {
/*  81 */     return this.type.getFinite();
/*     */   }
/*     */ 
/*     */   public short getFixedFacets() {
/*  85 */     return this.type.getFixedFacets();
/*     */   }
/*     */ 
/*     */   public XSSimpleTypeDefinition getItemType() {
/*  89 */     return this.type.getItemType();
/*     */   }
/*     */ 
/*     */   public StringList getLexicalEnumeration() {
/*  93 */     return this.type.getLexicalEnumeration();
/*     */   }
/*     */ 
/*     */   public String getLexicalFacetValue(short facetName) {
/*  97 */     return this.type.getLexicalFacetValue(facetName);
/*     */   }
/*     */ 
/*     */   public StringList getLexicalPattern() {
/* 101 */     return this.type.getLexicalPattern();
/*     */   }
/*     */ 
/*     */   public XSObjectList getMemberTypes() {
/* 105 */     return this.type.getMemberTypes();
/*     */   }
/*     */ 
/*     */   public XSObjectList getMultiValueFacets() {
/* 109 */     return this.type.getMultiValueFacets();
/*     */   }
/*     */ 
/*     */   public boolean getNumeric() {
/* 113 */     return this.type.getNumeric();
/*     */   }
/*     */ 
/*     */   public short getOrdered() {
/* 117 */     return this.type.getOrdered();
/*     */   }
/*     */ 
/*     */   public XSSimpleTypeDefinition getPrimitiveType() {
/* 121 */     return this.type.getPrimitiveType();
/*     */   }
/*     */ 
/*     */   public short getVariety() {
/* 125 */     return this.type.getVariety();
/*     */   }
/*     */ 
/*     */   public boolean isDefinedFacet(short facetName) {
/* 129 */     return this.type.isDefinedFacet(facetName);
/*     */   }
/*     */ 
/*     */   public boolean isFixedFacet(short facetName) {
/* 133 */     return this.type.isFixedFacet(facetName);
/*     */   }
/*     */ 
/*     */   public boolean derivedFrom(String namespace, String name, short derivationMethod) {
/* 137 */     return this.type.derivedFrom(namespace, name, derivationMethod);
/*     */   }
/*     */ 
/*     */   public boolean derivedFromType(XSTypeDefinition ancestorType, short derivationMethod) {
/* 141 */     return this.type.derivedFromType(ancestorType, derivationMethod);
/*     */   }
/*     */ 
/*     */   public boolean getAnonymous() {
/* 145 */     return this.type.getAnonymous();
/*     */   }
/*     */ 
/*     */   public XSTypeDefinition getBaseType() {
/* 149 */     return this.type.getBaseType();
/*     */   }
/*     */ 
/*     */   public short getFinal() {
/* 153 */     return this.type.getFinal();
/*     */   }
/*     */ 
/*     */   public short getTypeCategory() {
/* 157 */     return this.type.getTypeCategory();
/*     */   }
/*     */ 
/*     */   public boolean isFinal(short restriction) {
/* 161 */     return this.type.isFinal(restriction);
/*     */   }
/*     */ 
/*     */   public String getName() {
/* 165 */     return this.type.getName();
/*     */   }
/*     */ 
/*     */   public String getNamespace() {
/* 169 */     return this.type.getNamespace();
/*     */   }
/*     */ 
/*     */   public XSNamespaceItem getNamespaceItem() {
/* 173 */     return this.type.getNamespaceItem();
/*     */   }
/*     */ 
/*     */   public short getType() {
/* 177 */     return this.type.getType();
/*     */   }
/*     */ 
/*     */   public void applyFacets(XSFacets facets, short presentFacet, short fixedFacet, ValidationContext context) throws InvalidDatatypeFacetException
/*     */   {
/* 182 */     this.type.applyFacets(facets, presentFacet, fixedFacet, context);
/*     */   }
/*     */ 
/*     */   public short getPrimitiveKind() {
/* 186 */     return this.type.getPrimitiveKind();
/*     */   }
/*     */ 
/*     */   public short getWhitespace() throws DatatypeException {
/* 190 */     return this.type.getWhitespace();
/*     */   }
/*     */ 
/*     */   public boolean isEqual(Object value1, Object value2) {
/* 194 */     return this.type.isEqual(value1, value2);
/*     */   }
/*     */ 
/*     */   public boolean isIDType() {
/* 198 */     return this.type.isIDType();
/*     */   }
/*     */ 
/*     */   public void validate(ValidationContext context, ValidatedInfo validatedInfo) throws InvalidDatatypeValueException
/*     */   {
/* 203 */     this.type.validate(context, validatedInfo);
/*     */   }
/*     */ 
/*     */   public Object validate(String content, ValidationContext context, ValidatedInfo validatedInfo) throws InvalidDatatypeValueException
/*     */   {
/* 208 */     return this.type.validate(content, context, validatedInfo);
/*     */   }
/*     */ 
/*     */   public Object validate(Object content, ValidationContext context, ValidatedInfo validatedInfo) throws InvalidDatatypeValueException
/*     */   {
/* 213 */     return this.type.validate(content, context, validatedInfo);
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 217 */     return this.type.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dv.xs.XSSimpleTypeDelegate
 * JD-Core Version:    0.6.2
 */