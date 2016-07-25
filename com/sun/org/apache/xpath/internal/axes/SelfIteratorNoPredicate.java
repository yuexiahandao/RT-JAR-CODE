/*     */ package com.sun.org.apache.xpath.internal.axes;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.dtm.DTM;
/*     */ import com.sun.org.apache.xpath.internal.XPathContext;
/*     */ import com.sun.org.apache.xpath.internal.compiler.Compiler;
/*     */ import javax.xml.transform.TransformerException;
/*     */ 
/*     */ public class SelfIteratorNoPredicate extends LocPathIterator
/*     */ {
/*     */   static final long serialVersionUID = -4226887905279814201L;
/*     */ 
/*     */   SelfIteratorNoPredicate(Compiler compiler, int opPos, int analysis)
/*     */     throws TransformerException
/*     */   {
/*  52 */     super(compiler, opPos, analysis, false);
/*     */   }
/*     */ 
/*     */   public SelfIteratorNoPredicate()
/*     */     throws TransformerException
/*     */   {
/*  63 */     super(null);
/*     */   }
/*     */ 
/*     */   public int nextNode()
/*     */   {
/*  77 */     if (this.m_foundLast) {
/*  78 */       return -1;
/*     */     }
/*     */ 
/*  81 */     DTM dtm = this.m_cdtm;
/*     */     int next;
/*  83 */     this.m_lastFetched = (next = -1 == this.m_lastFetched ? this.m_context : -1);
/*     */ 
/*  88 */     if (-1 != next)
/*     */     {
/*  90 */       this.m_pos += 1;
/*     */ 
/*  92 */       return next;
/*     */     }
/*     */ 
/*  96 */     this.m_foundLast = true;
/*     */ 
/*  98 */     return -1;
/*     */   }
/*     */ 
/*     */   public int asNode(XPathContext xctxt)
/*     */     throws TransformerException
/*     */   {
/* 113 */     return xctxt.getCurrentNode();
/*     */   }
/*     */ 
/*     */   public int getLastPos(XPathContext xctxt)
/*     */   {
/* 126 */     return 1;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.axes.SelfIteratorNoPredicate
 * JD-Core Version:    0.6.2
 */