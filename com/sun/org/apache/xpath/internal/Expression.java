/*     */ package com.sun.org.apache.xpath.internal;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.res.XSLMessages;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTM;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMIterator;
/*     */ import com.sun.org.apache.xml.internal.utils.XMLString;
/*     */ import com.sun.org.apache.xpath.internal.objects.XNodeSet;
/*     */ import com.sun.org.apache.xpath.internal.objects.XObject;
/*     */ import java.io.Serializable;
/*     */ import java.util.Vector;
/*     */ import javax.xml.transform.ErrorListener;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public abstract class Expression
/*     */   implements Serializable, ExpressionNode, XPathVisitable
/*     */ {
/*     */   static final long serialVersionUID = 565665869777906902L;
/*     */   private ExpressionNode m_parent;
/*     */ 
/*     */   public boolean canTraverseOutsideSubtree()
/*     */   {
/*  65 */     return false;
/*     */   }
/*     */ 
/*     */   public XObject execute(XPathContext xctxt, int currentNode)
/*     */     throws TransformerException
/*     */   {
/*  98 */     return execute(xctxt);
/*     */   }
/*     */ 
/*     */   public XObject execute(XPathContext xctxt, int currentNode, DTM dtm, int expType)
/*     */     throws TransformerException
/*     */   {
/* 122 */     return execute(xctxt);
/*     */   }
/*     */ 
/*     */   public abstract XObject execute(XPathContext paramXPathContext)
/*     */     throws TransformerException;
/*     */ 
/*     */   public XObject execute(XPathContext xctxt, boolean destructiveOK)
/*     */     throws TransformerException
/*     */   {
/* 157 */     return execute(xctxt);
/*     */   }
/*     */ 
/*     */   public double num(XPathContext xctxt)
/*     */     throws TransformerException
/*     */   {
/* 173 */     return execute(xctxt).num();
/*     */   }
/*     */ 
/*     */   public boolean bool(XPathContext xctxt)
/*     */     throws TransformerException
/*     */   {
/* 188 */     return execute(xctxt).bool();
/*     */   }
/*     */ 
/*     */   public XMLString xstr(XPathContext xctxt)
/*     */     throws TransformerException
/*     */   {
/* 203 */     return execute(xctxt).xstr();
/*     */   }
/*     */ 
/*     */   public boolean isNodesetExpr()
/*     */   {
/* 213 */     return false;
/*     */   }
/*     */ 
/*     */   public int asNode(XPathContext xctxt)
/*     */     throws TransformerException
/*     */   {
/* 227 */     DTMIterator iter = execute(xctxt).iter();
/* 228 */     return iter.nextNode();
/*     */   }
/*     */ 
/*     */   public DTMIterator asIterator(XPathContext xctxt, int contextNode)
/*     */     throws TransformerException
/*     */   {
/*     */     try
/*     */     {
/* 252 */       xctxt.pushCurrentNodeAndExpression(contextNode, contextNode);
/*     */ 
/* 254 */       return execute(xctxt).iter();
/*     */     }
/*     */     finally
/*     */     {
/* 258 */       xctxt.popCurrentNodeAndExpression();
/*     */     }
/*     */   }
/*     */ 
/*     */   public DTMIterator asIteratorRaw(XPathContext xctxt, int contextNode)
/*     */     throws TransformerException
/*     */   {
/*     */     try
/*     */     {
/* 283 */       xctxt.pushCurrentNodeAndExpression(contextNode, contextNode);
/*     */ 
/* 285 */       XNodeSet nodeset = (XNodeSet)execute(xctxt);
/* 286 */       return nodeset.iterRaw();
/*     */     }
/*     */     finally
/*     */     {
/* 290 */       xctxt.popCurrentNodeAndExpression();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void executeCharsToContentHandler(XPathContext xctxt, ContentHandler handler)
/*     */     throws TransformerException, SAXException
/*     */   {
/* 315 */     XObject obj = execute(xctxt);
/*     */ 
/* 317 */     obj.dispatchCharactersEvents(handler);
/* 318 */     obj.detach();
/*     */   }
/*     */ 
/*     */   public boolean isStableNumber()
/*     */   {
/* 331 */     return false;
/*     */   }
/*     */ 
/*     */   public abstract void fixupVariables(Vector paramVector, int paramInt);
/*     */ 
/*     */   public abstract boolean deepEquals(Expression paramExpression);
/*     */ 
/*     */   protected final boolean isSameClass(Expression expr)
/*     */   {
/* 369 */     if (null == expr) {
/* 370 */       return false;
/*     */     }
/* 372 */     return getClass() == expr.getClass();
/*     */   }
/*     */ 
/*     */   public void warn(XPathContext xctxt, String msg, Object[] args)
/*     */     throws TransformerException
/*     */   {
/* 394 */     String fmsg = XSLMessages.createXPATHWarning(msg, args);
/*     */ 
/* 396 */     if (null != xctxt)
/*     */     {
/* 398 */       ErrorListener eh = xctxt.getErrorListener();
/*     */ 
/* 401 */       eh.warning(new TransformerException(fmsg, xctxt.getSAXLocator()));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void assertion(boolean b, String msg)
/*     */   {
/* 419 */     if (!b)
/*     */     {
/* 421 */       String fMsg = XSLMessages.createXPATHMessage("ER_INCORRECT_PROGRAMMER_ASSERTION", new Object[] { msg });
/*     */ 
/* 425 */       throw new RuntimeException(fMsg);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void error(XPathContext xctxt, String msg, Object[] args)
/*     */     throws TransformerException
/*     */   {
/* 449 */     String fmsg = XSLMessages.createXPATHMessage(msg, args);
/*     */ 
/* 451 */     if (null != xctxt)
/*     */     {
/* 453 */       ErrorListener eh = xctxt.getErrorListener();
/* 454 */       TransformerException te = new TransformerException(fmsg, this);
/*     */ 
/* 456 */       eh.fatalError(te);
/*     */     }
/*     */   }
/*     */ 
/*     */   public ExpressionNode getExpressionOwner()
/*     */   {
/* 466 */     ExpressionNode parent = exprGetParent();
/* 467 */     while ((null != parent) && ((parent instanceof Expression)))
/* 468 */       parent = parent.exprGetParent();
/* 469 */     return parent;
/*     */   }
/*     */ 
/*     */   public void exprSetParent(ExpressionNode n)
/*     */   {
/* 478 */     assertion(n != this, "Can not parent an expression to itself!");
/* 479 */     this.m_parent = n;
/*     */   }
/*     */ 
/*     */   public ExpressionNode exprGetParent()
/*     */   {
/* 484 */     return this.m_parent;
/*     */   }
/*     */ 
/*     */   public void exprAddChild(ExpressionNode n, int i)
/*     */   {
/* 491 */     assertion(false, "exprAddChild method not implemented!");
/*     */   }
/*     */ 
/*     */   public ExpressionNode exprGetChild(int i)
/*     */   {
/* 498 */     return null;
/*     */   }
/*     */ 
/*     */   public int exprGetNumChildren()
/*     */   {
/* 504 */     return 0;
/*     */   }
/*     */ 
/*     */   public String getPublicId()
/*     */   {
/* 522 */     if (null == this.m_parent)
/* 523 */       return null;
/* 524 */     return this.m_parent.getPublicId();
/*     */   }
/*     */ 
/*     */   public String getSystemId()
/*     */   {
/* 543 */     if (null == this.m_parent)
/* 544 */       return null;
/* 545 */     return this.m_parent.getSystemId();
/*     */   }
/*     */ 
/*     */   public int getLineNumber()
/*     */   {
/* 565 */     if (null == this.m_parent)
/* 566 */       return 0;
/* 567 */     return this.m_parent.getLineNumber();
/*     */   }
/*     */ 
/*     */   public int getColumnNumber()
/*     */   {
/* 587 */     if (null == this.m_parent)
/* 588 */       return 0;
/* 589 */     return this.m_parent.getColumnNumber();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.Expression
 * JD-Core Version:    0.6.2
 */