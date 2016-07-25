/*     */ package com.sun.org.apache.xpath.internal.axes;
/*     */ 
/*     */ import com.sun.org.apache.xpath.internal.XPathContext;
/*     */ import com.sun.org.apache.xpath.internal.objects.XObject;
/*     */ import com.sun.org.apache.xpath.internal.patterns.NodeTest;
/*     */ import java.util.Vector;
/*     */ import javax.xml.transform.TransformerException;
/*     */ 
/*     */ public class UnionChildIterator extends ChildTestIterator
/*     */ {
/*     */   static final long serialVersionUID = 3500298482193003495L;
/*  44 */   private PredicatedNodeTest[] m_nodeTests = null;
/*     */ 
/*     */   public UnionChildIterator()
/*     */   {
/*  51 */     super(null);
/*     */   }
/*     */ 
/*     */   public void addNodeTest(PredicatedNodeTest test)
/*     */   {
/*  67 */     if (null == this.m_nodeTests)
/*     */     {
/*  69 */       this.m_nodeTests = new PredicatedNodeTest[1];
/*  70 */       this.m_nodeTests[0] = test;
/*     */     }
/*     */     else
/*     */     {
/*  74 */       PredicatedNodeTest[] tests = this.m_nodeTests;
/*  75 */       int len = this.m_nodeTests.length;
/*     */ 
/*  77 */       this.m_nodeTests = new PredicatedNodeTest[len + 1];
/*     */ 
/*  79 */       System.arraycopy(tests, 0, this.m_nodeTests, 0, len);
/*     */ 
/*  81 */       this.m_nodeTests[len] = test;
/*     */     }
/*  83 */     test.exprSetParent(this);
/*     */   }
/*     */ 
/*     */   public void fixupVariables(Vector vars, int globalsSize)
/*     */   {
/*  98 */     super.fixupVariables(vars, globalsSize);
/*  99 */     if (this.m_nodeTests != null)
/* 100 */       for (int i = 0; i < this.m_nodeTests.length; i++)
/* 101 */         this.m_nodeTests[i].fixupVariables(vars, globalsSize);
/*     */   }
/*     */ 
/*     */   public short acceptNode(int n)
/*     */   {
/* 117 */     XPathContext xctxt = getXPathContext();
/*     */     try
/*     */     {
/* 120 */       xctxt.pushCurrentNode(n);
/* 121 */       for (int i = 0; i < this.m_nodeTests.length; i++)
/*     */       {
/* 123 */         PredicatedNodeTest pnt = this.m_nodeTests[i];
/* 124 */         XObject score = pnt.execute(xctxt, n);
/* 125 */         if (score != NodeTest.SCORE_NONE)
/*     */         {
/*     */           short s;
/* 128 */           if (pnt.getPredicateCount() > 0)
/*     */           {
/* 130 */             if (pnt.executePredicates(n, xctxt))
/* 131 */               return 1;
/*     */           }
/*     */           else {
/* 134 */             return 1;
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */     catch (TransformerException se)
/*     */     {
/* 143 */       throw new RuntimeException(se.getMessage());
/*     */     }
/*     */     finally
/*     */     {
/* 147 */       xctxt.popCurrentNode();
/*     */     }
/* 149 */     return 3;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.axes.UnionChildIterator
 * JD-Core Version:    0.6.2
 */