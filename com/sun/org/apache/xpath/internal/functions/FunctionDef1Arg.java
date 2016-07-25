/*     */ package com.sun.org.apache.xpath.internal.functions;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.res.XSLMessages;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTM;
/*     */ import com.sun.org.apache.xml.internal.utils.XMLString;
/*     */ import com.sun.org.apache.xpath.internal.Expression;
/*     */ import com.sun.org.apache.xpath.internal.XPathContext;
/*     */ import com.sun.org.apache.xpath.internal.objects.XObject;
/*     */ import com.sun.org.apache.xpath.internal.objects.XString;
/*     */ import javax.xml.transform.TransformerException;
/*     */ 
/*     */ public class FunctionDef1Arg extends FunctionOneArg
/*     */ {
/*     */   static final long serialVersionUID = 2325189412814149264L;
/*     */ 
/*     */   protected int getArg0AsNode(XPathContext xctxt)
/*     */     throws TransformerException
/*     */   {
/*  57 */     return null == this.m_arg0 ? xctxt.getCurrentNode() : this.m_arg0.asNode(xctxt);
/*     */   }
/*     */ 
/*     */   public boolean Arg0IsNodesetExpr()
/*     */   {
/*  67 */     return null == this.m_arg0 ? true : this.m_arg0.isNodesetExpr();
/*     */   }
/*     */ 
/*     */   protected XMLString getArg0AsString(XPathContext xctxt)
/*     */     throws TransformerException
/*     */   {
/*  86 */     if (null == this.m_arg0)
/*     */     {
/*  88 */       int currentNode = xctxt.getCurrentNode();
/*  89 */       if (-1 == currentNode) {
/*  90 */         return XString.EMPTYSTRING;
/*     */       }
/*     */ 
/*  93 */       DTM dtm = xctxt.getDTM(currentNode);
/*  94 */       return dtm.getStringValue(currentNode);
/*     */     }
/*     */ 
/*  99 */     return this.m_arg0.execute(xctxt).xstr();
/*     */   }
/*     */ 
/*     */   protected double getArg0AsNumber(XPathContext xctxt)
/*     */     throws TransformerException
/*     */   {
/* 119 */     if (null == this.m_arg0)
/*     */     {
/* 121 */       int currentNode = xctxt.getCurrentNode();
/* 122 */       if (-1 == currentNode) {
/* 123 */         return 0.0D;
/*     */       }
/*     */ 
/* 126 */       DTM dtm = xctxt.getDTM(currentNode);
/* 127 */       XMLString str = dtm.getStringValue(currentNode);
/* 128 */       return str.toDouble();
/*     */     }
/*     */ 
/* 133 */     return this.m_arg0.execute(xctxt).num();
/*     */   }
/*     */ 
/*     */   public void checkNumberArgs(int argNum)
/*     */     throws WrongNumberArgsException
/*     */   {
/* 145 */     if (argNum > 1)
/* 146 */       reportWrongNumberArgs();
/*     */   }
/*     */ 
/*     */   protected void reportWrongNumberArgs()
/*     */     throws WrongNumberArgsException
/*     */   {
/* 156 */     throw new WrongNumberArgsException(XSLMessages.createXPATHMessage("ER_ZERO_OR_ONE", null));
/*     */   }
/*     */ 
/*     */   public boolean canTraverseOutsideSubtree()
/*     */   {
/* 167 */     return null == this.m_arg0 ? false : super.canTraverseOutsideSubtree();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.functions.FunctionDef1Arg
 * JD-Core Version:    0.6.2
 */