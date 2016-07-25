/*     */ package com.sun.org.apache.xerces.internal.impl.dtd;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.DTDDVFactory;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
/*     */ 
/*     */ public class XML11DTDValidator extends XMLDTDValidator
/*     */ {
/*     */   protected static final String DTD_VALIDATOR_PROPERTY = "http://apache.org/xml/properties/internal/validator/dtd";
/*     */ 
/*     */   public void reset(XMLComponentManager manager)
/*     */   {
/*  98 */     XMLDTDValidator curr = null;
/*  99 */     if (((curr = (XMLDTDValidator)manager.getProperty("http://apache.org/xml/properties/internal/validator/dtd")) != null) && (curr != this))
/*     */     {
/* 101 */       this.fGrammarBucket = curr.getGrammarBucket();
/*     */     }
/* 103 */     super.reset(manager);
/*     */   }
/*     */ 
/*     */   protected void init() {
/* 107 */     if ((this.fValidation) || (this.fDynamicValidation)) {
/* 108 */       super.init();
/*     */       try
/*     */       {
/* 112 */         this.fValID = this.fDatatypeValidatorFactory.getBuiltInDV("XML11ID");
/* 113 */         this.fValIDRef = this.fDatatypeValidatorFactory.getBuiltInDV("XML11IDREF");
/* 114 */         this.fValIDRefs = this.fDatatypeValidatorFactory.getBuiltInDV("XML11IDREFS");
/* 115 */         this.fValNMTOKEN = this.fDatatypeValidatorFactory.getBuiltInDV("XML11NMTOKEN");
/* 116 */         this.fValNMTOKENS = this.fDatatypeValidatorFactory.getBuiltInDV("XML11NMTOKENS");
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/* 121 */         e.printStackTrace(System.err);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dtd.XML11DTDValidator
 * JD-Core Version:    0.6.2
 */