/*    */ package com.sun.org.apache.xerces.internal.jaxp.validation;
/*    */ 
/*    */ import com.sun.org.apache.xerces.internal.xni.grammars.Grammar;
/*    */ import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarDescription;
/*    */ import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;
/*    */ 
/*    */ final class SimpleXMLSchema extends AbstractXMLSchema
/*    */   implements XMLGrammarPool
/*    */ {
/* 36 */   private static final Grammar[] ZERO_LENGTH_GRAMMAR_ARRAY = new Grammar[0];
/*    */   private Grammar fGrammar;
/*    */   private Grammar[] fGrammars;
/*    */   private XMLGrammarDescription fGrammarDescription;
/*    */ 
/*    */   public SimpleXMLSchema(Grammar grammar)
/*    */   {
/* 43 */     this.fGrammar = grammar;
/* 44 */     this.fGrammars = new Grammar[] { grammar };
/* 45 */     this.fGrammarDescription = grammar.getGrammarDescription();
/*    */   }
/*    */ 
/*    */   public Grammar[] retrieveInitialGrammarSet(String grammarType)
/*    */   {
/* 53 */     return "http://www.w3.org/2001/XMLSchema".equals(grammarType) ? (Grammar[])this.fGrammars.clone() : ZERO_LENGTH_GRAMMAR_ARRAY;
/*    */   }
/*    */ 
/*    */   public void cacheGrammars(String grammarType, Grammar[] grammars) {
/*    */   }
/*    */ 
/*    */   public Grammar retrieveGrammar(XMLGrammarDescription desc) {
/* 60 */     return this.fGrammarDescription.equals(desc) ? this.fGrammar : null;
/*    */   }
/*    */ 
/*    */   public void lockPool()
/*    */   {
/*    */   }
/*    */ 
/*    */   public void unlockPool() {
/*    */   }
/*    */ 
/*    */   public void clear() {
/*    */   }
/*    */ 
/*    */   public XMLGrammarPool getGrammarPool() {
/* 74 */     return this;
/*    */   }
/*    */ 
/*    */   public boolean isFullyComposed() {
/* 78 */     return true;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.jaxp.validation.SimpleXMLSchema
 * JD-Core Version:    0.6.2
 */