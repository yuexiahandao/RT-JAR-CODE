/*     */ package com.sun.org.apache.xalan.internal.xsltc.dom;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
/*     */ import com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase;
/*     */ 
/*     */ public final class MatchingIterator extends DTMAxisIteratorBase
/*     */ {
/*     */   private DTMAxisIterator _source;
/*     */   private final int _match;
/*     */ 
/*     */   public MatchingIterator(int match, DTMAxisIterator source)
/*     */   {
/*  63 */     this._source = source;
/*  64 */     this._match = match;
/*     */   }
/*     */ 
/*     */   public void setRestartable(boolean isRestartable)
/*     */   {
/*  69 */     this._isRestartable = isRestartable;
/*  70 */     this._source.setRestartable(isRestartable);
/*     */   }
/*     */ 
/*     */   public DTMAxisIterator cloneIterator()
/*     */   {
/*     */     try {
/*  76 */       MatchingIterator clone = (MatchingIterator)super.clone();
/*  77 */       clone._source = this._source.cloneIterator();
/*  78 */       clone._isRestartable = false;
/*  79 */       return clone.reset();
/*     */     }
/*     */     catch (CloneNotSupportedException e) {
/*  82 */       BasisLibrary.runTimeError("ITERATOR_CLONE_ERR", e.toString());
/*     */     }
/*  84 */     return null;
/*     */   }
/*     */ 
/*     */   public DTMAxisIterator setStartNode(int node)
/*     */   {
/*  89 */     if (this._isRestartable)
/*     */     {
/*  91 */       this._source.setStartNode(node);
/*     */ 
/*  94 */       this._position = 1;
/*  95 */       while (((node = this._source.next()) != -1) && (node != this._match)) {
/*  96 */         this._position += 1;
/*     */       }
/*     */     }
/*  99 */     return this;
/*     */   }
/*     */ 
/*     */   public DTMAxisIterator reset() {
/* 103 */     this._source.reset();
/* 104 */     return resetPosition();
/*     */   }
/*     */ 
/*     */   public int next() {
/* 108 */     return this._source.next();
/*     */   }
/*     */ 
/*     */   public int getLast() {
/* 112 */     if (this._last == -1) {
/* 113 */       this._last = this._source.getLast();
/*     */     }
/* 115 */     return this._last;
/*     */   }
/*     */ 
/*     */   public int getPosition() {
/* 119 */     return this._position;
/*     */   }
/*     */ 
/*     */   public void setMark() {
/* 123 */     this._source.setMark();
/*     */   }
/*     */ 
/*     */   public void gotoMark() {
/* 127 */     this._source.gotoMark();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.dom.MatchingIterator
 * JD-Core Version:    0.6.2
 */