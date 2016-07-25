/*     */ package com.sun.org.apache.xerces.internal.impl.xs;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.util.StringListImpl;
/*     */ import com.sun.org.apache.xerces.internal.xs.AttributePSVI;
/*     */ import com.sun.org.apache.xerces.internal.xs.ShortList;
/*     */ import com.sun.org.apache.xerces.internal.xs.StringList;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSAttributeDeclaration;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSSimpleTypeDefinition;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;
/*     */ 
/*     */ public class AttributePSVImpl
/*     */   implements AttributePSVI
/*     */ {
/*  43 */   protected XSAttributeDeclaration fDeclaration = null;
/*     */ 
/*  46 */   protected XSTypeDefinition fTypeDecl = null;
/*     */ 
/*  50 */   protected boolean fSpecified = false;
/*     */ 
/*  53 */   protected String fNormalizedValue = null;
/*     */ 
/*  56 */   protected Object fActualValue = null;
/*     */ 
/*  59 */   protected short fActualValueType = 45;
/*     */ 
/*  62 */   protected ShortList fItemValueTypes = null;
/*     */ 
/*  65 */   protected XSSimpleTypeDefinition fMemberType = null;
/*     */ 
/*  68 */   protected short fValidationAttempted = 0;
/*     */ 
/*  71 */   protected short fValidity = 0;
/*     */ 
/*  74 */   protected String[] fErrorCodes = null;
/*     */ 
/*  77 */   protected String fValidationContext = null;
/*     */ 
/*     */   public String getSchemaDefault()
/*     */   {
/*  90 */     return this.fDeclaration == null ? null : this.fDeclaration.getConstraintValue();
/*     */   }
/*     */ 
/*     */   public String getSchemaNormalizedValue()
/*     */   {
/* 101 */     return this.fNormalizedValue;
/*     */   }
/*     */ 
/*     */   public boolean getIsSchemaSpecified()
/*     */   {
/* 110 */     return this.fSpecified;
/*     */   }
/*     */ 
/*     */   public short getValidationAttempted()
/*     */   {
/* 121 */     return this.fValidationAttempted;
/*     */   }
/*     */ 
/*     */   public short getValidity()
/*     */   {
/* 132 */     return this.fValidity;
/*     */   }
/*     */ 
/*     */   public StringList getErrorCodes()
/*     */   {
/* 142 */     if (this.fErrorCodes == null)
/* 143 */       return null;
/* 144 */     return new StringListImpl(this.fErrorCodes, this.fErrorCodes.length);
/*     */   }
/*     */ 
/*     */   public String getValidationContext()
/*     */   {
/* 149 */     return this.fValidationContext;
/*     */   }
/*     */ 
/*     */   public XSTypeDefinition getTypeDefinition()
/*     */   {
/* 158 */     return this.fTypeDecl;
/*     */   }
/*     */ 
/*     */   public XSSimpleTypeDefinition getMemberTypeDefinition()
/*     */   {
/* 171 */     return this.fMemberType;
/*     */   }
/*     */ 
/*     */   public XSAttributeDeclaration getAttributeDeclaration()
/*     */   {
/* 181 */     return this.fDeclaration;
/*     */   }
/*     */ 
/*     */   public Object getActualNormalizedValue()
/*     */   {
/* 188 */     return this.fActualValue;
/*     */   }
/*     */ 
/*     */   public short getActualNormalizedValueType()
/*     */   {
/* 195 */     return this.fActualValueType;
/*     */   }
/*     */ 
/*     */   public ShortList getItemValueTypes()
/*     */   {
/* 202 */     return this.fItemValueTypes;
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/* 209 */     this.fNormalizedValue = null;
/* 210 */     this.fActualValue = null;
/* 211 */     this.fActualValueType = 45;
/* 212 */     this.fItemValueTypes = null;
/* 213 */     this.fDeclaration = null;
/* 214 */     this.fTypeDecl = null;
/* 215 */     this.fSpecified = false;
/* 216 */     this.fMemberType = null;
/* 217 */     this.fValidationAttempted = 0;
/* 218 */     this.fValidity = 0;
/* 219 */     this.fErrorCodes = null;
/* 220 */     this.fValidationContext = null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.AttributePSVImpl
 * JD-Core Version:    0.6.2
 */