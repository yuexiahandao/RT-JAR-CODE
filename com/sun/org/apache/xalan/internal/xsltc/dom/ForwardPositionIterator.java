/*     */ package com.sun.org.apache.xalan.internal.xsltc.dom;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
/*     */ import com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase;
/*     */ 
/*     */ /** @deprecated */
/*     */ public final class ForwardPositionIterator extends DTMAxisIteratorBase
/*     */ {
/*     */   private DTMAxisIterator _source;
/*     */ 
/*     */   public ForwardPositionIterator(DTMAxisIterator source)
/*     */   {
/*  70 */     this._source = source;
/*     */   }
/*     */ 
/*     */   public DTMAxisIterator cloneIterator() {
/*     */     try {
/*  75 */       ForwardPositionIterator clone = (ForwardPositionIterator)super.clone();
/*     */ 
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
/*     */   public int next()
/*     */   {
/*  89 */     return returnNode(this._source.next());
/*     */   }
/*     */ 
/*     */   public DTMAxisIterator setStartNode(int node) {
/*  93 */     this._source.setStartNode(node);
/*  94 */     return this;
/*     */   }
/*     */ 
/*     */   public DTMAxisIterator reset() {
/*  98 */     this._source.reset();
/*  99 */     return resetPosition();
/*     */   }
/*     */ 
/*     */   public void setMark() {
/* 103 */     this._source.setMark();
/*     */   }
/*     */ 
/*     */   public void gotoMark() {
/* 107 */     this._source.gotoMark();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.dom.ForwardPositionIterator
 * JD-Core Version:    0.6.2
 */