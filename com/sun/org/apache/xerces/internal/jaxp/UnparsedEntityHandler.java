/*     */ package com.sun.org.apache.xerces.internal.jaxp;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.validation.EntityState;
/*     */ import com.sun.org.apache.xerces.internal.impl.validation.ValidationManager;
/*     */ import com.sun.org.apache.xerces.internal.xni.Augmentations;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLDTDHandler;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLLocator;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLString;
/*     */ import com.sun.org.apache.xerces.internal.xni.XNIException;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLDTDFilter;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLDTDSource;
/*     */ import java.util.HashMap;
/*     */ 
/*     */ final class UnparsedEntityHandler
/*     */   implements XMLDTDFilter, EntityState
/*     */ {
/*     */   private XMLDTDSource fDTDSource;
/*     */   private XMLDTDHandler fDTDHandler;
/*     */   private final ValidationManager fValidationManager;
/*  54 */   private HashMap fUnparsedEntities = null;
/*     */ 
/*     */   UnparsedEntityHandler(ValidationManager manager) {
/*  57 */     this.fValidationManager = manager;
/*     */   }
/*     */ 
/*     */   public void startDTD(XMLLocator locator, Augmentations augmentations)
/*     */     throws XNIException
/*     */   {
/*  66 */     this.fValidationManager.setEntityState(this);
/*  67 */     if (this.fDTDHandler != null)
/*  68 */       this.fDTDHandler.startDTD(locator, augmentations);
/*     */   }
/*     */ 
/*     */   public void startParameterEntity(String name, XMLResourceIdentifier identifier, String encoding, Augmentations augmentations)
/*     */     throws XNIException
/*     */   {
/*  75 */     if (this.fDTDHandler != null)
/*  76 */       this.fDTDHandler.startParameterEntity(name, identifier, encoding, augmentations);
/*     */   }
/*     */ 
/*     */   public void textDecl(String version, String encoding, Augmentations augmentations)
/*     */     throws XNIException
/*     */   {
/*  82 */     if (this.fDTDHandler != null)
/*  83 */       this.fDTDHandler.textDecl(version, encoding, augmentations);
/*     */   }
/*     */ 
/*     */   public void endParameterEntity(String name, Augmentations augmentations)
/*     */     throws XNIException
/*     */   {
/*  89 */     if (this.fDTDHandler != null)
/*  90 */       this.fDTDHandler.endParameterEntity(name, augmentations);
/*     */   }
/*     */ 
/*     */   public void startExternalSubset(XMLResourceIdentifier identifier, Augmentations augmentations)
/*     */     throws XNIException
/*     */   {
/*  96 */     if (this.fDTDHandler != null)
/*  97 */       this.fDTDHandler.startExternalSubset(identifier, augmentations);
/*     */   }
/*     */ 
/*     */   public void endExternalSubset(Augmentations augmentations)
/*     */     throws XNIException
/*     */   {
/* 103 */     if (this.fDTDHandler != null)
/* 104 */       this.fDTDHandler.endExternalSubset(augmentations);
/*     */   }
/*     */ 
/*     */   public void comment(XMLString text, Augmentations augmentations)
/*     */     throws XNIException
/*     */   {
/* 110 */     if (this.fDTDHandler != null)
/* 111 */       this.fDTDHandler.comment(text, augmentations);
/*     */   }
/*     */ 
/*     */   public void processingInstruction(String target, XMLString data, Augmentations augmentations)
/*     */     throws XNIException
/*     */   {
/* 117 */     if (this.fDTDHandler != null)
/* 118 */       this.fDTDHandler.processingInstruction(target, data, augmentations);
/*     */   }
/*     */ 
/*     */   public void elementDecl(String name, String contentModel, Augmentations augmentations)
/*     */     throws XNIException
/*     */   {
/* 124 */     if (this.fDTDHandler != null)
/* 125 */       this.fDTDHandler.elementDecl(name, contentModel, augmentations);
/*     */   }
/*     */ 
/*     */   public void startAttlist(String elementName, Augmentations augmentations)
/*     */     throws XNIException
/*     */   {
/* 131 */     if (this.fDTDHandler != null)
/* 132 */       this.fDTDHandler.startAttlist(elementName, augmentations);
/*     */   }
/*     */ 
/*     */   public void attributeDecl(String elementName, String attributeName, String type, String[] enumeration, String defaultType, XMLString defaultValue, XMLString nonNormalizedDefaultValue, Augmentations augmentations)
/*     */     throws XNIException
/*     */   {
/* 140 */     if (this.fDTDHandler != null)
/* 141 */       this.fDTDHandler.attributeDecl(elementName, attributeName, type, enumeration, defaultType, defaultValue, nonNormalizedDefaultValue, augmentations);
/*     */   }
/*     */ 
/*     */   public void endAttlist(Augmentations augmentations)
/*     */     throws XNIException
/*     */   {
/* 149 */     if (this.fDTDHandler != null)
/* 150 */       this.fDTDHandler.endAttlist(augmentations);
/*     */   }
/*     */ 
/*     */   public void internalEntityDecl(String name, XMLString text, XMLString nonNormalizedText, Augmentations augmentations)
/*     */     throws XNIException
/*     */   {
/* 157 */     if (this.fDTDHandler != null)
/* 158 */       this.fDTDHandler.internalEntityDecl(name, text, nonNormalizedText, augmentations);
/*     */   }
/*     */ 
/*     */   public void externalEntityDecl(String name, XMLResourceIdentifier identifier, Augmentations augmentations)
/*     */     throws XNIException
/*     */   {
/* 166 */     if (this.fDTDHandler != null)
/* 167 */       this.fDTDHandler.externalEntityDecl(name, identifier, augmentations);
/*     */   }
/*     */ 
/*     */   public void unparsedEntityDecl(String name, XMLResourceIdentifier identifier, String notation, Augmentations augmentations)
/*     */     throws XNIException
/*     */   {
/* 174 */     if (this.fUnparsedEntities == null) {
/* 175 */       this.fUnparsedEntities = new HashMap();
/*     */     }
/* 177 */     this.fUnparsedEntities.put(name, name);
/* 178 */     if (this.fDTDHandler != null)
/* 179 */       this.fDTDHandler.unparsedEntityDecl(name, identifier, notation, augmentations);
/*     */   }
/*     */ 
/*     */   public void notationDecl(String name, XMLResourceIdentifier identifier, Augmentations augmentations)
/*     */     throws XNIException
/*     */   {
/* 185 */     if (this.fDTDHandler != null)
/* 186 */       this.fDTDHandler.notationDecl(name, identifier, augmentations);
/*     */   }
/*     */ 
/*     */   public void startConditional(short type, Augmentations augmentations)
/*     */     throws XNIException
/*     */   {
/* 192 */     if (this.fDTDHandler != null)
/* 193 */       this.fDTDHandler.startConditional(type, augmentations);
/*     */   }
/*     */ 
/*     */   public void ignoredCharacters(XMLString text, Augmentations augmentations)
/*     */     throws XNIException
/*     */   {
/* 199 */     if (this.fDTDHandler != null)
/* 200 */       this.fDTDHandler.ignoredCharacters(text, augmentations);
/*     */   }
/*     */ 
/*     */   public void endConditional(Augmentations augmentations)
/*     */     throws XNIException
/*     */   {
/* 206 */     if (this.fDTDHandler != null)
/* 207 */       this.fDTDHandler.endConditional(augmentations);
/*     */   }
/*     */ 
/*     */   public void endDTD(Augmentations augmentations) throws XNIException
/*     */   {
/* 212 */     if (this.fDTDHandler != null)
/* 213 */       this.fDTDHandler.endDTD(augmentations);
/*     */   }
/*     */ 
/*     */   public void setDTDSource(XMLDTDSource source)
/*     */   {
/* 218 */     this.fDTDSource = source;
/*     */   }
/*     */ 
/*     */   public XMLDTDSource getDTDSource() {
/* 222 */     return this.fDTDSource;
/*     */   }
/*     */ 
/*     */   public void setDTDHandler(XMLDTDHandler handler)
/*     */   {
/* 230 */     this.fDTDHandler = handler;
/*     */   }
/*     */ 
/*     */   public XMLDTDHandler getDTDHandler() {
/* 234 */     return this.fDTDHandler;
/*     */   }
/*     */ 
/*     */   public boolean isEntityDeclared(String name)
/*     */   {
/* 242 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isEntityUnparsed(String name) {
/* 246 */     if (this.fUnparsedEntities != null) {
/* 247 */       return this.fUnparsedEntities.containsKey(name);
/*     */     }
/* 249 */     return false;
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/* 257 */     if ((this.fUnparsedEntities != null) && (!this.fUnparsedEntities.isEmpty()))
/*     */     {
/* 259 */       this.fUnparsedEntities.clear();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.jaxp.UnparsedEntityHandler
 * JD-Core Version:    0.6.2
 */