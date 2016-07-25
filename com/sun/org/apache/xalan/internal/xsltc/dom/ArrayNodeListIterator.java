/*    */ package com.sun.org.apache.xalan.internal.xsltc.dom;
/*    */ 
/*    */ import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
/*    */ 
/*    */ public class ArrayNodeListIterator
/*    */   implements DTMAxisIterator
/*    */ {
/* 30 */   private int _pos = 0;
/*    */ 
/* 32 */   private int _mark = 0;
/*    */   private int[] _nodes;
/* 36 */   private static final int[] EMPTY = new int[0];
/*    */ 
/*    */   public ArrayNodeListIterator(int[] nodes) {
/* 39 */     this._nodes = nodes;
/*    */   }
/*    */ 
/*    */   public int next() {
/* 43 */     return this._pos < this._nodes.length ? this._nodes[(this._pos++)] : -1;
/*    */   }
/*    */ 
/*    */   public DTMAxisIterator reset() {
/* 47 */     this._pos = 0;
/* 48 */     return this;
/*    */   }
/*    */ 
/*    */   public int getLast() {
/* 52 */     return this._nodes.length;
/*    */   }
/*    */ 
/*    */   public int getPosition() {
/* 56 */     return this._pos;
/*    */   }
/*    */ 
/*    */   public void setMark() {
/* 60 */     this._mark = this._pos;
/*    */   }
/*    */ 
/*    */   public void gotoMark() {
/* 64 */     this._pos = this._mark;
/*    */   }
/*    */ 
/*    */   public DTMAxisIterator setStartNode(int node) {
/* 68 */     if (node == -1) this._nodes = EMPTY;
/* 69 */     return this;
/*    */   }
/*    */ 
/*    */   public int getStartNode() {
/* 73 */     return -1;
/*    */   }
/*    */ 
/*    */   public boolean isReverse() {
/* 77 */     return false;
/*    */   }
/*    */ 
/*    */   public DTMAxisIterator cloneIterator() {
/* 81 */     return new ArrayNodeListIterator(this._nodes);
/*    */   }
/*    */ 
/*    */   public void setRestartable(boolean isRestartable) {
/*    */   }
/*    */ 
/*    */   public int getNodeByPosition(int position) {
/* 88 */     return this._nodes[(position - 1)];
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.dom.ArrayNodeListIterator
 * JD-Core Version:    0.6.2
 */