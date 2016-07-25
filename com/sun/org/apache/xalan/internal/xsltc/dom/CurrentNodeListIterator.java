/*     */ package com.sun.org.apache.xalan.internal.xsltc.dom;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.util.IntegerArray;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
/*     */ import com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase;
/*     */ 
/*     */ public final class CurrentNodeListIterator extends DTMAxisIteratorBase
/*     */ {
/*     */   private boolean _docOrder;
/*     */   private DTMAxisIterator _source;
/*     */   private final CurrentNodeListFilter _filter;
/*  65 */   private IntegerArray _nodes = new IntegerArray();
/*     */   private int _currentIndex;
/*     */   private final int _currentNode;
/*     */   private AbstractTranslet _translet;
/*     */ 
/*     */   public CurrentNodeListIterator(DTMAxisIterator source, CurrentNodeListFilter filter, int currentNode, AbstractTranslet translet)
/*     */   {
/*  87 */     this(source, !source.isReverse(), filter, currentNode, translet);
/*     */   }
/*     */ 
/*     */   public CurrentNodeListIterator(DTMAxisIterator source, boolean docOrder, CurrentNodeListFilter filter, int currentNode, AbstractTranslet translet)
/*     */   {
/*  95 */     this._source = source;
/*  96 */     this._filter = filter;
/*  97 */     this._translet = translet;
/*  98 */     this._docOrder = docOrder;
/*  99 */     this._currentNode = currentNode;
/*     */   }
/*     */ 
/*     */   public DTMAxisIterator forceNaturalOrder() {
/* 103 */     this._docOrder = true;
/* 104 */     return this;
/*     */   }
/*     */ 
/*     */   public void setRestartable(boolean isRestartable) {
/* 108 */     this._isRestartable = isRestartable;
/* 109 */     this._source.setRestartable(isRestartable);
/*     */   }
/*     */ 
/*     */   public boolean isReverse() {
/* 113 */     return !this._docOrder;
/*     */   }
/*     */ 
/*     */   public DTMAxisIterator cloneIterator() {
/*     */     try {
/* 118 */       CurrentNodeListIterator clone = (CurrentNodeListIterator)super.clone();
/*     */ 
/* 120 */       clone._nodes = ((IntegerArray)this._nodes.clone());
/* 121 */       clone._source = this._source.cloneIterator();
/* 122 */       clone._isRestartable = false;
/* 123 */       return clone.reset();
/*     */     }
/*     */     catch (CloneNotSupportedException e) {
/* 126 */       BasisLibrary.runTimeError("ITERATOR_CLONE_ERR", e.toString());
/*     */     }
/* 128 */     return null;
/*     */   }
/*     */ 
/*     */   public DTMAxisIterator reset()
/*     */   {
/* 133 */     this._currentIndex = 0;
/* 134 */     return resetPosition();
/*     */   }
/*     */ 
/*     */   public int next() {
/* 138 */     int last = this._nodes.cardinality();
/* 139 */     int currentNode = this._currentNode;
/* 140 */     AbstractTranslet translet = this._translet;
/*     */ 
/* 142 */     for (int index = this._currentIndex; index < last; ) {
/* 143 */       int position = this._docOrder ? index + 1 : last - index;
/* 144 */       int node = this._nodes.at(index++);
/*     */ 
/* 146 */       if (this._filter.test(node, position, last, currentNode, translet, this))
/*     */       {
/* 148 */         this._currentIndex = index;
/* 149 */         return returnNode(node);
/*     */       }
/*     */     }
/* 152 */     return -1;
/*     */   }
/*     */ 
/*     */   public DTMAxisIterator setStartNode(int node) {
/* 156 */     if (this._isRestartable) {
/* 157 */       this._source.setStartNode(this._startNode = node);
/*     */ 
/* 159 */       this._nodes.clear();
/* 160 */       while ((node = this._source.next()) != -1) {
/* 161 */         this._nodes.add(node);
/*     */       }
/* 163 */       this._currentIndex = 0;
/* 164 */       resetPosition();
/*     */     }
/* 166 */     return this;
/*     */   }
/*     */ 
/*     */   public int getLast() {
/* 170 */     if (this._last == -1) {
/* 171 */       this._last = computePositionOfLast();
/*     */     }
/* 173 */     return this._last;
/*     */   }
/*     */ 
/*     */   public void setMark() {
/* 177 */     this._markedNode = this._currentIndex;
/*     */   }
/*     */ 
/*     */   public void gotoMark() {
/* 181 */     this._currentIndex = this._markedNode;
/*     */   }
/*     */ 
/*     */   private int computePositionOfLast() {
/* 185 */     int last = this._nodes.cardinality();
/* 186 */     int currNode = this._currentNode;
/* 187 */     AbstractTranslet translet = this._translet;
/*     */ 
/* 189 */     int lastPosition = this._position;
/* 190 */     for (int index = this._currentIndex; index < last; ) {
/* 191 */       int position = this._docOrder ? index + 1 : last - index;
/* 192 */       int nodeIndex = this._nodes.at(index++);
/*     */ 
/* 194 */       if (this._filter.test(nodeIndex, position, last, currNode, translet, this))
/*     */       {
/* 196 */         lastPosition++;
/*     */       }
/*     */     }
/* 199 */     return lastPosition;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.dom.CurrentNodeListIterator
 * JD-Core Version:    0.6.2
 */