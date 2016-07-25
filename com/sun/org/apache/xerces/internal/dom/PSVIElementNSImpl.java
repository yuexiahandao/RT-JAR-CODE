/*     */ package com.sun.org.apache.xerces.internal.dom;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.xs.ElementPSVI;
/*     */ import com.sun.org.apache.xerces.internal.xs.ShortList;
/*     */ import com.sun.org.apache.xerces.internal.xs.StringList;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSElementDeclaration;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSModel;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSNotationDeclaration;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSSimpleTypeDefinition;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;
/*     */ import java.io.IOException;
/*     */ import java.io.NotSerializableException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ 
/*     */ public class PSVIElementNSImpl extends ElementNSImpl
/*     */   implements ElementPSVI
/*     */ {
/*     */   static final long serialVersionUID = 6815489624636016068L;
/*  62 */   protected XSElementDeclaration fDeclaration = null;
/*     */ 
/*  65 */   protected XSTypeDefinition fTypeDecl = null;
/*     */ 
/*  70 */   protected boolean fNil = false;
/*     */ 
/*  74 */   protected boolean fSpecified = true;
/*     */ 
/*  77 */   protected String fNormalizedValue = null;
/*     */ 
/*  80 */   protected Object fActualValue = null;
/*     */ 
/*  83 */   protected short fActualValueType = 45;
/*     */ 
/*  86 */   protected ShortList fItemValueTypes = null;
/*     */ 
/*  89 */   protected XSNotationDeclaration fNotation = null;
/*     */ 
/*  92 */   protected XSSimpleTypeDefinition fMemberType = null;
/*     */ 
/*  95 */   protected short fValidationAttempted = 0;
/*     */ 
/*  98 */   protected short fValidity = 0;
/*     */ 
/* 101 */   protected StringList fErrorCodes = null;
/*     */ 
/* 104 */   protected String fValidationContext = null;
/*     */ 
/* 107 */   protected XSModel fSchemaInformation = null;
/*     */ 
/*     */   public PSVIElementNSImpl(CoreDocumentImpl ownerDocument, String namespaceURI, String qualifiedName, String localName)
/*     */   {
/*  50 */     super(ownerDocument, namespaceURI, qualifiedName, localName);
/*     */   }
/*     */ 
/*     */   public PSVIElementNSImpl(CoreDocumentImpl ownerDocument, String namespaceURI, String qualifiedName)
/*     */   {
/*  58 */     super(ownerDocument, namespaceURI, qualifiedName);
/*     */   }
/*     */ 
/*     */   public String getSchemaDefault()
/*     */   {
/* 120 */     return this.fDeclaration == null ? null : this.fDeclaration.getConstraintValue();
/*     */   }
/*     */ 
/*     */   public String getSchemaNormalizedValue()
/*     */   {
/* 131 */     return this.fNormalizedValue;
/*     */   }
/*     */ 
/*     */   public boolean getIsSchemaSpecified()
/*     */   {
/* 140 */     return this.fSpecified;
/*     */   }
/*     */ 
/*     */   public short getValidationAttempted()
/*     */   {
/* 150 */     return this.fValidationAttempted;
/*     */   }
/*     */ 
/*     */   public short getValidity()
/*     */   {
/* 161 */     return this.fValidity;
/*     */   }
/*     */ 
/*     */   public StringList getErrorCodes()
/*     */   {
/* 171 */     return this.fErrorCodes;
/*     */   }
/*     */ 
/*     */   public String getValidationContext()
/*     */   {
/* 177 */     return this.fValidationContext;
/*     */   }
/*     */ 
/*     */   public boolean getNil()
/*     */   {
/* 186 */     return this.fNil;
/*     */   }
/*     */ 
/*     */   public XSNotationDeclaration getNotation()
/*     */   {
/* 195 */     return this.fNotation;
/*     */   }
/*     */ 
/*     */   public XSTypeDefinition getTypeDefinition()
/*     */   {
/* 204 */     return this.fTypeDecl;
/*     */   }
/*     */ 
/*     */   public XSSimpleTypeDefinition getMemberTypeDefinition()
/*     */   {
/* 217 */     return this.fMemberType;
/*     */   }
/*     */ 
/*     */   public XSElementDeclaration getElementDeclaration()
/*     */   {
/* 227 */     return this.fDeclaration;
/*     */   }
/*     */ 
/*     */   public XSModel getSchemaInformation()
/*     */   {
/* 237 */     return this.fSchemaInformation;
/*     */   }
/*     */ 
/*     */   public void setPSVI(ElementPSVI elem)
/*     */   {
/* 246 */     this.fDeclaration = elem.getElementDeclaration();
/* 247 */     this.fNotation = elem.getNotation();
/* 248 */     this.fValidationContext = elem.getValidationContext();
/* 249 */     this.fTypeDecl = elem.getTypeDefinition();
/* 250 */     this.fSchemaInformation = elem.getSchemaInformation();
/* 251 */     this.fValidity = elem.getValidity();
/* 252 */     this.fValidationAttempted = elem.getValidationAttempted();
/* 253 */     this.fErrorCodes = elem.getErrorCodes();
/* 254 */     this.fNormalizedValue = elem.getSchemaNormalizedValue();
/* 255 */     this.fActualValue = elem.getActualNormalizedValue();
/* 256 */     this.fActualValueType = elem.getActualNormalizedValueType();
/* 257 */     this.fItemValueTypes = elem.getItemValueTypes();
/* 258 */     this.fMemberType = elem.getMemberTypeDefinition();
/* 259 */     this.fSpecified = elem.getIsSchemaSpecified();
/* 260 */     this.fNil = elem.getNil();
/*     */   }
/*     */ 
/*     */   public Object getActualNormalizedValue()
/*     */   {
/* 267 */     return this.fActualValue;
/*     */   }
/*     */ 
/*     */   public short getActualNormalizedValueType()
/*     */   {
/* 274 */     return this.fActualValueType;
/*     */   }
/*     */ 
/*     */   public ShortList getItemValueTypes()
/*     */   {
/* 281 */     return this.fItemValueTypes;
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream out)
/*     */     throws IOException
/*     */   {
/* 289 */     throw new NotSerializableException(getClass().getName());
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
/*     */   {
/* 294 */     throw new NotSerializableException(getClass().getName());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.dom.PSVIElementNSImpl
 * JD-Core Version:    0.6.2
 */