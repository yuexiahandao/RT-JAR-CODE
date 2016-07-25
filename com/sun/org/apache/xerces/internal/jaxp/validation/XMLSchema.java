/*    */ package com.sun.org.apache.xerces.internal.jaxp.validation;
/*    */ 
/*    */ import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;
/*    */ 
/*    */ final class XMLSchema extends AbstractXMLSchema
/*    */ {
/*    */   private final XMLGrammarPool fGrammarPool;
/*    */ 
/*    */   public XMLSchema(XMLGrammarPool grammarPool)
/*    */   {
/* 37 */     this.fGrammarPool = grammarPool;
/*    */   }
/*    */ 
/*    */   public XMLGrammarPool getGrammarPool()
/*    */   {
/* 50 */     return this.fGrammarPool;
/*    */   }
/*    */ 
/*    */   public boolean isFullyComposed()
/*    */   {
/* 63 */     return true;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.jaxp.validation.XMLSchema
 * JD-Core Version:    0.6.2
 */