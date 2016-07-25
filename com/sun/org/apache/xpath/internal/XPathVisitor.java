/*     */ package com.sun.org.apache.xpath.internal;
/*     */ 
/*     */ import com.sun.org.apache.xpath.internal.axes.LocPathIterator;
/*     */ import com.sun.org.apache.xpath.internal.axes.UnionPathIterator;
/*     */ import com.sun.org.apache.xpath.internal.functions.Function;
/*     */ import com.sun.org.apache.xpath.internal.objects.XNumber;
/*     */ import com.sun.org.apache.xpath.internal.objects.XString;
/*     */ import com.sun.org.apache.xpath.internal.operations.Operation;
/*     */ import com.sun.org.apache.xpath.internal.operations.UnaryOperation;
/*     */ import com.sun.org.apache.xpath.internal.operations.Variable;
/*     */ import com.sun.org.apache.xpath.internal.patterns.NodeTest;
/*     */ import com.sun.org.apache.xpath.internal.patterns.StepPattern;
/*     */ import com.sun.org.apache.xpath.internal.patterns.UnionPattern;
/*     */ 
/*     */ public class XPathVisitor
/*     */ {
/*     */   public boolean visitLocationPath(ExpressionOwner owner, LocPathIterator path)
/*     */   {
/*  64 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean visitUnionPath(ExpressionOwner owner, UnionPathIterator path)
/*     */   {
/*  76 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean visitStep(ExpressionOwner owner, NodeTest step)
/*     */   {
/*  88 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean visitPredicate(ExpressionOwner owner, Expression pred)
/*     */   {
/* 103 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean visitBinaryOperation(ExpressionOwner owner, Operation op)
/*     */   {
/* 115 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean visitUnaryOperation(ExpressionOwner owner, UnaryOperation op)
/*     */   {
/* 127 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean visitVariableRef(ExpressionOwner owner, Variable var)
/*     */   {
/* 139 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean visitFunction(ExpressionOwner owner, Function func)
/*     */   {
/* 151 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean visitMatchPattern(ExpressionOwner owner, StepPattern pattern)
/*     */   {
/* 163 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean visitUnionPattern(ExpressionOwner owner, UnionPattern pattern)
/*     */   {
/* 175 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean visitStringLiteral(ExpressionOwner owner, XString str)
/*     */   {
/* 187 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean visitNumberLiteral(ExpressionOwner owner, XNumber num)
/*     */   {
/* 200 */     return true;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.XPathVisitor
 * JD-Core Version:    0.6.2
 */