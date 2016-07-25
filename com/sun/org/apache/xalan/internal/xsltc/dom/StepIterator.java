/*     */ package com.sun.org.apache.xalan.internal.xsltc.dom;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
/*     */ import com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase;
/*     */ 
/*     */ public class StepIterator extends DTMAxisIteratorBase
/*     */ {
/*     */   protected DTMAxisIterator _source;
/*     */   protected DTMAxisIterator _iterator;
/*  59 */   private int _pos = -1;
/*     */ 
/*     */   public StepIterator(DTMAxisIterator source, DTMAxisIterator iterator) {
/*  62 */     this._source = source;
/*  63 */     this._iterator = iterator;
/*     */   }
/*     */ 
/*     */   public void setRestartable(boolean isRestartable)
/*     */   {
/*  70 */     this._isRestartable = isRestartable;
/*  71 */     this._source.setRestartable(isRestartable);
/*  72 */     this._iterator.setRestartable(true);
/*     */   }
/*     */ 
/*     */   public DTMAxisIterator cloneIterator() {
/*  76 */     this._isRestartable = false;
/*     */     try {
/*  78 */       StepIterator clone = (StepIterator)super.clone();
/*  79 */       clone._source = this._source.cloneIterator();
/*  80 */       clone._iterator = this._iterator.cloneIterator();
/*  81 */       clone._iterator.setRestartable(true);
/*  82 */       clone._isRestartable = false;
/*  83 */       return clone.reset();
/*     */     }
/*     */     catch (CloneNotSupportedException e) {
/*  86 */       BasisLibrary.runTimeError("ITERATOR_CLONE_ERR", e.toString());
/*     */     }
/*  88 */     return null;
/*     */   }
/*     */ 
/*     */   public DTMAxisIterator setStartNode(int node)
/*     */   {
/*  93 */     if (this._isRestartable)
/*     */     {
/*  95 */       this._source.setStartNode(this._startNode = node);
/*     */ 
/*  99 */       this._iterator.setStartNode(this._includeSelf ? this._startNode : this._source.next());
/* 100 */       return resetPosition();
/*     */     }
/* 102 */     return this;
/*     */   }
/*     */ 
/*     */   public DTMAxisIterator reset() {
/* 106 */     this._source.reset();
/*     */ 
/* 108 */     this._iterator.setStartNode(this._includeSelf ? this._startNode : this._source.next());
/* 109 */     return resetPosition();
/*     */   }
/*     */ 
/*     */   public int next()
/*     */   {
/*     */     while (true)
/*     */     {
/*     */       int node;
/* 115 */       if ((node = this._iterator.next()) != -1) {
/* 116 */         return returnNode(node);
/*     */       }
/*     */ 
/* 119 */       if ((node = this._source.next()) == -1) {
/* 120 */         return -1;
/*     */       }
/*     */ 
/* 124 */       this._iterator.setStartNode(node);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setMark()
/*     */   {
/* 130 */     this._source.setMark();
/* 131 */     this._iterator.setMark();
/*     */   }
/*     */ 
/*     */   public void gotoMark()
/*     */   {
/* 136 */     this._source.gotoMark();
/* 137 */     this._iterator.gotoMark();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.dom.StepIterator
 * JD-Core Version:    0.6.2
 */