/*     */ package com.sun.org.apache.xerces.internal.dom;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.xs.AttributePSVI;
/*     */ import com.sun.org.apache.xerces.internal.xs.ShortList;
/*     */ import com.sun.org.apache.xerces.internal.xs.StringList;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSAttributeDeclaration;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSSimpleTypeDefinition;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;
/*     */ import java.io.IOException;
/*     */ import java.io.NotSerializableException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ 
/*     */ public class PSVIAttrNSImpl extends AttrNSImpl
/*     */   implements AttributePSVI
/*     */ {
/*     */   static final long serialVersionUID = -3241738699421018889L;
/*  61 */   protected XSAttributeDeclaration fDeclaration = null;
/*     */ 
/*  64 */   protected XSTypeDefinition fTypeDecl = null;
/*     */ 
/*  68 */   protected boolean fSpecified = true;
/*     */ 
/*  71 */   protected String fNormalizedValue = null;
/*     */ 
/*  74 */   protected Object fActualValue = null;
/*     */ 
/*  77 */   protected short fActualValueType = 45;
/*     */ 
/*  80 */   protected ShortList fItemValueTypes = null;
/*     */ 
/*  83 */   protected XSSimpleTypeDefinition fMemberType = null;
/*     */ 
/*  86 */   protected short fValidationAttempted = 0;
/*     */ 
/*  89 */   protected short fValidity = 0;
/*     */ 
/*  92 */   protected StringList fErrorCodes = null;
/*     */ 
/*  95 */   protected String fValidationContext = null;
/*     */ 
/*     */   public PSVIAttrNSImpl(CoreDocumentImpl ownerDocument, String namespaceURI, String qualifiedName, String localName)
/*     */   {
/*  49 */     super(ownerDocument, namespaceURI, qualifiedName, localName);
/*     */   }
/*     */ 
/*     */   public PSVIAttrNSImpl(CoreDocumentImpl ownerDocument, String namespaceURI, String qualifiedName)
/*     */   {
/*  57 */     super(ownerDocument, namespaceURI, qualifiedName);
/*     */   }
/*     */ 
/*     */   public String getSchemaDefault()
/*     */   {
/* 108 */     return this.fDeclaration == null ? null : this.fDeclaration.getConstraintValue();
/*     */   }
/*     */ 
/*     */   public String getSchemaNormalizedValue()
/*     */   {
/* 119 */     return this.fNormalizedValue;
/*     */   }
/*     */ 
/*     */   public boolean getIsSchemaSpecified()
/*     */   {
/* 128 */     return this.fSpecified;
/*     */   }
/*     */ 
/*     */   public short getValidationAttempted()
/*     */   {
/* 139 */     return this.fValidationAttempted;
/*     */   }
/*     */ 
/*     */   public short getValidity()
/*     */   {
/* 150 */     return this.fValidity;
/*     */   }
/*     */ 
/*     */   public StringList getErrorCodes()
/*     */   {
/* 160 */     return this.fErrorCodes;
/*     */   }
/*     */ 
/*     */   public String getValidationContext()
/*     */   {
/* 165 */     return this.fValidationContext;
/*     */   }
/*     */ 
/*     */   public XSTypeDefinition getTypeDefinition()
/*     */   {
/* 174 */     return this.fTypeDecl;
/*     */   }
/*     */ 
/*     */   public XSSimpleTypeDefinition getMemberTypeDefinition()
/*     */   {
/* 187 */     return this.fMemberType;
/*     */   }
/*     */ 
/*     */   public XSAttributeDeclaration getAttributeDeclaration()
/*     */   {
/* 197 */     return this.fDeclaration;
/*     */   }
/*     */ 
/*     */   public void setPSVI(AttributePSVI attr)
/*     */   {
/* 206 */     this.fDeclaration = attr.getAttributeDeclaration();
/* 207 */     this.fValidationContext = attr.getValidationContext();
/* 208 */     this.fValidity = attr.getValidity();
/* 209 */     this.fValidationAttempted = attr.getValidationAttempted();
/* 210 */     this.fErrorCodes = attr.getErrorCodes();
/* 211 */     this.fNormalizedValue = attr.getSchemaNormalizedValue();
/* 212 */     this.fActualValue = attr.getActualNormalizedValue();
/* 213 */     this.fActualValueType = attr.getActualNormalizedValueType();
/* 214 */     this.fItemValueTypes = attr.getItemValueTypes();
/* 215 */     this.fTypeDecl = attr.getTypeDefinition();
/* 216 */     this.fMemberType = attr.getMemberTypeDefinition();
/* 217 */     this.fSpecified = attr.getIsSchemaSpecified();
/*     */   }
/*     */ 
/*     */   public Object getActualNormalizedValue()
/*     */   {
/* 224 */     return this.fActualValue;
/*     */   }
/*     */ 
/*     */   public short getActualNormalizedValueType()
/*     */   {
/* 231 */     return this.fActualValueType;
/*     */   }
/*     */ 
/*     */   public ShortList getItemValueTypes()
/*     */   {
/* 238 */     return this.fItemValueTypes;
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream out)
/*     */     throws IOException
/*     */   {
/* 246 */     throw new NotSerializableException(getClass().getName());
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
/*     */   {
/* 251 */     throw new NotSerializableException(getClass().getName());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.dom.PSVIAttrNSImpl
 * JD-Core Version:    0.6.2
 */