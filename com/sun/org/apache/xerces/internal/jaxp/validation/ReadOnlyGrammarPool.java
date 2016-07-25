/*    */ package com.sun.org.apache.xerces.internal.jaxp.validation;
/*    */ 
/*    */ import com.sun.org.apache.xerces.internal.xni.grammars.Grammar;
/*    */ import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarDescription;
/*    */ import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;
/*    */ 
/*    */ final class ReadOnlyGrammarPool
/*    */   implements XMLGrammarPool
/*    */ {
/*    */   private final XMLGrammarPool core;
/*    */ 
/*    */   public ReadOnlyGrammarPool(XMLGrammarPool pool)
/*    */   {
/* 38 */     this.core = pool;
/*    */   }
/*    */ 
/*    */   public void cacheGrammars(String grammarType, Grammar[] grammars)
/*    */   {
/*    */   }
/*    */ 
/*    */   public void clear()
/*    */   {
/*    */   }
/*    */ 
/*    */   public void lockPool()
/*    */   {
/*    */   }
/*    */ 
/*    */   public Grammar retrieveGrammar(XMLGrammarDescription desc) {
/* 54 */     return this.core.retrieveGrammar(desc);
/*    */   }
/*    */ 
/*    */   public Grammar[] retrieveInitialGrammarSet(String grammarType) {
/* 58 */     return this.core.retrieveInitialGrammarSet(grammarType);
/*    */   }
/*    */ 
/*    */   public void unlockPool()
/*    */   {
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.jaxp.validation.ReadOnlyGrammarPool
 * JD-Core Version:    0.6.2
 */