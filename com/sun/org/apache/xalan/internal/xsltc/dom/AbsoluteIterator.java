/*    */ package com.sun.org.apache.xalan.internal.xsltc.dom;
/*    */ 
/*    */ import com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary;
/*    */ import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
/*    */ import com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase;
/*    */ 
/*    */ public final class AbsoluteIterator extends DTMAxisIteratorBase
/*    */ {
/*    */   private DTMAxisIterator _source;
/*    */ 
/*    */   public AbsoluteIterator(DTMAxisIterator source)
/*    */   {
/* 52 */     this._source = source;
/*    */   }
/*    */ 
/*    */   public void setRestartable(boolean isRestartable)
/*    */   {
/* 57 */     this._isRestartable = isRestartable;
/* 58 */     this._source.setRestartable(isRestartable);
/*    */   }
/*    */ 
/*    */   public DTMAxisIterator setStartNode(int node) {
/* 62 */     this._startNode = 0;
/* 63 */     if (this._isRestartable) {
/* 64 */       this._source.setStartNode(this._startNode);
/* 65 */       resetPosition();
/*    */     }
/* 67 */     return this;
/*    */   }
/*    */ 
/*    */   public int next() {
/* 71 */     return returnNode(this._source.next());
/*    */   }
/*    */ 
/*    */   public DTMAxisIterator cloneIterator() {
/*    */     try {
/* 76 */       AbsoluteIterator clone = (AbsoluteIterator)super.clone();
/* 77 */       clone._source = this._source.cloneIterator();
/* 78 */       clone.resetPosition();
/* 79 */       clone._isRestartable = false;
/* 80 */       return clone;
/*    */     }
/*    */     catch (CloneNotSupportedException e) {
/* 83 */       BasisLibrary.runTimeError("ITERATOR_CLONE_ERR", e.toString());
/*    */     }
/* 85 */     return null;
/*    */   }
/*    */ 
/*    */   public DTMAxisIterator reset()
/*    */   {
/* 90 */     this._source.reset();
/* 91 */     return resetPosition();
/*    */   }
/*    */ 
/*    */   public void setMark() {
/* 95 */     this._source.setMark();
/*    */   }
/*    */ 
/*    */   public void gotoMark() {
/* 99 */     this._source.gotoMark();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.dom.AbsoluteIterator
 * JD-Core Version:    0.6.2
 */