/*     */ package com.sun.org.apache.xerces.internal.parsers;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.util.ShadowedSymbolTable;
/*     */ import com.sun.org.apache.xerces.internal.util.SymbolTable;
/*     */ import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;
/*     */ import com.sun.org.apache.xerces.internal.util.XMLGrammarPoolImpl;
/*     */ import com.sun.org.apache.xerces.internal.xni.grammars.Grammar;
/*     */ import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarDescription;
/*     */ import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;
/*     */ 
/*     */ public class CachingParserPool
/*     */ {
/*     */   public static final boolean DEFAULT_SHADOW_SYMBOL_TABLE = false;
/*     */   public static final boolean DEFAULT_SHADOW_GRAMMAR_POOL = false;
/*     */   protected SymbolTable fSynchronizedSymbolTable;
/*     */   protected XMLGrammarPool fSynchronizedGrammarPool;
/*  99 */   protected boolean fShadowSymbolTable = false;
/*     */ 
/* 108 */   protected boolean fShadowGrammarPool = false;
/*     */ 
/*     */   public CachingParserPool()
/*     */   {
/* 116 */     this(new SymbolTable(), new XMLGrammarPoolImpl());
/*     */   }
/*     */ 
/*     */   public CachingParserPool(SymbolTable symbolTable, XMLGrammarPool grammarPool)
/*     */   {
/* 127 */     this.fSynchronizedSymbolTable = new SynchronizedSymbolTable(symbolTable);
/* 128 */     this.fSynchronizedGrammarPool = new SynchronizedGrammarPool(grammarPool);
/*     */   }
/*     */ 
/*     */   public SymbolTable getSymbolTable()
/*     */   {
/* 137 */     return this.fSynchronizedSymbolTable;
/*     */   }
/*     */ 
/*     */   public XMLGrammarPool getXMLGrammarPool()
/*     */   {
/* 142 */     return this.fSynchronizedGrammarPool;
/*     */   }
/*     */ 
/*     */   public void setShadowSymbolTable(boolean shadow)
/*     */   {
/* 160 */     this.fShadowSymbolTable = shadow;
/*     */   }
/*     */ 
/*     */   public DOMParser createDOMParser()
/*     */   {
/* 167 */     SymbolTable symbolTable = this.fShadowSymbolTable ? new ShadowedSymbolTable(this.fSynchronizedSymbolTable) : this.fSynchronizedSymbolTable;
/*     */ 
/* 170 */     XMLGrammarPool grammarPool = this.fShadowGrammarPool ? new ShadowedGrammarPool(this.fSynchronizedGrammarPool) : this.fSynchronizedGrammarPool;
/*     */ 
/* 173 */     return new DOMParser(symbolTable, grammarPool);
/*     */   }
/*     */ 
/*     */   public SAXParser createSAXParser()
/*     */   {
/* 178 */     SymbolTable symbolTable = this.fShadowSymbolTable ? new ShadowedSymbolTable(this.fSynchronizedSymbolTable) : this.fSynchronizedSymbolTable;
/*     */ 
/* 181 */     XMLGrammarPool grammarPool = this.fShadowGrammarPool ? new ShadowedGrammarPool(this.fSynchronizedGrammarPool) : this.fSynchronizedGrammarPool;
/*     */ 
/* 184 */     return new SAXParser(symbolTable, grammarPool);
/*     */   }
/*     */ 
/*     */   public static final class ShadowedGrammarPool extends XMLGrammarPoolImpl
/*     */   {
/*     */     private XMLGrammarPool fGrammarPool;
/*     */ 
/*     */     public ShadowedGrammarPool(XMLGrammarPool grammarPool)
/*     */     {
/* 357 */       this.fGrammarPool = grammarPool;
/*     */     }
/*     */ 
/*     */     public Grammar[] retrieveInitialGrammarSet(String grammarType)
/*     */     {
/* 372 */       Grammar[] grammars = super.retrieveInitialGrammarSet(grammarType);
/* 373 */       if (grammars != null) return grammars;
/* 374 */       return this.fGrammarPool.retrieveInitialGrammarSet(grammarType);
/*     */     }
/*     */ 
/*     */     public Grammar retrieveGrammar(XMLGrammarDescription gDesc)
/*     */     {
/* 385 */       Grammar g = super.retrieveGrammar(gDesc);
/* 386 */       if (g != null) return g;
/* 387 */       return this.fGrammarPool.retrieveGrammar(gDesc);
/*     */     }
/*     */ 
/*     */     public void cacheGrammars(String grammarType, Grammar[] grammars)
/*     */     {
/* 400 */       super.cacheGrammars(grammarType, grammars);
/* 401 */       this.fGrammarPool.cacheGrammars(grammarType, grammars);
/*     */     }
/*     */ 
/*     */     public Grammar getGrammar(XMLGrammarDescription desc)
/*     */     {
/* 411 */       if (super.containsGrammar(desc)) {
/* 412 */         return super.getGrammar(desc);
/*     */       }
/* 414 */       return null;
/*     */     }
/*     */ 
/*     */     public boolean containsGrammar(XMLGrammarDescription desc)
/*     */     {
/* 425 */       return super.containsGrammar(desc);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final class SynchronizedGrammarPool
/*     */     implements XMLGrammarPool
/*     */   {
/*     */     private XMLGrammarPool fGrammarPool;
/*     */ 
/*     */     public SynchronizedGrammarPool(XMLGrammarPool grammarPool)
/*     */     {
/* 212 */       this.fGrammarPool = grammarPool;
/*     */     }
/*     */ 
/*     */     public Grammar[] retrieveInitialGrammarSet(String grammarType)
/*     */     {
/* 225 */       synchronized (this.fGrammarPool) {
/* 226 */         return this.fGrammarPool.retrieveInitialGrammarSet(grammarType);
/*     */       }
/*     */     }
/*     */ 
/*     */     public Grammar retrieveGrammar(XMLGrammarDescription gDesc)
/*     */     {
/* 235 */       synchronized (this.fGrammarPool) {
/* 236 */         return this.fGrammarPool.retrieveGrammar(gDesc);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void cacheGrammars(String grammarType, Grammar[] grammars)
/*     */     {
/* 246 */       synchronized (this.fGrammarPool) {
/* 247 */         this.fGrammarPool.cacheGrammars(grammarType, grammars);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void lockPool()
/*     */     {
/* 253 */       synchronized (this.fGrammarPool) {
/* 254 */         this.fGrammarPool.lockPool();
/*     */       }
/*     */     }
/*     */ 
/*     */     public void clear()
/*     */     {
/* 260 */       synchronized (this.fGrammarPool) {
/* 261 */         this.fGrammarPool.clear();
/*     */       }
/*     */     }
/*     */ 
/*     */     public void unlockPool()
/*     */     {
/* 267 */       synchronized (this.fGrammarPool) {
/* 268 */         this.fGrammarPool.unlockPool();
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.parsers.CachingParserPool
 * JD-Core Version:    0.6.2
 */