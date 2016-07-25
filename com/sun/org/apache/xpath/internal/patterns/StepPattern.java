/*      */ package com.sun.org.apache.xpath.internal.patterns;
/*      */ 
/*      */ import com.sun.org.apache.xml.internal.dtm.Axis;
/*      */ import com.sun.org.apache.xml.internal.dtm.DTM;
/*      */ import com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser;
/*      */ import com.sun.org.apache.xpath.internal.Expression;
/*      */ import com.sun.org.apache.xpath.internal.ExpressionOwner;
/*      */ import com.sun.org.apache.xpath.internal.XPathContext;
/*      */ import com.sun.org.apache.xpath.internal.XPathVisitor;
/*      */ import com.sun.org.apache.xpath.internal.axes.SubContextList;
/*      */ import com.sun.org.apache.xpath.internal.objects.XObject;
/*      */ import java.util.Vector;
/*      */ import javax.xml.transform.TransformerException;
/*      */ 
/*      */ public class StepPattern extends NodeTest
/*      */   implements SubContextList, ExpressionOwner
/*      */ {
/*      */   static final long serialVersionUID = 9071668960168152644L;
/*      */   protected int m_axis;
/*      */   String m_targetString;
/*      */   StepPattern m_relativePathPattern;
/*      */   Expression[] m_predicates;
/*      */   private static final boolean DEBUG_MATCHES = false;
/*      */ 
/*      */   public StepPattern(int whatToShow, String namespace, String name, int axis, int axisForPredicate)
/*      */   {
/*   62 */     super(whatToShow, namespace, name);
/*      */ 
/*   64 */     this.m_axis = axis;
/*      */   }
/*      */ 
/*      */   public StepPattern(int whatToShow, int axis, int axisForPredicate)
/*      */   {
/*   78 */     super(whatToShow);
/*      */ 
/*   80 */     this.m_axis = axis;
/*      */   }
/*      */ 
/*      */   public void calcTargetString()
/*      */   {
/*   98 */     int whatToShow = getWhatToShow();
/*      */ 
/*  100 */     switch (whatToShow)
/*      */     {
/*      */     case 128:
/*  103 */       this.m_targetString = "#comment";
/*  104 */       break;
/*      */     case 4:
/*      */     case 8:
/*      */     case 12:
/*  108 */       this.m_targetString = "#text";
/*  109 */       break;
/*      */     case -1:
/*  111 */       this.m_targetString = "*";
/*  112 */       break;
/*      */     case 256:
/*      */     case 1280:
/*  115 */       this.m_targetString = "/";
/*  116 */       break;
/*      */     case 1:
/*  118 */       if ("*" == this.m_name)
/*  119 */         this.m_targetString = "*";
/*      */       else
/*  121 */         this.m_targetString = this.m_name;
/*  122 */       break;
/*      */     default:
/*  124 */       this.m_targetString = "*";
/*      */     }
/*      */   }
/*      */ 
/*      */   public String getTargetString()
/*      */   {
/*  139 */     return this.m_targetString;
/*      */   }
/*      */ 
/*      */   public void fixupVariables(Vector vars, int globalsSize)
/*      */   {
/*  163 */     super.fixupVariables(vars, globalsSize);
/*      */ 
/*  165 */     if (null != this.m_predicates)
/*      */     {
/*  167 */       for (int i = 0; i < this.m_predicates.length; i++)
/*      */       {
/*  169 */         this.m_predicates[i].fixupVariables(vars, globalsSize);
/*      */       }
/*      */     }
/*      */ 
/*  173 */     if (null != this.m_relativePathPattern)
/*      */     {
/*  175 */       this.m_relativePathPattern.fixupVariables(vars, globalsSize);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setRelativePathPattern(StepPattern expr)
/*      */   {
/*  189 */     this.m_relativePathPattern = expr;
/*  190 */     expr.exprSetParent(this);
/*      */ 
/*  192 */     calcScore();
/*      */   }
/*      */ 
/*      */   public StepPattern getRelativePathPattern()
/*      */   {
/*  204 */     return this.m_relativePathPattern;
/*      */   }
/*      */ 
/*      */   public Expression[] getPredicates()
/*      */   {
/*  222 */     return this.m_predicates;
/*      */   }
/*      */ 
/*      */   public boolean canTraverseOutsideSubtree()
/*      */   {
/*  243 */     int n = getPredicateCount();
/*      */ 
/*  245 */     for (int i = 0; i < n; i++)
/*      */     {
/*  247 */       if (getPredicate(i).canTraverseOutsideSubtree()) {
/*  248 */         return true;
/*      */       }
/*      */     }
/*  251 */     return false;
/*      */   }
/*      */ 
/*      */   public Expression getPredicate(int i)
/*      */   {
/*  264 */     return this.m_predicates[i];
/*      */   }
/*      */ 
/*      */   public final int getPredicateCount()
/*      */   {
/*  275 */     return null == this.m_predicates ? 0 : this.m_predicates.length;
/*      */   }
/*      */ 
/*      */   public void setPredicates(Expression[] predicates)
/*      */   {
/*  288 */     this.m_predicates = predicates;
/*  289 */     if (null != predicates)
/*      */     {
/*  291 */       for (int i = 0; i < predicates.length; i++)
/*      */       {
/*  293 */         predicates[i].exprSetParent(this);
/*      */       }
/*      */     }
/*      */ 
/*  297 */     calcScore();
/*      */   }
/*      */ 
/*      */   public void calcScore()
/*      */   {
/*  306 */     if ((getPredicateCount() > 0) || (null != this.m_relativePathPattern))
/*      */     {
/*  308 */       this.m_score = SCORE_OTHER;
/*      */     }
/*      */     else {
/*  311 */       super.calcScore();
/*      */     }
/*  313 */     if (null == this.m_targetString)
/*  314 */       calcTargetString();
/*      */   }
/*      */ 
/*      */   public XObject execute(XPathContext xctxt, int currentNode)
/*      */     throws TransformerException
/*      */   {
/*  336 */     DTM dtm = xctxt.getDTM(currentNode);
/*      */ 
/*  338 */     if (dtm != null)
/*      */     {
/*  340 */       int expType = dtm.getExpandedTypeID(currentNode);
/*      */ 
/*  342 */       return execute(xctxt, currentNode, dtm, expType);
/*      */     }
/*      */ 
/*  345 */     return NodeTest.SCORE_NONE;
/*      */   }
/*      */ 
/*      */   public XObject execute(XPathContext xctxt)
/*      */     throws TransformerException
/*      */   {
/*  365 */     return execute(xctxt, xctxt.getCurrentNode());
/*      */   }
/*      */ 
/*      */   public XObject execute(XPathContext xctxt, int currentNode, DTM dtm, int expType)
/*      */     throws TransformerException
/*      */   {
/*  388 */     if (this.m_whatToShow == 65536)
/*      */     {
/*  390 */       if (null != this.m_relativePathPattern)
/*      */       {
/*  392 */         return this.m_relativePathPattern.execute(xctxt);
/*      */       }
/*      */ 
/*  395 */       return NodeTest.SCORE_NONE;
/*      */     }
/*      */ 
/*  400 */     XObject score = super.execute(xctxt, currentNode, dtm, expType);
/*      */ 
/*  402 */     if (score == NodeTest.SCORE_NONE) {
/*  403 */       return NodeTest.SCORE_NONE;
/*      */     }
/*  405 */     if (getPredicateCount() != 0)
/*      */     {
/*  407 */       if (!executePredicates(xctxt, dtm, currentNode)) {
/*  408 */         return NodeTest.SCORE_NONE;
/*      */       }
/*      */     }
/*  411 */     if (null != this.m_relativePathPattern) {
/*  412 */       return this.m_relativePathPattern.executeRelativePathPattern(xctxt, dtm, currentNode);
/*      */     }
/*      */ 
/*  415 */     return score;
/*      */   }
/*      */ 
/*      */   private final boolean checkProximityPosition(XPathContext xctxt, int predPos, DTM dtm, int context, int pos)
/*      */   {
/*      */     try
/*      */     {
/*  436 */       DTMAxisTraverser traverser = dtm.getAxisTraverser(12);
/*      */ 
/*  439 */       for (int child = traverser.first(context); -1 != child; 
/*  440 */         child = traverser.next(context, child))
/*      */       {
/*      */         try
/*      */         {
/*  444 */           xctxt.pushCurrentNode(child);
/*      */ 
/*  446 */           if (NodeTest.SCORE_NONE != super.execute(xctxt, child))
/*      */           {
/*  448 */             boolean pass = true;
/*      */             int i;
/*      */             try {
/*  452 */               xctxt.pushSubContextList(this);
/*      */ 
/*  454 */               for (i = 0; i < predPos; i++)
/*      */               {
/*  456 */                 xctxt.pushPredicatePos(i);
/*      */                 try
/*      */                 {
/*  459 */                   XObject pred = this.m_predicates[i].execute(xctxt);
/*      */                   try
/*      */                   {
/*  463 */                     if (2 == pred.getType())
/*      */                     {
/*  465 */                       throw new Error("Why: Should never have been called");
/*      */                     }
/*  467 */                     if (!pred.boolWithSideEffects())
/*      */                     {
/*  469 */                       pass = false;
/*      */ 
/*  476 */                       pred.detach();
/*      */ 
/*  481 */                       xctxt.popPredicatePos(); break;
/*      */                     }
/*      */                   } finally {
/*      */                   }
/*      */                 } finally {
/*      */                 }
/*      */               }
/*      */             } finally {
/*      */             }
/*  490 */             if (pass) {
/*  491 */               pos--;
/*      */             }
/*  493 */             if (pos < 1)
/*  494 */               return 0;
/*      */           }
/*      */         }
/*      */         finally
/*      */         {
/*  499 */           xctxt.popCurrentNode();
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */     catch (TransformerException se)
/*      */     {
/*  507 */       throw new RuntimeException(se.getMessage());
/*      */     }
/*      */ 
/*  510 */     return pos == 1;
/*      */   }
/*      */ 
/*      */   private final int getProximityPosition(XPathContext xctxt, int predPos, boolean findLast)
/*      */   {
/*  529 */     int pos = 0;
/*  530 */     int context = xctxt.getCurrentNode();
/*  531 */     DTM dtm = xctxt.getDTM(context);
/*  532 */     int parent = dtm.getParent(context);
/*      */     try
/*      */     {
/*  536 */       DTMAxisTraverser traverser = dtm.getAxisTraverser(3);
/*      */ 
/*  538 */       for (int child = traverser.first(parent); -1 != child; 
/*  539 */         child = traverser.next(parent, child))
/*      */       {
/*      */         try
/*      */         {
/*  543 */           xctxt.pushCurrentNode(child);
/*      */ 
/*  545 */           if (NodeTest.SCORE_NONE != super.execute(xctxt, child))
/*      */           {
/*  547 */             boolean pass = true;
/*      */             int i;
/*      */             try {
/*  551 */               xctxt.pushSubContextList(this);
/*      */ 
/*  553 */               for (i = 0; i < predPos; i++)
/*      */               {
/*  555 */                 xctxt.pushPredicatePos(i);
/*      */                 try
/*      */                 {
/*  558 */                   XObject pred = this.m_predicates[i].execute(xctxt);
/*      */                   try
/*      */                   {
/*  562 */                     if (2 == pred.getType())
/*      */                     {
/*  564 */                       if (pos + 1 != (int)pred.numWithSideEffects())
/*      */                       {
/*  566 */                         pass = false;
/*      */ 
/*  580 */                         pred.detach();
/*      */ 
/*  585 */                         xctxt.popPredicatePos(); break;
/*      */                       }
/*      */                     }
/*  571 */                     else if (!pred.boolWithSideEffects())
/*      */                     {
/*  573 */                       pass = false;
/*      */ 
/*  580 */                       pred.detach();
/*      */ 
/*  585 */                       xctxt.popPredicatePos(); break;
/*      */                     }
/*      */                   } finally {
/*      */                   }
/*      */                 } finally {
/*      */                 }
/*      */               }
/*      */             } finally {
/*      */             }
/*  594 */             if (pass) {
/*  595 */               pos++;
/*      */             }
/*  597 */             if ((!findLast) && (child == context))
/*      */             {
/*  599 */               return pos;
/*      */             }
/*      */           }
/*      */         }
/*      */         finally
/*      */         {
/*  605 */           xctxt.popCurrentNode();
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */     catch (TransformerException se)
/*      */     {
/*  613 */       throw new RuntimeException(se.getMessage());
/*      */     }
/*      */ 
/*  616 */     return pos;
/*      */   }
/*      */ 
/*      */   public int getProximityPosition(XPathContext xctxt)
/*      */   {
/*  631 */     return getProximityPosition(xctxt, xctxt.getPredicatePos(), false);
/*      */   }
/*      */ 
/*      */   public int getLastPos(XPathContext xctxt)
/*      */   {
/*  647 */     return getProximityPosition(xctxt, xctxt.getPredicatePos(), true);
/*      */   }
/*      */ 
/*      */   protected final XObject executeRelativePathPattern(XPathContext xctxt, DTM dtm, int currentNode)
/*      */     throws TransformerException
/*      */   {
/*  671 */     XObject score = NodeTest.SCORE_NONE;
/*  672 */     int context = currentNode;
/*      */ 
/*  675 */     DTMAxisTraverser traverser = dtm.getAxisTraverser(this.m_axis);
/*      */ 
/*  677 */     for (int relative = traverser.first(context); -1 != relative; 
/*  678 */       relative = traverser.next(context, relative))
/*      */     {
/*      */       try
/*      */       {
/*  682 */         xctxt.pushCurrentNode(relative);
/*      */ 
/*  684 */         score = execute(xctxt);
/*      */ 
/*  686 */         if (score != NodeTest.SCORE_NONE)
/*      */         {
/*  691 */           xctxt.popCurrentNode(); break; }  } finally { xctxt.popCurrentNode(); }
/*      */ 
/*      */     }
/*      */ 
/*  695 */     return score;
/*      */   }
/*      */ 
/*      */   protected final boolean executePredicates(XPathContext xctxt, DTM dtm, int currentNode)
/*      */     throws TransformerException
/*      */   {
/*  715 */     boolean result = true;
/*  716 */     boolean positionAlreadySeen = false;
/*  717 */     int n = getPredicateCount();
/*      */     try
/*      */     {
/*  721 */       xctxt.pushSubContextList(this);
/*      */ 
/*  723 */       for (int i = 0; i < n; i++)
/*      */       {
/*  725 */         xctxt.pushPredicatePos(i);
/*      */         try
/*      */         {
/*  729 */           XObject pred = this.m_predicates[i].execute(xctxt);
/*      */           try
/*      */           {
/*  733 */             if (2 == pred.getType())
/*      */             {
/*  735 */               int pos = (int)pred.num();
/*      */ 
/*  737 */               if (positionAlreadySeen)
/*      */               {
/*  739 */                 result = pos == 1;
/*      */ 
/*  765 */                 pred.detach();
/*      */ 
/*  770 */                 xctxt.popPredicatePos(); break;
/*      */               }
/*  745 */               positionAlreadySeen = true;
/*      */ 
/*  747 */               if (!checkProximityPosition(xctxt, i, dtm, currentNode, pos))
/*      */               {
/*  749 */                 result = false;
/*      */ 
/*  765 */                 pred.detach();
/*      */ 
/*  770 */                 xctxt.popPredicatePos(); break;
/*      */               }
/*      */             }
/*  756 */             else if (!pred.boolWithSideEffects())
/*      */             {
/*  758 */               result = false;
/*      */ 
/*  765 */               pred.detach();
/*      */ 
/*  770 */               xctxt.popPredicatePos(); break;
/*      */             } } finally {
/*      */           }
/*      */         } finally {
/*      */         }
/*      */       }
/*      */     } finally { xctxt.popSubContextList(); }
/*      */ 
/*      */ 
/*  779 */     return result;
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/*  792 */     StringBuffer buf = new StringBuffer();
/*      */ 
/*  794 */     for (StepPattern pat = this; pat != null; pat = pat.m_relativePathPattern)
/*      */     {
/*  796 */       if (pat != this) {
/*  797 */         buf.append("/");
/*      */       }
/*  799 */       buf.append(Axis.getNames(pat.m_axis));
/*  800 */       buf.append("::");
/*      */ 
/*  802 */       if (20480 == pat.m_whatToShow)
/*      */       {
/*  804 */         buf.append("doc()");
/*      */       }
/*  806 */       else if (65536 == pat.m_whatToShow)
/*      */       {
/*  808 */         buf.append("function()");
/*      */       }
/*  810 */       else if (-1 == pat.m_whatToShow)
/*      */       {
/*  812 */         buf.append("node()");
/*      */       }
/*  814 */       else if (4 == pat.m_whatToShow)
/*      */       {
/*  816 */         buf.append("text()");
/*      */       }
/*  818 */       else if (64 == pat.m_whatToShow)
/*      */       {
/*  820 */         buf.append("processing-instruction(");
/*      */ 
/*  822 */         if (null != pat.m_name)
/*      */         {
/*  824 */           buf.append(pat.m_name);
/*      */         }
/*      */ 
/*  827 */         buf.append(")");
/*      */       }
/*  829 */       else if (128 == pat.m_whatToShow)
/*      */       {
/*  831 */         buf.append("comment()");
/*      */       }
/*  833 */       else if (null != pat.m_name)
/*      */       {
/*  835 */         if (2 == pat.m_whatToShow)
/*      */         {
/*  837 */           buf.append("@");
/*      */         }
/*      */ 
/*  840 */         if (null != pat.m_namespace)
/*      */         {
/*  842 */           buf.append("{");
/*  843 */           buf.append(pat.m_namespace);
/*  844 */           buf.append("}");
/*      */         }
/*      */ 
/*  847 */         buf.append(pat.m_name);
/*      */       }
/*  849 */       else if (2 == pat.m_whatToShow)
/*      */       {
/*  851 */         buf.append("@");
/*      */       }
/*  853 */       else if (1280 == pat.m_whatToShow)
/*      */       {
/*  856 */         buf.append("doc-root()");
/*      */       }
/*      */       else
/*      */       {
/*  860 */         buf.append('?').append(Integer.toHexString(pat.m_whatToShow));
/*      */       }
/*      */ 
/*  863 */       if (null != pat.m_predicates)
/*      */       {
/*  865 */         for (int i = 0; i < pat.m_predicates.length; i++)
/*      */         {
/*  867 */           buf.append("[");
/*  868 */           buf.append(pat.m_predicates[i]);
/*  869 */           buf.append("]");
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  874 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   public double getMatchScore(XPathContext xctxt, int context)
/*      */     throws TransformerException
/*      */   {
/*  898 */     xctxt.pushCurrentNode(context);
/*  899 */     xctxt.pushCurrentExpressionNode(context);
/*      */     try
/*      */     {
/*  903 */       XObject score = execute(xctxt);
/*      */ 
/*  905 */       return score.num();
/*      */     }
/*      */     finally
/*      */     {
/*  909 */       xctxt.popCurrentNode();
/*  910 */       xctxt.popCurrentExpressionNode();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setAxis(int axis)
/*      */   {
/*  924 */     this.m_axis = axis;
/*      */   }
/*      */ 
/*      */   public int getAxis()
/*      */   {
/*  935 */     return this.m_axis;
/*      */   }
/*      */ 
/*      */   public void callVisitors(ExpressionOwner owner, XPathVisitor visitor)
/*      */   {
/*  971 */     if (visitor.visitMatchPattern(owner, this))
/*      */     {
/*  973 */       callSubtreeVisitors(visitor);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void callSubtreeVisitors(XPathVisitor visitor)
/*      */   {
/*  983 */     if (null != this.m_predicates)
/*      */     {
/*  985 */       int n = this.m_predicates.length;
/*  986 */       for (int i = 0; i < n; i++)
/*      */       {
/*  988 */         ExpressionOwner predOwner = new PredOwner(i);
/*  989 */         if (visitor.visitPredicate(predOwner, this.m_predicates[i]))
/*      */         {
/*  991 */           this.m_predicates[i].callVisitors(predOwner, visitor);
/*      */         }
/*      */       }
/*      */     }
/*  995 */     if (null != this.m_relativePathPattern)
/*      */     {
/*  997 */       this.m_relativePathPattern.callVisitors(this, visitor);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Expression getExpression()
/*      */   {
/* 1007 */     return this.m_relativePathPattern;
/*      */   }
/*      */ 
/*      */   public void setExpression(Expression exp)
/*      */   {
/* 1015 */     exp.exprSetParent(this);
/* 1016 */     this.m_relativePathPattern = ((StepPattern)exp);
/*      */   }
/*      */ 
/*      */   public boolean deepEquals(Expression expr)
/*      */   {
/* 1024 */     if (!super.deepEquals(expr)) {
/* 1025 */       return false;
/*      */     }
/* 1027 */     StepPattern sp = (StepPattern)expr;
/*      */ 
/* 1029 */     if (null != this.m_predicates)
/*      */     {
/* 1031 */       int n = this.m_predicates.length;
/* 1032 */       if ((null == sp.m_predicates) || (sp.m_predicates.length != n))
/* 1033 */         return false;
/* 1034 */       for (int i = 0; i < n; i++)
/*      */       {
/* 1036 */         if (!this.m_predicates[i].deepEquals(sp.m_predicates[i]))
/* 1037 */           return false;
/*      */       }
/*      */     }
/* 1040 */     else if (null != sp.m_predicates) {
/* 1041 */       return false;
/*      */     }
/* 1043 */     if (null != this.m_relativePathPattern)
/*      */     {
/* 1045 */       if (!this.m_relativePathPattern.deepEquals(sp.m_relativePathPattern))
/* 1046 */         return false;
/*      */     }
/* 1048 */     else if (sp.m_relativePathPattern != null) {
/* 1049 */       return false;
/*      */     }
/* 1051 */     return true;
/*      */   }
/*      */ 
/*      */   class PredOwner
/*      */     implements ExpressionOwner
/*      */   {
/*      */     int m_index;
/*      */ 
/*      */     PredOwner(int index)
/*      */     {
/*  944 */       this.m_index = index;
/*      */     }
/*      */ 
/*      */     public Expression getExpression()
/*      */     {
/*  952 */       return StepPattern.this.m_predicates[this.m_index];
/*      */     }
/*      */ 
/*      */     public void setExpression(Expression exp)
/*      */     {
/*  961 */       exp.exprSetParent(StepPattern.this);
/*  962 */       StepPattern.this.m_predicates[this.m_index] = exp;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.patterns.StepPattern
 * JD-Core Version:    0.6.2
 */