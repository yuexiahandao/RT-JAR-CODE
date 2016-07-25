/*     */ package com.sun.org.apache.xalan.internal.xsltc.dom;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
/*     */ import com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase;
/*     */ 
/*     */ public abstract class MultiValuedNodeHeapIterator extends DTMAxisIteratorBase
/*     */ {
/*     */   private static final int InitSize = 8;
/* 136 */   private int _heapSize = 0;
/* 137 */   private int _size = 8;
/* 138 */   private HeapNode[] _heap = new HeapNode[8];
/* 139 */   private int _free = 0;
/*     */   private int _returnedLast;
/* 146 */   private int _cachedReturnedLast = -1;
/*     */   private int _cachedHeapSize;
/*     */ 
/*     */   public DTMAxisIterator cloneIterator()
/*     */   {
/* 153 */     this._isRestartable = false;
/* 154 */     HeapNode[] heapCopy = new HeapNode[this._heap.length];
/*     */     try {
/* 156 */       MultiValuedNodeHeapIterator clone = (MultiValuedNodeHeapIterator)super.clone();
/*     */ 
/* 159 */       for (int i = 0; i < this._free; i++) {
/* 160 */         heapCopy[i] = this._heap[i].cloneHeapNode();
/*     */       }
/* 162 */       clone.setRestartable(false);
/* 163 */       clone._heap = heapCopy;
/* 164 */       return clone.reset();
/*     */     }
/*     */     catch (CloneNotSupportedException e) {
/* 167 */       BasisLibrary.runTimeError("ITERATOR_CLONE_ERR", e.toString());
/*     */     }
/* 169 */     return null;
/*     */   }
/*     */ 
/*     */   protected void addHeapNode(HeapNode node)
/*     */   {
/* 174 */     if (this._free == this._size) {
/* 175 */       HeapNode[] newArray = new HeapNode[this._size *= 2];
/* 176 */       System.arraycopy(this._heap, 0, newArray, 0, this._free);
/* 177 */       this._heap = newArray;
/*     */     }
/* 179 */     this._heapSize += 1;
/* 180 */     this._heap[(this._free++)] = node;
/*     */   }
/*     */ 
/*     */   public int next() {
/* 184 */     while (this._heapSize > 0) {
/* 185 */       int smallest = this._heap[0]._node;
/* 186 */       if (smallest == -1) {
/* 187 */         if (this._heapSize > 1)
/*     */         {
/* 189 */           HeapNode temp = this._heap[0];
/* 190 */           this._heap[0] = this._heap[(--this._heapSize)];
/* 191 */           this._heap[this._heapSize] = temp;
/*     */         }
/*     */         else {
/* 194 */           return -1;
/*     */         }
/*     */       }
/* 197 */       else if (smallest == this._returnedLast) {
/* 198 */         this._heap[0].step();
/*     */       }
/*     */       else {
/* 201 */         this._heap[0].step();
/* 202 */         heapify(0);
/* 203 */         return returnNode(this._returnedLast = smallest);
/*     */       }
/*     */ 
/* 206 */       heapify(0);
/*     */     }
/* 208 */     return -1;
/*     */   }
/*     */ 
/*     */   public DTMAxisIterator setStartNode(int node) {
/* 212 */     if (this._isRestartable) {
/* 213 */       this._startNode = node;
/* 214 */       for (int i = 0; i < this._free; i++) {
/* 215 */         if (!this._heap[i]._isStartSet) {
/* 216 */           this._heap[i].setStartNode(node);
/* 217 */           this._heap[i].step();
/* 218 */           this._heap[i]._isStartSet = true;
/*     */         }
/*     */       }
/*     */ 
/* 222 */       for (int i = (this._heapSize = this._free) / 2; i >= 0; i--) {
/* 223 */         heapify(i);
/*     */       }
/* 225 */       this._returnedLast = -1;
/* 226 */       return resetPosition();
/*     */     }
/* 228 */     return this;
/*     */   }
/*     */ 
/*     */   protected void init() {
/* 232 */     for (int i = 0; i < this._free; i++) {
/* 233 */       this._heap[i] = null;
/*     */     }
/*     */ 
/* 236 */     this._heapSize = 0;
/* 237 */     this._free = 0;
/*     */   }
/*     */ 
/*     */   private void heapify(int i)
/*     */   {
/*     */     while (true)
/*     */     {
/* 245 */       int r = i + 1 << 1; int l = r - 1;
/* 246 */       int smallest = (l < this._heapSize) && (this._heap[l].isLessThan(this._heap[i])) ? l : i;
/*     */ 
/* 248 */       if ((r < this._heapSize) && (this._heap[r].isLessThan(this._heap[smallest]))) {
/* 249 */         smallest = r;
/*     */       }
/* 251 */       if (smallest == i) break;
/* 252 */       HeapNode temp = this._heap[smallest];
/* 253 */       this._heap[smallest] = this._heap[i];
/* 254 */       this._heap[i] = temp;
/* 255 */       i = smallest;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setMark()
/*     */   {
/* 263 */     for (int i = 0; i < this._free; i++) {
/* 264 */       this._heap[i].setMark();
/*     */     }
/* 266 */     this._cachedReturnedLast = this._returnedLast;
/* 267 */     this._cachedHeapSize = this._heapSize;
/*     */   }
/*     */ 
/*     */   public void gotoMark() {
/* 271 */     for (int i = 0; i < this._free; i++) {
/* 272 */       this._heap[i].gotoMark();
/*     */     }
/*     */ 
/* 275 */     for (int i = (this._heapSize = this._cachedHeapSize) / 2; i >= 0; i--) {
/* 276 */       heapify(i);
/*     */     }
/* 278 */     this._returnedLast = this._cachedReturnedLast;
/*     */   }
/*     */ 
/*     */   public DTMAxisIterator reset() {
/* 282 */     for (int i = 0; i < this._free; i++) {
/* 283 */       this._heap[i].reset();
/* 284 */       this._heap[i].step();
/*     */     }
/*     */ 
/* 288 */     for (int i = (this._heapSize = this._free) / 2; i >= 0; i--) {
/* 289 */       heapify(i);
/*     */     }
/*     */ 
/* 292 */     this._returnedLast = -1;
/* 293 */     return resetPosition();
/*     */   }
/*     */ 
/*     */   public abstract class HeapNode
/*     */     implements Cloneable
/*     */   {
/*     */     protected int _node;
/*     */     protected int _markedNode;
/*  59 */     protected boolean _isStartSet = false;
/*     */ 
/*     */     public HeapNode()
/*     */     {
/*     */     }
/*     */ 
/*     */     public abstract int step();
/*     */ 
/*     */     public HeapNode cloneHeapNode()
/*     */     {
/*     */       HeapNode clone;
/*     */       try
/*     */       {
/*  79 */         clone = (HeapNode)super.clone();
/*     */       } catch (CloneNotSupportedException e) {
/*  81 */         BasisLibrary.runTimeError("ITERATOR_CLONE_ERR", e.toString());
/*     */ 
/*  83 */         return null;
/*     */       }
/*     */ 
/*  86 */       clone._node = this._node;
/*  87 */       clone._markedNode = this._node;
/*     */ 
/*  89 */       return clone;
/*     */     }
/*     */ 
/*     */     public void setMark()
/*     */     {
/*  96 */       this._markedNode = this._node;
/*     */     }
/*     */ 
/*     */     public void gotoMark()
/*     */     {
/* 103 */       this._node = this._markedNode;
/*     */     }
/*     */ 
/*     */     public abstract boolean isLessThan(HeapNode paramHeapNode);
/*     */ 
/*     */     public abstract HeapNode setStartNode(int paramInt);
/*     */ 
/*     */     public abstract HeapNode reset();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.dom.MultiValuedNodeHeapIterator
 * JD-Core Version:    0.6.2
 */