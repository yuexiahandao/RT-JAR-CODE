/*     */ package com.sun.org.apache.xpath.internal.axes;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.dtm.DTM;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser;
/*     */ import com.sun.org.apache.xpath.internal.Expression;
/*     */ import com.sun.org.apache.xpath.internal.compiler.Compiler;
/*     */ import com.sun.org.apache.xpath.internal.compiler.OpMap;
/*     */ import javax.xml.transform.TransformerException;
/*     */ 
/*     */ public class OneStepIteratorForward extends ChildTestIterator
/*     */ {
/*     */   static final long serialVersionUID = -1576936606178190566L;
/*  43 */   protected int m_axis = -1;
/*     */ 
/*     */   OneStepIteratorForward(Compiler compiler, int opPos, int analysis)
/*     */     throws TransformerException
/*     */   {
/*  57 */     super(compiler, opPos, analysis);
/*  58 */     int firstStepPos = OpMap.getFirstChildPos(opPos);
/*     */ 
/*  60 */     this.m_axis = WalkerFactory.getAxisFromStep(compiler, firstStepPos);
/*     */   }
/*     */ 
/*     */   public OneStepIteratorForward(int axis)
/*     */   {
/*  73 */     super(null);
/*     */ 
/*  75 */     this.m_axis = axis;
/*  76 */     int whatToShow = -1;
/*  77 */     initNodeTest(whatToShow);
/*     */   }
/*     */ 
/*     */   public void setRoot(int context, Object environment)
/*     */   {
/*  92 */     super.setRoot(context, environment);
/*  93 */     this.m_traverser = this.m_cdtm.getAxisTraverser(this.m_axis);
/*     */   }
/*     */ 
/*     */   protected int getNextNode()
/*     */   {
/* 142 */     this.m_lastFetched = (-1 == this.m_lastFetched ? this.m_traverser.first(this.m_context) : this.m_traverser.next(this.m_context, this.m_lastFetched));
/*     */ 
/* 145 */     return this.m_lastFetched;
/*     */   }
/*     */ 
/*     */   public int getAxis()
/*     */   {
/* 156 */     return this.m_axis;
/*     */   }
/*     */ 
/*     */   public boolean deepEquals(Expression expr)
/*     */   {
/* 164 */     if (!super.deepEquals(expr)) {
/* 165 */       return false;
/*     */     }
/* 167 */     if (this.m_axis != ((OneStepIteratorForward)expr).m_axis) {
/* 168 */       return false;
/*     */     }
/* 170 */     return true;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.axes.OneStepIteratorForward
 * JD-Core Version:    0.6.2
 */