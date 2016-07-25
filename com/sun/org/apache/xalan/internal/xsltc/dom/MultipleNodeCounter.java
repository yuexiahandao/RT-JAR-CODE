/*     */ package com.sun.org.apache.xalan.internal.xsltc.dom;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.xsltc.DOM;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.Translet;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.util.IntegerArray;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
/*     */ 
/*     */ public abstract class MultipleNodeCounter extends NodeCounter
/*     */ {
/*  37 */   private DTMAxisIterator _precSiblings = null;
/*     */ 
/*     */   public MultipleNodeCounter(Translet translet, DOM document, DTMAxisIterator iterator)
/*     */   {
/*  41 */     super(translet, document, iterator);
/*     */   }
/*     */ 
/*     */   public MultipleNodeCounter(Translet translet, DOM document, DTMAxisIterator iterator, boolean hasFrom)
/*     */   {
/*  48 */     super(translet, document, iterator, hasFrom);
/*     */   }
/*     */ 
/*     */   public NodeCounter setStartNode(int node) {
/*  52 */     this._node = node;
/*  53 */     this._nodeType = this._document.getExpandedTypeID(node);
/*  54 */     this._precSiblings = this._document.getAxisIterator(12);
/*  55 */     return this;
/*     */   }
/*     */ 
/*     */   public String getCounter() {
/*  59 */     if (this._value != -2147483648.0D)
/*     */     {
/*  61 */       if (this._value == 0.0D) return "0";
/*  62 */       if (Double.isNaN(this._value)) return "NaN";
/*  63 */       if ((this._value < 0.0D) && (Double.isInfinite(this._value))) return "-Infinity";
/*  64 */       if (Double.isInfinite(this._value)) return "Infinity";
/*  65 */       return formatNumbers((int)this._value);
/*     */     }
/*     */ 
/*  68 */     IntegerArray ancestors = new IntegerArray();
/*     */ 
/*  71 */     int next = this._node;
/*  72 */     ancestors.add(next);
/*  73 */     while (((next = this._document.getParent(next)) > -1) && (!matchesFrom(next)))
/*     */     {
/*  75 */       ancestors.add(next);
/*     */     }
/*     */ 
/*  79 */     int nAncestors = ancestors.cardinality();
/*  80 */     int[] counters = new int[nAncestors];
/*  81 */     for (int i = 0; i < nAncestors; i++) {
/*  82 */       counters[i] = -2147483648;
/*     */     }
/*     */ 
/*  86 */     int j = 0; for (int i = nAncestors - 1; i >= 0; j++) {
/*  87 */       int counter = counters[j];
/*  88 */       int ancestor = ancestors.at(i);
/*     */ 
/*  90 */       if (matchesCount(ancestor)) {
/*  91 */         this._precSiblings.setStartNode(ancestor);
/*  92 */         while ((next = this._precSiblings.next()) != -1) {
/*  93 */           if (matchesCount(next)) {
/*  94 */             counters[j] = (counters[j] == -2147483648 ? 1 : counters[j] + 1);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*  99 */         counters[j] = (counters[j] == -2147483648 ? 1 : counters[j] + 1);
/*     */       }
/*  86 */       i--;
/*     */     }
/*     */ 
/* 104 */     return formatNumbers(counters);
/*     */   }
/*     */ 
/*     */   public static NodeCounter getDefaultNodeCounter(Translet translet, DOM document, DTMAxisIterator iterator)
/*     */   {
/* 110 */     return new DefaultMultipleNodeCounter(translet, document, iterator);
/*     */   }
/*     */ 
/*     */   static class DefaultMultipleNodeCounter extends MultipleNodeCounter
/*     */   {
/*     */     public DefaultMultipleNodeCounter(Translet translet, DOM document, DTMAxisIterator iterator)
/*     */     {
/* 117 */       super(document, iterator);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.dom.MultipleNodeCounter
 * JD-Core Version:    0.6.2
 */