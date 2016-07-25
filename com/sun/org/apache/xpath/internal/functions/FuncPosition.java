/*     */ package com.sun.org.apache.xpath.internal.functions;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMIterator;
/*     */ import com.sun.org.apache.xml.internal.utils.WrappedRuntimeException;
/*     */ import com.sun.org.apache.xpath.internal.XPathContext;
/*     */ import com.sun.org.apache.xpath.internal.axes.SubContextList;
/*     */ import com.sun.org.apache.xpath.internal.compiler.Compiler;
/*     */ import com.sun.org.apache.xpath.internal.objects.XNumber;
/*     */ import com.sun.org.apache.xpath.internal.objects.XObject;
/*     */ import java.util.Vector;
/*     */ import javax.xml.transform.TransformerException;
/*     */ 
/*     */ public class FuncPosition extends Function
/*     */ {
/*     */   static final long serialVersionUID = -9092846348197271582L;
/*     */   private boolean m_isTopLevel;
/*     */ 
/*     */   public void postCompileStep(Compiler compiler)
/*     */   {
/*  48 */     this.m_isTopLevel = (compiler.getLocationPathDepth() == -1);
/*     */   }
/*     */ 
/*     */   public int getPositionInContextNodeList(XPathContext xctxt)
/*     */   {
/*  64 */     SubContextList iter = this.m_isTopLevel ? null : xctxt.getSubContextList();
/*     */ 
/*  66 */     if (null != iter)
/*     */     {
/*  68 */       int prox = iter.getProximityPosition(xctxt);
/*     */ 
/*  71 */       return prox;
/*     */     }
/*     */ 
/*  74 */     DTMIterator cnl = xctxt.getContextNodeList();
/*     */ 
/*  76 */     if (null != cnl)
/*     */     {
/*  78 */       int n = cnl.getCurrentNode();
/*  79 */       if (n == -1)
/*     */       {
/*  81 */         if (cnl.getCurrentPos() == 0) {
/*  82 */           return 0;
/*     */         }
/*     */ 
/*     */         try
/*     */         {
/*  91 */           cnl = cnl.cloneWithReset();
/*     */         }
/*     */         catch (CloneNotSupportedException cnse)
/*     */         {
/*  95 */           throw new WrappedRuntimeException(cnse);
/*     */         }
/*  97 */         int currentNode = xctxt.getContextNode();
/*     */ 
/*  99 */         while (-1 != (n = cnl.nextNode()))
/*     */         {
/* 101 */           if (n == currentNode) {
/* 102 */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 107 */       return cnl.getCurrentPos();
/*     */     }
/*     */ 
/* 111 */     return -1;
/*     */   }
/*     */ 
/*     */   public XObject execute(XPathContext xctxt)
/*     */     throws TransformerException
/*     */   {
/* 124 */     double pos = getPositionInContextNodeList(xctxt);
/*     */ 
/* 126 */     return new XNumber(pos);
/*     */   }
/*     */ 
/*     */   public void fixupVariables(Vector vars, int globalsSize)
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.functions.FuncPosition
 * JD-Core Version:    0.6.2
 */