/*     */ package com.sun.org.apache.xalan.internal.xsltc.dom;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
/*     */ import com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase;
/*     */ 
/*     */ public final class NthIterator extends DTMAxisIteratorBase
/*     */ {
/*     */   private DTMAxisIterator _source;
/*     */   private final int _position;
/*     */   private boolean _ready;
/*     */ 
/*     */   public NthIterator(DTMAxisIterator source, int n)
/*     */   {
/*  41 */     this._source = source;
/*  42 */     this._position = n;
/*     */   }
/*     */ 
/*     */   public void setRestartable(boolean isRestartable) {
/*  46 */     this._isRestartable = isRestartable;
/*  47 */     this._source.setRestartable(isRestartable);
/*     */   }
/*     */ 
/*     */   public DTMAxisIterator cloneIterator() {
/*     */     try {
/*  52 */       NthIterator clone = (NthIterator)super.clone();
/*  53 */       clone._source = this._source.cloneIterator();
/*  54 */       clone._isRestartable = false;
/*  55 */       return clone;
/*     */     }
/*     */     catch (CloneNotSupportedException e) {
/*  58 */       BasisLibrary.runTimeError("ITERATOR_CLONE_ERR", e.toString());
/*     */     }
/*  60 */     return null;
/*     */   }
/*     */ 
/*     */   public int next()
/*     */   {
/*  65 */     if (this._ready) {
/*  66 */       this._ready = false;
/*  67 */       return this._source.getNodeByPosition(this._position);
/*     */     }
/*  69 */     return -1;
/*     */   }
/*     */ 
/*     */   public DTMAxisIterator setStartNode(int node)
/*     */   {
/*  89 */     if (this._isRestartable) {
/*  90 */       this._source.setStartNode(node);
/*  91 */       this._ready = true;
/*     */     }
/*  93 */     return this;
/*     */   }
/*     */ 
/*     */   public DTMAxisIterator reset() {
/*  97 */     this._source.reset();
/*  98 */     this._ready = true;
/*  99 */     return this;
/*     */   }
/*     */ 
/*     */   public int getLast() {
/* 103 */     return 1;
/*     */   }
/*     */ 
/*     */   public int getPosition() {
/* 107 */     return 1;
/*     */   }
/*     */ 
/*     */   public void setMark() {
/* 111 */     this._source.setMark();
/*     */   }
/*     */ 
/*     */   public void gotoMark() {
/* 115 */     this._source.gotoMark();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.dom.NthIterator
 * JD-Core Version:    0.6.2
 */