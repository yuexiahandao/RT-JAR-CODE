/*     */ package com.sun.org.apache.xalan.internal.xsltc.dom;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.util.IntegerArray;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
/*     */ import com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase;
/*     */ 
/*     */ public final class DupFilterIterator extends DTMAxisIteratorBase
/*     */ {
/*     */   private DTMAxisIterator _source;
/*  48 */   private IntegerArray _nodes = new IntegerArray();
/*     */ 
/*  53 */   private int _current = 0;
/*     */ 
/*  58 */   private int _nodesSize = 0;
/*     */ 
/*  63 */   private int _lastNext = -1;
/*     */ 
/*  68 */   private int _markedLastNext = -1;
/*     */ 
/*     */   public DupFilterIterator(DTMAxisIterator source) {
/*  71 */     this._source = source;
/*     */ 
/*  77 */     if ((source instanceof KeyIndex))
/*  78 */       setStartNode(0);
/*     */   }
/*     */ 
/*     */   public DTMAxisIterator setStartNode(int node)
/*     */   {
/*  88 */     if (this._isRestartable)
/*     */     {
/*  91 */       boolean sourceIsKeyIndex = this._source instanceof KeyIndex;
/*     */ 
/*  93 */       if ((sourceIsKeyIndex) && (this._startNode == 0))
/*     */       {
/*  95 */         return this;
/*     */       }
/*     */ 
/*  98 */       if (node != this._startNode) {
/*  99 */         this._source.setStartNode(this._startNode = node);
/*     */ 
/* 101 */         this._nodes.clear();
/* 102 */         while ((node = this._source.next()) != -1) {
/* 103 */           this._nodes.add(node);
/*     */         }
/*     */ 
/* 108 */         if (!sourceIsKeyIndex) {
/* 109 */           this._nodes.sort();
/*     */         }
/* 111 */         this._nodesSize = this._nodes.cardinality();
/* 112 */         this._current = 0;
/* 113 */         this._lastNext = -1;
/* 114 */         resetPosition();
/*     */       }
/*     */     }
/* 117 */     return this;
/*     */   }
/*     */ 
/*     */   public int next() {
/* 121 */     while (this._current < this._nodesSize) {
/* 122 */       int next = this._nodes.at(this._current++);
/* 123 */       if (next != this._lastNext) {
/* 124 */         return returnNode(this._lastNext = next);
/*     */       }
/*     */     }
/* 127 */     return -1;
/*     */   }
/*     */ 
/*     */   public DTMAxisIterator cloneIterator() {
/*     */     try {
/* 132 */       DupFilterIterator clone = (DupFilterIterator)super.clone();
/*     */ 
/* 134 */       clone._nodes = ((IntegerArray)this._nodes.clone());
/* 135 */       clone._source = this._source.cloneIterator();
/* 136 */       clone._isRestartable = false;
/* 137 */       return clone.reset();
/*     */     }
/*     */     catch (CloneNotSupportedException e) {
/* 140 */       BasisLibrary.runTimeError("ITERATOR_CLONE_ERR", e.toString());
/*     */     }
/* 142 */     return null;
/*     */   }
/*     */ 
/*     */   public void setRestartable(boolean isRestartable)
/*     */   {
/* 147 */     this._isRestartable = isRestartable;
/* 148 */     this._source.setRestartable(isRestartable);
/*     */   }
/*     */ 
/*     */   public void setMark() {
/* 152 */     this._markedNode = this._current;
/* 153 */     this._markedLastNext = this._lastNext;
/*     */   }
/*     */ 
/*     */   public void gotoMark() {
/* 157 */     this._current = this._markedNode;
/* 158 */     this._lastNext = this._markedLastNext;
/*     */   }
/*     */ 
/*     */   public DTMAxisIterator reset() {
/* 162 */     this._current = 0;
/* 163 */     this._lastNext = -1;
/* 164 */     return resetPosition();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.dom.DupFilterIterator
 * JD-Core Version:    0.6.2
 */