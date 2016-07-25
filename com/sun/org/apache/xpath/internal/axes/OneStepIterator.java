/*     */ package com.sun.org.apache.xpath.internal.axes;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.dtm.DTM;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMIterator;
/*     */ import com.sun.org.apache.xpath.internal.Expression;
/*     */ import com.sun.org.apache.xpath.internal.XPathContext;
/*     */ import com.sun.org.apache.xpath.internal.compiler.Compiler;
/*     */ import com.sun.org.apache.xpath.internal.compiler.OpMap;
/*     */ import javax.xml.transform.TransformerException;
/*     */ 
/*     */ public class OneStepIterator extends ChildTestIterator
/*     */ {
/*     */   static final long serialVersionUID = 4623710779664998283L;
/*  44 */   protected int m_axis = -1;
/*     */   protected DTMAxisIterator m_iterator;
/*     */ 
/*     */   OneStepIterator(Compiler compiler, int opPos, int analysis)
/*     */     throws TransformerException
/*     */   {
/*  61 */     super(compiler, opPos, analysis);
/*  62 */     int firstStepPos = OpMap.getFirstChildPos(opPos);
/*     */ 
/*  64 */     this.m_axis = WalkerFactory.getAxisFromStep(compiler, firstStepPos);
/*     */   }
/*     */ 
/*     */   public OneStepIterator(DTMAxisIterator iterator, int axis)
/*     */     throws TransformerException
/*     */   {
/*  80 */     super(null);
/*     */ 
/*  82 */     this.m_iterator = iterator;
/*  83 */     this.m_axis = axis;
/*  84 */     int whatToShow = -1;
/*  85 */     initNodeTest(whatToShow);
/*     */   }
/*     */ 
/*     */   public void setRoot(int context, Object environment)
/*     */   {
/*  97 */     super.setRoot(context, environment);
/*  98 */     if (this.m_axis > -1)
/*  99 */       this.m_iterator = this.m_cdtm.getAxisIterator(this.m_axis);
/* 100 */     this.m_iterator.setStartNode(this.m_context);
/*     */   }
/*     */ 
/*     */   public void detach()
/*     */   {
/* 112 */     if (this.m_allowDetach)
/*     */     {
/* 114 */       if (this.m_axis > -1) {
/* 115 */         this.m_iterator = null;
/*     */       }
/*     */ 
/* 118 */       super.detach();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected int getNextNode()
/*     */   {
/* 127 */     return this.m_lastFetched = this.m_iterator.next();
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */     throws CloneNotSupportedException
/*     */   {
/* 141 */     OneStepIterator clone = (OneStepIterator)super.clone();
/*     */ 
/* 143 */     if (this.m_iterator != null)
/*     */     {
/* 145 */       clone.m_iterator = this.m_iterator.cloneIterator();
/*     */     }
/* 147 */     return clone;
/*     */   }
/*     */ 
/*     */   public DTMIterator cloneWithReset()
/*     */     throws CloneNotSupportedException
/*     */   {
/* 161 */     OneStepIterator clone = (OneStepIterator)super.cloneWithReset();
/* 162 */     clone.m_iterator = this.m_iterator;
/*     */ 
/* 164 */     return clone;
/*     */   }
/*     */ 
/*     */   public boolean isReverseAxes()
/*     */   {
/* 176 */     return this.m_iterator.isReverse();
/*     */   }
/*     */ 
/*     */   protected int getProximityPosition(int predicateIndex)
/*     */   {
/* 192 */     if (!isReverseAxes()) {
/* 193 */       return super.getProximityPosition(predicateIndex);
/*     */     }
/*     */ 
/* 198 */     if (predicateIndex < 0) {
/* 199 */       return -1;
/*     */     }
/* 201 */     if (this.m_proximityPositions[predicateIndex] <= 0)
/*     */     {
/* 203 */       XPathContext xctxt = getXPathContext();
/*     */       try
/*     */       {
/* 206 */         OneStepIterator clone = (OneStepIterator)clone();
/*     */ 
/* 208 */         int root = getRoot();
/* 209 */         xctxt.pushCurrentNode(root);
/* 210 */         clone.setRoot(root, xctxt);
/*     */ 
/* 213 */         clone.m_predCount = predicateIndex;
/*     */ 
/* 216 */         int count = 1;
/*     */         int next;
/* 219 */         while (-1 != (next = clone.nextNode()))
/*     */         {
/* 221 */           count++;
/*     */         }
/*     */ 
/* 224 */         this.m_proximityPositions[predicateIndex] += count;
/*     */       }
/*     */       catch (CloneNotSupportedException cnse)
/*     */       {
/*     */       }
/*     */       finally
/*     */       {
/* 233 */         xctxt.popCurrentNode();
/*     */       }
/*     */     }
/*     */ 
/* 237 */     return this.m_proximityPositions[predicateIndex];
/*     */   }
/*     */ 
/*     */   public int getLength()
/*     */   {
/* 248 */     if (!isReverseAxes()) {
/* 249 */       return super.getLength();
/*     */     }
/*     */ 
/* 252 */     boolean isPredicateTest = this == this.m_execContext.getSubContextList();
/*     */ 
/* 255 */     int predCount = getPredicateCount();
/*     */ 
/* 260 */     if ((-1 != this.m_length) && (isPredicateTest) && (this.m_predicateIndex < 1)) {
/* 261 */       return this.m_length;
/*     */     }
/* 263 */     int count = 0;
/*     */ 
/* 265 */     XPathContext xctxt = getXPathContext();
/*     */     try
/*     */     {
/* 268 */       OneStepIterator clone = (OneStepIterator)cloneWithReset();
/*     */ 
/* 270 */       int root = getRoot();
/* 271 */       xctxt.pushCurrentNode(root);
/* 272 */       clone.setRoot(root, xctxt);
/*     */ 
/* 274 */       clone.m_predCount = this.m_predicateIndex;
/*     */       int next;
/* 278 */       while (-1 != (next = clone.nextNode()))
/*     */       {
/* 280 */         count++;
/*     */       }
/*     */ 
/*     */     }
/*     */     catch (CloneNotSupportedException cnse)
/*     */     {
/*     */     }
/*     */     finally
/*     */     {
/* 289 */       xctxt.popCurrentNode();
/*     */     }
/* 291 */     if ((isPredicateTest) && (this.m_predicateIndex < 1)) {
/* 292 */       this.m_length = count;
/*     */     }
/* 294 */     return count;
/*     */   }
/*     */ 
/*     */   protected void countProximityPosition(int i)
/*     */   {
/* 304 */     if (!isReverseAxes())
/* 305 */       super.countProximityPosition(i);
/* 306 */     else if (i < this.m_proximityPositions.length)
/* 307 */       this.m_proximityPositions[i] -= 1;
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/* 316 */     super.reset();
/* 317 */     if (null != this.m_iterator)
/* 318 */       this.m_iterator.reset();
/*     */   }
/*     */ 
/*     */   public int getAxis()
/*     */   {
/* 329 */     return this.m_axis;
/*     */   }
/*     */ 
/*     */   public boolean deepEquals(Expression expr)
/*     */   {
/* 337 */     if (!super.deepEquals(expr)) {
/* 338 */       return false;
/*     */     }
/* 340 */     if (this.m_axis != ((OneStepIterator)expr).m_axis) {
/* 341 */       return false;
/*     */     }
/* 343 */     return true;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.axes.OneStepIterator
 * JD-Core Version:    0.6.2
 */