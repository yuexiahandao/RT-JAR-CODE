/*     */ package com.sun.org.apache.xpath.internal.axes;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.dtm.DTM;
/*     */ import com.sun.org.apache.xml.internal.utils.PrefixResolver;
/*     */ import com.sun.org.apache.xml.internal.utils.WrappedRuntimeException;
/*     */ import com.sun.org.apache.xpath.internal.Expression;
/*     */ import com.sun.org.apache.xpath.internal.ExpressionOwner;
/*     */ import com.sun.org.apache.xpath.internal.XPathContext;
/*     */ import com.sun.org.apache.xpath.internal.XPathVisitor;
/*     */ import com.sun.org.apache.xpath.internal.compiler.Compiler;
/*     */ import com.sun.org.apache.xpath.internal.objects.XObject;
/*     */ import com.sun.org.apache.xpath.internal.patterns.NodeTest;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.util.Vector;
/*     */ import javax.xml.transform.TransformerException;
/*     */ 
/*     */ public abstract class PredicatedNodeTest extends NodeTest
/*     */   implements SubContextList
/*     */ {
/*     */   static final long serialVersionUID = -6193530757296377351L;
/* 111 */   protected int m_predCount = -1;
/*     */ 
/* 595 */   protected transient boolean m_foundLast = false;
/*     */   protected LocPathIterator m_lpi;
/* 604 */   transient int m_predicateIndex = -1;
/*     */   private Expression[] m_predicates;
/*     */   protected transient int[] m_proximityPositions;
/*     */   static final boolean DEBUG_PREDICATECOUNTING = false;
/*     */ 
/*     */   PredicatedNodeTest(LocPathIterator locPathIterator)
/*     */   {
/*  47 */     this.m_lpi = locPathIterator;
/*     */   }
/*     */ 
/*     */   PredicatedNodeTest()
/*     */   {
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream stream)
/*     */     throws IOException, TransformerException
/*     */   {
/*     */     try
/*     */     {
/*  71 */       stream.defaultReadObject();
/*  72 */       this.m_predicateIndex = -1;
/*  73 */       resetProximityPositions();
/*     */     }
/*     */     catch (ClassNotFoundException cnfe)
/*     */     {
/*  77 */       throw new TransformerException(cnfe);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */     throws CloneNotSupportedException
/*     */   {
/*  92 */     PredicatedNodeTest clone = (PredicatedNodeTest)super.clone();
/*     */ 
/*  94 */     if ((null != this.m_proximityPositions) && (this.m_proximityPositions == clone.m_proximityPositions))
/*     */     {
/*  97 */       clone.m_proximityPositions = new int[this.m_proximityPositions.length];
/*     */ 
/*  99 */       System.arraycopy(this.m_proximityPositions, 0, clone.m_proximityPositions, 0, this.m_proximityPositions.length);
/*     */     }
/*     */ 
/* 104 */     if (clone.m_lpi == this) {
/* 105 */       clone.m_lpi = ((LocPathIterator)clone);
/*     */     }
/* 107 */     return clone;
/*     */   }
/*     */ 
/*     */   public int getPredicateCount()
/*     */   {
/* 120 */     if (-1 == this.m_predCount) {
/* 121 */       return null == this.m_predicates ? 0 : this.m_predicates.length;
/*     */     }
/* 123 */     return this.m_predCount;
/*     */   }
/*     */ 
/*     */   public void setPredicateCount(int count)
/*     */   {
/* 138 */     if (count > 0)
/*     */     {
/* 140 */       Expression[] newPredicates = new Expression[count];
/* 141 */       for (int i = 0; i < count; i++)
/*     */       {
/* 143 */         newPredicates[i] = this.m_predicates[i];
/*     */       }
/* 145 */       this.m_predicates = newPredicates;
/*     */     }
/*     */     else {
/* 148 */       this.m_predicates = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void initPredicateInfo(Compiler compiler, int opPos)
/*     */     throws TransformerException
/*     */   {
/* 165 */     int pos = compiler.getFirstPredicateOpPos(opPos);
/*     */ 
/* 167 */     if (pos > 0)
/*     */     {
/* 169 */       this.m_predicates = compiler.getCompiledPredicates(pos);
/* 170 */       if (null != this.m_predicates)
/*     */       {
/* 172 */         for (int i = 0; i < this.m_predicates.length; i++)
/*     */         {
/* 174 */           this.m_predicates[i].exprSetParent(this);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public Expression getPredicate(int index)
/*     */   {
/* 190 */     return this.m_predicates[index];
/*     */   }
/*     */ 
/*     */   public int getProximityPosition()
/*     */   {
/* 202 */     return getProximityPosition(this.m_predicateIndex);
/*     */   }
/*     */ 
/*     */   public int getProximityPosition(XPathContext xctxt)
/*     */   {
/* 214 */     return getProximityPosition();
/*     */   }
/*     */ 
/*     */   public abstract int getLastPos(XPathContext paramXPathContext);
/*     */ 
/*     */   protected int getProximityPosition(int predicateIndex)
/*     */   {
/* 237 */     return predicateIndex >= 0 ? this.m_proximityPositions[predicateIndex] : 0;
/*     */   }
/*     */ 
/*     */   public void resetProximityPositions()
/*     */   {
/* 245 */     int nPredicates = getPredicateCount();
/* 246 */     if (nPredicates > 0)
/*     */     {
/* 248 */       if (null == this.m_proximityPositions) {
/* 249 */         this.m_proximityPositions = new int[nPredicates];
/*     */       }
/* 251 */       for (int i = 0; i < nPredicates; i++)
/*     */       {
/*     */         try
/*     */         {
/* 255 */           initProximityPosition(i);
/*     */         }
/*     */         catch (Exception e)
/*     */         {
/* 260 */           throw new WrappedRuntimeException(e);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void initProximityPosition(int i)
/*     */     throws TransformerException
/*     */   {
/* 275 */     this.m_proximityPositions[i] = 0;
/*     */   }
/*     */ 
/*     */   protected void countProximityPosition(int i)
/*     */   {
/* 288 */     int[] pp = this.m_proximityPositions;
/* 289 */     if ((null != pp) && (i < pp.length))
/* 290 */       pp[i] += 1;
/*     */   }
/*     */ 
/*     */   public boolean isReverseAxes()
/*     */   {
/* 300 */     return false;
/*     */   }
/*     */ 
/*     */   public int getPredicateIndex()
/*     */   {
/* 310 */     return this.m_predicateIndex;
/*     */   }
/*     */ 
/*     */   boolean executePredicates(int context, XPathContext xctxt)
/*     */     throws TransformerException
/*     */   {
/* 327 */     int nPredicates = getPredicateCount();
/*     */ 
/* 329 */     if (nPredicates == 0) {
/* 330 */       return true;
/*     */     }
/* 332 */     PrefixResolver savedResolver = xctxt.getNamespaceContext();
/*     */     try
/*     */     {
/* 336 */       this.m_predicateIndex = 0;
/* 337 */       xctxt.pushSubContextList(this);
/* 338 */       xctxt.pushNamespaceContext(this.m_lpi.getPrefixResolver());
/* 339 */       xctxt.pushCurrentNode(context);
/*     */ 
/* 341 */       for (int i = 0; i < nPredicates; i++)
/*     */       {
/* 344 */         XObject pred = this.m_predicates[i].execute(xctxt);
/*     */         int proxPos;
/* 347 */         if (2 == pred.getType())
/*     */         {
/* 359 */           proxPos = getProximityPosition(this.m_predicateIndex);
/* 360 */           int predIndex = (int)pred.num();
/* 361 */           if (proxPos != predIndex)
/*     */           {
/* 369 */             return false;
/*     */           }
/*     */ 
/* 387 */           if ((this.m_predicates[i].isStableNumber()) && (i == nPredicates - 1))
/*     */           {
/* 389 */             this.m_foundLast = true;
/*     */           }
/*     */         }
/* 392 */         else if (!pred.bool()) {
/* 393 */           return 0;
/*     */         }
/* 395 */         countProximityPosition(++this.m_predicateIndex);
/*     */       }
/*     */     }
/*     */     finally
/*     */     {
/* 400 */       xctxt.popCurrentNode();
/* 401 */       xctxt.popNamespaceContext();
/* 402 */       xctxt.popSubContextList();
/* 403 */       this.m_predicateIndex = -1;
/*     */     }
/*     */ 
/* 406 */     return true;
/*     */   }
/*     */ 
/*     */   public void fixupVariables(Vector vars, int globalsSize)
/*     */   {
/* 421 */     super.fixupVariables(vars, globalsSize);
/*     */ 
/* 423 */     int nPredicates = getPredicateCount();
/*     */ 
/* 425 */     for (int i = 0; i < nPredicates; i++)
/*     */     {
/* 427 */       this.m_predicates[i].fixupVariables(vars, globalsSize);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected String nodeToString(int n)
/*     */   {
/* 441 */     if (-1 != n)
/*     */     {
/* 443 */       DTM dtm = this.m_lpi.getXPathContext().getDTM(n);
/* 444 */       return dtm.getNodeName(n) + "{" + (n + 1) + "}";
/*     */     }
/*     */ 
/* 448 */     return "null";
/*     */   }
/*     */ 
/*     */   public short acceptNode(int n)
/*     */   {
/* 466 */     XPathContext xctxt = this.m_lpi.getXPathContext();
/*     */     try
/*     */     {
/* 470 */       xctxt.pushCurrentNode(n);
/*     */ 
/* 472 */       XObject score = execute(xctxt, n);
/*     */ 
/* 475 */       if (score != NodeTest.SCORE_NONE)
/*     */       {
/*     */         short s;
/* 477 */         if (getPredicateCount() > 0)
/*     */         {
/* 479 */           countProximityPosition(0);
/*     */ 
/* 481 */           if (!executePredicates(n, xctxt)) {
/* 482 */             return 3;
/*     */           }
/*     */         }
/* 485 */         return 1;
/*     */       }
/*     */ 
/*     */     }
/*     */     catch (TransformerException se)
/*     */     {
/* 492 */       throw new RuntimeException(se.getMessage());
/*     */     }
/*     */     finally
/*     */     {
/* 496 */       xctxt.popCurrentNode();
/*     */     }
/*     */ 
/* 499 */     return 3;
/*     */   }
/*     */ 
/*     */   public LocPathIterator getLocPathIterator()
/*     */   {
/* 510 */     return this.m_lpi;
/*     */   }
/*     */ 
/*     */   public void setLocPathIterator(LocPathIterator li)
/*     */   {
/* 521 */     this.m_lpi = li;
/* 522 */     if (this != li)
/* 523 */       li.exprSetParent(this);
/*     */   }
/*     */ 
/*     */   public boolean canTraverseOutsideSubtree()
/*     */   {
/* 534 */     int n = getPredicateCount();
/* 535 */     for (int i = 0; i < n; i++)
/*     */     {
/* 537 */       if (getPredicate(i).canTraverseOutsideSubtree())
/* 538 */         return true;
/*     */     }
/* 540 */     return false;
/*     */   }
/*     */ 
/*     */   public void callPredicateVisitors(XPathVisitor visitor)
/*     */   {
/* 552 */     if (null != this.m_predicates)
/*     */     {
/* 554 */       int n = this.m_predicates.length;
/* 555 */       for (int i = 0; i < n; i++)
/*     */       {
/* 557 */         ExpressionOwner predOwner = new PredOwner(i);
/* 558 */         if (visitor.visitPredicate(predOwner, this.m_predicates[i]))
/*     */         {
/* 560 */           this.m_predicates[i].callVisitors(predOwner, visitor);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean deepEquals(Expression expr)
/*     */   {
/* 572 */     if (!super.deepEquals(expr)) {
/* 573 */       return false;
/*     */     }
/* 575 */     PredicatedNodeTest pnt = (PredicatedNodeTest)expr;
/* 576 */     if (null != this.m_predicates)
/*     */     {
/* 579 */       int n = this.m_predicates.length;
/* 580 */       if ((null == pnt.m_predicates) || (pnt.m_predicates.length != n))
/* 581 */         return false;
/* 582 */       for (int i = 0; i < n; i++)
/*     */       {
/* 584 */         if (!this.m_predicates[i].deepEquals(pnt.m_predicates[i]))
/* 585 */           return false;
/*     */       }
/*     */     }
/* 588 */     else if (null != pnt.m_predicates) {
/* 589 */       return false;
/*     */     }
/* 591 */     return true;
/*     */   }
/*     */ 
/*     */   class PredOwner
/*     */     implements ExpressionOwner
/*     */   {
/*     */     int m_index;
/*     */ 
/*     */     PredOwner(int index)
/*     */     {
/* 627 */       this.m_index = index;
/*     */     }
/*     */ 
/*     */     public Expression getExpression()
/*     */     {
/* 635 */       return PredicatedNodeTest.this.m_predicates[this.m_index];
/*     */     }
/*     */ 
/*     */     public void setExpression(Expression exp)
/*     */     {
/* 644 */       exp.exprSetParent(PredicatedNodeTest.this);
/* 645 */       PredicatedNodeTest.this.m_predicates[this.m_index] = exp;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.axes.PredicatedNodeTest
 * JD-Core Version:    0.6.2
 */