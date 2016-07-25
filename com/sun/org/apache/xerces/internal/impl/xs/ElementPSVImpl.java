/*     */ package com.sun.org.apache.xerces.internal.impl.xs;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.util.StringListImpl;
/*     */ import com.sun.org.apache.xerces.internal.xs.ElementPSVI;
/*     */ import com.sun.org.apache.xerces.internal.xs.ShortList;
/*     */ import com.sun.org.apache.xerces.internal.xs.StringList;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSElementDeclaration;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSModel;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSNotationDeclaration;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSSimpleTypeDefinition;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;
/*     */ 
/*     */ public class ElementPSVImpl
/*     */   implements ElementPSVI
/*     */ {
/*  50 */   protected XSElementDeclaration fDeclaration = null;
/*     */ 
/*  53 */   protected XSTypeDefinition fTypeDecl = null;
/*     */ 
/*  58 */   protected boolean fNil = false;
/*     */ 
/*  62 */   protected boolean fSpecified = false;
/*     */ 
/*  65 */   protected String fNormalizedValue = null;
/*     */ 
/*  68 */   protected Object fActualValue = null;
/*     */ 
/*  71 */   protected short fActualValueType = 45;
/*     */ 
/*  74 */   protected ShortList fItemValueTypes = null;
/*     */ 
/*  77 */   protected XSNotationDeclaration fNotation = null;
/*     */ 
/*  80 */   protected XSSimpleTypeDefinition fMemberType = null;
/*     */ 
/*  83 */   protected short fValidationAttempted = 0;
/*     */ 
/*  86 */   protected short fValidity = 0;
/*     */ 
/*  89 */   protected String[] fErrorCodes = null;
/*     */ 
/*  92 */   protected String fValidationContext = null;
/*     */ 
/*  95 */   protected SchemaGrammar[] fGrammars = null;
/*     */ 
/*  98 */   protected XSModel fSchemaInformation = null;
/*     */ 
/*     */   public String getSchemaDefault()
/*     */   {
/* 111 */     return this.fDeclaration == null ? null : this.fDeclaration.getConstraintValue();
/*     */   }
/*     */ 
/*     */   public String getSchemaNormalizedValue()
/*     */   {
/* 122 */     return this.fNormalizedValue;
/*     */   }
/*     */ 
/*     */   public boolean getIsSchemaSpecified()
/*     */   {
/* 131 */     return this.fSpecified;
/*     */   }
/*     */ 
/*     */   public short getValidationAttempted()
/*     */   {
/* 141 */     return this.fValidationAttempted;
/*     */   }
/*     */ 
/*     */   public short getValidity()
/*     */   {
/* 152 */     return this.fValidity;
/*     */   }
/*     */ 
/*     */   public StringList getErrorCodes()
/*     */   {
/* 162 */     if (this.fErrorCodes == null)
/* 163 */       return null;
/* 164 */     return new StringListImpl(this.fErrorCodes, this.fErrorCodes.length);
/*     */   }
/*     */ 
/*     */   public String getValidationContext()
/*     */   {
/* 170 */     return this.fValidationContext;
/*     */   }
/*     */ 
/*     */   public boolean getNil()
/*     */   {
/* 179 */     return this.fNil;
/*     */   }
/*     */ 
/*     */   public XSNotationDeclaration getNotation()
/*     */   {
/* 188 */     return this.fNotation;
/*     */   }
/*     */ 
/*     */   public XSTypeDefinition getTypeDefinition()
/*     */   {
/* 197 */     return this.fTypeDecl;
/*     */   }
/*     */ 
/*     */   public XSSimpleTypeDefinition getMemberTypeDefinition()
/*     */   {
/* 210 */     return this.fMemberType;
/*     */   }
/*     */ 
/*     */   public XSElementDeclaration getElementDeclaration()
/*     */   {
/* 220 */     return this.fDeclaration;
/*     */   }
/*     */ 
/*     */   public synchronized XSModel getSchemaInformation()
/*     */   {
/* 230 */     if ((this.fSchemaInformation == null) && (this.fGrammars != null)) {
/* 231 */       this.fSchemaInformation = new XSModelImpl(this.fGrammars);
/*     */     }
/* 233 */     return this.fSchemaInformation;
/*     */   }
/*     */ 
/*     */   public Object getActualNormalizedValue()
/*     */   {
/* 240 */     return this.fActualValue;
/*     */   }
/*     */ 
/*     */   public short getActualNormalizedValueType()
/*     */   {
/* 247 */     return this.fActualValueType;
/*     */   }
/*     */ 
/*     */   public ShortList getItemValueTypes()
/*     */   {
/* 254 */     return this.fItemValueTypes;
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/* 261 */     this.fDeclaration = null;
/* 262 */     this.fTypeDecl = null;
/* 263 */     this.fNil = false;
/* 264 */     this.fSpecified = false;
/* 265 */     this.fNotation = null;
/* 266 */     this.fMemberType = null;
/* 267 */     this.fValidationAttempted = 0;
/* 268 */     this.fValidity = 0;
/* 269 */     this.fErrorCodes = null;
/* 270 */     this.fValidationContext = null;
/* 271 */     this.fNormalizedValue = null;
/* 272 */     this.fActualValue = null;
/* 273 */     this.fActualValueType = 45;
/* 274 */     this.fItemValueTypes = null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.ElementPSVImpl
 * JD-Core Version:    0.6.2
 */