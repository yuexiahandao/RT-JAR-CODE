/*     */ package com.sun.org.apache.xalan.internal.xsltc.dom;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.xsltc.NodeIterator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary;
/*     */ 
/*     */ public abstract class NodeIteratorBase
/*     */   implements NodeIterator
/*     */ {
/*  39 */   protected int _last = -1;
/*     */ 
/*  45 */   protected int _position = 0;
/*     */   protected int _markedNode;
/*  55 */   protected int _startNode = -1;
/*     */ 
/*  60 */   protected boolean _includeSelf = false;
/*     */ 
/*  65 */   protected boolean _isRestartable = true;
/*     */ 
/*     */   public void setRestartable(boolean isRestartable)
/*     */   {
/*  71 */     this._isRestartable = isRestartable;
/*     */   }
/*     */ 
/*     */   public abstract NodeIterator setStartNode(int paramInt);
/*     */ 
/*     */   public NodeIterator reset()
/*     */   {
/*  86 */     boolean temp = this._isRestartable;
/*  87 */     this._isRestartable = true;
/*     */ 
/*  89 */     setStartNode(this._includeSelf ? this._startNode + 1 : this._startNode);
/*  90 */     this._isRestartable = temp;
/*  91 */     return this;
/*     */   }
/*     */ 
/*     */   public NodeIterator includeSelf()
/*     */   {
/*  98 */     this._includeSelf = true;
/*  99 */     return this;
/*     */   }
/*     */ 
/*     */   public int getLast()
/*     */   {
/* 108 */     if (this._last == -1) {
/* 109 */       int temp = this._position;
/* 110 */       setMark();
/* 111 */       reset();
/*     */       do
/* 113 */         this._last += 1;
/* 114 */       while (next() != -1);
/* 115 */       gotoMark();
/* 116 */       this._position = temp;
/*     */     }
/* 118 */     return this._last;
/*     */   }
/*     */ 
/*     */   public int getPosition()
/*     */   {
/* 125 */     return this._position == 0 ? 1 : this._position;
/*     */   }
/*     */ 
/*     */   public boolean isReverse()
/*     */   {
/* 134 */     return false;
/*     */   }
/*     */ 
/*     */   public NodeIterator cloneIterator()
/*     */   {
/*     */     try
/*     */     {
/* 145 */       NodeIteratorBase clone = (NodeIteratorBase)super.clone();
/* 146 */       clone._isRestartable = false;
/* 147 */       return clone.reset();
/*     */     }
/*     */     catch (CloneNotSupportedException e) {
/* 150 */       BasisLibrary.runTimeError("ITERATOR_CLONE_ERR", e.toString());
/*     */     }
/* 152 */     return null;
/*     */   }
/*     */ 
/*     */   protected final int returnNode(int node)
/*     */   {
/* 161 */     this._position += 1;
/* 162 */     return node;
/*     */   }
/*     */ 
/*     */   protected final NodeIterator resetPosition()
/*     */   {
/* 169 */     this._position = 0;
/* 170 */     return this;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.dom.NodeIteratorBase
 * JD-Core Version:    0.6.2
 */