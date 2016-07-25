/*    */ package com.sun.org.apache.xalan.internal.xsltc.dom;
/*    */ 
/*    */ import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
/*    */ import com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase;
/*    */ 
/*    */ public class SingletonIterator extends DTMAxisIteratorBase
/*    */ {
/*    */   private int _node;
/*    */   private final boolean _isConstant;
/*    */ 
/*    */   public SingletonIterator()
/*    */   {
/* 38 */     this(-2147483648, false);
/*    */   }
/*    */ 
/*    */   public SingletonIterator(int node) {
/* 42 */     this(node, false);
/*    */   }
/*    */ 
/*    */   public SingletonIterator(int node, boolean constant) {
/* 46 */     this._node = (this._startNode = node);
/* 47 */     this._isConstant = constant;
/*    */   }
/*    */ 
/*    */   public DTMAxisIterator setStartNode(int node)
/*    */   {
/* 55 */     if (this._isConstant) {
/* 56 */       this._node = this._startNode;
/* 57 */       return resetPosition();
/*    */     }
/* 59 */     if (this._isRestartable) {
/* 60 */       if (this._node <= 0)
/* 61 */         this._node = (this._startNode = node);
/* 62 */       return resetPosition();
/*    */     }
/* 64 */     return this;
/*    */   }
/*    */ 
/*    */   public DTMAxisIterator reset() {
/* 68 */     if (this._isConstant) {
/* 69 */       this._node = this._startNode;
/* 70 */       return resetPosition();
/*    */     }
/*    */ 
/* 73 */     boolean temp = this._isRestartable;
/* 74 */     this._isRestartable = true;
/* 75 */     setStartNode(this._startNode);
/* 76 */     this._isRestartable = temp;
/*    */ 
/* 78 */     return this;
/*    */   }
/*    */ 
/*    */   public int next() {
/* 82 */     int result = this._node;
/* 83 */     this._node = -1;
/* 84 */     return returnNode(result);
/*    */   }
/*    */ 
/*    */   public void setMark() {
/* 88 */     this._markedNode = this._node;
/*    */   }
/*    */ 
/*    */   public void gotoMark() {
/* 92 */     this._node = this._markedNode;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.dom.SingletonIterator
 * JD-Core Version:    0.6.2
 */