/*    */ package com.sun.org.apache.xerces.internal.jaxp.validation;
/*    */ 
/*    */ import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;
/*    */ import java.lang.ref.WeakReference;
/*    */ 
/*    */ final class WeakReferenceXMLSchema extends AbstractXMLSchema
/*    */ {
/* 39 */   private WeakReference fGrammarPool = new WeakReference(null);
/*    */ 
/*    */   public synchronized XMLGrammarPool getGrammarPool()
/*    */   {
/* 48 */     XMLGrammarPool grammarPool = (XMLGrammarPool)this.fGrammarPool.get();
/*    */ 
/* 51 */     if (grammarPool == null) {
/* 52 */       grammarPool = new SoftReferenceGrammarPool();
/* 53 */       this.fGrammarPool = new WeakReference(grammarPool);
/*    */     }
/* 55 */     return grammarPool;
/*    */   }
/*    */ 
/*    */   public boolean isFullyComposed() {
/* 59 */     return false;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.jaxp.validation.WeakReferenceXMLSchema
 * JD-Core Version:    0.6.2
 */