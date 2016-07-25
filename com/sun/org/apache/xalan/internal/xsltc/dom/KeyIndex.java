/*     */ package com.sun.org.apache.xalan.internal.xsltc.dom;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.xsltc.DOM;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.DOMEnhancedForDTM;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.util.IntegerArray;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
/*     */ import com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ public class KeyIndex extends DTMAxisIteratorBase
/*     */ {
/*     */   private Hashtable _index;
/*  58 */   private int _currentDocumentNode = -1;
/*     */ 
/*  63 */   private Hashtable _rootToIndexMap = new Hashtable();
/*     */ 
/*  69 */   private IntegerArray _nodes = null;
/*     */   private DOM _dom;
/*     */   private DOMEnhancedForDTM _enhancedDOM;
/*  82 */   private int _markedPosition = 0;
/*     */ 
/* 491 */   private static final IntegerArray EMPTY_NODES = new IntegerArray(0);
/*     */ 
/*     */   public KeyIndex(int dummy)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setRestartable(boolean flag)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void add(Object value, int node, int rootNode)
/*     */   {
/*  95 */     if (this._currentDocumentNode != rootNode) {
/*  96 */       this._currentDocumentNode = rootNode;
/*  97 */       this._index = new Hashtable();
/*  98 */       this._rootToIndexMap.put(new Integer(rootNode), this._index);
/*     */     }
/*     */ 
/* 101 */     IntegerArray nodes = (IntegerArray)this._index.get(value);
/*     */ 
/* 103 */     if (nodes == null) {
/* 104 */       nodes = new IntegerArray();
/* 105 */       this._index.put(value, nodes);
/* 106 */       nodes.add(node);
/*     */     }
/* 110 */     else if (node != nodes.at(nodes.cardinality() - 1)) {
/* 111 */       nodes.add(node);
/*     */     }
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public void merge(KeyIndex other)
/*     */   {
/* 120 */     if (other == null) return;
/*     */ 
/* 122 */     if (other._nodes != null)
/* 123 */       if (this._nodes == null) {
/* 124 */         this._nodes = ((IntegerArray)other._nodes.clone());
/*     */       }
/*     */       else
/* 127 */         this._nodes.merge(other._nodes);
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public void lookupId(Object value)
/*     */   {
/* 142 */     this._nodes = null;
/*     */ 
/* 144 */     StringTokenizer values = new StringTokenizer((String)value, " \n\t");
/*     */ 
/* 146 */     while (values.hasMoreElements()) {
/* 147 */       String token = (String)values.nextElement();
/* 148 */       IntegerArray nodes = (IntegerArray)this._index.get(token);
/*     */ 
/* 150 */       if ((nodes == null) && (this._enhancedDOM != null) && (this._enhancedDOM.hasDOMSource()))
/*     */       {
/* 152 */         nodes = getDOMNodeById(token);
/*     */       }
/*     */ 
/* 155 */       if (nodes != null)
/*     */       {
/* 157 */         if (this._nodes == null) {
/* 158 */           nodes = (IntegerArray)nodes.clone();
/* 159 */           this._nodes = nodes;
/*     */         }
/*     */         else {
/* 162 */           this._nodes.merge(nodes);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public IntegerArray getDOMNodeById(String id)
/*     */   {
/* 174 */     IntegerArray nodes = null;
/*     */ 
/* 176 */     if (this._enhancedDOM != null) {
/* 177 */       int ident = this._enhancedDOM.getElementById(id);
/*     */ 
/* 179 */       if (ident != -1) {
/* 180 */         Integer root = new Integer(this._enhancedDOM.getDocument());
/* 181 */         Hashtable index = (Hashtable)this._rootToIndexMap.get(root);
/*     */ 
/* 183 */         if (index == null) {
/* 184 */           index = new Hashtable();
/* 185 */           this._rootToIndexMap.put(root, index);
/*     */         } else {
/* 187 */           nodes = (IntegerArray)index.get(id);
/*     */         }
/*     */ 
/* 190 */         if (nodes == null) {
/* 191 */           nodes = new IntegerArray();
/* 192 */           index.put(id, nodes);
/*     */         }
/*     */ 
/* 195 */         nodes.add(this._enhancedDOM.getNodeHandle(ident));
/*     */       }
/*     */     }
/*     */ 
/* 199 */     return nodes;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public void lookupKey(Object value)
/*     */   {
/* 210 */     IntegerArray nodes = (IntegerArray)this._index.get(value);
/* 211 */     this._nodes = (nodes != null ? (IntegerArray)nodes.clone() : null);
/* 212 */     this._position = 0;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public int next()
/*     */   {
/* 222 */     if (this._nodes == null) return -1;
/*     */ 
/* 224 */     return this._position < this._nodes.cardinality() ? this._dom.getNodeHandle(this._nodes.at(this._position++)) : -1;
/*     */   }
/*     */ 
/*     */   public int containsID(int node, Object value)
/*     */   {
/* 241 */     String string = (String)value;
/* 242 */     int rootHandle = this._dom.getAxisIterator(19).setStartNode(node).next();
/*     */ 
/* 246 */     Hashtable index = (Hashtable)this._rootToIndexMap.get(new Integer(rootHandle));
/*     */ 
/* 250 */     StringTokenizer values = new StringTokenizer(string, " \n\t");
/*     */ 
/* 252 */     while (values.hasMoreElements()) {
/* 253 */       String token = (String)values.nextElement();
/* 254 */       IntegerArray nodes = null;
/*     */ 
/* 256 */       if (index != null) {
/* 257 */         nodes = (IntegerArray)index.get(token);
/*     */       }
/*     */ 
/* 262 */       if ((nodes == null) && (this._enhancedDOM != null) && (this._enhancedDOM.hasDOMSource()))
/*     */       {
/* 264 */         nodes = getDOMNodeById(token);
/*     */       }
/*     */ 
/* 268 */       if ((nodes != null) && (nodes.indexOf(node) >= 0)) {
/* 269 */         return 1;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 274 */     return 0;
/*     */   }
/*     */ 
/*     */   public int containsKey(int node, Object value)
/*     */   {
/* 293 */     int rootHandle = this._dom.getAxisIterator(19).setStartNode(node).next();
/*     */ 
/* 297 */     Hashtable index = (Hashtable)this._rootToIndexMap.get(new Integer(rootHandle));
/*     */ 
/* 302 */     if (index != null) {
/* 303 */       IntegerArray nodes = (IntegerArray)index.get(value);
/* 304 */       return (nodes != null) && (nodes.indexOf(node) >= 0) ? 1 : 0;
/*     */     }
/*     */ 
/* 308 */     return 0;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public DTMAxisIterator reset()
/*     */   {
/* 318 */     this._position = 0;
/* 319 */     return this;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public int getLast()
/*     */   {
/* 329 */     return this._nodes == null ? 0 : this._nodes.cardinality();
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public int getPosition()
/*     */   {
/* 339 */     return this._position;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public void setMark()
/*     */   {
/* 349 */     this._markedPosition = this._position;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public void gotoMark()
/*     */   {
/* 359 */     this._position = this._markedPosition;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public DTMAxisIterator setStartNode(int start)
/*     */   {
/* 370 */     if (start == -1) {
/* 371 */       this._nodes = null;
/*     */     }
/* 373 */     else if (this._nodes != null) {
/* 374 */       this._position = 0;
/*     */     }
/* 376 */     return this;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public int getStartNode()
/*     */   {
/* 387 */     return 0;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public boolean isReverse()
/*     */   {
/* 397 */     return false;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public DTMAxisIterator cloneIterator()
/*     */   {
/* 407 */     KeyIndex other = new KeyIndex(0);
/* 408 */     other._index = this._index;
/* 409 */     other._rootToIndexMap = this._rootToIndexMap;
/* 410 */     other._nodes = this._nodes;
/* 411 */     other._position = this._position;
/* 412 */     return other;
/*     */   }
/*     */ 
/*     */   public void setDom(DOM dom, int node) {
/* 416 */     this._dom = dom;
/*     */ 
/* 420 */     if ((dom instanceof MultiDOM)) {
/* 421 */       dom = ((MultiDOM)dom).getDTM(node);
/*     */     }
/*     */ 
/* 424 */     if ((dom instanceof DOMEnhancedForDTM)) {
/* 425 */       this._enhancedDOM = ((DOMEnhancedForDTM)dom);
/*     */     }
/* 427 */     else if ((dom instanceof DOMAdapter)) {
/* 428 */       DOM idom = ((DOMAdapter)dom).getDOMImpl();
/* 429 */       if ((idom instanceof DOMEnhancedForDTM))
/* 430 */         this._enhancedDOM = ((DOMEnhancedForDTM)idom);
/*     */     }
/*     */   }
/*     */ 
/*     */   public KeyIndexIterator getKeyIndexIterator(Object keyValue, boolean isKeyCall)
/*     */   {
/* 448 */     if ((keyValue instanceof DTMAxisIterator)) {
/* 449 */       return getKeyIndexIterator((DTMAxisIterator)keyValue, isKeyCall);
/*     */     }
/* 451 */     return getKeyIndexIterator(BasisLibrary.stringF(keyValue, this._dom), isKeyCall);
/*     */   }
/*     */ 
/*     */   public KeyIndexIterator getKeyIndexIterator(String keyValue, boolean isKeyCall)
/*     */   {
/* 469 */     return new KeyIndexIterator(keyValue, isKeyCall);
/*     */   }
/*     */ 
/*     */   public KeyIndexIterator getKeyIndexIterator(DTMAxisIterator keyValue, boolean isKeyCall)
/*     */   {
/* 485 */     return new KeyIndexIterator(keyValue, isKeyCall);
/*     */   }
/*     */ 
/*     */   public class KeyIndexIterator extends MultiValuedNodeHeapIterator
/*     */   {
/*     */     private IntegerArray _nodes;
/*     */     private DTMAxisIterator _keyValueIterator;
/*     */     private String _keyValue;
/*     */     private boolean _isKeyIterator;
/*     */ 
/*     */     KeyIndexIterator(String keyValue, boolean isKeyIterator)
/*     */     {
/* 662 */       this._isKeyIterator = isKeyIterator;
/* 663 */       this._keyValue = keyValue;
/*     */     }
/*     */ 
/*     */     KeyIndexIterator(DTMAxisIterator keyValues, boolean isKeyIterator)
/*     */     {
/* 676 */       this._keyValueIterator = keyValues;
/* 677 */       this._isKeyIterator = isKeyIterator;
/*     */     }
/*     */ 
/*     */     protected IntegerArray lookupNodes(int root, String keyValue)
/*     */     {
/* 689 */       IntegerArray result = null;
/*     */ 
/* 692 */       Hashtable index = (Hashtable)KeyIndex.this._rootToIndexMap.get(new Integer(root));
/*     */ 
/* 694 */       if (!this._isKeyIterator)
/*     */       {
/* 697 */         StringTokenizer values = new StringTokenizer(keyValue, " \n\t");
/*     */ 
/* 700 */         while (values.hasMoreElements()) {
/* 701 */           String token = (String)values.nextElement();
/* 702 */           IntegerArray nodes = null;
/*     */ 
/* 705 */           if (index != null) {
/* 706 */             nodes = (IntegerArray)index.get(token);
/*     */           }
/*     */ 
/* 711 */           if ((nodes == null) && (KeyIndex.this._enhancedDOM != null) && (KeyIndex.this._enhancedDOM.hasDOMSource()))
/*     */           {
/* 713 */             nodes = KeyIndex.this.getDOMNodeById(token);
/*     */           }
/*     */ 
/* 718 */           if (nodes != null) {
/* 719 */             if (result == null)
/* 720 */               result = (IntegerArray)nodes.clone();
/*     */             else
/* 722 */               result.merge(nodes);
/*     */           }
/*     */         }
/*     */       }
/* 726 */       else if (index != null)
/*     */       {
/* 728 */         result = (IntegerArray)index.get(keyValue);
/*     */       }
/*     */ 
/* 731 */       return result;
/*     */     }
/*     */ 
/*     */     public DTMAxisIterator setStartNode(int node)
/*     */     {
/* 743 */       this._startNode = node;
/*     */ 
/* 747 */       if (this._keyValueIterator != null) {
/* 748 */         this._keyValueIterator = this._keyValueIterator.setStartNode(node);
/*     */       }
/*     */ 
/* 751 */       init();
/*     */ 
/* 753 */       return super.setStartNode(node);
/*     */     }
/*     */ 
/*     */     public int next()
/*     */     {
/*     */       int nodeHandle;
/*     */       int nodeHandle;
/* 769 */       if (this._nodes != null)
/*     */       {
/*     */         int nodeHandle;
/* 770 */         if (this._position < this._nodes.cardinality())
/* 771 */           nodeHandle = returnNode(this._nodes.at(this._position));
/*     */         else
/* 773 */           nodeHandle = -1;
/*     */       }
/*     */       else {
/* 776 */         nodeHandle = super.next();
/*     */       }
/*     */ 
/* 779 */       return nodeHandle;
/*     */     }
/*     */ 
/*     */     public DTMAxisIterator reset()
/*     */     {
/* 789 */       if (this._nodes == null)
/* 790 */         init();
/*     */       else {
/* 792 */         super.reset();
/*     */       }
/*     */ 
/* 795 */       return resetPosition();
/*     */     }
/*     */ 
/*     */     protected void init()
/*     */     {
/* 805 */       super.init();
/* 806 */       this._position = 0;
/*     */ 
/* 809 */       int rootHandle = KeyIndex.this._dom.getAxisIterator(19).setStartNode(this._startNode).next();
/*     */ 
/* 813 */       if (this._keyValueIterator == null)
/*     */       {
/* 815 */         this._nodes = lookupNodes(rootHandle, this._keyValue);
/*     */ 
/* 817 */         if (this._nodes == null)
/* 818 */           this._nodes = KeyIndex.EMPTY_NODES;
/*     */       }
/*     */       else {
/* 821 */         DTMAxisIterator keyValues = this._keyValueIterator.reset();
/* 822 */         int retrievedKeyValueIdx = 0;
/* 823 */         boolean foundNodes = false;
/*     */ 
/* 825 */         this._nodes = null;
/*     */ 
/* 832 */         for (int keyValueNode = keyValues.next(); 
/* 833 */           keyValueNode != -1; 
/* 834 */           keyValueNode = keyValues.next())
/*     */         {
/* 836 */           String keyValue = BasisLibrary.stringF(keyValueNode, KeyIndex.this._dom);
/*     */ 
/* 838 */           IntegerArray nodes = lookupNodes(rootHandle, keyValue);
/*     */ 
/* 840 */           if (nodes != null) {
/* 841 */             if (!foundNodes) {
/* 842 */               this._nodes = nodes;
/* 843 */               foundNodes = true;
/*     */             } else {
/* 845 */               if (this._nodes != null) {
/* 846 */                 addHeapNode(new KeyIndexHeapNode(this._nodes));
/* 847 */                 this._nodes = null;
/*     */               }
/* 849 */               addHeapNode(new KeyIndexHeapNode(nodes));
/*     */             }
/*     */           }
/*     */         }
/*     */ 
/* 854 */         if (!foundNodes)
/* 855 */           this._nodes = KeyIndex.EMPTY_NODES;
/*     */       }
/*     */     }
/*     */ 
/*     */     public int getLast()
/*     */     {
/* 869 */       return this._nodes != null ? this._nodes.cardinality() : super.getLast();
/*     */     }
/*     */ 
/*     */     public int getNodeByPosition(int position)
/*     */     {
/* 879 */       int node = -1;
/*     */ 
/* 885 */       if (this._nodes != null) {
/* 886 */         if (position > 0)
/* 887 */           if (position <= this._nodes.cardinality()) {
/* 888 */             this._position = position;
/* 889 */             node = this._nodes.at(position - 1);
/*     */           } else {
/* 891 */             this._position = this._nodes.cardinality();
/*     */           }
/*     */       }
/*     */       else {
/* 895 */         node = super.getNodeByPosition(position);
/*     */       }
/*     */ 
/* 898 */       return node;
/*     */     }
/*     */ 
/*     */     protected class KeyIndexHeapNode extends MultiValuedNodeHeapIterator.HeapNode
/*     */     {
/*     */       private IntegerArray _nodes;
/* 553 */       private int _position = 0;
/*     */ 
/* 559 */       private int _markPosition = -1;
/*     */ 
/*     */       KeyIndexHeapNode(IntegerArray nodes)
/*     */       {
/* 566 */         super();
/* 567 */         this._nodes = nodes;
/*     */       }
/*     */ 
/*     */       public int step()
/*     */       {
/* 576 */         if (this._position < this._nodes.cardinality()) {
/* 577 */           this._node = this._nodes.at(this._position);
/* 578 */           this._position += 1;
/*     */         } else {
/* 580 */           this._node = -1;
/*     */         }
/*     */ 
/* 583 */         return this._node;
/*     */       }
/*     */ 
/*     */       public MultiValuedNodeHeapIterator.HeapNode cloneHeapNode()
/*     */       {
/* 593 */         KeyIndexHeapNode clone = (KeyIndexHeapNode)super.cloneHeapNode();
/*     */ 
/* 596 */         clone._nodes = this._nodes;
/* 597 */         clone._position = this._position;
/* 598 */         clone._markPosition = this._markPosition;
/*     */ 
/* 600 */         return clone;
/*     */       }
/*     */ 
/*     */       public void setMark()
/*     */       {
/* 608 */         this._markPosition = this._position;
/*     */       }
/*     */ 
/*     */       public void gotoMark()
/*     */       {
/* 615 */         this._position = this._markPosition;
/*     */       }
/*     */ 
/*     */       public boolean isLessThan(MultiValuedNodeHeapIterator.HeapNode heapNode)
/*     */       {
/* 627 */         return this._node < heapNode._node;
/*     */       }
/*     */ 
/*     */       public MultiValuedNodeHeapIterator.HeapNode setStartNode(int node)
/*     */       {
/* 639 */         return this;
/*     */       }
/*     */ 
/*     */       public MultiValuedNodeHeapIterator.HeapNode reset()
/*     */       {
/* 646 */         this._position = 0;
/* 647 */         return this;
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.dom.KeyIndex
 * JD-Core Version:    0.6.2
 */