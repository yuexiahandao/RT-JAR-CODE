/*     */ package com.sun.org.apache.xpath.internal.axes;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.res.XSLMessages;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTM;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser;
/*     */ import com.sun.org.apache.xpath.internal.Expression;
/*     */ import com.sun.org.apache.xpath.internal.ExpressionOwner;
/*     */ import com.sun.org.apache.xpath.internal.XPathContext;
/*     */ import com.sun.org.apache.xpath.internal.XPathVisitor;
/*     */ import com.sun.org.apache.xpath.internal.compiler.Compiler;
/*     */ import java.util.Vector;
/*     */ import javax.xml.transform.TransformerException;
/*     */ 
/*     */ public class AxesWalker extends PredicatedNodeTest
/*     */   implements Cloneable, PathComponent, ExpressionOwner
/*     */ {
/*     */   static final long serialVersionUID = -2966031951306601247L;
/*     */   private DTM m_dtm;
/* 569 */   transient int m_root = -1;
/*     */ 
/* 574 */   private transient int m_currentNode = -1;
/*     */   transient boolean m_isFresh;
/*     */   protected AxesWalker m_nextWalker;
/*     */   AxesWalker m_prevWalker;
/* 588 */   protected int m_axis = -1;
/*     */   protected DTMAxisTraverser m_traverser;
/*     */ 
/*     */   public AxesWalker(LocPathIterator locPathIterator, int axis)
/*     */   {
/*  54 */     super(locPathIterator);
/*  55 */     this.m_axis = axis;
/*     */   }
/*     */ 
/*     */   public final WalkingIterator wi()
/*     */   {
/*  60 */     return (WalkingIterator)this.m_lpi;
/*     */   }
/*     */ 
/*     */   public void init(Compiler compiler, int opPos, int stepType)
/*     */     throws TransformerException
/*     */   {
/*  77 */     initPredicateInfo(compiler, opPos);
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */     throws CloneNotSupportedException
/*     */   {
/*  93 */     AxesWalker clone = (AxesWalker)super.clone();
/*     */ 
/*  99 */     return clone;
/*     */   }
/*     */ 
/*     */   AxesWalker cloneDeep(WalkingIterator cloneOwner, Vector cloneList)
/*     */     throws CloneNotSupportedException
/*     */   {
/* 118 */     AxesWalker clone = findClone(this, cloneList);
/* 119 */     if (null != clone)
/* 120 */       return clone;
/* 121 */     clone = (AxesWalker)clone();
/* 122 */     clone.setLocPathIterator(cloneOwner);
/* 123 */     if (null != cloneList)
/*     */     {
/* 125 */       cloneList.addElement(this);
/* 126 */       cloneList.addElement(clone);
/*     */     }
/*     */ 
/* 129 */     if (wi().m_lastUsedWalker == this) {
/* 130 */       cloneOwner.m_lastUsedWalker = clone;
/*     */     }
/* 132 */     if (null != this.m_nextWalker) {
/* 133 */       clone.m_nextWalker = this.m_nextWalker.cloneDeep(cloneOwner, cloneList);
/*     */     }
/*     */ 
/* 137 */     if (null != cloneList)
/*     */     {
/* 139 */       if (null != this.m_prevWalker) {
/* 140 */         clone.m_prevWalker = this.m_prevWalker.cloneDeep(cloneOwner, cloneList);
/*     */       }
/*     */ 
/*     */     }
/* 144 */     else if (null != this.m_nextWalker) {
/* 145 */       clone.m_nextWalker.m_prevWalker = clone;
/*     */     }
/* 147 */     return clone;
/*     */   }
/*     */ 
/*     */   static AxesWalker findClone(AxesWalker key, Vector cloneList)
/*     */   {
/* 161 */     if (null != cloneList)
/*     */     {
/* 164 */       int n = cloneList.size();
/* 165 */       for (int i = 0; i < n; i += 2)
/*     */       {
/* 167 */         if (key == cloneList.elementAt(i))
/* 168 */           return (AxesWalker)cloneList.elementAt(i + 1);
/*     */       }
/*     */     }
/* 171 */     return null;
/*     */   }
/*     */ 
/*     */   public void detach()
/*     */   {
/* 181 */     this.m_currentNode = -1;
/* 182 */     this.m_dtm = null;
/* 183 */     this.m_traverser = null;
/* 184 */     this.m_isFresh = true;
/* 185 */     this.m_root = -1;
/*     */   }
/*     */ 
/*     */   public int getRoot()
/*     */   {
/* 198 */     return this.m_root;
/*     */   }
/*     */ 
/*     */   public int getAnalysisBits()
/*     */   {
/* 207 */     int axis = getAxis();
/* 208 */     int bit = WalkerFactory.getAnalysisBitFromAxes(axis);
/* 209 */     return bit;
/*     */   }
/*     */ 
/*     */   public void setRoot(int root)
/*     */   {
/* 221 */     XPathContext xctxt = wi().getXPathContext();
/* 222 */     this.m_dtm = xctxt.getDTM(root);
/* 223 */     this.m_traverser = this.m_dtm.getAxisTraverser(this.m_axis);
/* 224 */     this.m_isFresh = true;
/* 225 */     this.m_foundLast = false;
/* 226 */     this.m_root = root;
/* 227 */     this.m_currentNode = root;
/*     */ 
/* 229 */     if (-1 == root)
/*     */     {
/* 231 */       throw new RuntimeException(XSLMessages.createXPATHMessage("ER_SETTING_WALKER_ROOT_TO_NULL", null));
/*     */     }
/*     */ 
/* 235 */     resetProximityPositions();
/*     */   }
/*     */ 
/*     */   public final int getCurrentNode()
/*     */   {
/* 254 */     return this.m_currentNode;
/*     */   }
/*     */ 
/*     */   public void setNextWalker(AxesWalker walker)
/*     */   {
/* 265 */     this.m_nextWalker = walker;
/*     */   }
/*     */ 
/*     */   public AxesWalker getNextWalker()
/*     */   {
/* 276 */     return this.m_nextWalker;
/*     */   }
/*     */ 
/*     */   public void setPrevWalker(AxesWalker walker)
/*     */   {
/* 288 */     this.m_prevWalker = walker;
/*     */   }
/*     */ 
/*     */   public AxesWalker getPrevWalker()
/*     */   {
/* 300 */     return this.m_prevWalker;
/*     */   }
/*     */ 
/*     */   private int returnNextNode(int n)
/*     */   {
/* 314 */     return n;
/*     */   }
/*     */ 
/*     */   protected int getNextNode()
/*     */   {
/* 324 */     if (this.m_foundLast) {
/* 325 */       return -1;
/*     */     }
/* 327 */     if (this.m_isFresh)
/*     */     {
/* 329 */       this.m_currentNode = this.m_traverser.first(this.m_root);
/* 330 */       this.m_isFresh = false;
/*     */     }
/* 335 */     else if (-1 != this.m_currentNode)
/*     */     {
/* 337 */       this.m_currentNode = this.m_traverser.next(this.m_root, this.m_currentNode);
/*     */     }
/*     */ 
/* 340 */     if (-1 == this.m_currentNode) {
/* 341 */       this.m_foundLast = true;
/*     */     }
/* 343 */     return this.m_currentNode;
/*     */   }
/*     */ 
/*     */   public int nextNode()
/*     */   {
/* 357 */     int nextNode = -1;
/* 358 */     AxesWalker walker = wi().getLastUsedWalker();
/*     */ 
/* 362 */     while (null != walker)
/*     */     {
/* 365 */       nextNode = walker.getNextNode();
/*     */ 
/* 367 */       if (-1 == nextNode)
/*     */       {
/* 370 */         walker = walker.m_prevWalker;
/*     */       }
/* 374 */       else if (walker.acceptNode(nextNode) == 1)
/*     */       {
/* 379 */         if (null == walker.m_nextWalker)
/*     */         {
/* 381 */           wi().setLastUsedWalker(walker);
/*     */ 
/* 384 */           break;
/*     */         }
/*     */ 
/* 388 */         AxesWalker prev = walker;
/*     */ 
/* 390 */         walker = walker.m_nextWalker;
/*     */ 
/* 392 */         walker.setRoot(nextNode);
/*     */ 
/* 394 */         walker.m_prevWalker = prev;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 401 */     return nextNode;
/*     */   }
/*     */ 
/*     */   public int getLastPos(XPathContext xctxt)
/*     */   {
/* 417 */     int pos = getProximityPosition();
/*     */     AxesWalker walker;
/*     */     try
/*     */     {
/* 423 */       walker = (AxesWalker)clone();
/*     */     }
/*     */     catch (CloneNotSupportedException cnse)
/*     */     {
/* 427 */       return -1;
/*     */     }
/*     */ 
/* 430 */     walker.setPredicateCount(this.m_predicateIndex);
/* 431 */     walker.setNextWalker(null);
/* 432 */     walker.setPrevWalker(null);
/*     */ 
/* 434 */     WalkingIterator lpi = wi();
/* 435 */     AxesWalker savedWalker = lpi.getLastUsedWalker();
/*     */     try
/*     */     {
/* 439 */       lpi.setLastUsedWalker(walker);
/*     */       int next;
/* 443 */       while (-1 != (next = walker.nextNode()))
/*     */       {
/* 445 */         pos++;
/*     */       }
/*     */ 
/*     */     }
/*     */     finally
/*     */     {
/* 452 */       lpi.setLastUsedWalker(savedWalker);
/*     */     }
/*     */ 
/* 456 */     return pos;
/*     */   }
/*     */ 
/*     */   public void setDefaultDTM(DTM dtm)
/*     */   {
/* 476 */     this.m_dtm = dtm;
/*     */   }
/*     */ 
/*     */   public DTM getDTM(int node)
/*     */   {
/* 487 */     return wi().getXPathContext().getDTM(node);
/*     */   }
/*     */ 
/*     */   public boolean isDocOrdered()
/*     */   {
/* 499 */     return true;
/*     */   }
/*     */ 
/*     */   public int getAxis()
/*     */   {
/* 510 */     return this.m_axis;
/*     */   }
/*     */ 
/*     */   public void callVisitors(ExpressionOwner owner, XPathVisitor visitor)
/*     */   {
/* 524 */     if (visitor.visitStep(owner, this))
/*     */     {
/* 526 */       callPredicateVisitors(visitor);
/* 527 */       if (null != this.m_nextWalker)
/*     */       {
/* 529 */         this.m_nextWalker.callVisitors(this, visitor);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public Expression getExpression()
/*     */   {
/* 539 */     return this.m_nextWalker;
/*     */   }
/*     */ 
/*     */   public void setExpression(Expression exp)
/*     */   {
/* 547 */     exp.exprSetParent(this);
/* 548 */     this.m_nextWalker = ((AxesWalker)exp);
/*     */   }
/*     */ 
/*     */   public boolean deepEquals(Expression expr)
/*     */   {
/* 556 */     if (!super.deepEquals(expr)) {
/* 557 */       return false;
/*     */     }
/* 559 */     AxesWalker walker = (AxesWalker)expr;
/* 560 */     if (this.m_axis != walker.m_axis) {
/* 561 */       return false;
/*     */     }
/* 563 */     return true;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.axes.AxesWalker
 * JD-Core Version:    0.6.2
 */