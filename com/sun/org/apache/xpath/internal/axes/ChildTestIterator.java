/*     */ package com.sun.org.apache.xpath.internal.axes;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.dtm.DTM;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMIterator;
/*     */ import com.sun.org.apache.xpath.internal.compiler.Compiler;
/*     */ import javax.xml.transform.TransformerException;
/*     */ 
/*     */ public class ChildTestIterator extends BasicTestIterator
/*     */ {
/*     */   static final long serialVersionUID = -7936835957960705722L;
/*     */   protected transient DTMAxisTraverser m_traverser;
/*     */ 
/*     */   ChildTestIterator(Compiler compiler, int opPos, int analysis)
/*     */     throws TransformerException
/*     */   {
/*  59 */     super(compiler, opPos, analysis);
/*     */   }
/*     */ 
/*     */   public ChildTestIterator(DTMAxisTraverser traverser)
/*     */   {
/*  72 */     super(null);
/*     */ 
/*  74 */     this.m_traverser = traverser;
/*     */   }
/*     */ 
/*     */   protected int getNextNode()
/*     */   {
/*  85 */     this.m_lastFetched = (-1 == this.m_lastFetched ? this.m_traverser.first(this.m_context) : this.m_traverser.next(this.m_context, this.m_lastFetched));
/*     */ 
/*  97 */     return this.m_lastFetched;
/*     */   }
/*     */ 
/*     */   public DTMIterator cloneWithReset()
/*     */     throws CloneNotSupportedException
/*     */   {
/* 112 */     ChildTestIterator clone = (ChildTestIterator)super.cloneWithReset();
/* 113 */     clone.m_traverser = this.m_traverser;
/*     */ 
/* 115 */     return clone;
/*     */   }
/*     */ 
/*     */   public void setRoot(int context, Object environment)
/*     */   {
/* 128 */     super.setRoot(context, environment);
/* 129 */     this.m_traverser = this.m_cdtm.getAxisTraverser(3);
/*     */   }
/*     */ 
/*     */   public int getAxis()
/*     */   {
/* 159 */     return 3;
/*     */   }
/*     */ 
/*     */   public void detach()
/*     */   {
/* 171 */     if (this.m_allowDetach)
/*     */     {
/* 173 */       this.m_traverser = null;
/*     */ 
/* 176 */       super.detach();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.axes.ChildTestIterator
 * JD-Core Version:    0.6.2
 */