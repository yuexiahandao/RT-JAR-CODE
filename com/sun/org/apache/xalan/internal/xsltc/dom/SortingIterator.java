/*     */ package com.sun.org.apache.xalan.internal.xsltc.dom;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
/*     */ import com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase;
/*     */ 
/*     */ public final class SortingIterator extends DTMAxisIteratorBase
/*     */ {
/*     */   private static final int INIT_DATA_SIZE = 16;
/*     */   private DTMAxisIterator _source;
/*     */   private NodeSortRecordFactory _factory;
/*     */   private NodeSortRecord[] _data;
/*  42 */   private int _free = 0;
/*     */   private int _current;
/*     */ 
/*     */   public SortingIterator(DTMAxisIterator source, NodeSortRecordFactory factory)
/*     */   {
/*  47 */     this._source = source;
/*  48 */     this._factory = factory;
/*     */   }
/*     */ 
/*     */   public int next() {
/*  52 */     return this._current < this._free ? this._data[(this._current++)].getNode() : -1;
/*     */   }
/*     */ 
/*     */   public DTMAxisIterator setStartNode(int node) {
/*     */     try {
/*  57 */       this._source.setStartNode(this._startNode = node);
/*  58 */       this._data = new NodeSortRecord[16];
/*  59 */       this._free = 0;
/*     */ 
/*  62 */       while ((node = this._source.next()) != -1) {
/*  63 */         addRecord(this._factory.makeNodeSortRecord(node, this._free));
/*     */       }
/*     */ 
/*  66 */       quicksort(0, this._free - 1);
/*     */ 
/*  68 */       this._current = 0;
/*  69 */       return this;
/*     */     } catch (Exception e) {
/*     */     }
/*  72 */     return this;
/*     */   }
/*     */ 
/*     */   public int getPosition()
/*     */   {
/*  77 */     return this._current == 0 ? 1 : this._current;
/*     */   }
/*     */ 
/*     */   public int getLast() {
/*  81 */     return this._free;
/*     */   }
/*     */ 
/*     */   public void setMark() {
/*  85 */     this._source.setMark();
/*  86 */     this._markedNode = this._current;
/*     */   }
/*     */ 
/*     */   public void gotoMark() {
/*  90 */     this._source.gotoMark();
/*  91 */     this._current = this._markedNode;
/*     */   }
/*     */ 
/*     */   public DTMAxisIterator cloneIterator()
/*     */   {
/*     */     try
/*     */     {
/* 101 */       SortingIterator clone = (SortingIterator)super.clone();
/* 102 */       clone._source = this._source.cloneIterator();
/* 103 */       clone._factory = this._factory;
/* 104 */       clone._data = this._data;
/* 105 */       clone._free = this._free;
/* 106 */       clone._current = this._current;
/* 107 */       clone.setRestartable(false);
/* 108 */       return clone.reset();
/*     */     }
/*     */     catch (CloneNotSupportedException e) {
/* 111 */       BasisLibrary.runTimeError("ITERATOR_CLONE_ERR", e.toString());
/*     */     }
/* 113 */     return null;
/*     */   }
/*     */ 
/*     */   private void addRecord(NodeSortRecord record)
/*     */   {
/* 118 */     if (this._free == this._data.length) {
/* 119 */       NodeSortRecord[] newArray = new NodeSortRecord[this._data.length * 2];
/* 120 */       System.arraycopy(this._data, 0, newArray, 0, this._free);
/* 121 */       this._data = newArray;
/*     */     }
/* 123 */     this._data[(this._free++)] = record;
/*     */   }
/*     */ 
/*     */   private void quicksort(int p, int r) {
/* 127 */     while (p < r) {
/* 128 */       int q = partition(p, r);
/* 129 */       quicksort(p, q);
/* 130 */       p = q + 1;
/*     */     }
/*     */   }
/*     */ 
/*     */   private int partition(int p, int r) {
/* 135 */     NodeSortRecord x = this._data[(p + r >>> 1)];
/* 136 */     int i = p - 1;
/* 137 */     int j = r + 1;
/*     */     while (true) {
/* 139 */       if (x.compareTo(this._data[(--j)]) >= 0) {
/* 140 */         while (x.compareTo(this._data[(++i)]) > 0);
/* 141 */         if (i >= j) break;
/* 142 */         NodeSortRecord t = this._data[i];
/* 143 */         this._data[i] = this._data[j];
/* 144 */         this._data[j] = t;
/*     */       }
/*     */     }
/* 147 */     return j;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.dom.SortingIterator
 * JD-Core Version:    0.6.2
 */