/*     */ package com.sun.org.apache.xpath.internal.axes;
/*     */ 
/*     */ import com.sun.org.apache.xpath.internal.Expression;
/*     */ import com.sun.org.apache.xpath.internal.ExpressionOwner;
/*     */ import com.sun.org.apache.xpath.internal.XPathVisitor;
/*     */ import com.sun.org.apache.xpath.internal.functions.FuncLast;
/*     */ import com.sun.org.apache.xpath.internal.functions.FuncPosition;
/*     */ import com.sun.org.apache.xpath.internal.functions.Function;
/*     */ import com.sun.org.apache.xpath.internal.objects.XNumber;
/*     */ import com.sun.org.apache.xpath.internal.operations.Div;
/*     */ import com.sun.org.apache.xpath.internal.operations.Minus;
/*     */ import com.sun.org.apache.xpath.internal.operations.Mod;
/*     */ import com.sun.org.apache.xpath.internal.operations.Mult;
/*     */ import com.sun.org.apache.xpath.internal.operations.Number;
/*     */ import com.sun.org.apache.xpath.internal.operations.Plus;
/*     */ import com.sun.org.apache.xpath.internal.operations.Quo;
/*     */ import com.sun.org.apache.xpath.internal.operations.Variable;
/*     */ 
/*     */ public class HasPositionalPredChecker extends XPathVisitor
/*     */ {
/*  42 */   private boolean m_hasPositionalPred = false;
/*  43 */   private int m_predDepth = 0;
/*     */ 
/*     */   public static boolean check(LocPathIterator path)
/*     */   {
/*  54 */     HasPositionalPredChecker hppc = new HasPositionalPredChecker();
/*  55 */     path.callVisitors(null, hppc);
/*  56 */     return hppc.m_hasPositionalPred;
/*     */   }
/*     */ 
/*     */   public boolean visitFunction(ExpressionOwner owner, Function func)
/*     */   {
/*  68 */     if (((func instanceof FuncPosition)) || ((func instanceof FuncLast)))
/*     */     {
/*  70 */       this.m_hasPositionalPred = true;
/*  71 */     }return true;
/*     */   }
/*     */ 
/*     */   public boolean visitPredicate(ExpressionOwner owner, Expression pred)
/*     */   {
/*  99 */     this.m_predDepth += 1;
/*     */ 
/* 101 */     if (this.m_predDepth == 1)
/*     */     {
/* 103 */       if (((pred instanceof Variable)) || ((pred instanceof XNumber)) || ((pred instanceof Div)) || ((pred instanceof Plus)) || ((pred instanceof Minus)) || ((pred instanceof Mod)) || ((pred instanceof Quo)) || ((pred instanceof Mult)) || ((pred instanceof Number)) || ((pred instanceof Function)))
/*     */       {
/* 113 */         this.m_hasPositionalPred = true;
/*     */       }
/* 115 */       else pred.callVisitors(owner, this);
/*     */     }
/*     */ 
/* 118 */     this.m_predDepth -= 1;
/*     */ 
/* 121 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.axes.HasPositionalPredChecker
 * JD-Core Version:    0.6.2
 */