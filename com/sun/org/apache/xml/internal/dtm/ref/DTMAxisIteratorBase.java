/*     */ package com.sun.org.apache.xml.internal.dtm.ref;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
/*     */ import com.sun.org.apache.xml.internal.utils.WrappedRuntimeException;
/*     */ 
/*     */ public abstract class DTMAxisIteratorBase
/*     */   implements DTMAxisIterator
/*     */ {
/*  38 */   protected int _last = -1;
/*     */ 
/*  43 */   protected int _position = 0;
/*     */   protected int _markedNode;
/*  54 */   protected int _startNode = -1;
/*     */ 
/*  59 */   protected boolean _includeSelf = false;
/*     */ 
/*  65 */   protected boolean _isRestartable = true;
/*     */ 
/*     */   public int getStartNode()
/*     */   {
/*  75 */     return this._startNode;
/*     */   }
/*     */ 
/*     */   public DTMAxisIterator reset()
/*     */   {
/*  85 */     boolean temp = this._isRestartable;
/*     */ 
/*  87 */     this._isRestartable = true;
/*     */ 
/*  89 */     setStartNode(this._startNode);
/*     */ 
/*  91 */     this._isRestartable = temp;
/*     */ 
/*  93 */     return this;
/*     */   }
/*     */ 
/*     */   public DTMAxisIterator includeSelf()
/*     */   {
/* 108 */     this._includeSelf = true;
/*     */ 
/* 110 */     return this;
/*     */   }
/*     */ 
/*     */   public int getLast()
/*     */   {
/* 127 */     if (this._last == -1)
/*     */     {
/* 136 */       int temp = this._position;
/* 137 */       setMark();
/*     */ 
/* 139 */       reset();
/*     */       do
/*     */       {
/* 142 */         this._last += 1;
/*     */       }
/* 144 */       while (next() != -1);
/*     */ 
/* 146 */       gotoMark();
/* 147 */       this._position = temp;
/*     */     }
/*     */ 
/* 150 */     return this._last;
/*     */   }
/*     */ 
/*     */   public int getPosition()
/*     */   {
/* 159 */     return this._position == 0 ? 1 : this._position;
/*     */   }
/*     */ 
/*     */   public boolean isReverse()
/*     */   {
/* 167 */     return false;
/*     */   }
/*     */ 
/*     */   public DTMAxisIterator cloneIterator()
/*     */   {
/*     */     try
/*     */     {
/* 182 */       DTMAxisIteratorBase clone = (DTMAxisIteratorBase)super.clone();
/*     */ 
/* 184 */       clone._isRestartable = false;
/*     */ 
/* 187 */       return clone;
/*     */     }
/*     */     catch (CloneNotSupportedException e)
/*     */     {
/* 191 */       throw new WrappedRuntimeException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final int returnNode(int node)
/*     */   {
/* 215 */     this._position += 1;
/*     */ 
/* 217 */     return node;
/*     */   }
/*     */ 
/*     */   protected final DTMAxisIterator resetPosition()
/*     */   {
/* 231 */     this._position = 0;
/*     */ 
/* 233 */     return this;
/*     */   }
/*     */ 
/*     */   public boolean isDocOrdered()
/*     */   {
/* 244 */     return true;
/*     */   }
/*     */ 
/*     */   public int getAxis()
/*     */   {
/* 255 */     return -1;
/*     */   }
/*     */ 
/*     */   public void setRestartable(boolean isRestartable) {
/* 259 */     this._isRestartable = isRestartable;
/*     */   }
/*     */ 
/*     */   public int getNodeByPosition(int position)
/*     */   {
/* 270 */     if (position > 0) {
/* 271 */       int pos = isReverse() ? getLast() - position + 1 : position;
/*     */       int node;
/* 274 */       while ((node = next()) != -1) {
/* 275 */         if (pos == getPosition()) {
/* 276 */           return node;
/*     */         }
/*     */       }
/*     */     }
/* 280 */     return -1;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase
 * JD-Core Version:    0.6.2
 */