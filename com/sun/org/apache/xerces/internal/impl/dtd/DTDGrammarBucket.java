/*     */ package com.sun.org.apache.xerces.internal.impl.dtd;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarDescription;
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ public class DTDGrammarBucket
/*     */ {
/*     */   protected Hashtable fGrammars;
/*     */   protected DTDGrammar fActiveGrammar;
/*     */   protected boolean fIsStandalone;
/*     */ 
/*     */   public DTDGrammarBucket()
/*     */   {
/* 105 */     this.fGrammars = new Hashtable();
/*     */   }
/*     */ 
/*     */   public void putGrammar(DTDGrammar grammar)
/*     */   {
/* 119 */     XMLDTDDescription desc = (XMLDTDDescription)grammar.getGrammarDescription();
/* 120 */     this.fGrammars.put(desc, grammar);
/*     */   }
/*     */ 
/*     */   public DTDGrammar getGrammar(XMLGrammarDescription desc)
/*     */   {
/* 125 */     return (DTDGrammar)this.fGrammars.get((XMLDTDDescription)desc);
/*     */   }
/*     */ 
/*     */   public void clear() {
/* 129 */     this.fGrammars.clear();
/* 130 */     this.fActiveGrammar = null;
/* 131 */     this.fIsStandalone = false;
/*     */   }
/*     */ 
/*     */   void setStandalone(boolean standalone)
/*     */   {
/* 138 */     this.fIsStandalone = standalone;
/*     */   }
/*     */ 
/*     */   boolean getStandalone() {
/* 142 */     return this.fIsStandalone;
/*     */   }
/*     */ 
/*     */   void setActiveGrammar(DTDGrammar grammar)
/*     */   {
/* 147 */     this.fActiveGrammar = grammar;
/*     */   }
/*     */   DTDGrammar getActiveGrammar() {
/* 150 */     return this.fActiveGrammar;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dtd.DTDGrammarBucket
 * JD-Core Version:    0.6.2
 */