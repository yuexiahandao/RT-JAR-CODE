/*     */ package com.sun.org.apache.xpath.internal.axes;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.dtm.DTM;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMIterator;
/*     */ import com.sun.org.apache.xml.internal.utils.WrappedRuntimeException;
/*     */ import com.sun.org.apache.xpath.internal.Expression;
/*     */ import com.sun.org.apache.xpath.internal.ExpressionOwner;
/*     */ import com.sun.org.apache.xpath.internal.XPathVisitor;
/*     */ import com.sun.org.apache.xpath.internal.compiler.Compiler;
/*     */ import com.sun.org.apache.xpath.internal.compiler.OpMap;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Vector;
/*     */ import javax.xml.transform.TransformerException;
/*     */ 
/*     */ public class UnionPathIterator extends LocPathIterator
/*     */   implements Cloneable, DTMIterator, Serializable, PathComponent
/*     */ {
/*     */   static final long serialVersionUID = -3910351546843826781L;
/*     */   protected LocPathIterator[] m_exprs;
/*     */   protected DTMIterator[] m_iterators;
/*     */ 
/*     */   public UnionPathIterator()
/*     */   {
/*  58 */     this.m_iterators = null;
/*  59 */     this.m_exprs = null;
/*     */   }
/*     */ 
/*     */   public void setRoot(int context, Object environment)
/*     */   {
/*  71 */     super.setRoot(context, environment);
/*     */     try
/*     */     {
/*  75 */       if (null != this.m_exprs)
/*     */       {
/*  77 */         int n = this.m_exprs.length;
/*  78 */         DTMIterator[] newIters = new DTMIterator[n];
/*     */ 
/*  80 */         for (int i = 0; i < n; i++)
/*     */         {
/*  82 */           DTMIterator iter = this.m_exprs[i].asIterator(this.m_execContext, context);
/*  83 */           newIters[i] = iter;
/*  84 */           iter.nextNode();
/*     */         }
/*  86 */         this.m_iterators = newIters;
/*     */       }
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*  91 */       throw new WrappedRuntimeException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addIterator(DTMIterator expr)
/*     */   {
/* 105 */     if (null == this.m_iterators)
/*     */     {
/* 107 */       this.m_iterators = new DTMIterator[1];
/* 108 */       this.m_iterators[0] = expr;
/*     */     }
/*     */     else
/*     */     {
/* 112 */       DTMIterator[] exprs = this.m_iterators;
/* 113 */       int len = this.m_iterators.length;
/*     */ 
/* 115 */       this.m_iterators = new DTMIterator[len + 1];
/*     */ 
/* 117 */       System.arraycopy(exprs, 0, this.m_iterators, 0, len);
/*     */ 
/* 119 */       this.m_iterators[len] = expr;
/*     */     }
/* 121 */     expr.nextNode();
/* 122 */     if ((expr instanceof Expression))
/* 123 */       ((Expression)expr).exprSetParent(this);
/*     */   }
/*     */ 
/*     */   public void detach()
/*     */   {
/* 135 */     if ((this.m_allowDetach) && (null != this.m_iterators)) {
/* 136 */       int n = this.m_iterators.length;
/* 137 */       for (int i = 0; i < n; i++)
/*     */       {
/* 139 */         this.m_iterators[i].detach();
/*     */       }
/* 141 */       this.m_iterators = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public UnionPathIterator(Compiler compiler, int opPos)
/*     */     throws TransformerException
/*     */   {
/* 164 */     opPos = OpMap.getFirstChildPos(opPos);
/*     */ 
/* 166 */     loadLocationPaths(compiler, opPos, 0);
/*     */   }
/*     */ 
/*     */   public static LocPathIterator createUnionIterator(Compiler compiler, int opPos)
/*     */     throws TransformerException
/*     */   {
/* 188 */     UnionPathIterator upi = new UnionPathIterator(compiler, opPos);
/* 189 */     int nPaths = upi.m_exprs.length;
/* 190 */     boolean isAllChildIterators = true;
/* 191 */     for (int i = 0; i < nPaths; i++)
/*     */     {
/* 193 */       LocPathIterator lpi = upi.m_exprs[i];
/*     */ 
/* 195 */       if (lpi.getAxis() != 3)
/*     */       {
/* 197 */         isAllChildIterators = false;
/* 198 */         break;
/*     */       }
/*     */ 
/* 203 */       if (HasPositionalPredChecker.check(lpi))
/*     */       {
/* 205 */         isAllChildIterators = false;
/* 206 */         break;
/*     */       }
/*     */     }
/*     */ 
/* 210 */     if (isAllChildIterators)
/*     */     {
/* 212 */       UnionChildIterator uci = new UnionChildIterator();
/*     */ 
/* 214 */       for (int i = 0; i < nPaths; i++)
/*     */       {
/* 216 */         PredicatedNodeTest lpi = upi.m_exprs[i];
/*     */ 
/* 220 */         uci.addNodeTest(lpi);
/*     */       }
/* 222 */       return uci;
/*     */     }
/*     */ 
/* 226 */     return upi;
/*     */   }
/*     */ 
/*     */   public int getAnalysisBits()
/*     */   {
/* 235 */     int bits = 0;
/*     */ 
/* 237 */     if (this.m_exprs != null)
/*     */     {
/* 239 */       int n = this.m_exprs.length;
/*     */ 
/* 241 */       for (int i = 0; i < n; i++)
/*     */       {
/* 243 */         int bit = this.m_exprs[i].getAnalysisBits();
/* 244 */         bits |= bit;
/*     */       }
/*     */     }
/*     */ 
/* 248 */     return bits;
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream stream)
/*     */     throws IOException, TransformerException
/*     */   {
/*     */     try
/*     */     {
/* 264 */       stream.defaultReadObject();
/* 265 */       this.m_clones = new IteratorPool(this);
/*     */     }
/*     */     catch (ClassNotFoundException cnfe)
/*     */     {
/* 269 */       throw new TransformerException(cnfe);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */     throws CloneNotSupportedException
/*     */   {
/* 284 */     UnionPathIterator clone = (UnionPathIterator)super.clone();
/* 285 */     if (this.m_iterators != null)
/*     */     {
/* 287 */       int n = this.m_iterators.length;
/*     */ 
/* 289 */       clone.m_iterators = new DTMIterator[n];
/*     */ 
/* 291 */       for (int i = 0; i < n; i++)
/*     */       {
/* 293 */         clone.m_iterators[i] = ((DTMIterator)this.m_iterators[i].clone());
/*     */       }
/*     */     }
/*     */ 
/* 297 */     return clone;
/*     */   }
/*     */ 
/*     */   protected LocPathIterator createDTMIterator(Compiler compiler, int opPos)
/*     */     throws TransformerException
/*     */   {
/* 315 */     LocPathIterator lpi = (LocPathIterator)WalkerFactory.newDTMIterator(compiler, opPos, compiler.getLocationPathDepth() <= 0);
/*     */ 
/* 317 */     return lpi;
/*     */   }
/*     */ 
/*     */   protected void loadLocationPaths(Compiler compiler, int opPos, int count)
/*     */     throws TransformerException
/*     */   {
/* 336 */     int steptype = compiler.getOp(opPos);
/*     */ 
/* 338 */     if (steptype == 28)
/*     */     {
/* 340 */       loadLocationPaths(compiler, compiler.getNextOpPos(opPos), count + 1);
/*     */ 
/* 342 */       this.m_exprs[count] = createDTMIterator(compiler, opPos);
/* 343 */       this.m_exprs[count].exprSetParent(this);
/*     */     }
/*     */     else
/*     */     {
/* 350 */       switch (steptype)
/*     */       {
/*     */       case 22:
/*     */       case 23:
/*     */       case 24:
/*     */       case 25:
/* 356 */         loadLocationPaths(compiler, compiler.getNextOpPos(opPos), count + 1);
/*     */ 
/* 358 */         WalkingIterator iter = new WalkingIterator(compiler.getNamespaceContext());
/*     */ 
/* 360 */         iter.exprSetParent(this);
/*     */ 
/* 362 */         if (compiler.getLocationPathDepth() <= 0) {
/* 363 */           iter.setIsTopLevel(true);
/*     */         }
/* 365 */         iter.m_firstWalker = new FilterExprWalker(iter);
/*     */ 
/* 367 */         iter.m_firstWalker.init(compiler, opPos, steptype);
/*     */ 
/* 369 */         this.m_exprs[count] = iter;
/* 370 */         break;
/*     */       default:
/* 372 */         this.m_exprs = new LocPathIterator[count];
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public int nextNode()
/*     */   {
/* 386 */     if (this.m_foundLast) {
/* 387 */       return -1;
/*     */     }
/*     */ 
/* 391 */     int earliestNode = -1;
/*     */ 
/* 393 */     if (null != this.m_iterators)
/*     */     {
/* 395 */       int n = this.m_iterators.length;
/* 396 */       int iteratorUsed = -1;
/*     */ 
/* 398 */       for (int i = 0; i < n; i++)
/*     */       {
/* 400 */         int node = this.m_iterators[i].getCurrentNode();
/*     */ 
/* 402 */         if (-1 != node)
/*     */         {
/* 404 */           if (-1 == earliestNode)
/*     */           {
/* 406 */             iteratorUsed = i;
/* 407 */             earliestNode = node;
/*     */           }
/* 411 */           else if (node == earliestNode)
/*     */           {
/* 415 */             this.m_iterators[i].nextNode();
/*     */           }
/*     */           else
/*     */           {
/* 419 */             DTM dtm = getDTM(node);
/*     */ 
/* 421 */             if (dtm.isNodeAfter(node, earliestNode))
/*     */             {
/* 423 */               iteratorUsed = i;
/* 424 */               earliestNode = node;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 430 */       if (-1 != earliestNode)
/*     */       {
/* 432 */         this.m_iterators[iteratorUsed].nextNode();
/*     */ 
/* 434 */         incrementCurrentPos();
/*     */       }
/*     */       else {
/* 437 */         this.m_foundLast = true;
/*     */       }
/*     */     }
/* 440 */     this.m_lastFetched = earliestNode;
/*     */ 
/* 442 */     return earliestNode;
/*     */   }
/*     */ 
/*     */   public void fixupVariables(Vector vars, int globalsSize)
/*     */   {
/* 457 */     for (int i = 0; i < this.m_exprs.length; i++)
/*     */     {
/* 459 */       this.m_exprs[i].fixupVariables(vars, globalsSize);
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getAxis()
/*     */   {
/* 490 */     return -1;
/*     */   }
/*     */ 
/*     */   public void callVisitors(ExpressionOwner owner, XPathVisitor visitor)
/*     */   {
/* 541 */     if (visitor.visitUnionPath(owner, this))
/*     */     {
/* 543 */       if (null != this.m_exprs)
/*     */       {
/* 545 */         int n = this.m_exprs.length;
/* 546 */         for (int i = 0; i < n; i++)
/*     */         {
/* 548 */           this.m_exprs[i].callVisitors(new iterOwner(i), visitor);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean deepEquals(Expression expr)
/*     */   {
/* 559 */     if (!super.deepEquals(expr)) {
/* 560 */       return false;
/*     */     }
/* 562 */     UnionPathIterator upi = (UnionPathIterator)expr;
/*     */ 
/* 564 */     if (null != this.m_exprs)
/*     */     {
/* 566 */       int n = this.m_exprs.length;
/*     */ 
/* 568 */       if ((null == upi.m_exprs) || (upi.m_exprs.length != n)) {
/* 569 */         return false;
/*     */       }
/* 571 */       for (int i = 0; i < n; i++)
/*     */       {
/* 573 */         if (!this.m_exprs[i].deepEquals(upi.m_exprs[i]))
/* 574 */           return false;
/*     */       }
/*     */     }
/* 577 */     else if (null != upi.m_exprs)
/*     */     {
/* 579 */       return false;
/*     */     }
/*     */ 
/* 582 */     return true;
/*     */   }
/*     */ 
/*     */   class iterOwner
/*     */     implements ExpressionOwner
/*     */   {
/*     */     int m_index;
/*     */ 
/*     */     iterOwner(int index)
/*     */     {
/* 499 */       this.m_index = index;
/*     */     }
/*     */ 
/*     */     public Expression getExpression()
/*     */     {
/* 507 */       return UnionPathIterator.this.m_exprs[this.m_index];
/*     */     }
/*     */ 
/*     */     public void setExpression(Expression exp)
/*     */     {
/* 516 */       if (!(exp instanceof LocPathIterator))
/*     */       {
/* 520 */         WalkingIterator wi = new WalkingIterator(UnionPathIterator.this.getPrefixResolver());
/* 521 */         FilterExprWalker few = new FilterExprWalker(wi);
/* 522 */         wi.setFirstWalker(few);
/* 523 */         few.setInnerExpression(exp);
/* 524 */         wi.exprSetParent(UnionPathIterator.this);
/* 525 */         few.exprSetParent(wi);
/* 526 */         exp.exprSetParent(few);
/* 527 */         exp = wi;
/*     */       }
/*     */       else {
/* 530 */         exp.exprSetParent(UnionPathIterator.this);
/* 531 */       }UnionPathIterator.this.m_exprs[this.m_index] = ((LocPathIterator)exp);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.axes.UnionPathIterator
 * JD-Core Version:    0.6.2
 */